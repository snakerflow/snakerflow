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
package org.snaker.engine.access.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.snaker.engine.access.AbstractDBAccess;
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
 * mybatis方式的数据库访问
 * @author yuqs
 * @version 1.0
 */
public class MybatisAccess extends AbstractDBAccess {
	/**
	 * mybatis的sqlSessionFactory
	 */
	private SqlSessionFactory sqlSessionFactory;
	/**
	 * setter
	 * @param sqlSessionFactory
	 */
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof SqlSessionFactory) {
			this.sqlSessionFactory = (SqlSessionFactory)accessObject;
		}
	}
	
	private SqlSession getSession() {
		return MybatisHelper.getSession(sqlSessionFactory);
	}

	@Override
	public boolean isORM() {
		return true;
	}

	@Override
	public void saveOrUpdate(Map<String, Object> map) {
		Object object = map.get(KEY_ENTITY);
		String className = object.getClass().getSimpleName();
		String su = (String)map.get(KEY_SU);
		String statement = className + "." + su;
		if(SAVE.equals(su)) {
			getSession().insert(statement, object);
		} else if(UPDATE.equals(su)) {
			getSession().update(statement, object);
		}
	}

	@Override
	public void saveProcess(Process process) {
		getSession().insert(process.getClass().getSimpleName() + "." + SAVE, process);
	}

	@Override
	public void updateProcess(Process process) {
		getSession().update(process.getClass().getSimpleName() + "." + UPDATE, process);
	}

	@Override
	public void deleteTask(Task task) {
		List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
		for(TaskActor actor : actors) {
			getSession().delete("TaskActor.DELETE", actor.getTaskId());
		}
		getSession().delete("Task.DELETE", task);
	}

	@Override
	public void deleteOrder(Order order) {
		getSession().update("Order.DELETE", order);
	}
	
	@Override
	public void removeTaskActor(String taskId, String... actors) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		params.put("actorIds", actors);
		getSession().delete("TaskActor.REDUCE", params);
	}

	@Override
	public Task getTask(String taskId) {
		return getSession().selectOne("Task.SELECTONE", taskId);
	}
	
	@Override
	public List<Task> getNextActiveTasks(String parentTaskId) {
		return getSession().selectList("Task.SELECTBYPARENT", parentTaskId);
	}
	
	@Override
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("taskName", taskName);
		params.put("parentTaskId", parentTaskId);
		return getSession().selectList("Query.getNextActiveTasks", params);
	}
	
	@Override
	public HistoryTask getHistTask(String taskId) {
		return getSession().selectOne("HistoryTask.SELECTONE", taskId);
	}
	
	@Override
	public HistoryOrder getHistOrder(String orderId) {
		return getSession().selectOne("HistoryOrder.SELECTONE", orderId);
	}

	@Override
	public List<TaskActor> getTaskActorsByTaskId(String taskId) {
		return getSession().selectList("TaskActor.SELECTLIST", taskId);
	}

	@Override
	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId) {
		return getSession().selectList("HistoryTaskActor.SELECTLIST", taskId);
	}

	@Override
	public Order getOrder(String orderId) {
		return getSession().selectOne("Order.SELECTONE", orderId);
	}

	@Override
	public Process getProcess(String idName) {
		return getSession().selectOne("Process.SELECTONE", idName);
	}

	@Override
	public List<Process> getAllProcess() {
		return getSession().selectList("Process.SELECTLIST");
	}

	@Override
	public List<Order> getActiveOrdersByParentId(String parentId, String... excludedId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("excludedId", excludedId);
		return getSession().selectList("Query.getActiveOrdersByParentId", params);
	}

	@Override
	public List<Task> getActiveTasks(String orderId, String excludedTaskId, String... taskNames) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("excludedTaskId", excludedTaskId);
		params.put("taskNames", taskNames);
		return getSession().selectList("Query.getActiveTasksByTaskNames", params);
	}

	@Override
	public List<Process> getProcesss(Page<Process> page, String name, Integer state) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("state", state);
		buildPageParameter(session, page, params, "Process.getProcesssCount");
		List<Process> list = session.selectList("Process.getProcesss", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<Order> getActiveOrders(Page<Order> page, String... processId) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", processId);
		buildPageParameter(session, page, params, "Query.getActiveOrdersCount");
		List<Order> list = session.selectList("Query.getActiveOrders", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<Task> getActiveTasks(Page<Task> page, String... actorIds) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", actorIds);
		buildPageParameter(session, page, params, "Query.getActiveTasksCount");
		params.put("orderby", " order by create_Time desc ");
		List<Task> list = session.selectList("Query.getActiveTasks", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<WorkItem> getWorkItems(Page<WorkItem> page, String processId,
			String... actorIds) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", actorIds);
		params.put("processId", processId);
		buildPageParameter(session, page, params, "Query.getWorkItemsCount");
		params.put("orderby", " order by t.create_Time desc ");
		List<WorkItem> list = session.selectList("Query.getWorkItems", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page,
			String... processIds) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processIds", processIds);
		buildPageParameter(session, page, params, "HistoryQuery.getHistoryOrdersCount");
		List<HistoryOrder> list = session.selectList("HistoryQuery.getHistoryOrders", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<HistoryOrder> getHistoryOrdersByParentId(String parentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		return getSession().selectList("HistoryQuery.getHistoryOrdersByParentId", params);
	}

	@Override
	public List<HistoryTask> getHistoryTasks(String orderId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		return getSession().selectList("HistoryQuery.getHistoryTasksByOrderId", params);
	}

	@Override
	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page,
			String... actorIds) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", actorIds);
		buildPageParameter(session, page, params, "HistoryQuery.getHistoryTasksCount");
		params.put("orderby", " order by create_Time desc ");
		List<HistoryTask> list = session.selectList("HistoryQuery.getHistoryTasks", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	@Override
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page,
			String processId, String... actorIds) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", actorIds);
		params.put("processId", processId);
		buildPageParameter(session, page, params, "HistoryQuery.getWorkItemsCount");
		params.put("orderby", " order by t.create_Time desc ");
		List<WorkItem> list = session.selectList("HistoryQuery.getWorkItems", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}
	
	private void buildPageParameter(SqlSession session, Page<?> page, Map<String, Object> params, String statement) {
		if(page != null) {
			params.put("limitBefore", getDialect().getPageBefore(page.getPageNo(), page.getPageSize()));
			params.put("limitAfter", getDialect().getPageAfter(page.getPageNo(), page.getPageSize()));
			Long count = getSession().selectOne(statement, params);
			page.setTotalCount(count);
		}
	}

	@Override
	public <T> T queryObject(Class<T> T, String sql, Object... args) {
		// not needed in this version
		return null;
	}

	@Override
	public <T> List<T> queryList(Class<T> T, String sql, Object... args) {
		// not needed in this version
		return null;
	}

	@Override
	public <T> List<T> queryList(Page<T> page, Class<T> T, String sql,
			Object... args) {
		// not needed in this version
		return null;
	}
}
