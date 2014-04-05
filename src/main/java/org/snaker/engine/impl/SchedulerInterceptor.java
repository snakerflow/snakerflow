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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.entity.Task;
import org.snaker.engine.scheduling.IScheduler;
import org.snaker.engine.scheduling.JobEntity;
import org.snaker.engine.scheduling.JobEntity.JobType;

/**
 * 时限控制拦截器
 * 主要拦截任务的expireDate(期望完成时间)
 * 再交给具体的调度器完成调度处理
 * @author yuqs
 * @since 1.4
 */
public class SchedulerInterceptor implements SnakerInterceptor {
	private static final Logger log = LoggerFactory.getLogger(SchedulerInterceptor.class);
	/**
	 * 调度器接口
	 */
	private IScheduler scheduler;
	/**
	 * 是否调度
	 */
	private boolean isScheduled = true;
	/**
	 * 时限控制拦截方法
	 */
	public void intercept(Execution execution) {
		if(!isScheduled) return;
		for(Task task : execution.getTasks()) {
			String id = execution.getProcess().getId() 
					+ "-" + execution.getOrder().getId() 
					+ "-" + task.getId();
			Date expireDate = task.getExpireDate();
			if(expireDate != null) {
				try {
				    JobEntity entity = new JobEntity(id, task, expireDate, execution.getArgs());
				    entity.setModelName(task.getTaskName());
				    entity.setJobType(JobType.EXECUTER.ordinal());
				    schedule(entity);
				} catch (Exception e) {
					log.error(e.getMessage());
					log.info("scheduler failed.task is:" + task);
				}
			}
			Date remindDate = task.getRemindDate();
			if(remindDate != null) {
				try {
				    JobEntity entity = new JobEntity(id, task, remindDate, execution.getArgs());
				    entity.setModelName(task.getTaskName());
				    entity.setJobType(JobType.REMINDER.ordinal());
				    schedule(entity);
				} catch (Exception e) {
					log.error(e.getMessage());
					log.info("scheduler failed.task is:" + task);
				}
			}
		}
	}
	
	private void schedule(JobEntity entity) {
	    if(scheduler == null) {
	    	scheduler = ServiceContext.getContext().find(IScheduler.class);
	    }
	    if(scheduler != null) {
	    	scheduler.schedule(entity);
	    } else {
	    	isScheduled = false;
	    }
	}
}
