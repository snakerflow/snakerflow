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

import java.util.List;

import org.snaker.engine.IQueryService;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.Page;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.HistoryTaskActor;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.entity.WorkItem;

/**
 * 查询服务实现类
 * @author yuqs
 * @version 1.0
 */
public class QueryService extends AccessService implements IQueryService {
	@Override
	public Order getOrder(String orderId) {
		return access().getOrder(orderId);
	}
	
	@Override
	public Task getTask(String taskId) {
		return access().getTask(taskId);
	}
	
	@Override
	public String[] getTaskActorsByTaskId(String taskId) {
		List<TaskActor> actors = access().getTaskActorsByTaskId(taskId);
		if(actors == null || actors.isEmpty()) return null;
		String[] actorIds = new String[actors.size()];
		for(int i = 0; i < actors.size(); i++) {
			TaskActor ta = actors.get(i);
			actorIds[i] = ta.getActorId();
		}
		return actorIds;
	}
	
	@Override
	public String[] getHistoryTaskActorsByTaskId(String taskId) {
		List<HistoryTaskActor> actors = access().getHistTaskActorsByTaskId(taskId);
		if(actors == null || actors.isEmpty()) return null;
		String[] actorIds = new String[actors.size()];
		for(int i = 0; i < actors.size(); i++) {
			HistoryTaskActor ta = actors.get(i);
			actorIds[i] = ta.getActorId();
		}
		return actorIds;
	}

	@Override
	public HistoryOrder getHistOrder(String orderId) {
		return access().getHistOrder(orderId);
	}

	@Override
	public HistoryTask getHistTask(String taskId) {
		return access().getHistTask(taskId);
	}
	
	@Override
	public List<Task> getActiveTasksByActors(String... actorIds) {
		return access().getActiveTasks(null, actorIds);
	}
	
	@Override
	public List<Task> getActiveTasks(Page<Task> page, String... actorIds) {
		if(actorIds == null || actorIds.length == 0) throw new SnakerException("查询任务列表，需要提供任务参与者ID");
		return access().getActiveTasks(page, actorIds);
	}
	
	@Override
	public List<Task> getActiveTasks(String orderId,String... taskNames) {
		return access().getActiveTasks(orderId, null, taskNames);
	}

	@Override
	public List<Task> getActiveTasks(String orderId, String excludedTaskId, String... taskNames) {
		return access().getActiveTasks(orderId, excludedTaskId, taskNames);
	}

	@Override
	public List<Order> getActiveOrders(String... processIds) {
		return getActiveOrders(null, processIds);
	}
	
	@Override
	public List<Order> getActiveOrdersByParentId(String parentId, String... excludedId) {
		return access().getActiveOrdersByParentId(parentId, excludedId);
	}

	@Override
	public List<Order> getActiveOrders(Page<Order> page, String... processIds) {
		return access().getActiveOrders(page, processIds);
	}
	
	@Override
	public List<WorkItem> getWorkItems(Page<WorkItem> page, String processId, String... actorIds) {
		return access().getWorkItems(page, processId, actorIds);
	}

	@Override
	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page,
			String... processIds) {
		return access().getHistoryOrders(page, processIds);
	}

	@Override
	public List<HistoryOrder> getHistoryOrdersByParentId(String parentId) {
		return access().getHistoryOrdersByParentId(parentId);
	}

	@Override
	public List<HistoryTask> getHistoryTasks(String orderId) {
		return access().getHistoryTasks(orderId);
	}

	@Override
	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page,
			String... actorIds) {
		return access().getHistoryTasks(page, actorIds);
	}

	@Override
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page,
			String processId, String... actorIds) {
		return access().getHistoryWorkItems(page, processId, actorIds);
	}
}
