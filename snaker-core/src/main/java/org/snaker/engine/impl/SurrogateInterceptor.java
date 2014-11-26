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

import org.snaker.engine.ITaskService;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StringHelper;

/**
 * 委托代理拦截器
 * 负责查询wf_surrogate表获取委托代理人，并通过addTaskActor设置为参与者
 * 这里是对新创建的任务通过添加参与者进行委托代理(即授权人、代理人都可处理任务)
 * 对于运行中且未处理的待办任务，可调用engine.task().addTaskActor方法
 * {@link ITaskService#addTaskActor(String, String...)}
 * @author yuqs
 * @since 1.4
 */
public class SurrogateInterceptor implements SnakerInterceptor {
	public void intercept(Execution execution) {
		SnakerEngine engine = execution.getEngine();
		for(Task task : execution.getTasks()) {
			if(task.getActorIds() == null) continue;
			for(String actor : task.getActorIds()) {
				if(StringHelper.isEmpty(actor)) continue;
				String agent = engine.manager().getSurrogate(actor, execution.getProcess().getName());
				if(StringHelper.isNotEmpty(agent) && !actor.equals(agent)) {
					engine.task().addTaskActor(task.getId(), agent);
				}
			}
		}
	}

}
