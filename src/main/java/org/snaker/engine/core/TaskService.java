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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.snaker.engine.AssignmentHandler;
import org.snaker.engine.ITaskService;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.helper.DateHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.BaseModel;
import org.snaker.engine.model.CustomModel;
import org.snaker.engine.model.NodeModel;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;
import org.snaker.engine.model.TaskModel.PerformType;

/**
 * 任务执行业务类
 * @author yuqs
 * @version 1.0
 */
public class TaskService extends AccessService implements ITaskService {
	private static final String START = "start";
	/**
	 * 任务类型
	 */
	public enum TaskType {
		Task, Custom;
	}
	/**
	 * 由DBAccess实现类更新task对象为完成状态
	 */
	@Override
	public Task completeTask(Task task) {
		return completeTask(task, null);
	}
	/**
	 * 由DBAccess实现类更新task对象为完成状态
	 */
	@Override
	public Task completeTask(Task task, String operator) {
		HistoryTask history = new HistoryTask(task);
		history.setFinishTime(DateHelper.getTime());
		history.setTaskState(STATE_FINISH);
		history.setOperator(operator);
		if(history.getActorIds() == null) {
			List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
			String[] actorIds = new String[actors.size()];
			for(int i = 0; i < actors.size(); i++) {
				actorIds[i] = actors.get(i).getActorId();
			}
			history.setActorIds(actorIds);
		}
		access().saveHistory(history);
		access().deleteTask(task);
		return task;
	}
	/**
	 * 由DBAccess实现类更新task对象为完成状态
	 */
	@Override
	public Task takeTask(Task task, String operator) {
		task.setOperator(operator);
		task.setFinishTime(DateHelper.getTime());
		access().updateTask(task);
		return task;
	}
	
	@Override
	public void addTaskActor(Task task, String... actors) {
		int performType = task.getPerformType() == null ? -1 : task.getPerformType().intValue();
		switch(performType) {
		case -1:
			break;
		case 0:
			assignTask(task.getId(), actors);
			break;
		case 1:
			try {
				for(String actor : actors) {
					Task newTask = (Task)task.clone();
					newTask.setId(StringHelper.getPrimaryKey());
					newTask.setCreateTime(DateHelper.getTime());
					newTask.setOperator(actor);
					saveTask(newTask);
					assignTask(newTask.getId(), actor);
				}
			} catch(CloneNotSupportedException ex) {
				throw new SnakerException("任务对象不支持复制", ex.getCause());
			}
			break;
		}
	}
	
	@Override
	public void removeTaskActor(Task task, String... actors) {
		access().removeTaskActor(task.getId(), actors);
	}
	
	@Override
	public Task withdrawTask(ProcessModel model, HistoryTask hist, String operator) {
		TaskModel histModel = (TaskModel)model.getNode(hist.getTaskName());
		List<Task> tasks = null;
		if(TaskModel.TYPE_ANY.equalsIgnoreCase(histModel.getPerformType())) {
			tasks = access().getNextActiveTasks(hist.getId());
		} else {
			tasks = access().getNextActiveTasks(hist.getOrderId(), hist.getTaskName(), hist.getParentTaskId());
		}
		if(tasks == null || tasks.isEmpty()) {
			throw new SnakerException("后续活动任务已完成或不存在，无法撤回.");
		}
		for(Task task : tasks) {
			access().deleteTask(task);
		}
		
		Task task = hist.undoTask();
		task.setId(StringHelper.getPrimaryKey());
		task.setCreateTime(DateHelper.getTime());
		saveTask(task);
		assignTask(task.getId(), task.getOperator());
		return task;
	}
	
