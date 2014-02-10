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
package org.snaker.engine.access.dialect;

/**
 * Oracle数据库方言实现
 * @author yuqs
 * @version 1.0
 */
public class OracleDialect implements Dialect {
	/**
	 * oracle分页通过rownum实现
	 */
	@Override
	public String getPageSql(String sql, int pageNo, int pageSize) {
		StringBuffer pageSql = new StringBuffer(sql.length() + 100);
		pageSql.append(getPageBefore(pageNo, pageSize));
		pageSql.append(sql);
		pageSql.append(getPageAfter(pageNo, pageSize));
		return pageSql.toString();
	}

	@Override
	public String getPageBefore(int pageNo, int pageSize) {
		return "select * from ( select row_.*, rownum rownum_ from ( ";
	}

	@Override
	public String getPageAfter(int pageNo, int pageSize) {
		long start = (pageNo - 1) * pageSize + 1;
		StringBuffer after = new StringBuffer();
		after.append(" ) row_ where rownum < ");
		after.append(start + pageSize);
		after.append(" ) where rownum_ >= ");
		after.append(start);
		return after.toString();
	}
}
