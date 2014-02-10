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
package org.snaker.engine.access.hibernate3;

import java.sql.Blob;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.AbstractDBAccess;
import org.snaker.engine.access.Page;
import org.snaker.engine.DBAccess;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * hibernate方式的数据库访问
 * 在无事务控制的情况下，使用cglib的拦截器+ThreadLocale控制
 * @see org.snaker.engine.access.transaction.Hibernate3TransactionInterceptor
 * @author yuqs
 * @version 1.0
 */
public class HibernateAccess extends AbstractDBAccess implements DBAccess {
	private static final Logger log = LoggerFactory.getLogger(HibernateAccess.class);
	/**
	 * hibernate的session工厂
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * setter
	 * @param sessionFactory
	 */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof SessionFactory) {
			this.sessionFactory = (SessionFactory)accessObject;
		}
	}
	
	/**
	 * 取得hibernate当前Session对象
	 */
	public Session getSession() {
		return Hibernate3Helper.getSession(sessionFactory);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void updateProcess(Process process) {
		try {
			if(process.getBytes() != null) {
				Blob blob = Hibernate.createBlob(process.getBytes());
				process.setContent(blob);
			}
		} catch (Exception e) {
			throw new SnakerException(e.getMessage(), e.getCause());
		}
		getSession().saveOrUpdate(process);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveProcess(Process process) {
		try {
			if(process.getBytes() != null) {
				Blob blob = Hibernate.createBlob(process.getBytes());
				process.setContent(blob);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnakerException(e.getMessage(), e.getCause());
		}

		getSession().saveOrUpdate(process);
	}
	
	@Override
	public void deleteTask(Task task) {
		List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
		for(TaskActor actor : actors) {
			getSession().delete(actor);
		}
		getSession().delete(task);
	}

	@Override
	public void deleteOrder(Order order) {
		getSession().delete(order);
	}
	
	@Override
	public void removeTaskActor(String taskId, String... actors) {
		for(String actorId : actors) {
			TaskActor ta = new TaskActor();
			ta.setTaskId(taskId);
			ta.setActorId(actorId);
			getSession().delete(ta);
		}
	}

	@Override
	public boolean isORM() {
		return true;
	}

	@Override
	public void saveOrUpdate(Map<String, Object> map) {
		getSession().saveOrUpdate(map.get(KEY_ENTITY));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryObject(Class<T> T, String sql, Object... args) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(T);
		for(int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return (T)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryList(Class<T> T, String sql, Object... args) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(T);
		for(int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return (List<T>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> queryList(Page<T> page, Class<T> T, String sql, Object... args) {
		try {
			String countSQL = "select count(1) from (" + sql + ") c ";
			String querySQL = sql;
			if(page.isOrderBySetted()) {
				querySQL = querySQL + StringHelper.buildPageOrder(page.getOrder(), page.getOrderBy());
			}
			SQLQuery countQuery = getSession().createSQLQuery(countSQL);
			SQLQuery pageQuery = getSession().createSQLQuery(querySQL);
			pageQuery.addEntity(T);
			if(args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					pageQuery.setParameter(i, args[i]);
					countQuery.setParameter(i, args[i]);
				}
			}
			//判断是否需要分页（根据pageSize判断）
			if(page.getPageSize() != Page.NON_PAGE) {
				pageQuery.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
				pageQuery.setMaxResults(page.getPageSize());
			}
			List<T> list = pageQuery.list();
			Object total = countQuery.uniqueResult();
			page.setResult(list);
			page.setTotalCount(ClassHelper.castLong(total));
			return list;
		} catch(RuntimeException e) {
			log.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}
}
