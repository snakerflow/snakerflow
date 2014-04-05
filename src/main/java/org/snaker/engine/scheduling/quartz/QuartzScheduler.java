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
package org.snaker.engine.scheduling.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.scheduling.IScheduler;
import org.snaker.engine.scheduling.JobEntity;

/**
 * quartz框架实现的调度实例
 * @author yuqs
 * @since 1.4
 */
public class QuartzScheduler implements IScheduler {
	private static final Logger log = LoggerFactory.getLogger(QuartzScheduler.class);
	private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	private Scheduler getScheduler() {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			//TODO add calendar
			return scheduler;
		} catch (SchedulerException e) {
			throw new SnakerException(e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * 
	 */
	public void schedule(JobEntity entity) {
		AssertHelper.notNull(entity);
	    JobDataMap data = new JobDataMap(entity.getArgs());
	    data.put(KEY, entity.getId());
	    data.put(MODEL, entity.getModelName());
	    Class<? extends Job> jobClazz = null;
	    switch(entity.getJobType()) {
	    case 0:
	    	jobClazz = ExecutorJob.class;
	    	break;
	    case 1:
	    	jobClazz = ReminderJob.class;
	    	break;
	    }
	    JobDetail job = JobBuilder
	    		.newJob(jobClazz)
	    		.usingJobData(data)
	    		.withIdentity(entity.getKey(), GROUP)
	    		.build();
	    Trigger trigger = null;
	    if(jobClazz == ReminderJob.class) {
	    	int count = ConfigHelper.getNumerProperty("repeatCount");
	    	if(count < 0) count = 1;
	    	trigger = TriggerBuilder
    		.newTrigger()
    		.withIdentity(StringHelper.getPrimaryKey(), GROUP)
    		.withSchedule(SimpleScheduleBuilder.
    				repeatMinutelyForTotalCount(count, entity.getPeriod()))
    		.startAt(entity.getStartTime())
    		.build();
	    } else {
	    	trigger = TriggerBuilder
    		.newTrigger()
    		.withIdentity(StringHelper.getPrimaryKey(), GROUP)
    		.startAt(entity.getStartTime())
    		.build();
	    }
	    try {
			getScheduler().scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}

	public void pause(String key) {
		AssertHelper.notEmpty(key);
		try {
			getScheduler().pauseJob(new JobKey(key, GROUP));
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}
}
