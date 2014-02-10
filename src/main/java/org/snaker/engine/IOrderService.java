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
package org.snaker.engine;

import java.util.Map;

import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;

/**
 * 流程实例业务类
 * @author yuqs
 * @version 1.0
 */
public interface IOrderService {
	/**
	 * 根据流程、操作人员、父流程实例ID创建流程实例
	 * @param process 流程定义对象
	 * @param operator 操作人员ID
	 * @param expireTime 期望完成时间
	 * @return
	 */
	Order createOrder(Process process, String operator, Map<String, Object> args);
	
	/**
	 * 根据流程、操作人员、父流程实例ID创建流程实例
	 * @param process 流程定义对象
	 * @param operator 操作人员ID
	 * @param expireTime 期望完成时间
	 * @param parentId 父流程实例ID
	 * @param parentNodeName 父流程节点模型
	 * @return
	 */
	Order createOrder(Process process, String operator, Map<String, Object> args, String parentId, String parentNodeName);
	
	/**
	 * 保存流程实例
	 * @param order 流程实例对象
	 */
	void saveOrder(Order order);
	
	/**
	 * 根据流程实例ID获取对象
	 * @param id
	 * @return Order
	 */
	Order getOrder(String id);
	
	/**
	 * 流程实例正常完成
	 * @param order
	 */
	void finish(Order order);
	
	/**
	 * 流程实例强制终止
	 * @param order
	 */
	void terminate(String orderId, String operator);
}
