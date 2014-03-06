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
import org.snaker.engine.access.QueryFilter;
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
	 * @param accessObject 数据库访问对象(Connection等)
	 */
	public void initialize(Object accessObject);
	/**
	 * 保存任务对象
	 * @param task 任务对象
	 */
	public void saveTask(Task task);
	
	/**
	 * 保存流程实例对象
	 * @param order 流程实例对象
	 */
	public void saveOrder(Order order);
	
	/**
	 * 保存流程定义对象
	 * @param process 流程定义对象
	 */
	public void saveProcess(Process process);
	
	/**
	 * 保存任务参与者对象
	 * @param taskActor 任务参与者对象
	 */
	public void saveTaskActor(TaskActor taskActor);
	
	/**
	 * 更新任务对象
	 * @param task 任务对象
	 */
	public void updateTask(Task task);
	
	/**
	 * 更新流程实例对象
	 * @param order 流程实例对象
	 */
	public void updateOrder(Order order);
	
	/**
	 * 更新流程定义对象
	 * @param process 流程定义对象
	 */
	public void updateProcess(Process process);
	
	/**
	 * 删除任务、任务参与者对象
	 * @param task 任务对象
	 */
	public void deleteTask(Task task);
	
	/**
	 * 删除流程实例对象
	 * @param order 流程实例对象
	 */
	public void deleteOrder(Order order);
	
	/**
	 * 删除参与者
	 * @param taskId 任务id
	 * @param actors 参与者集合
	 */
	public void removeTaskActor(String taskId, String... actors);
	
	/**
	 * 迁移活动任务
	 * @param order 历史流程实例对象
	 */
	public void saveHistory(HistoryOrder order);
	
	/**
	 * 更新历史流程实例状态
	 * @param order 历史流程实例对象
	 */
	public void updateHistory(HistoryOrder order);
	
	/**
	 * 迁移活动流程实例
	 * @param task 历史任务对象
	 */
	public void saveHistory(HistoryTask task);
	
	/**
	 * 根据任务id查询任务对象
	 * @param taskId 任务id
	 * @return Task 任务对象
	 */
	public Task getTask(String taskId);
	
	/**
	 * 根据任务ID获取历史任务对象
	 * @param taskId 历史任务id
	 * @return 历史任务对象
	 */
	HistoryTask getHistTask(String taskId);
	
	/**
	 * 根据父任务id查询所有子任务
	 * @param parentTaskId 父任务id
	 * @return List<Task> 活动任务集合
	 */
	public List<Task> getNextActiveTasks(String parentTaskId);
	
	/**
	 * 根据流程实例id、任务名称获取
	 * @param orderId 流程实例id
	 * @param taskName 任务名称
	 * @param parentTaskId 父任务id
	 * @return List<Task> 活动任务集合
	 */
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId);
	
	/**
	 * 根据任务id查询所有活动任务参与者集合
	 * @param taskId 活动任务id
	 * @return List<TaskActor> 活动任务参与者集合
	 */
	public List<TaskActor> getTaskActorsByTaskId(String taskId);
	
	/**
	 * 根据任务id查询所有历史任务参与者集合
	 * @param taskId 历史任务id
	 * @return List<HistoryTaskActor> 历史任务参与者集合
	 */
	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId);
	
	/**
	 * 根据流程实例id查询实例对象
	 * @param orderId 活动流程实例id
	 * @return Order 活动流程实例对象
	 */
	public Order getOrder(String orderId);
	
	/**
	 * 根据流程实例ID获取历史流程实例对象
	 * @param orderId 历史流程实例id
	 * @return HistoryOrder 历史流程实例对象
	 */
	HistoryOrder getHistOrder(String orderId);
	
	/**
	 * 根据流程定义id查询流程定义对象
	 * @param id 流程定义id
	 * @return Process 流程定义对象
	 */
	public Process getProcess(String id);
	
	/**
	 * 根据流程名称查询最近的版本号
	 * @param name
	 * @return Integer 流程定义版本号
	 */
	public Integer getLatestProcessVersion(String name);
	
	/**
	 * 根据查询的参数，分页对象，返回分页后的查询结果
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<Process> 流程定义集合
	 */
	public List<Process> getProcesss(Page<Process> page, QueryFilter filter);
	
	/**
	 * 分页查询流程实例
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<Order> 活动流程实例集合
	 */
	public List<Order> getActiveOrders(Page<Order> page, QueryFilter filter);
	
	/**
	 * 分页查询活动任务列表
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<Task> 活动任务集合
	 */
	public List<Task> getActiveTasks(Page<Task> page, QueryFilter filter);
	
	/**
	 * 分页查询历史流程实例
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<HistoryOrder> 历史流程实例集合
	 */
	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, QueryFilter filter);
	
	/**
	 * 根据参与者分页查询已完成的历史任务
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<HistoryTask> 历史任务集合
	 */
	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, QueryFilter filter);
	
	/**
	 * 根据查询的参数，分页对象，返回分页后的查询结果
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<WorkItem> 活动工作项
	 */
	public List<WorkItem> getWorkItems(Page<WorkItem> page, QueryFilter filter);
	
	/**
	 * 根据流程定义ID、参与者分页查询已完成的历史任务项
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<WorkItem> 历史工作项
	 */
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, QueryFilter filter);
	/**
	 * 根据类型T、Sql语句、参数查询单个对象
	 * @param T 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return
	 */
	public <T> T queryObject(Class<T> T, String sql, Object... args);
	/**
	 * 根据类型T、Sql语句、参数查询列表对象
	 * @param T 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return
	 */
	public <T> List<T> queryList(Class<T> T, String sql, Object... args);
	
	/**
	 * 根据类型T、Sql语句、参数分页查询列表对象
	 * @param page 分页对象
	 * @param T 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return
	 */
	public <T> List<T> queryList(Page<T> page, Class<T> T, String sql, Object... args);
}
