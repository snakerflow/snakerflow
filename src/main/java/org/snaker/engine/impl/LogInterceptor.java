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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.TaskInterceptor;
import org.snaker.engine.entity.Task;

/**
 * 日志拦截器
 * @author yuqs
 * @version 1.0
 */
public class LogInterceptor implements TaskInterceptor {
	private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
	/**
	 * 拦截产生的任务对象，打印日志
	 */
	@Override
	public void intercept(List<Task> tasks) {
		if(log.isInfoEnabled()) {
			for(Task task : tasks) {
				StringBuffer buffer = new StringBuffer(100);
				buffer.append("创建任务[标识=").append(task.getId());
				buffer.append(",名称=").append(task.getDisplayName());
				buffer.append(",创建时间=").append(task.getCreateTime());
				buffer.append(",参与者={");
				if(task.getActorIds() != null) {
					for(String actor : task.getActorIds()) {
						buffer.append(actor).append(";");
					}
				}
				buffer.append("}]");
				log.info(buffer.toString());
			}
		}
	}
}
