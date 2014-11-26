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
package org.snaker.engine.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.Context;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.helper.AssertHelper;

/**
 * 单实例的服务上下文
 * 具体的上下文查找服务交给Context的实现类
 * @author yuqs
 * @since 1.5
 */
public abstract class ServiceContext {
	private static final Logger log = LoggerFactory.getLogger(ServiceContext.class);
	
	/**
	 * 上下文接口服务{@link Context}
	 */
	private static Context context;
	
	/**
	 * 流程引擎的引用
	 */
	private static SnakerEngine engine;
	
	/**
	 * 获取Context实现类
	 * @return
	 */
	public static Context getContext() {
		return context;
	}
	
	/**
	 * 设置Context实现类
	 * @param context
	 */
	public static void setContext(Context context) {
		ServiceContext.context = context;
	}
	
	/**
	 * 获取注册的引擎实例
	 * @return
	 */
	public static SnakerEngine getEngine() {
		AssertHelper.notNull(context, "未注册服务上下文");
		if(engine == null) {
			engine = context.find(SnakerEngine.class);
		}
		return engine;
	}
	
	/**
	 * 向上下文添加服务实例
	 * @param name 服务名称
	 * @param object 服务实例
	 */
	public static void put(String name, Object object) {
		AssertHelper.notNull(context, "未注册服务上下文");
		if(log.isInfoEnabled()) {
			log.info("put new instance[name=" + name + "][object=" + object + "]");
		}
		context.put(name, object);
	}
	
	/**
	 * 向上下文添加服务实例
	 * @param name 服务名称
	 * @param clazz 服务类型
	 */
	public static void put(String name, Class<?> clazz) {
		AssertHelper.notNull(context, "未注册服务上下文");
		if(log.isInfoEnabled()) {
			log.info("put new instance[name=" + name + "][clazz=" + clazz.getName() + "]");
		}
		context.put(name, clazz);
	}
	
	/**
	 * 根据服务名称判断是否存在服务实例
	 * @param name 服务名称
	 * @return
	 */
	public static boolean exist(String name) {
		AssertHelper.notNull(context, "未注册服务上下文");
		return context.exist(name);
	}

	/**
	 * 对外部提供的查找对象方法，根据class类型查找
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> T find(Class<T> clazz) {
		AssertHelper.notNull(context, "未注册服务上下文");
		return context.find(clazz);
	}
	
	/**
	 * 对外部提供的查找对象实例列表方法，根据class类型查找集合
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> List<T> findList(Class<T> clazz) {
		AssertHelper.notNull(context, "未注册服务上下文");
		return context.findList(clazz);
	}
	
	/**
	 * 对外部提供的查找对象方法，根据名称、class类型查找
	 * @param name 服务名称
	 * @param clazz 服务类型
	 * @return
	 */
	public static <T> T findByName(String name, Class<T> clazz) {
		AssertHelper.notNull(context, "未注册服务上下文");
		return context.findByName(name, clazz);
	}
}
