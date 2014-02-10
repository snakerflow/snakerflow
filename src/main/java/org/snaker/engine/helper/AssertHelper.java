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

/**
 * 断言帮助类
 * @author yuqs
 * @version 1.0
 */
public abstract class AssertHelper {
	/**
	 * 断言表达式为true
	 * @param expression
	 * @param message 异常打印信息
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言表达式为true
	 * @param expression
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	/**
	 * 断言给定的object对象为空
	 * @param object
	 * @param message 异常打印信息
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言给定的object对象为空
	 * @param object
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	/**
	 * 断言给定的object对象为非空
	 * @param object
	 * @param message 异常打印信息
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 断言给定的object对象为非空
	 * @param object
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}
}
