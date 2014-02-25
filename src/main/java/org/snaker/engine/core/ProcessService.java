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
package org.snaker.engine.core;

import java.io.InputStream;
import java.util.List;

import org.snaker.engine.IProcessService;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.Page;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.parser.ModelParser;

/**
 * 流程定义业务类
 * @author yuqs
 * @version 1.0
 */
public class ProcessService extends AccessService implements IProcessService {
	/**
	 * 由DBAccess实现类持久化order对象
	 */
	public void saveProcess(Process process) {
		access().saveProcess(process);
	}
	
	/**
	 * 由DBAccess实现类根据id或name获取process对象
	 */
	public Process getProcess(String idName) {
		return access().getProcess(idName);
	}
	
	/**
	 * 根据流程定义xml的输入流解析为字节数组，保存至数据库中，并且push到模型容器中
	 * @param input
	 */
	public String deploy(InputStream input) {
		try {
			byte[] bytes = StreamHelper.readBytes(input);
			ProcessModel model = ModelParser.parse(bytes);
			if(model.isExistSub()) {
				ModelContainer.cascadeReference(model);
			}
			Process process = getProcess(model.getName());
			boolean isNew = (process == null);
			if(process == null) {
				process = new Process();
				process.setId(StringHelper.getPrimaryKey());
			}
			process.setName(model.getName());
			process.setDisplayName(model.getDisplayName());
			process.setState(STATE_ACTIVE);
			process.setInstanceUrl(model.getInstanceUrl());
			process.setModel(model);
			process.setBytes(bytes);
			if(isNew) {
				saveProcess(process);
			} else {
				access().updateProcess(process);
			}
			ModelContainer.pushEntity(process.getId(), process);
			return process.getId();
		} catch(Exception e) {
			e.printStackTrace();
			throw new SnakerException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Process> getProcesss(Page<Process> page, String name, Integer state) {
		if(page == null) throw new SnakerException("分页对象不能为空.");
		return access().getProcesss(page, name, state);
	}

	public List<Process> getAllProcess() {
		return access().getAllProcess();
	}

	public void undeploy(String processId) {
		Process process = access().getProcess(processId);
		process.setState(STATE_FINISH);
		process.setName(process.getName() + "." + processId);
		access().updateProcess(process);
	}
}
