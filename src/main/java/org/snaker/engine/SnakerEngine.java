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
import java.util.Map;

import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.core.Execution;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.model.WorkModel;

/**
 * 流程引擎接口
 * @author yuqs
 * @version 1.0
 */
public interface SnakerEngine {
	public static final String ADMIN = "snaker.admin";
	/**
	 * 根据Configuration对象配置实现类
	 * @param context
	 */
	public SnakerEngine configure(Configuration config);
	
	/**
	 * 获取process服务
	 * @return
	 */
	public IProcessService process();
	
	/**
	 * 获取查询服务
	 */
	public IQueryService query();
	
	/**
	 * 获取实例服务
	 * @return
	 */
	public IOrderService order();
	
	/**
	 * 获取任务服务
	 * @return
	 */
	public ITaskService task();
	
	/**
	 * 根据流程定义ID启动流程实例
	 * @param id 流程定义ID
	 * @return Order 流程实例
	 */
	public Order startInstanceById(String id);
	
	/**
	 * 根据流程定义ID，操作人ID启动流程实例
	 * @param id 流程定义ID
	 * @param operator 操作人ID
	 * @return Order 流程实例
	 */
	public Order startInstanceById(String id, String operator);
	
	/**
	 * 根据流程定义ID，操作人ID，参数列表启动流程实例
	 * @param id 流程定义ID
	 * @param operator 操作人ID
	 * @param args 参数列表
	 * @return Order 流程实例
	 */
	public Order startInstanceById(String id, String operator, Map<String, Object> args);
	
	/**
	 * 根据父执行对象启动子流程实例
	 * @param execution
	 * @return
	 */
	public Order startInstanceByExecution(Execution execution);
	
	/**
	 * 根据任务主键ID执行任务
	 * @param taskId 任务主键ID
	 * @return List<Task> 任务集合
	 */
	public List<Task> executeTask(String taskId);
	
	/**
	 * 根据任务主键ID，操作人ID执行任务
	 * @param taskId 任务主键ID
	 * @param operator 操作人主键ID
	 * @return List<Task> 任务集合
	 */
	public List<Task> executeTask(String taskId, String operator);
	
	/**
	 * 根据任务主键ID，操作人ID，参数列表执行任务
	 * @param taskId 任务主键ID
	 * @param operator 操作人主键ID
	 * @param args 参数列表
	 * @return List<Task> 任务集合
	 */
	public List<Task> executeTask(String taskId, String operator, Map<String, Object> args);
	
	/**
	 * 根据任务主键ID，操作人ID，参数列表执行任务，并且根据nodeName跳转到任意节点
	 * 1、nodeName为null时，则跳转至上一步处理
	 * 2、nodeName不为null时，则任意跳转，即动态创建转移
	 * @param taskId 任务主键ID
	 * @param operator 操作人主键ID
	 * @param args 参数列表
	 * @param nodeName 跳转的节点名称
	 * @return List<Task> 任务集合
	 */
	public List<Task> executeAndJumpTask(String taskId, String operator, Map<String, Object> args, String nodeName);
	
	/**
	 * 创建新的任务
	 * @param model 节点模型
	 * @param execution 执行对象
	 * @return List<Task> 任务集合
	 */
	public List<Task> createTask(WorkModel model, Execution execution);
	
	/**
	 * 根据任务主键ID，操作人ID完成任务
	 * @param taskId 任务id
	 * @param operator 操作人id
	 * @param args 参数集合
	 * @return
	 */
	public Task finishTask(String taskId, String operator, Map<String, Object> args);
	
	/**
	 * 根据流程实例ID，操作人ID，参数列表按照节点模型model创建新的自由任务
	 * @param orderId 流程实例id
	 * @param operator 操作人id
	 * @param args 参数列表
	 * @param model 节点模型
	 * @return
	 */
	public List<Task> createFreeTask(String orderId, String operator, Map<String, Object> args, WorkModel model);
	
	/**
	 * 根据任务主键ID，操作人ID提取任务
	 * 提取任务相当于预受理操作，仅仅标识此任务只能由此操作人处理
	 * @param taskId 任务主键ID
	 * @param operator 操作人主键ID
	 */
	public void takeTask(String taskId, String operator);
	
	/**
	 * 终止指定ID的流程实例
	 * @param orderId 流程实例ID
	 */
	public void terminateById(String orderId);
	
	/**
	 * 终止指定ID的流程实例
	 * @param orderId 流程实例ID
	 * @param operator 操作人主键ID
	 */
	public void terminateById(String orderId, String operator);
	
	/**
	 * 根据任务主键id、处理人撤回任务
	 * @param taskId 任务主键ID
	 * @param operator 操作人主键ID
	 * @return Task
	 */
	public Task withdrawTask(String taskId, String operator);
	
	/**
	 * 完成指定id的流程实例
	 * @param orderId 执行对象
	 */
	public void finishInstanceById(String orderId);
	
	/**
	 * 向指定的任务id，添加参与者
	 * @param taskId 任务主键id
	 * @param actors 参与者
	 */
	public void addTaskActor(String taskId, String... actors);
	
	/**
	 * 对指定的任务id，删除参与者
	 * @param taskId
	 * @param actors
	 */
	public void removeTaskActor(String taskId, String... actors);
}
