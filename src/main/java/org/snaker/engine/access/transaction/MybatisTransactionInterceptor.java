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
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.snaker.engine.access.mybatis.MybatisHelper;
import org.snaker.engine.helper.AssertHelper;

/**
 * mybatis方式的数据库事务拦截处理
 * 定义mybatis事务拦截器主要考虑到在现有的mybatis项目中增加snaker时，仅仅是注入已有的SqlSessionFactory
 * 那么对于已经有事务管理方式的mybatis，需要此拦截器控制SqlSession的commit操作。
 * @author yuqs
 * @version 1.0
 */
public class MybatisTransactionInterceptor extends TransactionInterceptor {
	private static final Logger log = LoggerFactory.getLogger(MybatisTransactionInterceptor.class);
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof SqlSessionFactory) {
			this.sqlSessionFactory = (SqlSessionFactory)accessObject;
		}
	}
	@Override
	protected TransactionStatus getTransaction() {
		try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}
			SqlSession session = MybatisHelper.getSession(sqlSessionFactory);
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
		SqlSession session = (SqlSession)status.getTransaction();
        if (session != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("commit transaction=" + session.hashCode());
            	}
                session.commit();
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	session.close();
            	TransactionObjectHolder.unbind();
            }
        }
	}

	@Override
	protected void rollback(TransactionStatus status) {
		SqlSession session = (SqlSession)status.getTransaction();
        if (session != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("rollback transaction=" + session.hashCode());
            	}
            	session.rollback();
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	session.close();
            	TransactionObjectHolder.unbind();
            }
        }
	}
}
