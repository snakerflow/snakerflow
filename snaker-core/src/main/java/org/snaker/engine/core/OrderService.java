/* Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snaker.engine.core;

import java.util.List;
import java.util.Map;

import org.snaker.engine.IOrderService;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.CCOrder;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.DateHelper;
import org.snaker.engine.helper.JsonHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.ProcessModel;

/**
 * 流程实例业务类
 * @author yuqs
 * @version 1.0
 */
public class OrderService extends AccessService implements IOrderService {
	/**
	 * 创建活动实例
	 * @see org.snaker.engine.core.OrderService#createOrder(Process, String, Map, String, String)
	 */
	public Order createOrder(Process process, String operator, Map<String, Object> args) {
		return createOrder(process, operator, args, null, null);
	}
	
	/**
	 * 创建活动实例
	 */
	public Order createOrder(Process process, String operator, Map<String, Object> args, 
			String parentId, String parentNodeName) {
		Order order = new Order();
		order.setId(StringHelper.getPrimaryKey());
		order.setParentId(parentId);
		order.setParentNodeName(parentNodeName);
		order.setCreateTime(DateHelper.getTime());
		order.setLastUpdateTime(order.getCreateTime());
		order.setCreator(operator);
		order.setLastUpdator(order.getCreator());
		order.setProcessId(process.getId());
		ProcessModel model = process.getModel();
		if(model != null && args != null) {
			if(StringHelper.isNotEmpty(model.getExpireTime())) {
				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
				order.setExpireTime(expireTime);
			}
            String orderNo = (String)args.get(SnakerEngine.ID);
            if(StringHelper.isNotEmpty(orderNo)) {
                order.setOrderNo(orderNo);
            } else {
                order.setOrderNo(model.getGenerator().generate(model));
            }
		}

		order.setVariable(JsonHelper.toJson(args));
		saveOrder(order);
		return order;
	}

    /**
     * 向活动实例临时添加全局变量数据
     * @param orderId 实例id
     * @param args 变量数据
     */
    public void addVariable(String orderId, Map<String, Object> args) {
        Order order = access().getOrder(orderId);
        Map<String, Object> data = order.getVariableMap();
        data.putAll(args);
        order.setVariable(JsonHelper.toJson(data));
        access().updateOrderVariable(order);
    }

    /**
	 * 创建实例的抄送
	 */
	public void createCCOrder(String orderId, String... actorIds) {
		for(String actorId : actorIds) {
			CCOrder ccorder = new CCOrder();
			ccorder.setOrderId(orderId);
			ccorder.setActorId(actorId);
			ccorder.setStatus(STATE_ACTIVE);
            ccorder.setCreateTime(DateHelper.getTime());
			access().saveCCOrder(ccorder);
		}
	}
	
	/**
	 * 流程实例数据会保存至活动实例表、历史实例表
	 */
	public void saveOrder(Order order) {
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_ACTIVE);
		access().saveOrder(order);
		access().saveHistory(history);
	}
	
	/**
	 * 更新活动实例的last_Updator、last_Update_Time、version、variable
	 */
	public void updateOrder(Order order) {
		access().updateOrder(order);
	}
	
	/**
	 * 更新抄送记录状态为已阅
	 */
	public void updateCCStatus(String orderId, String... actorIds) {
        List<CCOrder> ccorders = access().getCCOrder(orderId, actorIds);
        AssertHelper.notNull(ccorders);
        for(CCOrder ccorder : ccorders) {
            ccorder.setStatus(STATE_FINISH);
            ccorder.setFinishTime(DateHelper.getTime());
            access().updateCCOrder(ccorder);
        }
	}
	
	/**
	 * 删除指定的抄送记录
	 */
	public void deleteCCOrder(String orderId, String actorId) {
        List<CCOrder> ccorders = access().getCCOrder(orderId, actorId);
		AssertHelper.notNull(ccorders);
        for(CCOrder ccorder : ccorders) {
		    access().deleteCCOrder(ccorder);
        }
	}

	/**
	 * 删除活动流程实例数据，更新历史流程实例的状态、结束时间
	 */
	public void complete(String orderId) {
		Order order = access().getOrder(orderId);
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_FINISH);
		history.setEndTime(DateHelper.getTime());
		
		access().updateHistory(history);
		access().deleteOrder(order);
	}
	
	/**
	 * 强制中止流程实例
	 * @see org.snaker.engine.core.OrderService#terminate(String, String)
	 */
	public void terminate(String orderId) {
		terminate(orderId, null);
	}

	/**
	 * 强制中止活动实例,并强制完成活动任务
	 */
	public void terminate(String orderId, String operator) {
		SnakerEngine engine = ServiceContext.getEngine();
		List<Task> tasks = engine
				.query()
				.getActiveTasks(new QueryFilter().setOrderId(orderId));
		for(Task task : tasks) {
			engine.task().complete(task.getId(), operator);
		}
		Order order = access().getOrder(orderId);
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_TERMINATION);
		history.setEndTime(DateHelper.getTime());
		
		access().updateHistory(history);
		access().deleteOrder(order);
	}
}
