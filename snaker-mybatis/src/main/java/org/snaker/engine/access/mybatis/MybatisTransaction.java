/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package org.snaker.engine.access.mybatis;

import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.transaction.TransactionObjectHolder;
import org.snaker.engine.helper.AssertHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * mybatis事务对象，仅提供connection对象及事务操作 这里取消了对事务的commit、rooback操作
 * @author yuqs
 * @since 1.0
 */
public class MybatisTransaction implements Transaction {
	private static final Logger log = LoggerFactory.getLogger(MybatisTransaction.class);
	private DataSource dataSource;
	protected Connection connection;
	protected boolean autoCommit;

	public MybatisTransaction(DataSource dataSource) {
		AssertHelper.notNull(dataSource, "No DataSource specified");
		this.dataSource = dataSource;
	}

	public Connection getConnection() throws SQLException {
		this.connection = (Connection) TransactionObjectHolder.get();
		if (this.connection == null) {
			this.connection = dataSource.getConnection();
		}
		this.autoCommit = this.connection.getAutoCommit();
		return this.connection;
	}
	
	public void commit() throws SQLException {
		if (this.connection != null && !this.autoCommit && !isConnectionTransactional()) {
			if (log.isDebugEnabled()) {
				log.debug("Committing JDBC Connection [" + this.connection.hashCode() + "]");
			}
			this.connection.commit();
		}
	}
	
	public void rollback() throws SQLException {
		if (this.connection != null && !this.autoCommit && !isConnectionTransactional()) {
			if (log.isDebugEnabled()) {
				log.debug("Rolling back JDBC Connection [" + this.connection.hashCode() + "]");
			}
			this.connection.rollback();
		}
	}
	
	private boolean isConnectionTransactional() {
		Connection holdCon = (Connection)TransactionObjectHolder.get();
		return (holdCon == connection || holdCon.equals(connection));
	}

	public void close() throws SQLException {
		// not needed in this version
	}
}
