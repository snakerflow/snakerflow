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
package org.snaker.engine;

import java.util.List;

import org.snaker.engine.cfg.Configuration;

/**
 * 服务查找接口，由{@link Configuration}设置
 * @author yuqs
 * @since 1.5
 */
public interface Context {
	/**
	 * 根据服务名称、实例向服务工厂注册
	 * @param name 服务名称
	 * @param object 服务实例
	 */
	void put(String name, Object object);
	
	/**
	 * 根据服务名称、类型向服务工厂注册
	 * @param name 服务名称
	 * @param clazz 类型
	 */
	void put(String name, Class<?> clazz);
	
	/**
	 * 判断是否存在给定的服务名称
	 * @param name 服务名称
	 * @return
	 */
	boolean exist(String name);
	
	/**
	 * 根据给定的类型查找服务实例
	 * @param clazz 类型
	 * @return
	 */
	<T> T find(Class<T> clazz);
	
	/**
	 * 根据给定的类型查找所有此类型的服务实例
	 * @param clazz 类型
	 * @return
	 */
	<T> List<T> findList(Class<T> clazz);
	
	/**
	 * 根据给定的服务名称、类型查找服务实例
	 * @param name 服务名称
	 * @param clazz 类型
	 * @return
	 */
	<T> T findByName(String name, Class<T> clazz);
}
