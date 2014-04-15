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

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.Page;
import org.snaker.engine.helper.StringHelper;

/**
 * SQLServer数据库方言实现
 * @author yuqs
 * @version 1.0
 */
public class SQLServerDialect extends AbstractDialect {
	public String getPageSql(String sql, Page<?> page) {
		int orderIdx = sql.indexOf(" order by ");
		if(orderIdx != -1) {
			sql = sql.substring(0, orderIdx);
		}
		StringBuffer pageSql = new StringBuffer();
		pageSql.append("select top ");
		pageSql.append(page.getPageSize());
		pageSql.append(" * from (select row_number() over (");
		String orderby = getOrderby(sql, page);
		pageSql.append(StringHelper.isEmpty(orderby) ? " order by id desc " : orderby);
		pageSql.append(") row_number, * from (");
		pageSql.append(sql);
		int start = (page.getPageNo() - 1) * page.getPageSize();
		pageSql.append(") aa ) a where row_number > ");
		pageSql.append(start);
		pageSql.append(" order by row_number");
		return pageSql.toString();
	}

	@Override
	public String getOrderby(String sql, Page<?> page) {
		String orderby = page.getOrderBy();
		String order = page.getOrder();
		if(StringHelper.isEmpty(orderby) || StringHelper.isEmpty(order)) return "";
		String[] orderByArray = StringUtils.split(orderby, ',');
		String[] orderArray = StringUtils.split(order, ',');
		if(orderArray.length != orderByArray.length) throw new SnakerException("分页多重排序参数中,排序字段与排序方向的个数不相等");
		StringBuffer orderStr = new StringBuffer(30);
		orderStr.append(" order by ");

		for (int i = 0; i < orderByArray.length; i++) {
			String aliasColumn = orderByArray[i] + " as ";
			int orderbyIdx = sql.indexOf(aliasColumn);
			if(orderbyIdx != -1) {
				String after = sql.substring(orderbyIdx + aliasColumn.length());
				String aliasName = after.substring(0, after.indexOf(","));
				orderStr.append(aliasName).append(" ").append(orderArray[i]).append(" ,");
			} else {
				orderStr.append(orderByArray[i]).append(" ").append(orderArray[i]).append(" ,");
			}
		}
		orderStr.deleteCharAt(orderStr.length() - 1);
		return orderStr.toString();
	}
}
