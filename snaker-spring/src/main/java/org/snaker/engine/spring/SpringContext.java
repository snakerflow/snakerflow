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
package org.snaker.engine.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.Context;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;

/**
 * spring的服务查找实现
 * @author yuqs
 * @since 1.5
 */
public class SpringContext implements Context {
	private static final Logger log = LoggerFactory.getLogger(SpringContext.class);
	/**
	 * Spring context
	 */
	private ApplicationContext applicationContext;
	
	private DefaultListableBeanFactory beanFactory;
	
	/**
	 * 根据spring的上下文构造SpringContext
	 * @param ctx 上下文
	 */
	public SpringContext(ApplicationContext ctx) {
		this.applicationContext = ctx;
		beanFactory = (DefaultListableBeanFactory)ctx.getAutowireCapableBeanFactory();
	}

	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> clazz) {
		String[] names = applicationContext.getBeanNamesForType(clazz);
		if (names.length > 1 && log.isWarnEnabled()) {
			log.warn("重复定义类型:" + clazz);
		}
		
		if (names.length >= 1) {
		  return (T) applicationContext.getBean(names[0]);
		} 
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findList(Class<T> clazz) {
		String[] names = applicationContext.getBeanNamesForType(clazz);
		List<T> beans = new ArrayList<T>();
		for(String name : names) {
			beans.add((T)applicationContext.getBean(name));
		}
		return beans;
	}

	public <T> T findByName(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

	/**
	 * spring不支持向applicationContext中直接添加对象
	 */
	public void put(String name, Object object) {
		log.warn("spring不支持向applicationContext中直接添加对象");
	}
	
	/**
	 * 向spring添加bean的定义
	 */
	public void put(String name, Class<?> clazz) {
		BeanDefinition definition = new RootBeanDefinition(clazz);
		beanFactory.registerBeanDefinition(name, definition);
	}

	/**
	 * 判断是否存在服务
	 */
	public boolean exist(String name) {
		return applicationContext.containsBean(name);
	}
}
