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

import org.snaker.engine.model.ProcessModel;

/**
 * 编号生成器接口
 * 流程实例的编号字段使用该接口实现类来产生对应的编号
 * @author yuqs
 * @version 1.0
 */
public interface INoGenerator {
	/**
	 * 生成器方法
	 * @param model
	 * @return
	 */
	String generate(ProcessModel model);
}
