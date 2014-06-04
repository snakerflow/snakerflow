/* Copyright 2013-2014 the original author or authors.
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
import org.snaker.engine.DBAccess;
import org.snaker.engine.entity.CCOrder;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.TaskActor;
import org.snaker.engine.helper.ClassHelper;

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
	
	public void deleteTask(Task task) {
		List<TaskActor> actors = getTaskActorsByTaskId(task.getId());
		for(TaskActor actor : actors) {
			getSession().delete(actor);
		}
		getSession().delete(task);
	}

	public void deleteOrder(Order order) {
		getSession().delete(order);
	}
	
	public void deleteSurrogate(Surrogate surrogate) {
		getSession().delete(surrogate);
	}
	
	public void deleteCCOrder(CCOrder ccorder) {
		getSession().delete(ccorder);
	}
	
	public void removeTaskActor(String taskId, String... actors) {
		for(String actorId : actors) {
			TaskActor ta = new TaskActor();
			ta.setTaskId(taskId);
			ta.setActorId(actorId);
			getSession().delete(ta);
		}
	}
	
	public boolean isORM() {
		return true;
	}
	
	public void saveOrUpdate(Map<String, Object> map) {
		getSession().saveOrUpdate(map.get(KEY_ENTITY));
	}
	
	public Integer getLatestProcessVersion(String name) {
		SQLQuery query = getSession().createSQLQuery(QUERY_VERSION + " where name = ?");
		query.setParameter(0, name);
		Object result = query.uniqueResult();
		return new Long(ClassHelper.castLong(result)).intValue();
	}

	@SuppressWarnings("unchecked")
	public <T> T queryObject(Class<T> T, String sql, Object... args) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(T);
		for(int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return (T)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryList(Class<T> clazz, String sql, Object... args) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addEntity(clazz);
		for(int i = 0; i < args.length; i++) {
			query.setParameter(i, args[i]);
		}
		return (List<T>)query.list();
	}

    public Object queryCount(String sql, Object... args) {
        SQLQuery countQuery = getSession().createSQLQuery(sql);
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                countQuery.setParameter(i, args[i]);
            }
        }
        return countQuery.uniqueResult();
    }
}
