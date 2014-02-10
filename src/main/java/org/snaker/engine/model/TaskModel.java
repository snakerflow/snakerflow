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

import java.util.ArrayList;
import java.util.List;

import org.snaker.engine.TaskInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.handlers.impl.MergeActorHandler;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;

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
	 * 局部拦截器
	 */
	private String interceptors;
	/**
	 * 局部拦截器实例集合
	 */
	private List<TaskInterceptor> interceptorList = new ArrayList<TaskInterceptor>();
	
	/**
	 * 所有task节点的transition子节点都会执行
	 */
	@Override
	public void execute(Execution execution) {
		if(performType == null || performType.equalsIgnoreCase(TYPE_ANY)) {
			/**
			 * any方式，直接执行输出变迁
			 */
			super.execute(execution);
		} else {
			/**
			 * all方式，需要判断是否已全部合并
			 * 由于all方式分配任务，是每人一个任务
			 * 那么此时需要判断之前分配的所有任务都执行完成后，才可执行下一步，否则不处理
			 */
			fire(new MergeActorHandler(getName()), execution);
			if(execution.isMerged()) super.execute(execution);
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

	public String getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(String interceptors) {
		this.interceptors = interceptors;
		if(StringHelper.isNotEmpty(interceptors)) {
			for(String interceptor : interceptors.split(",")) {
				TaskInterceptor instance = (TaskInterceptor)ClassHelper.newInstance(interceptor);
				if(instance != null) this.interceptorList.add(instance);
			}
		}
	}

	public List<TaskInterceptor> getInterceptorList() {
		return interceptorList;
	}
}
