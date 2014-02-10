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
 * 数据库差异的方言接口
 * @author yuqs
 * @version 1.0
 */
public interface Dialect {
	/**
	 * 根据提供的sql、分页参数：start、pagesize获取分页后的sql语句
	 * @param sql 未分页sql语句
	 * @param pageNo 当前页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public String getPageSql(String sql, int pageNo, int pageSize);
	
	/**
	 * 根据提供的sql、分页参数：start、pagesize获取分页sql语句before字符串
	 * @param pageNo 当前页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public String getPageBefore(int pageNo, int pageSize);
	
	/**
	 * 根据提供的sql、分页参数：start、pagesize获取分页sql语句after字符串
	 * @param pageNo 当前页数
	 * @param pageSize 每页记录数
	 * @return
	 */
	public String getPageAfter(int pageNo, int pageSize);
}
