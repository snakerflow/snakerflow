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
package org.snaker.engine.handlers.impl;

/**
 * actor all方式的合并处理器
 * @author yuqs
 * @since 1.0
 */
public class MergeActorHandler extends AbstractMergeHandler {
	/**
	 * 调用者需要提供actor all的任务名称
	 */
	private String taskName;
	
	/**
	 * 构造函数，由调用者提供taskName
	 * @param taskName
	 */
	public MergeActorHandler(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * actor all方式，查询参数为：orderId、taskName
	 * @see org.snaker.engine.handlers.impl.AbstractMergeHandler#findActiveNodes()
	 */
	protected String[] findActiveNodes() {
		return new String[]{taskName};
	}
}
