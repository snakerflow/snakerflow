/* Copyright 2013-2015 www.snakerflow.com.
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
import org.snaker.engine.entity.CCOrder;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.HistoryTaskActor;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.entity.WorkItem;

/**
 * 数据库访问接口
 * 主要提供保存、更新、查询流程的相关table
 * @author yuqs
 * @since 1.0
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
	 * 保存抄送实例
	 * @param ccorder 抄送实体
	 * @since 1.5
	 */
	public void saveCCOrder(CCOrder ccorder);
	
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
	 * 更新抄送状态
	 * @param ccorder 抄送实体对象
	 */
	public void updateCCOrder(CCOrder ccorder);
	
	/**
	 * 更新流程定义对象
	 * @param process 流程定义对象
	 */
	public void updateProcess(Process process);

	/**
	 * 删除流程定义对象
	 * @param process 流程定义对象
	 */
	public void deleteProcess(Process process);
	
	/**
	 * 更新流程定义类别
	 * @param type 类别
	 * @since 1.5
	 */
	public void updateProcessType(String id, String type);
	
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
	 * 删除抄送记录
	 * @param ccorder 抄送实体对象
	 */
	public void deleteCCOrder(CCOrder ccorder);
	
	/**
	 * 删除参与者
	 * @param taskId 任务id
	 * @param actors 参与者集合
	 */
	public void removeTaskActor(String taskId, String... actors);
	
	/**
	 * 迁移活动实例
	 * @param order 历史流程实例对象
	 */
	public void saveHistory(HistoryOrder order);
	
	/**
	 * 更新历史流程实例状态
	 * @param order 历史流程实例对象
	 */
	public void updateHistory(HistoryOrder order);
	
	/**
	 * 迁移活动任务
	 * @param task 历史任务对象
	 */
	public void saveHistory(HistoryTask task);

	/**
	 * 删除历史实例记录
	 * @param historyOrder 历史实例
	 */
	public void deleteHistoryOrder(HistoryOrder historyOrder);

	/**
	 * 删除历史任务记录
	 * @param historyTask 历史任务
	 */
	public void deleteHistoryTask(HistoryTask historyTask);

    /**
     * 更新实例变量（包括历史实例表）
     * @param order 实例对象
     */
    public void updateOrderVariable(Order order);
	
	/**
	 * 保存委托代理对象
	 * @param surrogate 委托代理对象
	 */
	public void saveSurrogate(Surrogate surrogate);
	
	/**
	 * 更新委托代理对象
	 * @param surrogate 委托代理对象
	 */
	public void updateSurrogate(Surrogate surrogate);
	
	/**
	 * 删除委托代理对象
	 * @param surrogate 委托代理对象
	 */
	public void deleteSurrogate(Surrogate surrogate);
	
	/**
	 * 根据主键id查询委托代理对象
	 * @param id 主键id
	 * @return surrogate 委托代理对象
	 */
	public Surrogate getSurrogate(String id);
	
	/**
	 * 根据授权人、流程名称查询委托代理对象
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<Surrogate> 委托代理对象集合
	 */
	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter);
	
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
	 * 根据流程实例id、参与者id获取抄送记录
	 * @param orderId 活动流程实例id
	 * @param actorIds 参与者id
	 * @return 传送记录列表
	 */
	public List<CCOrder> getCCOrder(String orderId, String... actorIds);
	
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
	 * @param name 流程名称
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
	 * 根据查询的参数，分页对象，返回分页后的活动工作项
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<WorkItem> 活动工作项
	 */
	public List<WorkItem> getWorkItems(Page<WorkItem> page, QueryFilter filter);
	
	/**
	 * 根据查询的参数，分页对象，返回分页后的抄送任务项
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<WorkItem> 活动工作项
	 */
	public List<HistoryOrder> getCCWorks(Page<HistoryOrder> page, QueryFilter filter);
	
	/**
	 * 根据流程定义ID、参与者分页查询已完成的历史任务项
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<WorkItem> 历史工作项
	 */
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, QueryFilter filter);
	
	/**
	 * 根据类型clazz、Sql语句、参数查询单个对象
	 * @param clazz 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return 结果对象
	 */
	public <T> T queryObject(Class<T> clazz, String sql, Object... args);
	
	/**
	 * 根据类型clazz、Sql语句、参数查询列表对象
	 * @param clazz 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return 结果对象列表
	 */
	public <T> List<T> queryList(Class<T> clazz, String sql, Object... args);
	
	/**
	 * 根据类型clazz、Sql语句、参数分页查询列表对象
	 * @param page 分页对象
     * @param filter 查询过滤器
	 * @param clazz 类型
	 * @param sql sql语句
	 * @param args 参数列表
	 * @return 结果对象列表
	 */
	public <T> List<T> queryList(Page<T> page, QueryFilter filter, Class<T> clazz, String sql, Object... args);

    /**
     * 运行脚本文件
     */
    public void runScript();
}
