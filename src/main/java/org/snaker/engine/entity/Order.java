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
package org.snaker.engine.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 流程工作单实体类（一般称为流程实例）
 * @author yuqs
 * @version 1.0
 */
public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8335779448165343933L;
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 版本
	 */
	private Integer version;
	/**
	 * 流程定义ID
	 */
    private String processId;
    /**
     * 流程实例创建者ID
     */
    private String creator;
    /**
     * 流程实例创建时间
     */
    private String createTime;
    /**
     * 流程实例为子流程时，该字段标识父流程实例ID
     */
    private String parentId;
    /**
     * 流程实例为子流程时，该字段标识父流程哪个节点模型启动的子流程
     */
    private String parentNodeName;
    /**
     * 流程实例期望完成时间
     */
    private String expireTime;
    /**
     * 流程实例上一次更新时间
     */
    private String lastUpdateTime;
    /**
     * 流程实例上一次更新人员ID
     */
    private String lastUpdator;
    /**
     * 流程实例优先级
     */
    private Integer priority;
    /**
     * 流程实例编号
     */
    private String orderNo;
	/**
     * 流程实例附属变量
     */
    private String variable;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastUpdator() {
		return lastUpdator;
	}

	public void setLastUpdator(String lastUpdator) {
		this.lastUpdator = lastUpdator;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}
	
    public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
