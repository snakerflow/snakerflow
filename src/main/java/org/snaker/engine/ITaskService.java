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

import org.snaker.engine.core.Execution;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.model.CustomModel;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;

/**
 * 任务业务类，包括以下服务：
 * 1、创建任务
 * 2、根据任务ID获取活动、历史对象
 * 3、对指定任务分配参与者
 * 4、完成任务
 * 5、撤回任务
 * 6、回退任务
 * 7、提取任务
 * @author yuqs
 * @version 1.0
 */
public interface ITaskService {
	/**
	 * 完成指定的任务，删除活动任务记录，创建历史任务
	 * @param task 任务对象
	 * @return Task
	 */
	Task completeTask(Task task);
	/**
	 * 完成指定的任务，删除活动任务记录，创建历史任务
	 * @param task 任务对象
	 * @param operator 操作人
	 * @return Task
	 */
	Task completeTask(Task task, String operator);
	
	/**
	 * 提取指定的任务，只更新操作人字段标识参与者
	 * @param task 任务对象
	 * @param operator 操作人
	 * @return Task
	 */
	Task takeTask(Task task, String operator);
	
	/**
	 * 向指定的任务id添加参与者
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	void addTaskActor(Task task, String... actors);
	
	/**
	 * 对指定的任务id删除参与者
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	void removeTaskActor(Task task, String... actors);
	
	/**
	 * 根据任务主键id、操作人撤回任务
	 * @param model 流程定义模型
	 * @param hist 历史任务对象
	 * @param operator 操作人
	 * @return Task
	 */
	Task withdrawTask(ProcessModel model, HistoryTask hist, String operator);
	
	/**
	 * 根据当前任务对象驳回至上一步处理
	 * @param model 流程定义模型，用以获取上一步模型对象
	 * @param currentTask 当前任务对象
	 * @return Task
	 */
	Task rejectTask(ProcessModel model, Task currentTask);
	
	/**
	 * 根据taskId获取所有该任务的参与者集合
	 * @param taskId 任务id
	 * @return List<TaskActor>
	 */
	List<TaskActor> getTaskActorsByTaskId(String taskId);
	
	/**
	 * 根据taskId、operator，判断操作人operator是否允许执行任务
	 * @param taskId 任务id
	 * @param operator 操作人
	 * @return boolean
	 */
	boolean isAllowed(Task task, String operator);

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * @param taskId 任务id
	 * @param actorIds 参与者id集合
	 */
	void assignTask(String taskId, String... actorIds);
	
	/**
	 * 根据任务编号获取任务实例
	 * @param taskId 任务id
	 * @return Task
	 */
	Task getTask(String taskId);
	
	/**
	 * 根据任务编号获取历史任务实例
	 * @param taskId 任务id
	 * @return HistoryTask
	 */
	HistoryTask getHistoryTask(String taskId);
	
	/**
	 * 根据任务模型、执行对象创建新的任务
	 * @param taskModel 任务模型
	 * @param execution 执行对象
	 * @return List<Task>
	 */
	List<Task> createTask(TaskModel taskModel, Execution execution);
	
	/**
	 * 根据自定义模型、执行对象创建新的任务 
	 * @param customModel 自定义模型
	 * @param execution 执行对象
	 * @return List<Task>
	 */
	List<Task> createTask(CustomModel customModel, Execution execution);
	
	/**
	 * 保存任务对象
	 * @param task 任务对象
	 */
	void saveTask(Task task);
	
	/**
	 * 创建新的任务对象
	 * @return Task
	 */
	Task newTask();
}
