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
package org.snaker.engine.cfg;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.transaction.TransactionInterceptor;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.XmlHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 只允许应用程序存在一个Configuration实例
 * 初始化服务上下文，查找流程引擎实现类并初始化依赖的服务
 * @author yuqs
 * @version 1.0
 */
public class Configuration {
	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	private final static String DEFAULT_CONFIG_FILE = "default.config.xml";
	private final static String SPRING_CONFIG_FILE = "spring.config.xml";
	private final static String USER_CONFIG_FILE = "snaker.xml";
	/**
	 * 服务上下文
	 */
	private ServiceContext context;
	/**
	 * spring的application对象
	 * 注意：这里使用Object定义，主要是考虑非spring环境的依赖库问题
	 */
	private Object applicationContext;
	/**
	 * 访问数据库的对象，根据使用的orm框架进行设置。如果未提供此项设置，则按照默认orm加载方式初始化
	 * jdbc:DataSource
	 * hibernate:SessionFactory
	 * mybatis:SqlSessionFactory
	 */
	private Object accessDBObject;
	/**
	 * 事务拦截器抽象类
	 */
	private TransactionInterceptor interceptor = null;
	/**
	 * 需要事务管理的class类型
	 */
	private Map<String, Class<?>> txClass = new HashMap<String, Class<?>>();

	/**
	 * 构造SnakerEngine对象，适合spring环境使用
	 * 通过SpringHelper调用
	 * @return
	 * @throws SnakerException
	 */
	public SnakerEngine buildSnakerEngine() throws SnakerException {
		if(log.isInfoEnabled()) {
			log.info("SnakerEngine start......");
		}
		/**
		 * 初始化服务上下文
		 */
		context = ServiceContext.getContext();
		parser();
		context.put(Configuration.class.getName(), this);
		/**
		 * 由服务上下文返回流程引擎
		 */
		SnakerEngine configEngine = context.getEngine();
		if(configEngine == null) {
			throw new SnakerException("配置无法发现SnakerEngine的实现类");
		}
		if(log.isInfoEnabled()) {
			log.info("SnakerEngine be found:" + configEngine.getClass());
		}
		return configEngine.configure(this);
	}
	
	/**
	 * 依次解析框架固定的配置及用户自定义的配置
	 * 固定配置文件:default.config.xml或spring.config.xml
	 * 用户自定义配置文件:snaker.xml
	 */
	private void parser() {
		if(log.isDebugEnabled()) {
			log.debug("ServiceContext loading......");
		}

		if(getApplicationContext() == null) {
			parser(DEFAULT_CONFIG_FILE);
		} else {
			parser(SPRING_CONFIG_FILE);
		}
		
		//默认使用snaker.xml配置自定义的bean
		String config = ConfigHelper.getProperty("config");
		if (config == null || config.equals("")) {
			config = USER_CONFIG_FILE;
		}
		parser(config);
		
		if(interceptor != null) {
			for(Entry<String, Class<?>> entry : txClass.entrySet()) {
				context.put(entry.getKey(), interceptor.getProxy(entry.getValue()));
			}
		}
		if(log.isDebugEnabled()) {
			log.debug("ServiceContext load finish......");
		}
	}
	
	/**
	 * 解析给定resource配置，并注册到ServiceContext上下文中
	 * @param resource
	 */
	private void parser(String resource) {
		//解析所有配置节点，并实例化class指定的类
		DocumentBuilder documentBuilder = XmlHelper.createDocumentBuilder();
		try {
			if (documentBuilder != null) {
				InputStream input = StreamHelper.openStream(resource);
				if(input == null) return;
				Document doc = documentBuilder.parse(input);
				Element configElement = doc.getDocumentElement();
				NodeList nodeList = configElement.getChildNodes();
				int nodeSize = nodeList.getLength();
				for(int i = 0; i < nodeSize; i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element)node;
						String name = element.getAttribute("name");
						String className = element.getAttribute("class");
						String proxy = element.getAttribute("proxy");
						if(name == null || name.equals("")) {
							name = className;
						}
						Class<?> clazz = ClassHelper.loadClass(className);
						if(TransactionInterceptor.class.isAssignableFrom(clazz)) {
							interceptor = (TransactionInterceptor)ClassHelper.instantiate(clazz);
							context.put(name, interceptor);
							continue;
						}
						if(proxy != null && proxy.equalsIgnoreCase("transaction")) {
							txClass.put(name, clazz);
						} else {
							context.put(name, ClassHelper.instantiate(clazz));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnakerException("资源解析失败，请检查配置文件[" + resource + "]");
		}
	}
	
	/**
	 * spring环境下，将applicationContext放到配置对象中
	 * @param applicationContext
	 * @return
	 */
	public Configuration initSpringContext(Object applicationContext) {
		this.applicationContext = applicationContext;
		return this;
	}
	
	/**
	 * 初始化DBAccess的数据库访问对象
	 * @param dbObject
	 * @return
	 */
	public Configuration initAccessDBObject(Object dbObject) {
		this.accessDBObject = dbObject;
		return this;
	}
	
	/**
	 * 返回服务上下文
	 * @return
	 */
	public ServiceContext getContext() {
		return context;
	}

	/**
	 * 返回spring的applicationContext
	 * @return
	 */
	public Object getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * 返回DBAccess的数据库访问对象
	 * @return
	 */
	public Object getAccessDBObject() {
		return accessDBObject;
	}
}
