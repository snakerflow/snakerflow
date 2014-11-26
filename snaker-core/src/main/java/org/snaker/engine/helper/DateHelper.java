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
package org.snaker.engine.helper;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * 日期帮助类
 * @author yuqs
 * @since 1.0
 */
public class DateHelper {
	private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 返回标准格式的当前时间
	 * @return
	 */
	public static String getTime() {
		return new DateTime().toString(DATE_FORMAT_DEFAULT);
	}
	
	/**
	 * 解析日期时间对象
	 * @param date
	 * @return
	 */
	public static String parseTime(Object date) {
		if(date == null) return null;
		if(date instanceof Date) {
			return new DateTime((Date)date).toString(DATE_FORMAT_DEFAULT);
		} else if(date instanceof String) {
			return String.valueOf(date);
		}
		return "";
	}
	
	/**
	 * 对时限数据进行处理
	 * 1、运行时设置的date型数据直接返回
	 * 2、模型设置的需要特殊转换成date类型
	 * 3、运行时设置的转换为date型
	 * @param args 运行时参数
	 * @param parameter 模型参数
	 * @return Date类型
	 */
	public static Date processTime(Map<String, Object> args, String parameter) {
		if(StringHelper.isEmpty(parameter)) return null;
		Object data = args.get(parameter);
		if(data == null) data = parameter;
		
		Date result = null;
		if(data instanceof Date) {
			return (Date)data;
		} else if(data instanceof Long) {
			return new Date((Long)data);
		} else if(data instanceof String) {
			//TODO 1.4-dev ignore
		}
		return result;
	}
}
