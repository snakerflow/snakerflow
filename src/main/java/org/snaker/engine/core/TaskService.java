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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snaker.engine.ITaskService;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.TaskAccessStrategy;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.DateHelper;
import org.snaker.engine.helper.JsonHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.impl.GeneralAccessStrategy;
import org.snaker.engine.model.NodeModel;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;
import org.snaker.engine.model.TaskModel.PerformType;
import org.snaker.engine.model.TaskModel.TaskType;

/**
 * 任务执行业务类
 * @author yuqs
 * @version 1.0
 */
public class TaskService extends AccessService implements ITaskService {
	private static final String START = "start";
	
	private TaskAccessStrategy strategy = null;

	/**
	 * 完成指定任务
	 */
	public Task complete(String taskId) {
		return complete(taskId, null, null);
	}

	/**
	 * 完成指定任务
	 */
	public Task complete(String taskId, String operator) {
		return complete(taskId, operator, null);
	}
	
	/**
	 * 完成指定任务
	 * 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * @see org.snaker.engine.core.SnakerEngineImpl#executeTask(String, String, Map)
	 */
	public Task complete(String taskId, String operator, Map<String, Object> args) {
		Task task = access().getTask(taskId);
		AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
		task.setVariable(JsonHelper.toJson(args));
		if(!isAllowed(task, operator)) {
			throw new SnakerException("当前参与者[" + operator + "]不允许执行任务[taskId=" + taskId + "]");
		}
		HistoryTask history = new HistoryTask(task);
		history.setFinishTime(DateHelper.getTime());
		history.setTaskState(STATE_FINISH);
		history.setOperator(operator);
		if(history.getActorIds() == null) {
			List<TaskActor> actors = access().getTaskActorsByTaskId(task.getId());
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
	 * 提取指定任务，设置完成时间及操作人，状态不改变
	 */
	public Task take(String taskId, String operator) {
		Task task = access().getTask(taskId);
		AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
		if(!isAllowed(task, operator)) {
			throw new SnakerException("当前参与者[" + operator + "]不允许提取任务[taskId=" + taskId + "]");
		}
		task.setOperator(operator);
		task.setFinishTime(DateHelper.getTime());
		access().updateTask(task);
		return task;
	}
	
	/**
	 * 向指定任务添加参与者
	 */
	public void addTaskActor(String taskId, String... actors) {
		addTaskActor(taskId, null, actors);
	}
	
	/**
	 * 向指定任务添加参与者
	 * 该方法根据performType类型判断是否需要创建新的活动任务
	 */
	public void addTaskActor(String taskId, Integer performType, String... actors) {
		Task task = access().getTask(taskId);
		AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
		if(!task.isMajor()) return;
		if(performType == null) performType = task.getPerformType();
		if(performType == null) performType = 0;
		switch(performType) {
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
					access().saveTask(newTask);
					assignTask(newTask.getId(), actor);
				}
			} catch(CloneNotSupportedException ex) {
				throw new SnakerException("任务对象不支持复制", ex.getCause());
			}
			break;
		default :
			break;
		}
	}
	
	/**
	 * 向指定任务移除参与者
	 */
	public void removeTaskActor(String taskId, String... actors) {
		Task task = access().getTask(taskId);
		AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
		if(task.isMajor()) {
			access().removeTaskActor(task.getId(), actors);
		}
	}
	
