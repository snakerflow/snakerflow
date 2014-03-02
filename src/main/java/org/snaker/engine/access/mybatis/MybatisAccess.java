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
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.HistoryTaskActor;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.entity.WorkItem;
import org.snaker.engine.helper.StringHelper;

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
	
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof SqlSessionFactory) {
			this.sqlSessionFactory = (SqlSessionFactory)accessObject;
		}
	}
	
	private SqlSession getSession() {
		return MybatisHelper.getSession(sqlSessionFactory);
	}

	public boolean isORM() {
		return true;
	}

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

	public void saveProcess(Process process) {
		getSession().insert(process.getClass().getSimpleName() + "." + SAVE, process);
	}
	
	public void updateProcess(Process process) {
		getSession().update(process.getClass().getSimpleName() + "." + UPDATE, process);
	}

	public void deleteTask(Task task) {
		List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
		for(TaskActor actor : actors) {
			getSession().delete("TaskActor.DELETE", actor.getTaskId());
		}
		getSession().delete("Task.DELETE", task);
	}

	public void deleteOrder(Order order) {
		getSession().update("Order.DELETE", order);
	}
	
	public void removeTaskActor(String taskId, String... actors) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		params.put("actorIds", actors);
		getSession().delete("TaskActor.REDUCE", params);
	}
	
	public Task getTask(String taskId) {
		return getSession().selectOne("Task.SELECTONE", taskId);
	}
	
	public List<Task> getNextActiveTasks(String parentTaskId) {
		return getSession().selectList("Task.SELECTBYPARENT", parentTaskId);
	}
	
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("taskName", taskName);
		params.put("parentTaskId", parentTaskId);
		return getSession().selectList("Query.getNextActiveTasks", params);
	}
	
	public HistoryTask getHistTask(String taskId) {
		return getSession().selectOne("HistoryTask.SELECTONE", taskId);
	}
	
	public HistoryOrder getHistOrder(String orderId) {
		return getSession().selectOne("HistoryOrder.SELECTONE", orderId);
	}

	public List<TaskActor> getTaskActorsByTaskId(String taskId) {
		return getSession().selectList("TaskActor.SELECTLIST", taskId);
	}

	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId) {
		return getSession().selectList("HistoryTaskActor.SELECTLIST", taskId);
	}
	
	public Order getOrder(String orderId) {
		return getSession().selectOne("Order.SELECTONE", orderId);
	}

	public Process getProcess(String idName) {
		return getSession().selectOne("Process.SELECTONE", idName);
	}
	
	public List<Process> getProcesss(Page<Process> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("names", filter.getNames());
		params.put("displayName", filter.getDisplayName());
		params.put("state", filter.getState());
		params.put("version", filter.getVersion());
		buildPageParameter(session, page, params, "Process.getProcesssCount");
		List<Process> list = session.selectList("Process.getProcesss", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}
	
	public List<Order> getActiveOrders(Page<Order> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", filter.getParentId());
		params.put("excludedIds", filter.getExcludedIds());
		params.put("processId", filter.getProcessId());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		params.put("orderNo", filter.getOrderNo());
		buildPageParameter(session, page, params, "Query.getActiveOrdersCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by create_Time desc ");
		}
		List<Order> list = session.selectList("Query.getActiveOrders", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}
	
	public List<Task> getActiveTasks(Page<Task> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", filter.getOperators());
		params.put("excludedIds", filter.getExcludedIds());
		params.put("orderId", filter.getOrderId());
		params.put("taskNames", filter.getNames());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		buildPageParameter(session, page, params, "Query.getActiveTasksCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by create_Time desc ");
		}
		List<Task> list = session.selectList("Query.getActiveTasks", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}
	
	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", filter.getProcessId());
		params.put("parentId", filter.getParentId());
		params.put("orderNo", filter.getOrderNo());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		buildPageParameter(session, page, params, "HistoryQuery.getHistoryOrdersCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by create_Time desc ");
		}
		List<HistoryOrder> list = session.selectList("HistoryQuery.getHistoryOrders", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", filter.getOperators());
		params.put("orderId", filter.getOrderId());
		params.put("taskNames", filter.getNames());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		buildPageParameter(session, page, params, "HistoryQuery.getHistoryTasksCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by create_Time desc ");
		}
		List<HistoryTask> list = session.selectList("HistoryQuery.getHistoryTasks", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	public List<WorkItem> getWorkItems(Page<WorkItem> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("actorIds", filter.getOperators());
		params.put("processId", filter.getProcessId());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		params.put("parentId", filter.getParentId());
		params.put("taskType", filter.getTaskType());
		params.put("performType", filter.getPerformType());
		buildPageParameter(session, page, params, "Query.getWorkItemsCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by t.create_Time desc ");
		}
		List<WorkItem> list = session.selectList("Query.getWorkItems", params);
		if(page != null) {
			page.setResult(list);
		}
		return list;
	}

	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, QueryFilter filter) {
		SqlSession session = getSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actorIds", filter.getOperators());
		params.put("processId", filter.getProcessId());
		params.put("createTimeStart", filter.getCreateTimeStart());
		params.put("createTimeEnd", filter.getCreateTimeEnd());
		params.put("parentId", filter.getParentId());
		params.put("taskType", filter.getTaskType());
		params.put("performType", filter.getPerformType());
		buildPageParameter(session, page, params, "HistoryQuery.getWorkItemsCount");
		if(page != null && !page.isOrderBySetted()) {
			params.put("orderby", " order by t.create_Time desc ");
		}
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
			// modify by HuangS at 2014-01-28 变getSession取session为直接使用session
			Long count = session.selectOne(statement, params);
			page.setTotalCount(count);
			if(page.isOrderBySetted()) {
				params.put("orderby", StringHelper.buildPageOrder(page.getOrder(), page.getOrderBy()));
			}
		}
	}

	public <T> T queryObject(Class<T> T, String sql, Object... args) {
		// not needed in this version
		return null;
	}

	public <T> List<T> queryList(Class<T> T, String sql, Object... args) {
		// not needed in this version
		return null;
	}

	public <T> List<T> queryList(Page<T> page, Class<T> T, String sql,
			Object... args) {
		// not needed in this version
		return null;
	}
}
