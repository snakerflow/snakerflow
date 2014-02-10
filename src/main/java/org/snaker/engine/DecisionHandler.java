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
 * 决策处理器接口
 * @author yuqs
 * @version 1.0
 */
public interface DecisionHandler {
	/**
	 * 定义决策方法，实现类需要根据执行对象做处理，并返回后置流转的name
	 * @param execution
	 * @return
	 */
	String decide(Execution execution);
}
