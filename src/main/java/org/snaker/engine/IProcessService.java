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
	 * @param process 流程定义对象
	 */
	void saveProcess(Process process);
	
	/**
	 * 根据主键ID、或name获取流程定义对象
	 * @param idName 流程定义id或name
	 * @return Process 流程定义对象
	 */
	Process getProcess(String idName);
	
	/**
	 * 根据给定的参数列表args分页查询process
	 * @param page 分页对象
	 * @param name 流程定义name
	 * @param state 流程定义状态
	 * @return List<Process> 流程定义对象集合
	 */
	List<Process> getProcesss(Page<Process> page, String name, Integer state);
	
	/**
	 * 获取所有的流程定义集合
	 * @return List<Process> 流程定义对象集合
	 */
	List<Process> getAllProcess();
	
	/**
	 * 根據InputStream輸入流，部署流程定义
	 * @param input 流程定义输入流
	 * @return String 流程定义id
	 */
	String deploy(InputStream input);
	
	/**
	 * 卸载指定的流程定义
	 * @param processId 流程定义id
	 */
	void undeploy(String processId);
}
