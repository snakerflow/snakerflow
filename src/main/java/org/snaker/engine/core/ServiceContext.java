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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerEngine;

/**
 * 服务上下文（单实例），提供可配置的服务注册
 * @author yuqs
 * @version 1.0
 */
public class ServiceContext {
	private static final Logger log = LoggerFactory.getLogger(ServiceContext.class);
	
	/**
	 * 单态实例的静态对象定义
	 */
	private static ServiceContext context = new ServiceContext();
	/**
	 * 上下文注册的配置对象
	 */
	private Map<String, Object> contextMap = new HashMap<String, Object>();

	/**
	 * 单态实例的构造函数
	 */
	private ServiceContext() {
	}
	
	/**
	 * 获取单态实例的ServiceContext对象
	 * @return
	 */
	public static ServiceContext getContext() {
		return context;
	}
	
	/**
	 * 获取注册的引擎实例
	 * @return
	 */
	public SnakerEngine getEngine() {
		return find(SnakerEngine.class);
	}
	
	/**
	 * 对外部提供的put方法
	 * @param name
	 * @param object
	 */
	public void put(String name, Object object) {
		if(log.isInfoEnabled()) {
			log.info("put new instance[name=" + name + "][object=" + object + "]");
		}
		contextMap.put(name, object);
	}
	
	/**
	 * 根据服务名称查找对象
	 * @param name
	 * @return
	 */
	public boolean exist(String name) {
		return contextMap.get(name) != null;
	}

	/**
	 * 对外部提供的查找对象方法，根据class类型查找
	 * @param clazz
	 * @return
	 */
	public <T> T find(Class<T> clazz) {
		for (Entry<String, Object> entry : contextMap.entrySet()) {
			if (clazz.isInstance(entry.getValue())) {
				return clazz.cast(entry.getValue());
			}
		}
		return null;
	}
	
	/**
	 * 对外部提供的查找对象实例列表方法，根据class类型查找
	 * @param clazz
	 * @return
	 */
	public <T> List<T> findList(Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for (Entry<String, Object> entry : contextMap.entrySet()) {
			if (clazz.isInstance(entry.getValue())) {
				list.add(clazz.cast(entry.getValue()));
			}
		}
		return list;
	}
	
	/**
	 * 对外部提供的查找对象方法，根据名称、class类型查找
	 * @param name
	 * @param clazz
	 * @return
	 */
	public <T> T findByName(String name, Class<T> clazz) {
		for (Entry<String, Object> entry : contextMap.entrySet()) {
			if (entry.getKey().equals(name) && clazz.isInstance(entry.getValue())) {
				return clazz.cast(entry.getValue());
			}
		}
		return null;
	}
}
