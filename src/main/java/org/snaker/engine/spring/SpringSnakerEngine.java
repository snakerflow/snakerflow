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
package org.snaker.engine.spring;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.DBAccess;
import org.snaker.engine.core.SnakerEngineImpl;
import org.snaker.engine.helper.ConfigHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring环境使用的SnakerEngine实现类，主要接收spring的applicationContext对象
 * @author yuqs
 * @version 1.0
 */
public class SpringSnakerEngine extends SnakerEngineImpl {
	private ApplicationContext applicationContext;
	/**
	 * 根据applicatinContext上下文，查找processService、orderService、taskService服务
	 */
	@Override
	public SnakerEngine configure(Configuration config) {
		/**
		 * 通过Configuration获取applicationContext对象（通过SpringHelper添加到Configuration中）
		 */
		applicationContext = (ApplicationContext) config.getApplicationContext();
		if(applicationContext == null) {
			//如果不存在applicationContext，通过配置的spring属性初始化applicationContext
			String configFile = ConfigHelper.getProperty("spring");
			if(configFile == null || configFile.equals("")) {
				configFile = "applicationContext.xml";
			}
			applicationContext = new ClassPathXmlApplicationContext(configFile);
		}
		/**
		 * 调用父类configure方法
		 */
		super.configure(config);
		/**
		 * 由于SnakerEngine非spring的bean，而DBAccess实现类在spring中配置
		 * 所以需要手动设置DBAccess
		 */
		DBAccess access = applicationContext.getBean(DBAccess.class);
		super.setDBAccess(access);
		return this;
	}
}
