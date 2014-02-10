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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.entity.Process;
import org.snaker.engine.model.*;
import org.snaker.engine.parser.ModelParser;

/**
 * 流程定义模型容器类
 * @author yuqs
 * @version 1.0
 */
public class ModelContainer {
	private static final Logger log = LoggerFactory.getLogger(ModelContainer.class);
	/**
	 * 流程定义模型容器类，key为流程名称；value为流程模型对象
	 */
	private static Map<String, ProcessModel> modelMap = new HashMap<String, ProcessModel>();
	
	/**
	 * 流程定义po容器类，key为流程定义主键ID，value为流程定义po对象
	 */
	private static Map<String, Process> entityMap = new HashMap<String, Process>();
	
	/**
	 * 同步方法，对外部类提供向容器增加model
	 * @param name
	 * @param model
	 */
	private static synchronized void pushModel(String name, ProcessModel model) {
		log.info("push model[name=" + name + "]" + model);
		modelMap.put(name, model);
	}
	
	/**
	 * 同步方法，对外部类提供向容器增加entity
	 * @param id
	 * @param process
	 */
	public static synchronized void pushEntity(String id, Process process) {
		log.info("push entity[id=" + id + "]" + process);
		if(entityMap.containsKey(id)) log.warn("entity[id=" + id + "] be replaced.");
		if(process.getModel() == null && process.getDBContent() != null) {
			process.setModel(ModelParser.parse(process.getDBContent()));
		}
		pushModel(process.getName(), process.getModel());
		entityMap.put(id, process);
	}
	
	/**
	 * 根据流程定义ID，删除对应的缓存数据
	 * @param id
	 */
	public static void popEntity(String id) {
		log.info("pop entity[id=" + id + "]");
		Process process = entityMap.get(id);
		if(process != null) {
			modelMap.remove(process.getName());
			entityMap.remove(id);
		}
	}
	
	/**
	 * 处理容器中所有子流程级联引用
	 */
	public static void cascadeReference() {
		for(Entry<String, ProcessModel> entry : modelMap.entrySet()) {
			ProcessModel model = entry.getValue();
			cascadeReference(model);
		}
	}
	
	/**
	 * 处理单个子流程级联引用
	 * @param model
	 */
	public static void cascadeReference(ProcessModel model) {
		if(model != null && model.isExistSub()) {
			for(NodeModel node : model.getNodes()) {
				if(node instanceof SubProcessModel) {
					SubProcessModel spm = (SubProcessModel)node;
					String subName = spm.getProcessName();
					ProcessModel subModel = modelMap.get(subName);
					if(subModel == null) throw new SnakerException("流程定义[name=" + model.getName() + "]包含的子流程[name=" + subName + "]不存在.");
					spm.setSubProcess(subModel);
				}
			}
		}
	}
	
	/**
	 * 根据流程定义ID，从容器中获取对应的process对象
	 * @param id
	 * @return
	 */
	public static Process getEntity(String key) {
		Process entity = entityMap.get(key);
		if(entity != null) return entity;
		for(Entry<String, Process> entry : entityMap.entrySet()) {
			if(entry.getValue().getName().equalsIgnoreCase(key)) {
				return entry.getValue();
			}
		}
		throw new SnakerException("流程定义[key=" + key + "]对应的实体对象为空.");
	}
}
