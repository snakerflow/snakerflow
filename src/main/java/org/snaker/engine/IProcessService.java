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
package org.snaker.engine;

import java.io.InputStream;
import java.util.List;

import org.snaker.engine.access.Page;
import org.snaker.engine.entity.Process;

/**
 * 流程定义业务类
 * @author yuqs
 * @version 1.0
 */
public interface IProcessService {
	/**
	 * 保存流程定义
	 * @param process
	 */
	void saveProcess(Process process);
	
	/**
	 * 更新指定的流程定义ID
	 * @param processId
	 */
	void update(Process process, InputStream input);
	
	/**
	 * 根据主键ID、或name获取流程定义对象
	 * @param id
	 * @return Process
	 */
	Process getProcess(String idName);
	
	/**
	 * 根据给定的参数列表args分页查询process
	 * @param page
	 * @param args
	 * @return Page<Process>
	 */
	List<Process> getProcesss(Page<Process> page, String name, Integer state);
	
	/**
	 * 获取所有的流程定义集合
	 * @return List<Process>
	 */
	List<Process> getAllProcess();
	
	/**
	 * 根據InputStream輸入流，部署流程定義
	 * @param input
	 */
	String deploy(InputStream input);
	
	/**
	 * 卸载指定的流程定义
	 * @param processId
	 */
	void undeploy(String processId);
}
