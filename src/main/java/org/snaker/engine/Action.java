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
package org.snaker.engine;

import org.snaker.engine.core.Execution;

/**
 * 所有的模型对象需要实现的接口，需要实现execute方法，每个节点的执行方式不一样
 * @author yuqs
 * @version 1.0
 */
public interface Action {
	/**
	 * 根据当前的执行对象所维持的process、order、model、args对所属流程实例进行执行
	 * @param execution
	 */
	public void execute(Execution execution);
}
