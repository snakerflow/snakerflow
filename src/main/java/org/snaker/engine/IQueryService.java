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

import java.util.List;

import org.snaker.engine.access.Page;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.WorkItem;

/**
 * 流程相关的查询服务
 * @author yuqs
 * @version 1.0
 */
public interface IQueryService {
	/**
	 * 根据流程实例ID获取流程实例对象
	 * @param orderId 流程实例id
	 * @return Order 流程实例对象
	 */
	Order getOrder(String orderId);
	/**
	 * 根据流程实例ID获取历史流程实例对象
	 * @param orderId 历史流程实例id
	 * @return HistoryOrder 历史流程实例对象
	 */
	HistoryOrder getHistOrder(String orderId);
	/**
	 * 根据任务ID获取任务对象
	 * @param taskId 任务id
	 * @return Task 任务对象
	 */
	Task getTask(String taskId);
	/**
	 * 根据任务ID获取历史任务对象
	 * @param taskId 历史任务id
	 * @return HistoryTask 历史任务对象
	 */
	HistoryTask getHistTask(String taskId);
	/**
	 * 根据任务ID获取活动任务参与者数组
	 * @param taskId 任务id
	 * @return String[] 参与者id数组
	 */
	String[] getTaskActorsByTaskId(String taskId);
	/**
	 * 根据任务ID获取历史任务参与者数组
	 * @param taskId 历史任务id
	 * @return String[] 历史参与者id数组
	 */
	String[] getHistoryTaskActorsByTaskId(String taskId);
	/**
	 * 根据参与者获取任务集合
	 * @param actorIds 参与者id集合
	 * @return List<Task> 活动任务集合
	 */
	List<Task> getActiveTasksByActors(String... actorIds);
	
	/**
	 * 根据流程实例ID、任务名称查询所有活动任务列表
	 * @param orderId 流程实例id
	 * @param taskNames 任务名称集合
	 * @return List<Task> 活动任务集合
	 */
	List<Task> getActiveTasks(String orderId, String... taskNames);
	
	/**
	 * 根据流程实例ID、排除的任务ID、任务名称查询所有活动任务列表
	 * @param orderId 流程实例id
	 * @param excludedTaskId 过滤的任务id
	 * @param taskNames 任务名称集合
	 * @return List<Task> 活动任务集合
	 */
	List<Task> getActiveTasks(String orderId, String excludedTaskId, String... taskNames);
	
	/**
	 * 根据参与者分页查询活动任务
	 * @param page 分页对象
	 * @param actorIds 参与者id集合
	 * @return List<Task> 活动任务集合
	 */
	List<Task> getActiveTasks(Page<Task> page, String... actorIds);
	
	/**
	 * 根据流程定义ID查询流程实例列表
	 * @param processIds 流程定义id集合
	 * @return List<Order> 活动实例集合
	 */
	List<Order> getActiveOrders(String... processIds);
	
	/**
	 * 根据父流程实例ID，查询所有活动的子流程
	 * @param parentId 父流程实例id
	 * @param excludedId 过滤的流程实例id集合
	 * @return List<Order> 活动实例集合
	 */
	List<Order> getActiveOrdersByParentId(String parentId, String... excludedId);
	
	/**
	 * 根据流程定义ID分页查询活动流程实例
	 * @param page 分页对象
	 * @param processIds 流程定义id集合
	 * @return List<Order> 活动实例集合
	 */
	List<Order> getActiveOrders(Page<Order> page, String... processIds);
	
	/**
	 * 根据给定的分页对象page、流程定义processId、参与者ID列表，查询工作项（包含process、order、task三个实体的字段集合）
	 * @param page 分页对象
	 * @param processId 流程定义id
	 * @param actorIds 参与者id集合
	 * @return List<WorkItem> 活动工作项集合
	 */
	List<WorkItem> getWorkItems(Page<WorkItem> page, String processId, String... actorIds);
	
	/**
	 * 分页查询历史流程实例
	 * @param page 分页对象
	 * @param processIds 流程定义id集合
	 * @return List<HistoryOrder> 历史实例集合
	 */
	List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, String... processIds);
	
	/**
	 * 根据父流程ID查询子流程实例集合
	 * @param parentId 父流程实例id
	 * @return List<HistoryOrder> 历史实例集合
	 */
	List<HistoryOrder> getHistoryOrdersByParentId(String parentId);
	
	/**
	 * 根据流程实例ID查询所有已完成的任务
	 * @param orderId 流程实例id
	 * @return List<HistoryTask> 历史任务集合
	 */
	List<HistoryTask> getHistoryTasks(String orderId);
	
	/**
	 * 根据参与者分页查询已完成的历史任务
	 * @param page 分页对象
	 * @param actorIds 参与者id集合
	 * @return List<HistoryTask> 历史任务集合
	 */
	List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, String... actorIds);
	
	/**
	 * 根据流程定义ID、参与者分页查询已完成的历史任务项
	 * @param page 分页对象
	 * @param processId 流程定义id
	 * @param actorIds 参与者id集合
	 * @return List<WorkItem> 历史工作项集合
	 */
	List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, String processId, String... actorIds);
}
