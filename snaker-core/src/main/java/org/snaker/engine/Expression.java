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
package org.snaker.engine;

import java.util.Map;

/**
 * 表达式解析接口
 * @author yuqs
 * @since 1.0
 * @since 1.2.1
 */
public interface Expression {
	/**
	 * 根据表达式串、参数解析表达式并返回指定类型
	 * @param T 返回类型
	 * @param expr 表达式串
	 * @param args 参数列表
	 * @return T 返回对象
	 */
	public <T> T eval(Class<T> T, String expr, Map<String, Object> args);
}
