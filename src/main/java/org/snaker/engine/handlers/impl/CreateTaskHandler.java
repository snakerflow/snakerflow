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
package org.snaker.engine.handlers.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.TaskInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.entity.Task;
import org.snaker.engine.handlers.IHandler;
import org.snaker.engine.model.TaskModel;
import org.snaker.engine.model.WorkModel;

/**
 * 任务创建操作的处理器
 * @author yuqs
 * @version 1.0
 */
public class CreateTaskHandler implements IHandler {
	private static final Logger log = LoggerFactory.getLogger(CreateTaskHandler.class);
	/**
	 * 任务模型
	 */
	private WorkModel model;
	
	/**
	 * 调用者需要提供任务模型
	 * @param model
	 */
	public CreateTaskHandler(WorkModel model) {
		this.model = model;
	}
	
	/**
	 * 根据任务模型、执行对象，创建下一个任务，并添加到execution对象的tasks集合中
	 */
	@Override
	public void handle(Execution execution) {
		List<Task> tasks = execution.getEngine().createTask(model, execution);
		
		/**
		 * 从服务上下文中查找任务拦截器列表，依次对task集合进行拦截处理
		 */
		List<TaskInterceptor> interceptors = ServiceContext.getContext().findInterceptors();
		if(model instanceof TaskModel) {
			List<TaskInterceptor> localInterceptors = ((TaskModel)model).getInterceptorList();
			interceptors.addAll(localInterceptors);
		}
		try {
			for(TaskInterceptor interceptor : interceptors) {
				interceptor.intercept(tasks);
			}
		} catch(Exception e) {
			//拦截器执行过程中出现的异常不影响流程执行逻辑
			log.error("拦截器执行失败=" + e.getMessage());
		}

		execution.addTasks(tasks);
	}
}
