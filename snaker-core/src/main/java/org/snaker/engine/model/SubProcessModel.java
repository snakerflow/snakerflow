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
package org.snaker.engine.model;

import org.snaker.engine.core.Execution;

/**
 * 子流程定义subprocess元素
 * @author yuqs
 * @since 1.0
 */
public class SubProcessModel extends WorkModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3923955459202018147L;
	/**
	 * 子流程名称
	 */
	private String processName;
	/**
	 * 子流程版本号
	 */
	private Integer version;
	/**
	 * 子流程定义引用
	 */
	private ProcessModel subProcess;

	protected void exec(Execution execution) {
		runOutTransition(execution);	
	}
	
	public ProcessModel getSubProcess() {
		return subProcess;
	}
	public void setSubProcess(ProcessModel subProcess) {
		this.subProcess = subProcess;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
