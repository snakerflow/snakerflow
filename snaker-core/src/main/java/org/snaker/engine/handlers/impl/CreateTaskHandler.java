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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.SnakerInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.entity.Task;
import org.snaker.engine.handlers.IHandler;
import org.snaker.engine.model.TaskModel;

/**
 * 任务创建操作的处理器
 * @author yuqs
 * @since 1.0
 */
public class CreateTaskHandler implements IHandler {
	private static final Logger log = LoggerFactory.getLogger(CreateTaskHandler.class);
	/**
	 * 任务模型
	 */
	private TaskModel model;
	
	/**
	 * 调用者需要提供任务模型
	 * @param model 模型
	 */
	public CreateTaskHandler(TaskModel model) {
		this.model = model;
	}
	
	/**
	 * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中
	 */
	public void handle(Execution execution) {
		List<Task> tasks = execution.getEngine().task().createTask(model, execution);
		execution.addTasks(tasks);
		/**
		 * 从服务上下文中查找任务拦截器列表，依次对task集合进行拦截处理
		 */
		List<SnakerInterceptor> interceptors = ServiceContext.getContext().findList(SnakerInterceptor.class);
		try {
			for(SnakerInterceptor interceptor : interceptors) {
				interceptor.intercept(execution);
			}
		} catch(Exception e) {
			log.error("拦截器执行失败=" + e.getMessage());
			throw new SnakerException(e);
		}
	}
}
