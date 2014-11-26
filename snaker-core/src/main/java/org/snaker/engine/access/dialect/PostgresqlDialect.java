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
package org.snaker.engine.access.dialect;

import org.snaker.engine.access.Page;

/**
 * Postgresql数据库方言实现
 * @author yuqs
 * @since 1.3
 */
public class PostgresqlDialect implements Dialect {
	/**
	 * Postgresql分页通过limit实现
	 */
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append(getPageBefore(sql, page));
		pageSql.append(sql);
		pageSql.append(getPageAfter(sql, page));
		return pageSql.toString();
	}

	
	public String getPageBefore(String sql, Page<?> page) {
		return "";
	}

	public String getPageAfter(String sql, Page<?> page) {
		long start = (page.getPageNo() - 1) * page.getPageSize();
		StringBuffer sb = new StringBuffer();
		sb.append(" limit ").append(page.getPageSize()).append(" offset ").append(start);
		return sb.toString();
	}
}
