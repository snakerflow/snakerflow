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
package org.snaker.engine.access;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snaker.engine.DBAccess;
import org.snaker.engine.access.dialect.Dialect;
import org.snaker.engine.core.ServiceContext;
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
 * 抽象数据库访问类
 * 封装SQL语句的构造
 * @author yuqs
 * @version 1.0
 */
public abstract class AbstractDBAccess implements DBAccess {
	protected static final String KEY_SQL = "SQL";
	protected static final String KEY_ARGS = "ARGS";
	protected static final String KEY_TYPE = "TYPE";
	protected static final String KEY_ENTITY = "ENTITY";
	
	protected static final String KEY_SU = "SU";
	protected static final String SAVE = "SAVE";
	protected static final String UPDATE = "UPDATE";
	
	protected static final String PROCESS_INSERT = "insert into wf_process (id,parent_Id,name,display_Name,type,instance_Url,query_Url,state,version) values (?,?,?,?,?,?,?,?,0)";
	protected static final String PROCESS_UPDATE = "update wf_process set name=?, display_Name=?,state=?,instance_Url=?,query_Url=? where id=?";
	protected static final String PROCESS_UPDATE_BLOB = "update wf_process set content=? where id=?";
	
	protected static final String ORDER_INSERT = "insert into wf_order (id,process_Id,creator,create_Time,parent_Id,parent_Node_Name,expire_Time,last_Update_Time,last_Updator,order_No,variable,version) values (?,?,?,?,?,?,?,?,?,?,?,0)";
	protected static final String ORDER_UPDATE = "update wf_order set last_Updator=?, last_Update_Time=?, version = version + 1 where id=? and version = ?";
	protected static final String ORDER_HISTORY_INSERT = "insert into wf_hist_order (id,process_Id,order_State,creator,create_Time,end_Time,parent_Id,expire_Time,order_No,variable) values (?,?,?,?,?,?,?,?,?,?)";
	protected static final String ORDER_HISTORY_UPDATE = "update wf_hist_order set order_State = ?, end_Time = ? where id = ? ";
	protected static final String ORDER_DELETE = "delete from wf_order where id = ?";
	
	protected static final String TASK_INSERT = "insert into wf_task (id,order_Id,task_Name,display_Name,task_Type,perform_Type,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable,version) values (?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
	protected static final String TASK_UPDATE = "update wf_task set finish_Time=?, operator=?, version = version + 1 where id=? and version = ?";
	protected static final String TASK_HISTORY_INSERT = "insert into wf_hist_task (id,order_Id,task_Name,display_Name,task_Type,perform_Type,task_State,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	protected static final String TASK_DELETE = "delete from wf_task where id = ?";
	
	protected static final String TASK_ACTOR_INSERT = "insert into wf_task_actor (task_Id, actor_Id) values (?, ?)";
	protected static final String TASK_ACTOR_HISTORY_INSERT = "insert into wf_hist_task_actor (task_Id, actor_Id) values (?, ?)";
	protected static final String TASK_ACTOR_DELETE = "delete from wf_task_actor where task_Id = ?";
	protected static final String TASK_ACTOR_REDUCE = "delete from wf_task_actor where task_Id = ? and actor_Id = ?";
	
	protected static final String QUERY_PROCESS = "select id,parent_Id,name,display_Name,type,instance_Url,query_Url,state, content, version from wf_process ";
	protected static final String QUERY_ORDER = "select id,process_Id,creator,create_Time,parent_Id,parent_Node_Name,expire_Time,last_Update_Time,last_Updator,priority,order_No,variable, version from wf_order ";
	protected static final String QUERY_TASK = "select id,order_Id,task_Name,display_Name,task_Type,perform_Type,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable, version from wf_task ";
	protected static final String QUERY_TASK_ACTOR = "select task_Id, actor_Id from wf_task_actor ";
	
	protected static final String QUERY_HIST_ORDER = "select id,process_Id,order_State,priority,creator,create_Time,end_Time,parent_Id,expire_Time,order_No,variable from wf_hist_order ";
	protected static final String QUERY_HIST_TASK = "select id,order_Id,task_Name,display_Name,task_Type,perform_Type,task_State,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable from wf_hist_task ";
	protected static final String QUERY_HIST_TASK_ACTOR = "select task_Id, actor_Id from wf_hist_task_actor ";
	
