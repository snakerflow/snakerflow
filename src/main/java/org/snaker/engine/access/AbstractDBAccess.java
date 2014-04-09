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
import org.snaker.engine.entity.Surrogate;
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
	
	protected static final String PROCESS_INSERT = "insert into wf_process (id,name,display_Name,type,instance_Url,state,version,create_Time,creator) values (?,?,?,?,?,?,?,?,?)";
	protected static final String PROCESS_UPDATE = "update wf_process set name=?, display_Name=?,state=?,instance_Url=?,create_Time=?,creator=? where id=? ";
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
	
	protected static final String QUERY_VERSION = "select max(version) from wf_process ";
	protected static final String QUERY_PROCESS = "select id,name,display_Name,type,instance_Url,state, content, version,create_Time,creator from wf_process ";
	protected static final String QUERY_ORDER = "select id,process_Id,creator,create_Time,parent_Id,parent_Node_Name,expire_Time,last_Update_Time,last_Updator,priority,order_No,variable, version from wf_order ";
	protected static final String QUERY_TASK = "select id,order_Id,task_Name,display_Name,task_Type,perform_Type,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable, version from wf_task ";
	protected static final String QUERY_TASK_ACTOR = "select task_Id, actor_Id from wf_task_actor ";
	
	protected static final String QUERY_HIST_ORDER = "select id,process_Id,order_State,priority,creator,create_Time,end_Time,parent_Id,expire_Time,order_No,variable from wf_hist_order ";
	protected static final String QUERY_HIST_TASK = "select id,order_Id,task_Name,display_Name,task_Type,perform_Type,task_State,operator,create_Time,finish_Time,expire_Time,action_Url,parent_Task_Id,variable from wf_hist_task ";
	protected static final String QUERY_HIST_TASK_ACTOR = "select task_Id, actor_Id from wf_hist_task_actor ";
	
	/**委托代理CRUD*/
	protected static final String SURROGATE_INSERT = "insert into wf_surrogate (id, process_Name, operator, surrogate, odate, sdate, edate, state) values (?,?,?,?,?,?,?,?)";
	protected static final String SURROGATE_UPDATE = "update wf_surrogate set process_Name=?, surrogate=?, odate=?, sdate=?, edate=?, state=? where id = ?";
	protected static final String SURROGATE_DELETE = "delete from wf_surrogate where id = ?";
	protected static final String SURROGATE_QUERY = "select id, process_Name, operator, surrogate, odate, sdate, edate, state from wf_surrogate";
	
	protected Dialect dialect;
	
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
		if(dialect == null) {
			dialect = ServiceContext.getContext().find(Dialect.class);
		}
		return dialect;
	}
	
	/**
	 * 由于process中涉及blob字段，未对各种框架统一，所以process操作交给具体的实现类处理
	 */
	public void saveProcess(Process process) {
		if(isORM()) {
			saveOrUpdate(buildMap(process, SAVE));
		} else {
			Object[] args = new Object[]{process.getId(), process.getName(), process.getDisplayName(), process.getType(), 
					process.getInstanceUrl(), process.getState(), process.getVersion(), process.getCreateTime(), process.getCreator()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, 
					Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
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
					process.getInstanceUrl(), process.getCreateTime(), process.getCreator(), process.getId()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(PROCESS_UPDATE, args, type));
		}
	}

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

	public void saveTaskActor(TaskActor taskActor) {
		if(isORM()) {
			saveOrUpdate(buildMap(taskActor, SAVE));
		} else {
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_ACTOR_INSERT, new Object[]{taskActor.getTaskId(), taskActor.getActorId() }, type));
		}
	}

	public void updateTask(Task task) {
		if(isORM()) {
			saveOrUpdate(buildMap(task, UPDATE));
		} else {
			Object[] args = new Object[]{task.getFinishTime(), task.getOperator(), task.getId(), task.getVersion() };
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(TASK_UPDATE, args, type));
		}
	}

	public void updateOrder(Order order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, UPDATE));
		} else {
			Object[] args = new Object[]{order.getLastUpdator(), order.getLastUpdateTime(), order.getId(), order.getVersion() };
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(ORDER_UPDATE, args, type));
		}
	}
	
	public void deleteTask(Task task) {
		if(!isORM()) {
			Object[] args = new Object[]{task.getId()};
			int[] type = new int[]{Types.VARCHAR};
			saveOrUpdate(buildMap(TASK_ACTOR_DELETE, args, type));
			saveOrUpdate(buildMap(TASK_DELETE, args, type));
		}
	}

	public void deleteOrder(Order order) {
		if(!isORM()) {
			int[] type = new int[]{Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_DELETE, new Object[]{order.getId()}, type));
		}
	}
	
	public void removeTaskActor(String taskId, String... actors) {
		if(!isORM()) {
			for(String actorId : actors) {
				int[] type = new int[]{Types.VARCHAR, Types.VARCHAR};
				saveOrUpdate(buildMap(TASK_ACTOR_REDUCE, new Object[]{taskId, actorId}, type));
			}
		}
	}
	
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

	public void updateHistory(HistoryOrder order) {
		if(isORM()) {
			saveOrUpdate(buildMap(order, UPDATE));
		} else {
			Object[] args = new Object[]{order.getOrderState(), order.getEndTime(), order.getId()};
			int[] type = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			saveOrUpdate(buildMap(ORDER_HISTORY_UPDATE, args, type));
		}
	}

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
	
	public void saveSurrogate(Surrogate surrogate) {
		if(isORM()) {
			saveOrUpdate(buildMap(surrogate, SAVE));
		} else {
			Object[] args = new Object[]{surrogate.getId(), surrogate.getProcessName(), surrogate.getOperator(),
					surrogate.getSurrogate(), surrogate.getOdate(), surrogate.getSdate(), surrogate.getEdate(),
					surrogate.getState()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.INTEGER};
			saveOrUpdate(buildMap(SURROGATE_INSERT, args, type));
		}
	}
	
	public void updateSurrogate(Surrogate surrogate) {
		if(isORM()) {
			saveOrUpdate(buildMap(surrogate, UPDATE));
		} else {
			Object[] args = new Object[]{surrogate.getProcessName(), surrogate.getSurrogate(), surrogate.getOdate(), 
					surrogate.getSdate(), surrogate.getEdate(), surrogate.getState(), surrogate.getId()};
			int[] type = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, 
					Types.INTEGER, Types.VARCHAR};
			saveOrUpdate(buildMap(SURROGATE_UPDATE, args, type));
		}
	}
	
	public void deleteSurrogate(Surrogate surrogate) {
		if(!isORM()) {
			Object[] args = new Object[]{surrogate.getId()};
			int[] type = new int[]{Types.VARCHAR};
			saveOrUpdate(buildMap(SURROGATE_DELETE, args, type));
		}
	}
	
	public Surrogate getSurrogate(String id) {
		String where = " where id = ?";
		return queryObject(Surrogate.class, SURROGATE_QUERY + where, id);
	}
	
	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(SURROGATE_QUERY);
		sql.append(" where 1=1 and state = 1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(filter.getNames() != null && filter.getNames().length > 0) {
			sql.append(" and process_Name in(");
			for(int i = 0; i < filter.getNames().length; i++) {
				sql.append("?,");
				paramList.add(filter.getNames()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(filter.getOperators() != null && filter.getOperators().length > 0) {
			sql.append(" and operator in (");
			for(String actor : filter.getOperators()) {
				sql.append("?,");
				paramList.add(actor);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(StringHelper.isNotEmpty(filter.getOperateTime())) {
			sql.append(" and sdate <= ? and edate >= ? ");
			paramList.add(filter.getOperateTime());
			paramList.add(filter.getOperateTime());
		}
		if(page == null) {
			return queryList(Surrogate.class, sql.toString(), paramList.toArray());
		} else {
			return queryList(page, Surrogate.class, sql.toString(), paramList.toArray());
		}
	}

	public Task getTask(String taskId) {
		String where = " where id = ?";
		return queryObject(Task.class, QUERY_TASK + where, taskId);
	}
	
	public List<Task> getNextActiveTasks(String parentTaskId) {
		String where = " where parent_Task_Id = ?";
		return queryList(Task.class, QUERY_TASK + where, parentTaskId);
	}
	
	public List<Task> getNextActiveTasks(String orderId, String taskName, String parentTaskId) {
		StringBuffer sql = new StringBuffer(QUERY_TASK);
		sql.append(" where parent_task_id in ( ");
		sql.append("select ht.id from wf_hist_task ht where ht.order_id=? and ht.task_name=? and ht.parent_task_id=? ");
		sql.append(")");
		return queryList(Task.class, sql.toString(), orderId, taskName, parentTaskId);
	}
	
	public HistoryTask getHistTask(String taskId) {
		String where = " where id = ?";
		return queryObject(HistoryTask.class, QUERY_HIST_TASK + where, taskId);
	}
	
	public HistoryOrder getHistOrder(String orderId) {
		String where = " where id = ?";
		return queryObject(HistoryOrder.class, QUERY_HIST_ORDER + where, orderId);
	}

	public List<TaskActor> getTaskActorsByTaskId(String taskId) {
		String where = " where task_Id = ?";
		return queryList(TaskActor.class, QUERY_TASK_ACTOR + where, taskId);
	}
	
	public List<HistoryTaskActor> getHistTaskActorsByTaskId(String taskId) {
		String where = " where task_Id = ?";
		return queryList(HistoryTaskActor.class, QUERY_HIST_TASK_ACTOR + where, taskId);
	}

	public Order getOrder(String orderId) {
		String where = " where id = ?";
		return queryObject(Order.class, QUERY_ORDER + where, orderId);
	}

	public Process getProcess(String id) {
		String where = " where id = ?";
		return queryObject(Process.class, QUERY_PROCESS + where, id);
	}
	
	public List<Process> getProcesss(Page<Process> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(QUERY_PROCESS);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(filter.getNames() != null && filter.getNames().length > 0) {
			sql.append(" and name in(");
			for(int i = 0; i < filter.getNames().length; i++) {
				sql.append("?,");
				paramList.add(filter.getNames()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(filter.getVersion() != null) {
			sql.append(" and version = ? ");
			paramList.add(filter.getVersion());
		}
		if(filter.getState() != null) {
			sql.append(" and state = ? ");
			paramList.add(filter.getState());
		}
		if(StringHelper.isNotEmpty(filter.getDisplayName())) {
			sql.append(" and display_Name like ? ");
			paramList.add("%" + filter.getDisplayName() + "%");
		}
		if(page == null) {
			return queryList(Process.class, sql.toString(), paramList.toArray());
		} else {
			return queryList(page, Process.class, sql.toString(), paramList.toArray());
		}
	}

	public List<Order> getActiveOrders(Page<Order> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(QUERY_ORDER);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(filter.getParentId())) {
			sql.append(" and parent_Id = ? ");
		}
		if(StringHelper.isNotEmpty(filter.getProcessId())) {
			sql.append(" and process_Id = ? ");
			paramList.add(filter.getProcessId());
		}
		if(filter.getExcludedIds() != null && filter.getExcludedIds().length > 0) {
			sql.append(" and id not in(");
			for(int i = 0; i < filter.getExcludedIds().length; i++) {
				sql.append("?,");
				paramList.add(filter.getExcludedIds()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		if(StringHelper.isNotEmpty(filter.getOrderNo())) {
			sql.append(" and order_No = ? ");
			paramList.add(filter.getOrderNo());
		}
		
		if(page == null) {
			sql.append(" order by create_Time desc ");
			return queryList(Order.class, sql.toString(), paramList.toArray());
		} else {
			if(!page.isOrderBySetted()) {
				page.setOrder(Page.DESC);
				page.setOrderBy("create_Time");
			}
			return queryList(page, Order.class, sql.toString(), paramList.toArray());
		}
	}

	public List<Task> getActiveTasks(Page<Task> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(QUERY_TASK);
		boolean isFetchActor = filter.getOperators() != null && filter.getOperators().length > 0;
		if(isFetchActor) {
			sql.append(" left join wf_task_actor ta on ta.task_id = id ");
		}
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(filter.getOrderId())) {
			sql.append(" and order_Id = ? ");
			paramList.add(filter.getOrderId());
		}
		if(filter.getExcludedIds() != null && filter.getExcludedIds().length > 0) {
			sql.append(" and id not in(");
			for(int i = 0; i < filter.getExcludedIds().length; i++) {
				sql.append("?,");
				paramList.add(filter.getExcludedIds()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(isFetchActor) {
			sql.append(" and ta.actor_Id in (");
			for(int i = 0; i < filter.getOperators().length; i++) {
				sql.append("?,");
				paramList.add(filter.getOperators()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(filter.getNames() != null && filter.getNames().length > 0) {
			sql.append(" and task_Name in (");
			for(int i = 0; i < filter.getNames().length; i++) {
				sql.append("?,");
				paramList.add(filter.getNames()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		if(page == null) {
			sql.append(" order by create_Time desc ");
			return queryList(Task.class, sql.toString(), paramList.toArray());
		} else {
			if(!page.isOrderBySetted()) {
				page.setOrder(Page.DESC);
				page.setOrderBy("create_Time");
			}
			return queryList(page, Task.class, sql.toString(), paramList.toArray());
		}
	}

	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_ORDER);
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(filter.getProcessId())) {
			sql.append(" and process_Id = ? ");
			paramList.add(filter.getProcessId());
		}
		if(StringHelper.isNotEmpty(filter.getParentId())) {
			sql.append(" and parent_Id = ? ");
			paramList.add(filter.getParentId());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		if(StringHelper.isNotEmpty(filter.getOrderNo())) {
			sql.append(" and order_No = ? ");
			paramList.add(filter.getOrderNo());
		}
		if(page == null) {
			sql.append(" order by create_Time desc ");
			return queryList(HistoryOrder.class, sql.toString(), paramList.toArray());
		} else {
			if(!page.isOrderBySetted()) {
				page.setOrder(Page.DESC);
				page.setOrderBy("create_Time");
			}
			return queryList(page, HistoryOrder.class, sql.toString(), paramList.toArray());
		}
	}
	
	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer(QUERY_HIST_TASK);
		boolean isFetchActor = filter.getOperators() != null && filter.getOperators().length > 0;
		if(isFetchActor) {
			sql.append(" left join wf_hist_task_actor ta on ta.task_id = id ");
		}
		sql.append(" where 1=1 ");
		List<Object> paramList = new ArrayList<Object>();
		if(StringHelper.isNotEmpty(filter.getOrderId())) {
			sql.append(" and order_Id = ? ");
			paramList.add(filter.getOrderId());
		}
		if(isFetchActor) {
			sql.append(" and ta.actor_Id in (");
			for(int i = 0; i < filter.getOperators().length; i++) {
				sql.append("?,");
				paramList.add(filter.getOperators()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(filter.getNames() != null && filter.getNames().length > 0) {
			sql.append(" and task_Name in (");
			for(int i = 0; i < filter.getNames().length; i++) {
				sql.append("?,");
				paramList.add(filter.getNames()[i]);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		if(page == null) {
			return queryList(HistoryTask.class, sql.toString(), paramList.toArray());
		} else {
			return queryList(page, HistoryTask.class, sql.toString(), paramList.toArray());
		}
	}
	
	public List<WorkItem> getWorkItems(Page<WorkItem> page, QueryFilter filter) {
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
		if(filter.getOperators() != null && filter.getOperators().length > 0) {
			sql.append(" and ta.actor_Id in (");
			for(String actor : filter.getOperators()) {
				sql.append("?,");
				paramList.add(actor);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(StringHelper.isNotEmpty(filter.getProcessId())) {
			sql.append(" and o.process_Id = ?");
			paramList.add(filter.getProcessId());
		}
		if(StringHelper.isNotEmpty(filter.getParentId())) {
			sql.append(" and o.parent_Id = ? ");
			paramList.add(filter.getParentId());
		}
		if(filter.getTaskType() != null) {
			sql.append(" and t.task_Type = ? ");
			paramList.add(filter.getTaskType());
		}
		if(filter.getPerformType() != null) {
			sql.append(" and t.perform_Type = ? ");
			paramList.add(filter.getPerformType());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and t.create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and t.create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		if(!page.isOrderBySetted()) {
			page.setOrder(Page.DESC);
			page.setOrderBy("t.create_Time");
		}

		return queryList(page, WorkItem.class, sql.toString(), paramList.toArray());
	}
	
	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, QueryFilter filter) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select o.process_Id, t.order_Id, t.id as task_Id, p.display_Name as process_Name, p.instance_Url, o.parent_Id, o.creator, ");
		sql.append(" o.create_Time as order_Create_Time, o.expire_Time as order_Expire_Time, o.order_No, o.variable as order_Variable, ");
		sql.append(" t.display_Name as task_Name, t.task_Type, t.perform_Type,t.operator, t.action_Url, ");
		sql.append(" t.create_Time as task_Create_Time, t.finish_Time as task_End_Time, t.expire_Time as task_Expire_Time, t.variable as task_Variable ");
		sql.append(" from wf_hist_task t ");
		sql.append(" left join wf_hist_order o on t.order_id = o.id ");
		sql.append(" left join wf_hist_task_actor ta on ta.task_id=t.id ");
		sql.append(" left join wf_process p on p.id = o.process_id ");
		sql.append(" where 1=1 ");
		/**
		 * 查询条件构造sql的where条件
		 */
		List<Object> paramList = new ArrayList<Object>();
		if(filter.getOperators() != null && filter.getOperators().length > 0) {
			sql.append(" and ta.actor_Id in (");
			for(String actor : filter.getOperators()) {
				sql.append("?,");
				paramList.add(actor);
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") ");
		}
		
		if(StringHelper.isNotEmpty(filter.getProcessId())) {
			sql.append(" and o.process_Id = ?");
			paramList.add(filter.getProcessId());
		}
		if(StringHelper.isNotEmpty(filter.getParentId())) {
			sql.append(" and o.parent_Id = ? ");
			paramList.add(filter.getParentId());
		}
		if(filter.getTaskType() != null) {
			sql.append(" and t.task_Type = ? ");
			paramList.add(filter.getTaskType());
		}
		if(filter.getPerformType() != null) {
			sql.append(" and t.perform_Type = ? ");
			paramList.add(filter.getPerformType());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeStart())) {
			sql.append(" and t.create_Time >= ? ");
			paramList.add(filter.getCreateTimeStart());
		}
		if(StringHelper.isNotEmpty(filter.getCreateTimeEnd())) {
			sql.append(" and t.create_Time <= ? ");
			paramList.add(filter.getCreateTimeEnd());
		}
		
		if(!page.isOrderBySetted()) {
			page.setOrder(Page.DESC);
			page.setOrderBy("t.create_Time");
		}
		return queryList(page, WorkItem.class, sql.toString(), paramList.toArray());
	}
}
