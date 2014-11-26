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
package org.snaker.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.snaker.engine.TaskAccessStrategy;
import org.snaker.engine.entity.TaskActor;

/**
 * 基于用户或组（角色、部门等）的访问策略类
 * 该策略类适合组作为参与者的情况
 * @author yuqs
 * @since 1.4
 */
public class GeneralAccessStrategy implements TaskAccessStrategy {
	/**
	 * 根据操作人id确定所有的组集合
	 * @param operator 操作人id
	 * @return List<String> 确定的组集合[如操作人属于多个部门、拥有多个角色]
	 */
	protected List<String> ensureGroup(String operator) {
		return null;
	}
	
	/**
	 * 如果操作人id所属的组只要有一项存在于参与者集合中，则表示可访问
	 */
	public boolean isAllowed(String operator, List<TaskActor> actors) {
		List<String> assignees = ensureGroup(operator);
		if(assignees == null) assignees = new ArrayList<String>();
		assignees.add(operator);
		boolean isAllowed = false;
		for (TaskActor actor : actors) {
			for (String assignee : assignees) {
				if (actor.getActorId().equals(assignee)) {
					isAllowed = true;
					break;
				}
			}
		}
		return isAllowed;
	}
}
