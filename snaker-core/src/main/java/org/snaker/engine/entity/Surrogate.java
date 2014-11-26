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

/**
 * 委托代理实体类
 * @author yuqs
 * @since 1.4
 */
public class Surrogate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7359321877096338448L;
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 流程name
	 */
	private String processName;
	/**
	 * 授权人
	 */
	private String operator;
	/**
	 * 代理人
	 */
	private String surrogate;
	/**
	 * 操作时间
	 */
	private String odate;
	/**
	 * 开始时间
	 */
	private String sdate;
	/**
	 * 结束时间
	 */
	private String edate;
	/**
	 * 状态
	 */
	private Integer state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSurrogate() {
		return surrogate;
	}
	public void setSurrogate(String surrogate) {
		this.surrogate = surrogate;
	}
	public String getOdate() {
		return odate;
	}
	public void setOdate(String odate) {
		this.odate = odate;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
