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
package org.snaker.engine.entity.var;

import java.io.Serializable;

/**
 * 历史变量类
 * @author yuqs
 * @since 3.0
 */
public class HistoryVariable implements ValueFields, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private String id;
    /**
     * 版本
     */
    private Integer version = 0;
    /**
     * 流程实例ID
     */
    private String orderId;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 变量名称
     */
    private String name;
    /**
     * 变量类型
     */
    protected VariableType variableType;
    /**
     * 变量类型索引
     */
    protected Integer type;
    /**
     * 长整型值
     */
    protected Long longValue;
    /**
     * 浮点型值
     */
    protected Double doubleValue;
    /**
     * 文本值
     */
    protected String textValue;

    public HistoryVariable() {}

    public HistoryVariable(Variable variable) {
        this.id = variable.getId();
        this.orderId = variable.getOrderId();
        this.taskId = variable.getTaskId();
        this.name = variable.getName();
        this.type = variable.getType();
        this.variableType = variable.getVariableType();
        this.doubleValue = variable.getDoubleValue();
        this.textValue = variable.getTextValue();
        this.longValue = variable.getLongValue();
        this.version = variable.getVersion();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableType getVariableType() {
        if(variableType == null && type != null) {
            variableType = VariableHelper.getVariableType(type);
        }
        return variableType;
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }
    public Integer getType() {
        if(type == null || type == -1) {
            type = VariableHelper.getTypeIndex(variableType);
        }
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setValue(Object value) {
        getVariableType().setValue(value, this);
    }

    public Object getValue() {
        return getVariableType().getValue(this);
    }
}
