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

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.snaker.engine.access.jdbc.JdbcAccess;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * mybatis方式的数据库访问
 * @author yuqs
 * @since 1.0
 */
public class MybatisAccess extends JdbcAccess {
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
			setDataSource(this.sqlSessionFactory.getConfiguration().getEnvironment().getDataSource());
		}
	}
	
	private SqlSession getSession() {
		return MybatisHelper.getSession(sqlSessionFactory);
	}

	public boolean isORM() {
		return false;
	}

	protected Connection getConnection() throws SQLException {
		return getSession().getConnection();
	}
}