	/**
	 * 是否为ORM框架，用以标识对象直接持久化
	 * @return
	 */
	public abstract boolean isORM();
	/**
	 * 保存或更新对象
	 * isORM为true，则参数map只存放对象
	 * isORM为false，则参数map需要放入SQL、ARGS、TYPE
	 * @param map
	 */
	public abstract void saveOrUpdate(Map<String, Object> map);
	/**
	 * 根据类型T、Sql语句、参数查询单个对象
	 * @param T
	 * @param sql
	 * @param args
	 * @return
	 */
	public abstract <T> T queryObject(Class<T> T, String sql, Object... args);
	/**
	 * 根据类型T、Sql语句、参数查询列表对象
	 * @param T
	 * @param sql
	 * @param args
	 * @return
	 */
	public abstract <T> List<T> queryList(Class<T> T, String sql, Object... args);
	
	/**
	 * 根据类型T、Sql语句、参数分页查询列表对象
	 * @param page
	 * @param T
	 * @param sql
	 * @param args
	 * @return
	 */
	public abstract <T> List<T> queryList(Page<T> page, Class<T> T, String sql, Object... args);
	
	@Override
	public void initialize(Object accessObject) {
		
	}
	
	/**
	 * isORM为false，需要构造map传递给实现类
	 * @param sql
	 * @param args
	 * @param type
	 * @return
	 */
	private Map<String, Object> buildMap(String sql, Object[] args, int[] type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_SQL, sql);
		map.put(KEY_ARGS, args);
		map.put(KEY_TYPE, type);
		return map;
	}
	
	/**
	 * isORM为true，只存放对象传递给orm框架
	 * @param entity
	 * @return
	 */
	private Map<String, Object> buildMap(Object entity, String su) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_ENTITY, entity);
		map.put(KEY_SU, su);
		return map;
	}
	
	/**
	 * 获取数据库方言
	 * @return
	 */
	protected Dialect getDialect() {
		return ServiceContext.getContext().getDialect();
	}
	
	/**
	 * 由于process中涉及blob字段，未对各种框架统一，所以process操作交给具体的实现类处理
	 */
	public void saveProcess(Process process) {
		if(isORM()) {
			saveOrUpdate(buildMap(process, SAVE));
		} else {
			Object[] args = new Object[]{process.getId(), process.getParentId(), process.getName(), process.getDisplayName(), process.getType(), 
					process.getInstanceUrl(), process.getQueryUrl(), process.getState()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
					Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(PROCESS_INSERT, args, type));
		}
	}
	/**
	 * 由于process中涉及blob字段，未对各种框架统一，所以process操作交给具体的实现类处理
	 */
	public void updateProcess(Process process) {
		if(isORM()) {
			saveOrUpdate(buildMap(process, UPDATE));
		} else {
			Object[] args = new Object[]{process.getName(), process.getDisplayName(), process.getState(), 
					process.getInstanceUrl(), process.getQueryUrl(), process.getId()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(PROCESS_UPDATE, args, type));
		}
	}

	
	@Override
	public void saveTask(Task task) {
		if(isORM()) {
			saveOrUpdate(buildMap(task, SAVE));
		} else {
			Object[] args = new Object[]{task.getId(), task.getOrderId(), task.getTaskName(), task.getDisplayName(), task.getTaskType(), 
					task.getPerformType(), task.getOperator(), task.getCreateTime(), task.getFinishTime(), 
					task.getExpireTime(), task.getActionUrl(), task.getParentTaskId(), task.getVariable()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
					Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_INSERT, args, type));
		}
	}

	@Override
	public void saveOrder(Order order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, SAVE));
		} else {
			Object[] args = new Object[]{order.getId(), order.getProcessId(), order.getCreator(), order.getCreateTime(), order.getParentId(), 
					order.getParentNodeName(), order.getExpireTime(), order.getLastUpdateTime(), order.getLastUpdator(), order.getOrderNo(), order.getVariable()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_INSERT, args, type));
		}
	}

	@Override
	public void saveTaskActor(TaskActor taskActor) {
		if(isORM()) {
			saveOrUpdate(buildMap(taskActor, SAVE));
		} else {
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_ACTOR_INSERT, new Object[]{taskActor.getTaskId(), taskActor.getActorId() }, type));
		}
	}

	@Override
	public void updateTask(Task task) {
		if(isORM()) {
			saveOrUpdate(buildMap(task, UPDATE));
		} else {
			Object[] args = new Object[]{task.getFinishTime(), task.getOperator(), task.getId(), task.getVersion() };
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(TASK_UPDATE, args, type));
		}
	}

	@Override
	public void updateOrder(Order order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, UPDATE));
		} else {
			Object[] args = new Object[]{order.getLastUpdator(), order.getLastUpdateTime(), order.getId(), order.getVersion() };
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(ORDER_UPDATE, args, type));
		}
	}

	@Override
	public void deleteTask(Task task) {
		if(!isORM()) {
			Object[] args = new Object[]{task.getId()};
			int[] type = new int[]{Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_ACTOR_DELETE, args, type));
			saveOrUpdate(buildMap(TASK_DELETE, args, type));
		}
	}

	@Override
	public void deleteOrder(Order order) {
		if(!isORM()) {
			int[] type = new int[]{Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_DELETE, new Object[]{order.getId()}, type));
		}
	}
	
	@Override
	public void removeTaskActor(String taskId, String... actors) {
		if(!isORM()) {
			for(String actorId : actors) {
				int[] type = new int[]{Types.VARCHAR, Types.VARCHAR};
				saveOrUpdate(buildMap(TASK_ACTOR_REDUCE, new Object[]{taskId, actorId}, type));
			}
		}
	}

	@Override
	public void saveHistory(HistoryOrder order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, SAVE));
		} else {
			Object[] args = new Object[]{order.getId(), order.getProcessId(), order.getOrderState(), order.getCreator(), 
					order.getCreateTime(), order.getEndTime(), order.getParentId(), order.getExpireTime(), order.getOrderNo(), order.getVariable()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, 
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_HISTORY_INSERT, args, type));
		}
	}

	@Override
	public void updateHistory(HistoryOrder order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, UPDATE));
		} else {
			Object[] args = new Object[]{order.getOrderState(), order.getEndTime(), order.getId()};
			int[] type = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_HISTORY_UPDATE, args, type));
		}
	}

	@Override
	public void saveHistory(HistoryTask task) {
		if(isORM()) {
			saveOrUpdate(buildMap(task, SAVE));
			for(String actorId : task.getActorIds()) {
				HistoryTaskActor hist = new HistoryTaskActor();
				hist.setActorId(actorId);
				hist.setTaskId(task.getId());
				saveOrUpdate(buildMap(hist, SAVE));
			}
		} else {
			Object[] args = new Object[]{task.getId(), task.getOrderId(), task.getTaskName(), task.getDisplayName(), task.getTaskType(), 
					task.getPerformType(), task.getTaskState(), task.getOperator(), task.getCreateTime(), task.getFinishTime(), 
					task.getExpireTime(), task.getActionUrl(), task.getParentTaskId(), task.getVariable()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
					Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_HISTORY_INSERT, args, type));
			for(String actorId : task.getActorIds()) {
				saveOrUpdate(buildMap(TASK_ACTOR_HISTORY_INSERT, new Object[]{task.getId(), actorId}, new int[]{Types.VARCHAR, Types.VARCHAR}));
			}
		}
	}

	@Override
	public Task getTask(String taskId) {
		String where = " where id = ?";
		return queryObject(Task.class, QUERY_TASK + where, taskId);
	}
	
	@Override
	public List<Task> getNextActiveTasks(String parentTaskId) {
		String where = " where parent_Task_Id = ?";
		return queryList(Task.class, QUERY_TASK + where, parentTaskId);
	}
	
	@Override
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId) {
		StringBuffer sql = new StringBuffer(QUERY_TASK);
		sql.append(" where parent_task_id in ( ");
		sql.append("select ht.id from wf_hist_task ht where ht.order_id=? and ht.task_name=? and ht.parent_task_id=? ");
		sql.append(")");
		return queryList(Task.class, sql.toString(), orderId, taskName, parentTaskId);
	}
	
	@Override
	public HistoryTask getHistTask(String taskId) {
		String where = " where id = ?";
		return queryObject(HistoryTask.class, QUERY_HIST_TASK + where, taskId);
	}
	
	@Override
	public HistoryOrder getHistOrder(String orderId) {
		String where = " where id = ?";
		return queryObject(HistoryOrder.class, QUERY_HIST_ORDER + where, orderId);
	}

	@Override
	public List<TaskActor> getTaskActorsByTaskId(String taskId) {
		String where = " where task_Id = ?";
		return queryList(TaskActor.class, QUERY_TASK_ACTOR + where, taskId);
	}
	
	@Override
	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId) {
		String where = " where task_Id = ?";
		return queryList(HistoryTaskActor.class, QUERY_HIST_TASK_ACTOR + where, taskId);
	}

	@Override
	public Order getOrder(String orderId) {
		String where = " where id = ?";
		return queryObject(Order.class, QUERY_ORDER + where, orderId);
	}

	@Override
	public Process getProcess(String idName) {
		String where = " where id = ? or name = ?";
		return queryObject(Process.class, QUERY_PROCESS + where, idName, idName);
	}

	@Override
	public List<Process> getAllProcess() {
		String where = " where state = 1";
		return queryList(Process.class, QUERY_PROCESS + where);
	}

	@Override
	public List<Order> getActiveOrdersByParentId(String parentId, String... excludedId) {
		StringBuffer sql = new StringBuffer(QUERY_ORDER);
		sql.append(" where parent_Id = ? ");
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(parentId);
		if(excludedId.length > 0 && excludedId[0] != null) {
			sql.append(" and id not in(");
			for(int i = 0; i < excludedId.length; i++) {
				sql.append("?,");
				paramList.add(excludedId[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		return queryList(Order.class, sql.toString(), paramList.toArray());
	}

	@Override
	public List<Task> getActiveTasks(String orderId, String excludedTaskId, String... taskNames) {
		StringBuffer sql = new StringBuffer(QUERY_TASK);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(orderId)) {
			sql.append(" and order_Id = ? ");
			paramList.add(orderId);
		}
		if(StringHelper.isNotEmpty(excludedTaskId)) {
			sql.append(" and id != ? ");
			paramList.add(excludedTaskId);
		}
		
		if(taskNames.length > 0) {
			sql.append(" and task_Name in (");
			for(int i = 0; i < taskNames.length; i++) {
				sql.append("?,");
				paramList.add(taskNames[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		return queryList(Task.class, sql.toString(), paramList.toArray());
	}

	@Override
	public List<Process> getProcesss(Page<Process> page, String name, Integer state) {
		StringBuffer sql = new StringBuffer(QUERY_PROCESS);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(name)) {
			sql.append(" and name = ? ");
			paramList.add(name);
		}
		if(state != null) {
			sql.append(" and state = ? ");
			paramList.add(state);
		}
		return queryList(page, Process.class, sql.toString(), paramList.toArray());
	}

	@Override
	public List<Order> getActiveOrders(Page<Order> page, String... processId) {
		StringBuffer sql = new StringBuffer(QUERY_ORDER);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(processId.length > 0) {
			sql.append(" and process_Id in (");
			for(int i = 0; i < processId.length; i++) {
				sql.append("?,");
				paramList.add(processId[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(page == null) {
			sql.append(" order by create_Time desc ");
			return queryList(Order.class, sql.toString(), paramList.toArray());
		} else {
			page.setOrder(Page.DESC);
			page.setOrderBy("create_Time");
			return queryList(page, Order.class, sql.toString(), paramList.toArray());
		}
	}

	@Override
	public List<Task> getActiveTasks(Page<Task> page, String... actorIds) {
		StringBuffer sql = new StringBuffer(QUERY_TASK);
		sql.append(" left join wf_task_actor ta on ta.task_id = id ");
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(actorIds.length > 0) {
			sql.append(" and ta.actor_Id in (");
			for(int i = 0; i < actorIds.length; i++) {
				sql.append("?,");
				paramList.add(actorIds[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(page == null) {
			return queryList(Task.class, sql.toString(), paramList.toArray());
		} else {
			return queryList(page, Task.class, sql.toString(), paramList.toArray());
		}
	}

	@Override
	public List<WorkItem> getWorkItems(Page<WorkItem> page, String processId, String... actorIds) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select o.process_Id, t.order_Id, t.id as task_Id, p.display_Name as process_Name, p.instance_Url, o.parent_Id, o.creator, ");
		sql.append(" o.create_Time as order_Create_Time, o.expire_Time as order_Expire_Time, o.order_No, o.variable as order_Variable, ");
		sql.append(" t.display_Name as task_Name, t.task_Type, t.perform_Type, t.operator, t.action_Url, ");
		sql.append(" t.create_Time as task_Create_Time, t.finish_Time as task_End_Time, t.expire_Time as task_Expire_Time, t.variable as task_Variable ");
		sql.append(" from wf_task t ");
		sql.append(" left join wf_order o on t.order_id = o.id ");
		sql.append(" left join wf_task_actor ta on ta.task_id=t.id ");
		sql.append(" left join wf_process p on p.id = o.process_id ");
		sql.append(" where 1=1 ");
		
		/**
		 * 查询条件构造sql的where条件
		 */
		List<Object> paramList = new ArrayList<Object>();
		if(actorIds.length > 0) {
			sql.append(" and ta.actor_Id in (");
			for(String actor : actorIds) {
				sql.append("?,");
				paramList.add(actor);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(StringHelper.isNotEmpty(processId)) {
			sql.append(" and o.process_Id = ?");
			paramList.add(processId);
		}
		page.setOrder(Page.DESC);
		page.setOrderBy("t.create_Time");
		return queryList(page, WorkItem.class, sql.toString(), paramList.toArray());
	}
	
	@Override
	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page,
			String... processIds) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_ORDER);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(processIds.length > 0) {
			sql.append(" and process_Id in (");
			for(int i = 0; i < processIds.length; i++) {
				sql.append("?,");
				paramList.add(processIds[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(page == null) {
			sql.append(" order by create_Time desc ");
			return queryList(HistoryOrder.class, sql.toString(), paramList.toArray());
		} else {
			page.setOrder(Page.DESC);
			page.setOrderBy("create_Time");
			return queryList(page, HistoryOrder.class, sql.toString(), paramList.toArray());
		}
	}
	@Override
	public List<HistoryOrder> getHistoryOrdersByParentId(String parentId) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_ORDER);
		sql.append(" where parent_Id = ? ");
		return queryList(HistoryOrder.class, sql.toString(), new Object[]{parentId });
	}
	@Override
	public List<HistoryTask> getHistoryTasks(String orderId) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_TASK);
		sql.append(" where order_Id = ? order by create_Time desc ");
		return queryList(HistoryTask.class, sql.toString(), new Object[]{orderId });
	}
	@Override
	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page,
			String... actorIds) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_TASK);
		sql.append(" left join wf_hist_task_actor ta on ta.task_id = id ");
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(actorIds.length > 0) {
			sql.append(" and ta.actor_Id in (");
			for(int i = 0; i < actorIds.length; i++) {
				sql.append("?,");
				paramList.add(actorIds[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(page == null) {
			return queryList(HistoryTask.class, sql.toString(), paramList.toArray());
		} else {
			return queryList(page, HistoryTask.class, sql.toString(), paramList.toArray());
		}
	}
	@Override
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page,
			String processId, String... actorIds) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select o.process_Id, t.order_Id, t.id as task_Id, p.display_Name as process_Name, p.instance_Url, o.parent_Id, o.creator, ");
		sql.append(" o.create_Time as order_Create_Time, o.expire_Time as order_Expire_Time, o.order_No, o.variable as order_Variable, ");
		sql.append(" t.display_Name as task_Name, t.task_Type, t.perform_Type,t.operator, t.action_Url, ");
		sql.append(" t.create_Time as task_Create_Time, t.finish_Time as task_End_Time, t.expire_Time as task_Expire_Time, t.variable as task_Variable ");
		sql.append(" from wf_hist_task t ");
		sql.append(" left join wf_hist_order o on t.order_id = o.id ");
		sql.append(" left join wf_process p on p.id = o.process_id ");
		sql.append(" where 1=1 ");
		
		/**
		 * 查询条件构造sql的where条件
		 */
		List<Object> paramList = new ArrayList<Object>();
		if(actorIds.length > 0) {
			sql.append(" and t.operator in (");
			for(String actor : actorIds) {
				sql.append("?,");
				paramList.add(actor);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(StringHelper.isNotEmpty(processId)) {
			sql.append(" and o.process_Id = ?");
			paramList.add(processId);
		}
		page.setOrder(Page.DESC);
		page.setOrderBy("t.create_Time");
		return queryList(page, WorkItem.class, sql.toString(), paramList.toArray());
	}
}
