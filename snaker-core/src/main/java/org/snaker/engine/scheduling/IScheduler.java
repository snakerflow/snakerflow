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
package org.snaker.engine.scheduling;

/**
 * 调度器接口，与具体的定时调度框架无关
 * @author yuqs
 * @since 1.4
 */
public interface IScheduler {
	public static final String CONFIG_REPEAT = "scheduler.repeat";
	public static final String CONFIG_USECALENDAR = "scheduler.useCalendar";
	public static final String CONFIG_HOLIDAYS = "scheduler.holidays";
	public static final String CONFIG_WEEKS = "scheduler.weeks";
	public static final String CONFIG_WORKTIME = "scheduler.workTime";
	
	public static final String CALENDAR_NAME = "snakerCalendar";
	
	public static final String KEY = "id";
	public static final String MODEL = "model";
	public static final String GROUP = "snaker";
	
	public static final String TYPE_EXECUTOR = "executor.";
	public static final String TYPE_REMINDER = "reminder.";
	/**
	 * 调度执行方法
	 * @param entity 调度DTO
	 */
    void schedule(JobEntity entity);
    
    /**
     * 停止调度
     * @param key job主键
     */
    void delete(String key);
}
