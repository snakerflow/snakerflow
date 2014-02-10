/* Copyright 2012-2013 the original author or authors.
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

import java.util.Map;

import org.snaker.engine.IOrderService;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
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
	 * 由DBAccess实现类持久化order对象
	 */
	@Override
	public void saveOrder(Order order) {
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_ACTIVE);
		access().saveOrder(order);
		access().saveHistory(history);
	}

	/**
	 * 由DBAccess实现类根据主键ID获取order对象
	 */
	@Override
	public Order getOrder(String id) {
		return access().getOrder(id);
	}

	/**
	 * 由DBAccess实现类更新order状态
	 */
	@Override
	public void finish(Order order) {
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_FINISH);
		history.setEndTime(DateHelper.getTime());
		
		access().updateHistory(history);
		access().deleteOrder(order);
	}

	/**
	 * 由DBAccess实现类更新order状态
	 */
	@Override
	public void terminate(String orderId, String operator) {
		Order order = getOrder(orderId);
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_TERMINATION);
		history.setEndTime(DateHelper.getTime());
		
		access().saveHistory(history);
		access().deleteOrder(order);
	}
	
	/**
	 * 由DBAccess实现类持久化新建的order对象
	 */
	@Override
	public Order createOrder(Process process, String operator, Map<String, Object> args) {
		return createOrder(process, operator, args, null, null);
	}
	
	/**
	 * 由DBAccess实现类持久化新建的order对象
	 */
	@Override
	public Order createOrder(Process process, String operator, Map<String, Object> args, String parentId, String parentNodeName) {
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
		if(model != null) {
			if(StringHelper.isNotEmpty(model.getExpireTime())) {
				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
				order.setExpireTime(expireTime);
			}
			order.setOrderNo(model.getGenerator().generate(model));
		}
		order.setVariable(JsonHelper.toJson(args));
		saveOrder(order);
		return order;
	}
}
