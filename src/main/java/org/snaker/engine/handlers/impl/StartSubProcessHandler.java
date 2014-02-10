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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ModelContainer;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.handlers.IHandler;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.SubProcessModel;

/**
 * 启动子流程的处理器
 * @author yuqs
 * @version 1.0
 */
public class StartSubProcessHandler implements IHandler {
	private SubProcessModel model;
	/**
	 * 是否以future方式执行启动子流程任务
	 */
	private boolean isFutureRunning = false;
	public StartSubProcessHandler(SubProcessModel model) {
		this.model = model;
	}
	
	public StartSubProcessHandler(SubProcessModel model, boolean isFutureRunning) {
		this.model = model;
		this.isFutureRunning = isFutureRunning;
	}
	
	/**
	 * 子流程执行的处理
	 */
	@Override
	public void handle(Execution execution) {
		//获取子流程模型对象
		ProcessModel pm = model.getSubProcess();
		//根据子流程模型名称获取子流程定义对象
		Process process = ModelContainer.getEntity(pm.getName());
		SnakerEngine engine = execution.getEngine();
		
		Execution child = execution.createSubExecution(execution, process, model.getName());
		Order order = null;
		if(isFutureRunning) {
			//创建单个线程执行器来执行启动子流程的任务
			ExecutorService es = Executors.newSingleThreadExecutor();
			//提交执行任务，并返回future
			Future<Order> future = es.submit(new ExecuteTask(execution, process, model.getName()));
			try {
				es.shutdown();
				order = future.get();
			} catch (InterruptedException e) {
				throw new SnakerException("Future线程被强制终止执行", e.getCause());
			} catch (ExecutionException e) {
				throw new SnakerException("Future线程执行异常.", e.getCause());
			}
		} else {
			order  = engine.startInstanceByExecution(child);
		}
		if(order == null) throw new SnakerException("子流程创建失败");

		execution.addTasks(engine.query().getActiveTasks(order.getId()));
	}

	/**
	 * Future模式的任务执行。通过call返回任务结果集
	 * @author yuqs
	 * @version 1.0
	 */
	class ExecuteTask implements Callable<Order> {
		private SnakerEngine engine;
		private Execution child;
		/**
		 * 构造函数
		 * @param execution
		 * @param process
		 * @param parentNodeName
		 */
		public ExecuteTask(Execution execution, Process process,String parentNodeName) {
			this.engine = execution.getEngine();
			child = execution.createSubExecution(execution, process, parentNodeName);
		}
		@Override
		public Order call() throws Exception {
			return engine.startInstanceByExecution(child);
		}
	}
}
