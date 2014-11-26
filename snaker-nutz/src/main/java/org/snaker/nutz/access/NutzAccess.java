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

package org.snaker.nutz.access;

import java.sql.Connection;
import java.sql.SQLException;

import org.nutz.trans.Trans;
import org.snaker.engine.access.jdbc.JdbcAccess;

/**
 * Nutz扩展的实现。主要覆盖getConnection方法
 * @author yuqs
 * @since 2.0
 */
public class NutzAccess extends JdbcAccess {
    /**
     * 借助Nutz的Trans静态方法获取连接。之前已经被事务拦截器拦截
     * @return
     * @throws SQLException
     */
	protected Connection getConnection() throws SQLException {
		return Trans.getConnectionAuto(dataSource);
	}
}
