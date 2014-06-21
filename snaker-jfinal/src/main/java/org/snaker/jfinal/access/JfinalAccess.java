/* Copyright 2012-2014 the original author or authors.
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
package org.snaker.jfinal.access;

import java.sql.Connection;
import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Config;
import org.snaker.engine.access.jdbc.JdbcAccess;

import org.snaker.jfinal.JfinalHelper;

/**
 * jfinal的数据访问实现类
 * 主要重构getConnection方法
 * 从jfinal的threadlocal中获取数据连接，事务统一由jfinal管理
 * @author yuqs
 * @since 2.0
 */
public class JfinalAccess extends JdbcAccess {
	/**
	 * 从jfinal的threadlocal中获取数据库连接
	 */
	protected Connection getConnection() throws SQLException {
        Config config = JfinalHelper.getConfig();
		Connection conn = config.getThreadLocalConnection();
		if(conn == null) {
			conn = config.getConnection();
            conn.setAutoCommit(true);
		}
		return conn;
	}
}