	@Override
	public Task rejectTask(ProcessModel model, Task currentTask) {
		String parentTaskId = currentTask.getParentTaskId();
		if(StringHelper.isEmpty(parentTaskId)) {
			throw new SnakerException("上一步任务ID为空，无法驳回至上一步处理");
		}
		NodeModel current = model.getNode(currentTask.getTaskName());
		HistoryTask history = getHistoryTask(parentTaskId);
		NodeModel parent = model.getNode(history.getTaskName());
		if(!current.canRejected(parent)) {
			throw new SnakerException("无法驳回至上一步处理，请确认上一步骤并非fork、join、suprocess以及会签任务");
		}

		Task task = history.undoTask();
		task.setId(StringHelper.getPrimaryKey());
		task.setCreateTime(DateHelper.getTime());
		task.setOperator(history.getOperator());
		saveTask(task);
		assignTask(task.getId(), task.getOperator());
		return task;
	}

	/**
	 * 由DBAccess实现类分派task的参与者。关联taskActor对象
	 */
	@Override
	public void assignTask(String taskId, String... actorIds) {
		if(actorIds == null || actorIds.length == 0) return;
		for(String actorId : actorIds) {
			//修复当actorId为null的bug
			if(StringHelper.isEmpty(actorId)) continue;
			TaskActor taskActor = new TaskActor();
			taskActor.setTaskId(taskId);
			taskActor.setActorId(actorId);
			access().saveTaskActor(taskActor);
		}
	}

	/**
	 * 由DBAccess实现类根据taskId获取task对象
	 */
	@Override
	public Task getTask(String taskId) {
		return access().getTask(taskId);
	}
	
	/**
	 * 由DBAccess实现类根据taskId获取historytask对象
	 */
	@Override
	public HistoryTask getHistoryTask(String taskId) {
		return access().getHistTask(taskId);
	}
	
	/**
	 * 由DBAccess实现类创建task，并根据model类型决定是否分配参与者
	 * @param model 模型
	 * @param order 流程实例对象
	 * @param args 执行参数
	 * @return List<Task> 任务列表
	 */
	@Override
	public List<Task> createTask(TaskModel taskModel, Execution execution) {
		List<Task> tasks = new ArrayList<Task>();
		
		Map<String, Object> args = execution.getArgs();
		String expireTime = null;
		if(args != null && !args.isEmpty()) {
			expireTime = DateHelper.parseTime(args.get(taskModel.getExpireTime()));
		}
		String[] actors = getTaskActors(taskModel.getAssignee(), args, taskModel.getAssignmentHandler(), execution);
		
		String type = taskModel.getPerformType();
		if(type == null || type.equalsIgnoreCase(TaskModel.TYPE_ANY)) {
			//任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
			Task task = createTask(taskModel, execution, PerformType.ANY.ordinal(), expireTime, actors);
			tasks.add(task);
		} else {
			//任务执行方式为参与者中每个都要执行完才可驱动流程继续流转，该方法根据参与者个数产生对应的task数量
			for(String actor : actors) {
				Task ftask = createTask(taskModel, execution, PerformType.ALL.ordinal(), expireTime, actor);
				tasks.add(ftask);
			}
		}
		return tasks;
	}
	
	/**
	 * 由自定义模型创建任务
	 * @param customModel
	 * @param order
	 * @return
	 */
	@Override
	public List<Task> createTask(CustomModel customModel, Execution execution) {
		List<Task> tasks = new ArrayList<Task>();
		Task task = createTask(customModel, execution, TaskType.Custom.ordinal());
		saveTask(task);
		tasks.add(task);
		return tasks;
	}
	
	/**
	 * 由任务模型创建任务
	 * @param taskModel 任务模型
	 * @param order 流程实例对象
	 * @param type 任务类型
	 * @param expireTime 期望完成时间
	 * @param actors 任务参与者集合
	 * @return
	 */
	private Task createTask(TaskModel taskModel, Execution execution, int performType, String expireTime, String... actors) {
		Task task = createTask(taskModel, execution, TaskType.Task.ordinal());
		task.setActionUrl(taskModel.getUrl());
		task.setExpireTime(expireTime);
		task.setPerformType(performType);
		task.setVariable(StringHelper.getStringByArray(actors));
		saveTask(task);
		assignTask(task.getId(), actors);
		task.setActorIds(actors);
		return task;
	}
	