	/**
	 * 撤回指定的任务
	 */
	public Task withdrawTask(String taskId, String operator) {
		HistoryTask hist = ServiceContext.getEngine().query().getHistTask(taskId);
		AssertHelper.notNull(hist, "指定的历史任务[id=" + taskId + "]不存在");
		List<Task> tasks = null;
		if(hist.isPerformAny()) {
			tasks = access().getNextActiveTasks(hist.getId());
		} else {
			tasks = access().getNextActiveTasks(hist.getOrderId(), 
					hist.getTaskName(), hist.getParentTaskId());
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
		access().saveTask(task);
		assignTask(task.getId(), task.getOperator());
		return task;
	}
	
	/**
	 * 驳回任务
	 */
	public Task rejectTask(ProcessModel model, Task currentTask) {
		String parentTaskId = currentTask.getParentTaskId();
		if(StringHelper.isEmpty(parentTaskId) || parentTaskId.equals(START)) {
			throw new SnakerException("上一步任务ID为空，无法驳回至上一步处理");
		}
		NodeModel current = model.getNode(currentTask.getTaskName());
		HistoryTask history = access().getHistTask(parentTaskId);
		NodeModel parent = model.getNode(history.getTaskName());
		if(!current.canRejected(parent)) {
			throw new SnakerException("无法驳回至上一步处理，请确认上一步骤并非fork、join、suprocess以及会签任务");
		}

		Task task = history.undoTask();
		task.setId(StringHelper.getPrimaryKey());
		task.setCreateTime(DateHelper.getTime());
		task.setOperator(history.getOperator());
		access().saveTask(task);
		assignTask(task.getId(), task.getOperator());
		return task;
	}

	/**
	 * 对指定的任务分配参与者。参与者可以为用户、部门、角色
	 * @param taskId 任务id
	 * @param actorIds 参与者id集合
	 */
	private void assignTask(String taskId, String... actorIds) {
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
	 * 根据已有任务、任务类型、参与者创建新的任务
	 * 适用于转派，动态协办处理
	 */
	public List<Task> createNewTask(String taskId, int taskType, String... actors) {
		Task task = access().getTask(taskId);
		AssertHelper.notNull(task, "指定的任务[id=" + taskId + "]不存在");
		List<Task> tasks = new ArrayList<Task>();
		try {
			Task newTask = (Task)task.clone();
			newTask.setTaskType(taskType);
			newTask.setCreateTime(DateHelper.getTime());
			newTask.setParentTaskId(taskId);
			tasks.add(saveTask(newTask, PerformType.ANY.ordinal(), actors));
		} catch (CloneNotSupportedException e) {
			throw new SnakerException("任务对象不支持复制", e.getCause());
		}
		return tasks;
	}
	
	/**
	 * 由DBAccess实现类创建task，并根据model类型决定是否分配参与者
	 * @param taskModel 模型
	 * @param execution 执行对象
	 * @return List<Task> 任务列表
	 */
	public List<Task> createTask(TaskModel taskModel, Execution execution) {
		List<Task> tasks = new ArrayList<Task>();
		
		Map<String, Object> args = execution.getArgs();
		if(args == null) args = new HashMap<String, Object>();
		Date expireDate = DateHelper.processTime(args, taskModel.getExpireTime());
		Date remindDate = DateHelper.processTime(args, taskModel.getReminderTime());
		String form = (String)args.get(taskModel.getForm());
		String actionUrl = StringHelper.isEmpty(form) ? taskModel.getForm() : form;
		
		String[] actors = getTaskActors(taskModel, execution);
		Task task = createTaskBase(taskModel, execution);
		task.setActionUrl(actionUrl);
		task.setExpireDate(expireDate);
		task.setExpireTime(DateHelper.parseTime(expireDate));
		task.setVariable(StringHelper.getStringByArray(actors));
		
		if(taskModel.isPerformAny()) {
			//任务执行方式为参与者中任何一个执行即可驱动流程继续流转，该方法只产生一个task
			task = saveTask(task, PerformType.ANY.ordinal(), actors);
			task.setRemindDate(remindDate);
			tasks.add(task);
		} else if(taskModel.isPerformAll()){
			//任务执行方式为参与者中每个都要执行完才可驱动流程继续流转，该方法根据参与者个数产生对应的task数量
			for(String actor : actors) {
				Task singleTask = saveTask(task, PerformType.ALL.ordinal(), actor);
				singleTask.setRemindDate(remindDate);
				tasks.add(singleTask);
			}
		}
		return tasks;
	}
	
	/**
	 * 根据模型、执行对象、任务类型构建基本的task对象
	 * @param model 模型
	 * @param execution 执行对象
	 * @return
	 */
	private Task createTaskBase(TaskModel model, Execution execution) {
		Task task = new Task();
		task.setOrderId(execution.getOrder().getId());
		task.setTaskName(model.getName());
		task.setDisplayName(model.getDisplayName());
		task.setCreateTime(DateHelper.getTime());
		if(model.isMajor()) {
			task.setTaskType(TaskType.Major.ordinal());
		} else {
			task.setTaskType(TaskType.Aidant.ordinal());
		}
		task.setParentTaskId(execution.getTask() == null ? 
				START : execution.getTask().getId());
		task.setModel(model);
		return task;
	}
	
	/**
	 * 由DBAccess实现类持久化task对象
	 */
	public Task saveTask(Task task, int performType, String... actors) {
		task.setId(StringHelper.getPrimaryKey());
		task.setPerformType(PerformType.ANY.ordinal());
		access().saveTask(task);
		assignTask(task.getId(), actors);
		task.setActorIds(actors);
		return task;
	}

	/**
	 * 根据Task模型的assignee、assignmentHandler属性以及运行时数据，确定参与者
	 * @param model 模型
	 * @param execution 执行对象
	 * @return
	 */
	private String[] getTaskActors(TaskModel model, Execution execution) {
		if(StringHelper.isEmpty(model.getAssignee())) return null;
		Object assigneeObject = null;
		if(StringHelper.isNotEmpty(model.getAssignee())) {
			assigneeObject = execution.getArgs().get(model.getAssignee());
		} else if(model.getAssignmentHandlerObject() != null) {
			assigneeObject = model.getAssignmentHandlerObject().assign(execution);
		}
		return assigneeObject == null ? new String[]{model.getAssignee() } : 
			getTaskActors(assigneeObject);
	}

	/**
	 * 根据taskmodel指定的assignee属性，从args中取值
	 * 将取到的值处理为String[]类型。
	 * @param actors
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
		} else if(actors instanceof Integer) {
			//如果为Long类型，则返回1个元素的String[]
			results = new String[1];
			results[0] = String.valueOf((Integer)actors);
			return results;
		} else if(actors instanceof String[]) {
			//如果为String[]类型，则直接返回
			return (String[])actors;
		} else {
			//其它类型，抛出不支持的类型异常
			throw new SnakerException("任务参与者对象[" + actors + "]类型不支持."
					+ "合法参数示例:Long,new String[]{},'10000,20000'");
		}
	}

	/**
	 * 判断当前操作人operator是否允许执行taskId指定的任务
	 */
	public boolean isAllowed(Task task, String operator) {
		if(StringHelper.isNotEmpty(operator)) {
			if(SnakerEngine.ADMIN.equalsIgnoreCase(operator)
					|| SnakerEngine.AUTO.equalsIgnoreCase(operator)) {
				return true;
			}
			if(StringHelper.isNotEmpty(task.getOperator())) {
				return operator.equals(task.getOperator());
			}
		}
		List<TaskActor> actors = access().getTaskActorsByTaskId(task.getId());
		if(actors == null || actors.isEmpty()) return true;
		if(StringHelper.isEmpty(operator)) return false;
		return getStrategy().isAllowed(operator, actors);
	}

	public void setStrategy(TaskAccessStrategy strategy) {
		this.strategy = strategy;
	}

	public TaskAccessStrategy getStrategy() {
		if(strategy == null) {
			strategy = ServiceContext.getContext().find(TaskAccessStrategy.class);
		}
		if(strategy == null) {
			strategy = new GeneralAccessStrategy();
			ServiceContext.getContext().put(strategy.getClass().getName(), strategy);
		}
		return strategy;
	}
}
