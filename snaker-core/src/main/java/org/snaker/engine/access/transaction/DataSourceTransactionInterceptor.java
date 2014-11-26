/* Copyright 2013-2015 www.snakerflow.com.
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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.jdbc.JdbcHelper;
import org.snaker.engine.helper.AssertHelper;

/**
 * Jdbc方式的数据库事务拦截处理
 * @author yuqs
 * @since 1.0
 */
public class DataSourceTransactionInterceptor extends TransactionInterceptor {
	private static final Logger log = LoggerFactory.getLogger(DataSourceTransactionInterceptor.class);
	private DataSource dataSource;
	
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof DataSource) {
			this.dataSource = (DataSource)accessObject;
		}
	}
	
	protected TransactionStatus getTransaction() {
		try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if(isExistingTransaction) {
				return new TransactionStatus(TransactionObjectHolder.get(), false);
			}
			Connection conn = JdbcHelper.getConnection(dataSource);
			conn.setAutoCommit(false);
        	if(log.isInfoEnabled()) {
        		log.info("begin transaction=" + conn.hashCode());
        	}
			TransactionObjectHolder.bind(conn);
			return new TransactionStatus(conn, true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected void commit(TransactionStatus status) {
		AssertHelper.isTrue(status.isNewTransaction());
        Connection conn = (Connection)status.getTransaction();
        if (conn != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("commit transaction=" + conn.hashCode());
            	}
                conn.commit();
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            } finally {
            	try {
					JdbcHelper.close(conn);
				} catch (SQLException e) {
	            	log.error(e.getMessage(), e);
	                throw new RuntimeException(e.getMessage(), e);
				}
            	TransactionObjectHolder.unbind();
            }
        }
	}

	protected void rollback(TransactionStatus status) {
		Connection conn = (Connection)status.getTransaction();
        if (conn != null) {
            try {
            	if(log.isInfoEnabled()) {
            		log.info("rollback transaction=" + conn.hashCode());
            	}
            	if(!conn.isClosed()) {
            		conn.rollback();
            	}
            } catch (Exception e) {
            	log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e.getCause());
            } finally {
            	try {
					JdbcHelper.close(conn);
				} catch (SQLException e) {
	            	log.error(e.getMessage(), e);
	                throw new RuntimeException(e.getMessage(), e);
				}
            	TransactionObjectHolder.unbind();
            }
        }
	}
}