	/**
	 * 根据模型、执行对象、任务类型构建基本的task对象
	 * @param model
	 * @param execution
	 * @param taskType
	 * @return
	 */
	private Task createTask(BaseModel model, Execution execution, int taskType) {
		Task task = newTask();
		task.setOrderId(execution.getOrder().getId());
		task.setTaskName(model.getName());
		task.setDisplayName(model.getDisplayName());
		task.setCreateTime(DateHelper.getTime());
		task.setTaskType(taskType);
		task.setParentTaskId(execution.getTask() == null ? START : execution.getTask().getId());
		return task;
	}
	
	/**
	 * 由DBAccess实现类持久化task对象
	 */
	@Override
	public void saveTask(Task task) {
		access().saveTask(task);
	}

	/**
	 * 新建task对象，并设置初始化状态、类型
	 */
	@Override
	public Task newTask() {
		Task task = new Task();
		task.setId(StringHelper.getPrimaryKey());//uuid
		return task;
	}
	
	/**
	 * 根据Task模型的assignee、assignmentHandler属性以及运行时数据，确定参与者
	 * @param assignee
	 * @param args
	 * @param handler
	 * @param execution
	 * @return
	 */
	private String[] getTaskActors(String assignee, Map<String, Object> args, AssignmentHandler handler, Execution execution) {
		Object assigneeObject = null;
		if(StringHelper.isNotEmpty(assignee) && args != null && !args.isEmpty()) {
			assigneeObject = args.get(assignee);
		} else if(handler != null) {
			assigneeObject = handler.assign(execution);
		}
		return assigneeObject == null ? new String[]{assignee } : getTaskActors(assigneeObject);
	}

	/**
	 * 根据taskmodel指定的assignee属性，从args中取值
	 * 将取到的值处理为String[]类型。
	 * @param actors
	 * @param key
	 * @return
	 */
	private String[] getTaskActors(Object actors) {
		if(actors == null) return null;
		String[] results = null;
		if(actors instanceof String) {
			//如果值为字符串类型，则使用逗号,分隔，并解析为Long类型
			String[] actorStrs = ((String)actors).split(",");
			results = new String[actorStrs.length];
			for(int i = 0; i < actorStrs.length; i++) {
				results[i] = actorStrs[i];
			}
			return results;
		} else if(actors instanceof Long) {
			//如果为Long类型，则返回1个元素的String[]
			results = new String[1];
			results[0] = String.valueOf((Long)actors);
			return results;
		} else if(actors instanceof String[]) {
			//如果为String[]类型，则直接返回
			return (String[])actors;
		} else {
			//其它类型，抛出不支持的类型异常
			throw new SnakerException("任务参与者对象[" + actors + "]类型不支持.合法参数示例:Long,new String[]{},'10000,20000'");
		}
	}

	/**
	 * 根据taskId返回所有的任务参与对象集合
	 */
	@Override
	public List<TaskActor> getTaskActorsByTaskId(String taskId) {
		return access().getTaskActorsByTaskId(taskId);
	}

	/**
	 * 判断当前操作人operator是否允许执行taskId指定的任务
	 */
	@Override
	public boolean isAllowed(Task task, String operator) {
		if(StringHelper.isNotEmpty(operator)) {
			if(SnakerEngine.ADMIN.equalsIgnoreCase(operator)) {
				return true;
			}
			if(StringHelper.isNotEmpty(task.getOperator())) {
				return operator.equals(task.getOperator());
			}
		}
		List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
		if(actors == null || actors.isEmpty()) return true;
		if(StringHelper.isEmpty(operator)) return false;
		boolean isAllowed = false;
		for(TaskActor actor : actors) {
			if(actor.getActorId().equals(operator)) {
				isAllowed = true;
				break;
			}
		}
		return isAllowed;
	}
}
