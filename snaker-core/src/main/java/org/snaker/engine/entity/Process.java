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

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;

import org.snaker.engine.SnakerException;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.model.ProcessModel;

/**
 * 流程定义实体类
 * @author yuqs
 * @since 1.0
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
	private String type;
	/**
	 * 当前流程的实例url（一般为流程第一步的url）
	 * 该字段可以直接打开流程申请的表单
	 */
	private String instanceUrl;
    /**
     * 是否可用的开关
     */
	private Integer state;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 创建人
	 */
	private String creator;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	public ProcessModel getModel() {
		return model;
	}
	
	/**
	 * setter name/displayName/instanceUrl
	 * @param processModel
	 */
	public void setModel(ProcessModel processModel) {
		this.model = processModel;
    	this.name = processModel.getName();
    	this.displayName = processModel.getDisplayName();
    	this.instanceUrl = processModel.getInstanceUrl();
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
			} catch (Exception e) {
				try {
					InputStream is = content.getBinaryStream();
					return StreamHelper.readBytes(is);
				} catch (Exception e1) {
					throw new SnakerException("couldn't extract stream out of blob", e1);
				}
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Process(id=").append(this.id);
		sb.append(",name=").append(this.name);
		sb.append(",displayName=").append(this.displayName);
		sb.append(",version=").append(this.version);
		sb.append(",state=").append(this.state).append(")");
		return sb.toString();
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
}
