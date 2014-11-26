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
package org.snaker.engine.access;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.helper.AssertHelper;

/**
 * 通用查询过滤器
 * @author yuqs
 * @since 1.2.5
 */
public class QueryFilter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8155136377911571881L;
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    //排序字段
    private String orderBy;
    //排序类型ASC/DESC
    private String order;

	/*********common parameters***********/
	/**
	 * 流程定义id
	 */
	private String processId;
	/**
	 * 流程定义版本号
	 */
	private Integer version;
	/**
	 * 流程实例id
	 */
	private String orderId;
	/**
	 * 任务id
	 */
	private String taskId;
	/**
	 * 创建时间范围
	 */
	private String createTimeStart;
	private String createTimeEnd;
	private String operateTime;
	/**
	 * 操作人员id
	 */
	private String[] operators;
	/**
	 * 名称
	 */
	private String[] names;
	/**
	 * 显示名称
	 */
	private String displayName;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 流程类型
	 */
	private String processType;
	/**
	 * exclude ids
	 */
	private String[] excludedIds;
	
	/*********order parameters***********/
	/**
	 * 父实例id
	 */
	private String parentId;
	/**
	 * 实例编号
	 */
	private String orderNo;
	
	/*********task parameters***********/
	/**
	 * 任务类型
	 */
	private Integer taskType;
	/**
	 * 任务参与类型
	 */
	private Integer performType;
	
	public String getProcessId() {
		return processId;
	}
	public QueryFilter setProcessId(String processId) {
		AssertHelper.notEmpty(processId);
		this.processId = processId;
		return this;
	}
	public String getOrderId() {
		return orderId;
	}
	public QueryFilter setOrderId(String orderId) {
		AssertHelper.notEmpty(orderId);
		this.orderId = orderId;
		return this;
	}
	public String getTaskId() {
		return taskId;
	}
	public QueryFilter setTaskId(String taskId) {
		AssertHelper.notEmpty(taskId);
		this.taskId = taskId;
		return this;
	}
	public String getCreateTimeStart() {
		return createTimeStart;
	}
	public QueryFilter setCreateTimeStart(String createTimeStart) {
		AssertHelper.notEmpty(createTimeStart);
		this.createTimeStart = createTimeStart;
		return this;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public QueryFilter setCreateTimeEnd(String createTimeEnd) {
		AssertHelper.notEmpty(createTimeEnd);
		this.createTimeEnd = createTimeEnd;
		return this;
	}
	public String[] getOperators() {
		return operators;
	}
	public QueryFilter setOperators(String[] operators) {
		AssertHelper.notNull(operators);
		this.operators = operators;
		return this;
	}
	public QueryFilter setOperator(String operator) {
		AssertHelper.notEmpty(operator);
		this.operators = new String[1];
		this.operators[0] = operator;
		return this;
	}
	public String[] getNames() {
		return names;
	}
	public QueryFilter setNames(String[] names) {
		AssertHelper.notNull(names);
		this.names = names;
		return this;
	}
	public QueryFilter setName(String name) {
		AssertHelper.notEmpty(name);
		this.names = new String[1];
		this.names[0] = name;
		return this;
	}
	public String getDisplayName() {
		return displayName;
	}
	public QueryFilter setDisplayName(String displayName) {
		AssertHelper.notEmpty(displayName);
		this.displayName = displayName;
		return this;
	}
	public Integer getState() {
		return state;
	}
	public QueryFilter setState(Integer state) {
		AssertHelper.notNull(state);
		this.state = state;
		return this;
	}
	public String getParentId() {
		return parentId;
	}
	public QueryFilter setParentId(String parentId) {
		AssertHelper.notEmpty(parentId);
		this.parentId = parentId;
		return this;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public QueryFilter setOrderNo(String orderNo) {
		AssertHelper.notEmpty(orderNo);
		this.orderNo = orderNo;
		return this;
	}
	public Integer getTaskType() {
		return taskType;
	}
	public QueryFilter setTaskType(Integer taskType) {
		AssertHelper.notNull(taskType);
		this.taskType = taskType;
		return this;
	}
	public Integer getPerformType() {
		return performType;
	}
	public QueryFilter setPerformType(Integer performType) {
		AssertHelper.notNull(performType);
		this.performType = performType;
		return this;
	}
	public String[] getExcludedIds() {
		return excludedIds;
	}
	public QueryFilter setExcludedIds(String[] excludedIds) {
		AssertHelper.notNull(excludedIds);
		this.excludedIds = excludedIds;
		return this;
	}
	public Integer getVersion() {
		return version;
	}
	public QueryFilter setVersion(Integer version) {
		AssertHelper.notNull(version);
		this.version = version;
		return this;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public QueryFilter setOperateTime(String operateTime) {
		AssertHelper.notEmpty(operateTime);
		this.operateTime = operateTime;
		return this;
	}
	public String getProcessType() {
		return processType;
	}
	public QueryFilter setProcessType(String processType) {
		AssertHelper.notEmpty(processType);
		this.processType = processType;
		return this;
	}
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public QueryFilter orderBy(String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }
    public String getOrder() {
        return order;
    }
    /**
     * 设置排序类型.
     * @param order 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(String order) {
        String lowcaseOrder = StringUtils.lowerCase(order);
        //检查order字符串的合法值
        String[] orders = StringUtils.split(lowcaseOrder, ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序类型[" + orderStr + "]不是合法值");
            }
        }
        this.order = lowcaseOrder;
    }
    public QueryFilter order(String theOrder) {
        setOrder(theOrder);
        return this;
    }
    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }
}
