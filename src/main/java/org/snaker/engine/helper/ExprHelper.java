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

import java.util.Map;
import java.util.Map.Entry;

import org.snaker.engine.SnakerException;

import bsh.Interpreter;

/**
 * 表达式引擎帮助类 1.0版本使用bsh的引擎
 * @author yuqs
 * @version 1.0
 */
public class ExprHelper {
	/**
	 * 根据给定的参数集合、表达式语句，使用bsh执行，并返回结果
	 * @param args
	 * @param expr
	 * @return
	 */
	public static String evalString(Map<String, Object> args, String expr) {
		Interpreter bsh = new Interpreter();
		try {
			for (Entry<String, Object> entry : args.entrySet()) {
				bsh.set(entry.getKey(), entry.getValue());
			}
			return (String) bsh.eval(escape(expr));
		} catch (Exception e) {
			throw new SnakerException("表达式[" + expr + "]解析失败.", e.getCause());
		}
	}
	
	/**
	 * 根据给定的参数集合、表达式语句，使用bsh执行，并返回结果
	 * @param args
	 * @param expr
	 * @return
	 */
	public static boolean evalBoolean(Map<String, Object> args, String expr) {
		Interpreter bsh = new Interpreter();
		try {
			for (Entry<String, Object> entry : args.entrySet()) {
				bsh.set(entry.getKey(), entry.getValue());
			}
			return (Boolean) bsh.eval(escape(expr));
		} catch (Exception e) {
			throw new SnakerException("表达式[" + expr + "]解析失败.", e.getCause());
		}
	}

	/**
	 * 对给定的表达式进行预处理，以达到bsh的成功解析
	 * 这里主要处理表达式存在单引号情况，将单引号转换为双引号
	 * @param value
	 * @return
	 */
	private static String escape(String value) {
		if (value == null || value.length() == 0) {
			value = "";
		}
		StringBuilder b = new StringBuilder(value.length());
		for (int i = 0, len = value.length(); i < len; i++) {
			char c = value.charAt(i);
			if (c == '\'') {
				b.append("\"");
			} else {
				b.append(c);
			}
		}
		return b.toString();
	}
}
