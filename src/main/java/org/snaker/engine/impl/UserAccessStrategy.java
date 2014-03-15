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
package org.snaker.engine.impl;

import java.util.List;

import org.snaker.engine.TaskAccessStrategy;
import org.snaker.engine.entity.TaskActor;

/**
 * 基于用户的访问策略类
 * 该策略类适合用户标识作为参与者的情况
 * @author yuqs
 * @since 1.4
 */
public class UserAccessStrategy implements TaskAccessStrategy {
	/**
	 * 如果操作人id存在于参与者集合中，则表示可访问
	 */
	public boolean isAllowed(String operator, List<TaskActor> actors) {
		boolean isAllowed = false;
		for(TaskActor actor : actors) {
			if(actor.getActorId().equals(operator)) {
				isAllowed = true;
				break;
			}
		}
		return isAllowed;
	}
}
