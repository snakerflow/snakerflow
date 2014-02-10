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
import org.snaker.engine.entity.HistoryTaskActor;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.entity.WorkItem;

/**
 * 数据库访问接口
 * 主要提供保存、更新、查询流程的相关table
 * @author yuqs
 * @version 1.0
 */
public interface DBAccess {
	/**
	 * 根据访问对象，设置具体的实现类
	 * @param accessObject
	 */
	public void initialize(Object accessObject);
	/**
	 * 保存任务对象
	 * @param task
	 */
	public void saveTask(Task task);
	
	/**
	 * 保存流程实例对象
	 * @param order
	 */
	public void saveOrder(Order order);
	
	/**
	 * 保存流程定义对象
	 * @param process
	 */
	public void saveProcess(Process process);
	
	/**
	 * 保存任务参与者对象
	 * @param taskActor
	 */
	public void saveTaskActor(TaskActor taskActor);
	
	/**
	 * 更新任务对象
	 * @param task
	 */
	public void updateTask(Task task);
	
	/**
	 * 更新流程实例对象
	 * @param order
	 */
	public void updateOrder(Order order);
	
	/**
	 * 更新流程定义对象
	 * @param process
	 */
	public void updateProcess(Process process);
	
	/**
	 * 删除任务、任务参与者对象
	 * @param taskId
	 */
	public void deleteTask(Task task);
	
	/**
	 * 删除流程实例对象
	 * @param orderId
	 */
	public void deleteOrder(Order order);
	
	/**
	 * 删除参与者
	 * @param task
	 * @param actors
	 */
	public void removeTaskActor(String taskId, String... actors);
	
	/**
	 * 迁移活动任务
	 * @param task
	 */
	public void saveHistory(HistoryOrder order);
	
	/**
	 * 更新历史流程实例状态
	 * @param order
	 */
	public void updateHistory(HistoryOrder order);
	
	/**
	 * 迁移活动流程实例
	 * @param order
	 */
	public void saveHistory(HistoryTask task);
	
	/**
	 * 根据任务id查询任务对象
	 * @param taskId
	 * @return
	 */
	public Task getTask(String taskId);
	
	/**
	 * 根据任务ID获取历史任务对象
	 * @param taskId
	 * @return
	 */
	HistoryTask getHistTask(String taskId);
	
	/**
	 * 根据父任务id查询所有子任务
	 * @param parentTaskId
	 * @return
	 */
	public List<Task> getNextActiveTasks(String parentTaskId);
	
	/**
	 * 根据流程实例id、任务名称获取
	 * @param orderId
	 * @param taskName
	 * @param parentTaskId
	 * @return
	 */
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId);
	
	/**
	 * 根据任务id查询所有活动任务参与者集合
	 * @param taskId
	 * @return
	 */
	public List<TaskActor> getTaskActorsByTaskId(String taskId);
	
	/**
	 * 根据任务id查询所有历史任务参与者集合
	 * @param taskId
	 * @return
	 */
	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId);
	
	/**
	 * 根据流程实例id查询实例对象
	 * @param orderId
	 * @return
	 */
	public Order getOrder(String orderId);
	
	/**
	 * 根据流程实例ID获取历史流程实例对象
	 * @param orderId
	 * @return
	 */
	HistoryOrder getHistOrder(String orderId);
	
	/**
	 * 根据流程定义id、或name查询流程定义对象
	 * @param processId
	 * @return
	 */
	public Process getProcess(String idName);
	
	/**
	 * 根据查询的参数，分页对象，返回分页后的查询结果
	 * @param page
	 * @param params
	 * @return
	 */
	public List<Process> getProcesss(Page<Process> page, String name, Integer state);
	
	/**
	 * 获取所有的流程定义集合
	 * @return
	 */
	public List<Process> getAllProcess();
	
	/**
	 * 分页查询流程实例
	 * @param page
	 * @param processIds
	 * @return
	 */
	public List<Order> getActiveOrders(Page<Order> page, String... processId);
	
	/**
	 * 根据父流程实例ID，查询所有活动的子流程
	 * @param parentId
	 * @return
	 */
	public List<Order> getActiveOrdersByParentId(String parentId, String... excludedId);
	
	/**
	 * 分页查询活动任务列表
	 * @param page
	 * @param actorIds
	 * @return
	 */
	public List<Task> getActiveTasks(Page<Task> page, String... actorIds);
	
	/**
	 * 获取指定流程实例ID的任务列表
	 * @param orderId
	 * @param taskNames
	 * @return
	 */
	public List<Task> getActiveTasks(String orderId, String excludedTaskId, String... taskNames);
	
	/**
	 * 根据查询的参数，分页对象，返回分页后的查询结果
	 * @param page
	 * @param params
	 * @return
	 */
	public List<WorkItem> getWorkItems(Page<WorkItem> page, String processId, String... actorIds);
	
	/**
	 * 分页查询历史流程实例
	 * @param page
	 * @param processIds
	 * @return
	 */
	List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, String... processIds);
	
	/**
	 * 根据父流程ID查询子流程实例集合
	 * @param parentId
	 * @return
	 */
	List<HistoryOrder> getHistoryOrdersByParentId(String parentId);
	
	/**
	 * 根据流程实例ID查询所有已完成的任务
	 * @param orderId
	 * @return
	 */
	List<HistoryTask> getHistoryTasks(String orderId);
	
	/**
	 * 根据参与者分页查询已完成的历史任务
	 * @param page
	 * @param actorIds
	 * @return
	 */
	List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, String... actorIds);
	
	/**
	 * 根据流程定义ID、参与者分页查询已完成的历史任务项
	 * @param page
	 * @param processId
	 * @param actorIds
	 * @return
	 */
	List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, String processId, String... actorIds);
}
