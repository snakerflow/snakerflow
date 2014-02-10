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
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.snaker.engine.SnakerException;
import org.snaker.engine.model.ProcessModel;

/**
 * 流程定义实体类
 * @author yuqs
 * @version 1.0
 */
public class Process implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6541688543201014542L;
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 版本
	 */
	private Integer version;
	/**
     * 存在父子流程时，需要设置父流程定义的ID
     */
	private String parentId;
    /**
     * 流程定义名称
     */
	private String name;
    /**
     * 流程定义显示名称
     */
	private String displayName;
    /**
     * 流程定义类型（预留字段）
     */
	private Integer type;
	/**
	 * 当前流程的实例url（一般为流程第一步的url）
	 * 该字段可以直接打开流程申请的表单
	 */
	private String instanceUrl;
    /**
     * 当前流程的查询url
     */
	private String queryUrl;
    /**
     * 是否可用的开关
     */
	private Integer state;
	/**
	 * 流程定义模型
	 */
    private ProcessModel model;
    /**
     * 流程定义xml
     */
    private Blob content;
    /**
     * 流程定义字节数组
     */
    private byte[] bytes;
    
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getQueryUrl() {
		return queryUrl;
	}
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public ProcessModel getModel() {
		return model;
	}
	public void setModel(ProcessModel model) {
		this.model = model;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public byte[] getDBContent() {
		if(this.content != null) {
			try {
				return this.content.getBytes(1L, Long.valueOf(this.content.length()).intValue());
			} catch (SQLException e) {
				throw new SnakerException("couldn't extract stream out of blob", e);
			}
		}
		
		return bytes;
	}
	public Blob getContent() {
		return content;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
    public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
