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

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.access.Page;
import org.snaker.engine.helper.StringHelper;

/**
 * SQLServer数据库方言实现
 * @author yuqs
 * @since 1.0
 */
public class SQLServerDialect implements Dialect {
    private static final String STR_ORDERBY = " order by ";

	public String getPageSql(String sql, Page<?> page) {
		int orderIdx = sql.indexOf(STR_ORDERBY);
        String orderStr = null;
		if(orderIdx != -1) {
            orderStr = sql.substring(orderIdx + 10);
			sql = sql.substring(0, orderIdx);
		}
		StringBuffer pageSql = new StringBuffer();
		pageSql.append("select top ");
		pageSql.append(page.getPageSize());
		pageSql.append(" * from (select row_number() over (");
		String orderBy = getOrderBy(sql, orderStr);
		pageSql.append(orderBy);
		pageSql.append(") row_number, * from (");
		pageSql.append(sql);
		int start = (page.getPageNo() - 1) * page.getPageSize();
		pageSql.append(") aa ) a where row_number > ");
		pageSql.append(start);
		pageSql.append(" order by row_number");
		return pageSql.toString();
	}

    public String getOrderBy(String sql, String orderBy) {
        if(StringHelper.isEmpty(orderBy)) {
            return STR_ORDERBY + " id desc ";
        }
        StringBuffer orderBuffer = new StringBuffer(30);
        String[] orderByArray = StringUtils.split(orderBy, ',');
        for (int i = 0; i < orderByArray.length; i++) {
            String orderByItem = orderByArray[i].trim();
            String orderByName = null;
            String orderByDirect = "";
            if(orderByItem.indexOf(" ") == -1) {
                orderByName = orderByItem;
            } else {
                orderByName = orderByItem.substring(0, orderByItem.indexOf(" "));
                orderByDirect = orderByItem.substring(orderByItem.indexOf(" ") + 1);
            }
            if(orderByName.indexOf(".") > -1) {
                orderByName = orderByName.substring(orderByName.indexOf(".") + 1);
            }
            String columnAlias = orderByName + " as ";
            int columnIndex = sql.indexOf(columnAlias);
            if(columnIndex == -1) {
                orderBuffer.append(orderByName).append(" ").append(orderByDirect).append(" ,");
            } else {
                String after = sql.substring(columnIndex + columnAlias.length());
                String aliasName = null;
                if(after.indexOf(",") != -1 && after.indexOf(" from") > after.indexOf(",")) {
                    aliasName = after.substring(0, after.indexOf(","));
                } else {
                    aliasName = after.substring(0, after.indexOf(" "));
                }
                orderBuffer.append(aliasName).append(" ").append(orderByDirect).append(" ,");
            }
        }
        orderBuffer.deleteCharAt(orderBuffer.length() - 1);
        return STR_ORDERBY + orderBuffer.toString();
    }
}
