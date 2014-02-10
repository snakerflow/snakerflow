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
package org.snaker.engine.access.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.snaker.engine.access.hibernate3.Hibernate3Helper;
import org.snaker.engine.helper.AssertHelper;

/**
 * Hibernate方式的数据库事务拦截处理
 * @author yuqs
 * @version 1.0
 */
public class Hibernate3TransactionInterceptor extends TransactionInterceptor {
	private static final Logger log = LoggerFactory.getLogger(Hibernate3TransactionInterceptor.class);
	private SessionFactory sessionFactory;
	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof SessionFactory) {
			this.sessionFactory = (SessionFactory)accessObject;
		}
	}
	@Override
	protected TransactionStatus getTransaction() {
		try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}
			Session session = Hibernate3Helper.getSession(sessionFactory);
            session.getTransaction().begin();
            if(log.isInfoEnabled()) {
            	log.info("begin transaction=" + session.hashCode());
            }
			TransactionObjectHolder.bind(session);
			return new TransactionStatus(session, true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	protected void commit(TransactionStatus status) {
		AssertHelper.isTrue(status.isNewTransaction());
		Session session = (Session)status.getTransaction();
        if (session != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("commit transaction=" + session.hashCode());
            	}
            	session.getTransaction().commit();
            	session.close();
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	TransactionObjectHolder.unbind();
            }
        }
	}

	@Override
	protected void rollback(TransactionStatus status) {
		Session session = (Session)status.getTransaction();
        if (session != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("rollback transaction=" + session.hashCode());
            	}
            	if(session.isOpen()) {
            		session.getTransaction().rollback();
            		session.close();
            	}
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	TransactionObjectHolder.unbind();
            }
        }
	}
}
