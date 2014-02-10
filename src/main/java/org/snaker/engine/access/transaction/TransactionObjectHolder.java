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
package org.snaker.engine.access.transaction;

/**
 * 事务对象保持类
 * 该类用于绑定数据库访问对象（Connection、Session）
 * @author yuqs
 * @version 1.0
 */
public class TransactionObjectHolder {
	/**
	 * 线程局部容器，用于保持数据库访问对象
	 */
	private static final ThreadLocal<Object> container = new ThreadLocal<Object>();
	
	/**
	 * 绑定对象
	 * @param object
	 */
	public static void bind(Object object) {
		container.set(object);
	}
	
	/**
	 * 移除对象
	 * @return
	 */
	public static Object unbind() {
		Object object = container.get();
		container.remove();
		return object;
	}
	
	/**
	 * 返回当前对象
	 * @return
	 */
	public static Object get() {
		return container.get();
	}
	
	/**
	 * 判断是否存在事务对象
	 * @return
	 */
	public static boolean isExistingTransaction() {
		return get() != null;
	}
}
