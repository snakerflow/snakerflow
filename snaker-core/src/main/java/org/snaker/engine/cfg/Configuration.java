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
package org.snaker.engine.cfg;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.Context;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.transaction.TransactionInterceptor;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.helper.XmlHelper;
import org.snaker.engine.impl.SimpleContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 只允许应用程序存在一个Configuration实例
 * 初始化服务上下文，查找流程引擎实现类并初始化依赖的服务
 * @author yuqs
 * @since 1.0
 */
public class Configuration {
	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	private static final String BASE_CONFIG_FILE = "base.config.xml";
	private final static String EXT_CONFIG_FILE = "ext.config.xml";
	private final static String USER_CONFIG_FILE = "snaker.xml";
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
	 * 无参构造方法，创建简单的Context实现类，并调用{@link Configuration#Configuration(Context)}
	 */
	public Configuration() {
		this(new SimpleContext());
	}
	
	/**
	 * 根据服务查找实现类构造配置对象
	 * @param context 上下文实现
	 */
	public Configuration(Context context) {
		ServiceContext.setContext(context);
	}

	/**
	 * 构造SnakerEngine对象，用于api集成
	 * 通过SpringHelper调用
	 * @return SnakerEngine
	 * @throws SnakerException
	 */
	public SnakerEngine buildSnakerEngine() throws SnakerException {
		if(log.isInfoEnabled()) {
			log.info("SnakerEngine start......");
		}
		parser();
		/**
		 * 由服务上下文返回流程引擎
		 */
		SnakerEngine configEngine = ServiceContext.getEngine();
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
	 * 固定配置文件:base.config.xml
	 * 扩展配置文件:ext.config.xml
	 * 用户自定义配置文件:snaker.xml
	 */
	protected void parser() {
		if(log.isDebugEnabled()) {
			log.debug("Service parsing start......");
		}

		//默认使用snaker.xml配置自定义的bean
		String config = ConfigHelper.getProperty("config");
		if (StringHelper.isEmpty(config)) {
			config = USER_CONFIG_FILE;
		}
		parser(config);
		parser(BASE_CONFIG_FILE);
		if (!isCMB()) {
		    parser(EXT_CONFIG_FILE);
			for(Entry<String, Class<?>> entry : txClass.entrySet()) {
				if(interceptor != null) {
                    Object instance = interceptor.getProxy(entry.getValue());
                    ServiceContext.put(entry.getKey(), instance);
				} else {
                    ServiceContext.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		if(log.isDebugEnabled()) {
			log.debug("Service parsing finish......");
		}
	}
	
	/**
	 * 解析给定resource配置，并注册到ServiceContext上下文中
	 * @param resource 资源
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
						if(StringHelper.isEmpty(name)) {
							name = className;
						}
						if(ServiceContext.exist(name)) {
							log.warn("Duplicate name is:" + name);
							continue;
						}
						Class<?> clazz = ClassHelper.loadClass(className);
						if(TransactionInterceptor.class.isAssignableFrom(clazz)) {
							interceptor = (TransactionInterceptor)ClassHelper.instantiate(clazz);
							ServiceContext.put(name, interceptor);
							continue;
						}
						if(proxy != null && proxy.equalsIgnoreCase("transaction")) {
							txClass.put(name, clazz);
						} else {
							ServiceContext.put(name, clazz);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SnakerException("资源解析失败，请检查配置文件[" + resource + "]", e.getCause());
		}
	}
	
	/**
	 * 初始化DBAccess的数据库访问对象
	 * @param dbObject 数据访问对象
	 * @return Configuration
	 */
	public Configuration initAccessDBObject(Object dbObject) {
		this.accessDBObject = dbObject;
		return this;
	}

    /**
     * 可装载自定义的属性配置文件
     * @param fileName 属性文件名称
     * @return Configuration
     */
    public Configuration initProperties(String fileName) {
        ConfigHelper.loadProperties(fileName);
        return this;
    }

    /**
     * 可装载已有的属性对象
     * @param properties 属性对象
     * @return Configuration
     */
    public Configuration initProperties(Properties properties) {
        ConfigHelper.loadProperties(properties);
        return this;
    }
	
	/**
	 * 返回DBAccess的数据库访问对象
	 * @return 数据访问对象
	 */
	public Object getAccessDBObject() {
		return accessDBObject;
	}

	/**
	 * 返回是否容器托管的bean
	 * @return 是否容器托管
	 */
	public boolean isCMB() {
		return false;
	}
}
