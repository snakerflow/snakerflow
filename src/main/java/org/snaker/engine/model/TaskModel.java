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
package org.snaker.engine.model;

import org.apache.commons.lang.math.NumberUtils;
import org.snaker.engine.AssignmentHandler;
import org.snaker.engine.core.Execution;
import org.snaker.engine.handlers.impl.MergeActorHandler;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.scheduling.JobCallback;

/**
 * 任务定义task元素
 * @author yuqs
 * @version 1.0
 */
public class TaskModel extends WorkModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1775545243233990922L;
	/**
	 * 类型：普通任务
	 */
	public static final String TYPE_ANY = "ANY";
	/**
	 * 类型：参与者fork任务
	 */
	public static final String TYPE_ALL = "ALL";
	/**
	 * 参与类型
	 */
	public enum PerformType {
		ANY, ALL;
	}
	/**
	 * 参与者变量名称
	 */
	private String assignee;
	/**
	 * 参与方式
	 * any：任何一个参与者处理完即执行下一步
	 * all：所有参与者都完成，才可执行下一步
	 */
	private String performType;
	/**
	 * 期望完成时间
	 */
	private String expireTime;
	/**
	 * 提醒时间
	 */
	private String reminderTime;
	/**
	 * 提醒次数
	 */
	private int reminderRepeat = 0;
	/**
	 * 是否自动执行
	 */
	private String autoExecute;
	/**
	 * 任务执行后回调方法
	 */
	private JobCallback callback;
	/**
	 * 分配参与者处理类
	 */
	private AssignmentHandler assignmentHandler;

	protected void exec(Execution execution) {
		if(performType == null || performType.equalsIgnoreCase(TYPE_ANY)) {
			/**
			 * any方式，直接执行输出变迁
			 */
			runOutTransition(execution);
		} else {
			/**
			 * all方式，需要判断是否已全部合并
			 * 由于all方式分配任务，是每人一个任务
			 * 那么此时需要判断之前分配的所有任务都执行完成后，才可执行下一步，否则不处理
			 */
			fire(new MergeActorHandler(getName()), execution);
			if(execution.isMerged()) runOutTransition(execution);
		}
	}
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getPerformType() {
		return performType;
	}

	public void setPerformType(String performType) {
		this.performType = performType;
	}

	public String getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(String reminderTime) {
		this.reminderTime = reminderTime;
	}

	public int getReminderRepeat() {
		return reminderRepeat;
	}

	public void setReminderRepeat(String reminderRepeat) {
		if(NumberUtils.isNumber(reminderRepeat)) {
			this.reminderRepeat = Integer.parseInt(reminderRepeat);
		}
	}

	public String getAutoExecute() {
		return autoExecute;
	}

	public void setAutoExecute(String autoExecute) {
		this.autoExecute = autoExecute;
	}

	public JobCallback getJobCallback() {
		return callback;
	}

	public void setJobCallback(String callbackStr) {
		if(StringHelper.isNotEmpty(callbackStr)) {
			callback = (JobCallback)ClassHelper.newInstance(callbackStr);
			AssertHelper.notNull(callback, "回调处理类实例化失败");
		}
	}

	public AssignmentHandler getAssignmentHandler() {
		return assignmentHandler;
	}

	public void setAssignmentHandler(String assignmentHandlerStr) {
		if(StringHelper.isNotEmpty(assignmentHandlerStr)) {
			assignmentHandler = (AssignmentHandler)ClassHelper.newInstance(assignmentHandlerStr);
			AssertHelper.notNull(assignmentHandler, "分配参与者处理类实例化失败");
		}
	}
}
