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
 * Oracle数据库方言实现
 * @author yuqs
 * @since 1.0
 */
public class OracleDialect implements Dialect {
	/**
	 * oracle分页通过rownum实现
	 */
	public String getPageSql(String sql, Page<?> page) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pageSql.append(sql);
		long start = (page.getPageNo() - 1) * page.getPageSize() + 1;
		pageSql.append(" ) row_ where rownum < ");
		pageSql.append(start + page.getPageSize());
		pageSql.append(" ) where rownum_ >= ");
		pageSql.append(start);
		return pageSql.toString();
	}
}
