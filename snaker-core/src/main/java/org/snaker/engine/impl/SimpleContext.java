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
package org.snaker.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.snaker.engine.Context;
import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.helper.ClassHelper;

/**
 * 简单的服务查找实现类，由 {@link Configuration}设置
 * @author yuqs
 * @since 1.5
 */
public class SimpleContext implements Context {
	/**
	 * 上下文注册的配置对象
	 */
	private Map<String, Object> contextMap = new HashMap<String, Object>();
	
	/**
	 * 根据名称查找实例
	 * @param name 服务名称
	 * @return 是否存在标识
	 */
	public boolean exist(String name) {
		return contextMap.get(name) != null;
	}
	/**
	 * 对外部提供的查找对象方法，根据class类型查找
	 * @param clazz 类型
	 * @return 实例
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
	 * @param clazz 类型
	 * @return 实例列表
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
	 * @param name 名称
	 * @param clazz 类型
	 * @return 实例
	 */
	public <T> T findByName(String name, Class<T> clazz) {
		for (Entry<String, Object> entry : contextMap.entrySet()) {
			if (entry.getKey().equals(name) && clazz.isInstance(entry.getValue())) {
				return clazz.cast(entry.getValue());
			}
		}
		return null;
	}
	
	/**
	 * 对外部提供的put方法
	 * @param name 名称
	 * @param object 实例
	 */
	public void put(String name, Object object) {
		contextMap.put(name, object);
	}
	
	/**
	 * 对外部提供的put方法
	 * @param name 名称
	 * @param clazz 类型
	 */
	public Object put(String name, Class<?> clazz) {
		Object instance = ClassHelper.instantiate(clazz);
		contextMap.put(name, instance);
		return instance;
	}
}
