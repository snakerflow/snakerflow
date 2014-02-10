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
package org.snaker.engine.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期帮助类
 * @author yuqs
 * @version 1.0
 */
public class DateHelper {
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 返回标准格式的当前时间
	 * @return
	 */
	public static String getTime() {
		return df.format(new Date());
	}
	
	/**
	 * 解析日期时间对象
	 * @param date
	 * @return
	 */
	public static String parseTime(Object date) {
		if(date == null) return null;
		if(date instanceof Date) {
			return df.format(date);
		} else if(date instanceof String) {
			return String.valueOf(date);
		}
		return "";
	}
}
