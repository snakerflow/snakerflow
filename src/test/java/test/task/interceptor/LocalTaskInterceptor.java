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
package test.task.interceptor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snaker.engine.TaskInterceptor;
import org.snaker.engine.entity.Task;

/**
 * @author yuqs
 * @version 1.0
 */
public class LocalTaskInterceptor implements TaskInterceptor {
	private static final Log log = LogFactory.getLog(LocalTaskInterceptor.class);
	@Override
	public void intercept(List<Task> tasks) {
		if(log.isInfoEnabled()) {
			log.info("LocalTaskInterceptor start...");
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
			log.info("LocalTaskInterceptor finish...");
		}
	}

}
