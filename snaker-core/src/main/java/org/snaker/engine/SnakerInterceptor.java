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

import org.snaker.engine.core.Execution;

/**
 * 任务拦截器，对产生的任务结果进行拦截
 * @author yuqs
 * @since 1.2
 */
public interface SnakerInterceptor {
	/**
	 * 拦截方法，参数为执行对象
	 * @param execution 执行对象。可从中获取执行的数据
	 */
	public void intercept(Execution execution);
}
