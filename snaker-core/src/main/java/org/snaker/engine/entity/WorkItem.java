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

import org.snaker.engine.helper.JsonHelper;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 工作项（待办、已处理任务的查询结果实体）
 * @author yuqs
 * @since 1.0
 */
public class WorkItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2630386406754942892L;
	private String id;
    /**
     * 流程定义ID
     */
    private String processId;
    /**
     * 流程实例ID
     */
    private String orderId;
	/**
	 * 任务ID
	 */
	private String taskId;
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 流程实例url
     */
    private String instanceUrl;
	/**
     * 流程实例为子流程时，该字段标识父流程实例ID
     */
    private String parentId;
    /**
     * 流程实例创建者ID
     */
    private String creator;
    /**
     * 流程实例创建时间
     */
    private String orderCreateTime;
    /**
     * 流程实例结束时间
     */
    private String orderEndTime;
    /**
     * 流程实例期望完成时间
     */
    private String orderExpireTime;
    /**
     * 流程实例编号
     */
    private String orderNo;
	/**
     * 流程实例附属变量
     */
    private String orderVariable;
    /**
     * 任务名称
     */
	private String taskName;
	/**
	 * 任务标识名称
	 */
	private String taskKey;
	/**
	 * 参与类型（0：普通任务；1：参与者fork任务[即：如果10个参与者，需要每个人都要完成，才继续流转]）
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
     * 任务创建时间
     */
    private String taskCreateTime;
    /**
     * 任务完成时间
     */
    private String taskEndTime;
    /**
     * 期望任务完成时间
     */
    private String taskExpireTime;
	/**
     * 任务附属变量
     */
    private String taskVariable;
    /**
     * 任务处理者ID
     */
    private String operator;
    /**
     * 任务关联的表单url
     */
    private String actionUrl;
    /**
     * 任务参与者列表
     */
    private String[] actorIds;

	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
    public String getInstanceUrl() {
		return instanceUrl;
	}
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getOrderEndTime() {
		return orderEndTime;
	}
	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
	public String getOrderExpireTime() {
		return orderExpireTime;
	}
	public void setOrderExpireTime(String orderExpireTime) {
		this.orderExpireTime = orderExpireTime;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	public Integer getPerformType() {
		return performType;
	}
	public void setPerformType(Integer performType) {
		this.performType = performType;
	}
	public Integer getTaskState() {
		return taskState;
	}
	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}
	public String getTaskCreateTime() {
		return taskCreateTime;
	}
	public void setTaskCreateTime(String taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}
	public String getTaskEndTime() {
		return taskEndTime;
	}
	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	public String getTaskExpireTime() {
		return taskExpireTime;
	}
	public void setTaskExpireTime(String taskExpireTime) {
		this.taskExpireTime = taskExpireTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String[] getActorIds() {
		return actorIds;
	}
	public void setActorIds(String[] actorIds) {
		this.actorIds = actorIds;
	}
	public String getOrderVariable() {
		return orderVariable;
	}
	public void setOrderVariable(String orderVariable) {
		this.orderVariable = orderVariable;
	}
	public String getTaskVariable() {
		return taskVariable;
	}
	public void setTaskVariable(String taskVariable) {
		this.taskVariable = taskVariable;
	}
    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrderVariableMap() {
        Map<String, Object> map = JsonHelper.fromJson(this.orderVariable, Map.class);
        if(map == null) return Collections.emptyMap();
        return map;
    }
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTaskVariableMap() {
        Map<String, Object> map = JsonHelper.fromJson(this.taskVariable, Map.class);
        if(map == null) return Collections.emptyMap();
        return map;
    }
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("WorkItem(processId=").append(this.processId);
		sb.append(",orderId=").append(this.orderId);
		sb.append(",taskId=").append(this.taskId);
		sb.append(",processName").append(this.processName);
		sb.append(",taskType=").append(this.taskType);
		sb.append(",taskName").append(this.taskName);
		sb.append(",performType=").append(this.performType).append(")");
		return sb.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
