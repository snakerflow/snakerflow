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

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis自定义的事务工厂
 * @author yuqs
 * @since 1.0
 */
public class MybatisTransactionFactory implements TransactionFactory {
	
	public void setProperties(Properties props) {
		// not needed in this version
	}

	public Transaction newTransaction(Connection conn) {
		throw new UnsupportedOperationException("New transactions require a DataSource");
	}

	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new MybatisTransaction(dataSource);
	}
}
