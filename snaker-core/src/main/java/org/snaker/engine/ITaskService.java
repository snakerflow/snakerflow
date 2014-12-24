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
import java.util.Map;

import org.snaker.engine.core.Execution;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.snaker.engine.model.CustomModel;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;

/**
 * 任务业务类，包括以下服务：
 * 1、创建任务
 * 2、添加、删除参与者
 * 3、完成任务
 * 4、撤回任务
 * 5、回退任务
 * 6、提取任务
 * @author yuqs
 * @since 1.0
 */
public interface ITaskService {
	/**
	 * 完成指定的任务，删除活动任务记录，创建历史任务
	 * @param taskId 任务id
	 * @return Task 任务对象
	 */
	Task complete(String taskId);
	/**
	 * 完成指定的任务，删除活动任务记录，创建历史任务
	 * @param taskId 任务id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	Task complete(String taskId, String operator);
	
	/**
	 * 根据任务主键ID，操作人ID完成任务
	 * @param taskId 任务id
	 * @param operator 操作人id
	 * @param args 参数集合
	 * @return Task 任务对象
	 */
	Task complete(String taskId, String operator, Map<String, Object> args);

	/**
	 * 更新任务对象
	 * @param task 任务对象
	 */
	void updateTask(Task task);
	/**
	 * 根据执行对象、自定义节点模型创建历史任务记录
	 * @param execution 执行对象
	 * @param model 自定义节点模型
	 * @return 历史任务
	 */
	HistoryTask history(Execution execution, CustomModel model);
	
	/**
	 * 根据任务主键ID，操作人ID提取任务
	 * 提取任务相当于预受理操作，仅仅标识此任务只能由此操作人处理
	 * @param taskId 任务id
	 * @param operator 操作人id
	 * @return Task 任务对象
	 */
	Task take(String taskId, String operator);

    /**
     * 根据历史任务主键id，操作人唤醒历史任务
     * 该方法会导致流程状态不可控，请慎用
     * @param taskId 历史任务id
     * @param operator 操作人id
     * @return Task 唤醒后的任务对象
     */
    Task resume(String taskId, String operator);
	
	/**
	 * 向指定的任务id添加参与者
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	void addTaskActor(String taskId, String... actors);
	
	/**
	 * 向指定的任务id添加参与者
	 * @param taskId 任务id
	 * @param performType 参与类型
	 * @param actors 参与者
	 */
	void addTaskActor(String taskId, Integer performType, String... actors);
	
	/**
	 * 对指定的任务id删除参与者
	 * @param taskId 任务id
	 * @param actors 参与者
	 */
	void removeTaskActor(String taskId, String... actors);
	
	/**
	 * 根据任务主键id、操作人撤回任务
	 * @param taskId 任务id
	 * @param operator 操作人
	 * @return Task 任务对象
	 */
	Task withdrawTask(String taskId, String operator);
	
	/**
	 * 根据当前任务对象驳回至上一步处理
	 * @param model 流程定义模型，用以获取上一步模型对象
	 * @param currentTask 当前任务对象
	 * @return Task 任务对象
	 */
	Task rejectTask(ProcessModel model, Task currentTask);
	
	/**
	 * 根据taskId、operator，判断操作人operator是否允许执行任务
	 * @param task 任务对象
	 * @param operator 操作人
	 * @return boolean 是否允许操作
	 */
	boolean isAllowed(Task task, String operator);
	
	/**
	 * 根据任务模型、执行对象创建新的任务
	 * @param taskModel 任务模型
	 * @param execution 执行对象
	 * @return List<Task> 创建任务集合
	 */
	List<Task> createTask(TaskModel taskModel, Execution execution);
	
	/**
	 * 根据已有任务id、任务类型、参与者创建新的任务
	 * @param taskId 主办任务id
	 * @param taskType 任务类型
	 * @param actors 参与者集合
	 * @return List<Task> 创建任务集合
	 */
	List<Task> createNewTask(String taskId, int taskType, String... actors);

    /**
     * 根据任务id获取任务模型
     * @param taskId 任务id
     * @return
     */
    TaskModel getTaskModel(String taskId);
}
