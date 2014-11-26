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
package org.snaker.engine.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.snaker.engine.helper.JsonHelper;
import org.snaker.engine.model.TaskModel.PerformType;

/**
 * 历史任务实体类
 * @author yuqs
 * @since 1.0
 */
public class HistoryTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6814632180050362450L;
	/**
	 * 主键ID
	 */
	private String id;
    /**
     * 流程实例ID
     */
    private String orderId;
    /**
     * 任务名称
     */
	private String taskName;
	/**
	 * 任务显示名称
	 */
	private String displayName;
	/**
	 * 参与方式（0：普通任务；1：参与者fork任务[即：如果10个参与者，需要每个人都要完成，才继续流转]）
	 */
	private Integer performType;
	/**
	 * 任务类型
	 */
    private Integer taskType;
    /**
     * 任务状态（0：结束；1：活动）
     */
    private Integer taskState;
    /**
     * 任务处理者ID
     */
    private String operator;
    /**
     * 任务创建时间
     */
    private String createTime;
    /**
     * 任务完成时间
     */
    private String finishTime;
    /**
     * 期望任务完成时间
     */
    private String expireTime;
    /**
     * 任务关联的表单url
     */
    private String actionUrl;
    /**
     * 任务参与者列表
     */
    private String[] actorIds;
    /**
     * 父任务Id
     */
    private String parentTaskId;
	/**
     * 任务附属变量
     */
    private String variable;
    
    public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public HistoryTask() {
    	
    }
    
    public HistoryTask(Task task) {
    	this.id = task.getId();
    	this.orderId = task.getOrderId();
    	this.createTime = task.getCreateTime();
    	this.displayName = task.getDisplayName();
    	this.taskName = task.getTaskName();
    	this.taskType = task.getTaskType();
    	this.expireTime = task.getExpireTime();
    	this.actionUrl = task.getActionUrl();
    	this.actorIds = task.getActorIds();
    	this.parentTaskId = task.getParentTaskId();
    	this.variable = task.getVariable();
    	this.performType = task.getPerformType();
    }
    
    /**
     * 根据历史任务产生撤回的任务对象
     * @return 任务对象
     */
    public Task undoTask() {
    	Task task = new Task();
    	task.setOrderId(this.getOrderId());;
    	task.setTaskName(this.getTaskName());
    	task.setDisplayName(this.getDisplayName());
    	task.setTaskType(this.getTaskType());
    	task.setExpireTime(this.getExpireTime());
    	task.setActionUrl(this.getActionUrl());
    	task.setParentTaskId(this.getParentTaskId());
    	task.setVariable(this.getVariable());
    	task.setPerformType(this.getPerformType());
    	task.setOperator(this.getOperator());
    	return task;
    }
    
    public boolean isPerformAny() {
    	return this.performType.intValue() == PerformType.ANY.ordinal();
    }
    
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public Integer getPerformType() {
		return performType;
	}

	public void setPerformType(Integer performType) {
		this.performType = performType;
	}

	public String[] getActorIds() {
		return actorIds;
	}

	public void setActorIds(String[] actorIds) {
		this.actorIds = actorIds;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getVariableMap() {
        Map<String, Object> map = JsonHelper.fromJson(this.variable, Map.class);
        if(map == null) return Collections.emptyMap();
        return map;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HistoryTask(id=").append(this.id);
		sb.append(",orderId=").append(this.orderId);
		sb.append(",taskName=").append(this.taskName);
		sb.append(",displayName").append(this.displayName);
		sb.append(",taskType=").append(this.taskType);
		sb.append(",createTime").append(this.createTime);
		sb.append(",performType=").append(this.performType).append(")");
		return sb.toString();
	}
}
