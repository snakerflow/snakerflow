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

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.scheduling.IScheduler;
import org.snaker.engine.scheduling.JobEntity;

/**
 * 时限控制拦截器
 * 主要拦截任务的expireTime(期望完成时间)，并且向定时任务表插入数据
 * 由具体的定时器轮训产生提醒操作
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
	 * 时限控制拦截方法
	 */
	public void intercept(Execution execution) {
		for(Task task : execution.getTasks()) {
			String expireTime = task.getExpireTime();
			if(StringHelper.isNotEmpty(expireTime)) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					DateTime time = new DateTime(df.parse(expireTime));
				    JobEntity entity = new JobEntity(task, time.toDate(), execution.getArgs());
				    if(scheduler == null) {
				    	scheduler = ServiceContext.getContext().find(IScheduler.class);
				    }
				    if(scheduler != null) {
				    	scheduler.schedule(entity);
				    }
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
	}
}
