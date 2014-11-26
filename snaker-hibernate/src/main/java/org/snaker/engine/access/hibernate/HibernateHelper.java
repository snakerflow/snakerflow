/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package org.snaker.engine.access.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.transaction.TransactionObjectHolder;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * Hibernate3操作帮助类
 * @author yuqs
 * @since 1.0
 */
public abstract class HibernateHelper {
	private static final Logger log = LoggerFactory.getLogger(HibernateHelper.class);
	/**
	 * hibernate的session工厂
	 */
	private static SessionFactory sessionFactory;
	/**
	 * 在没有任何SessionFactory注入的情况下，默认加载hibernate.cfg.xml初始化sessionFactory
	 * 这里只设置了常用的属性，建议调用Configuration.initAccessDBObject方法注入sessionfactory
	 * 或者使用ioc容器注入
	 */
	private static void initialize() {
		String driver = ConfigHelper.getProperty("jdbc.driver");
		String url = ConfigHelper.getProperty("jdbc.url");
		String username = ConfigHelper.getProperty("jdbc.username");
		String password = ConfigHelper.getProperty("jdbc.password");
		String dialect = ConfigHelper.getProperty("hibernate.dialect");
		AssertHelper.notNull(driver);
		AssertHelper.notNull(url);
		AssertHelper.notNull(username);
		AssertHelper.notNull(password);
		AssertHelper.notNull(dialect);
		String formatSql = ConfigHelper.getProperty("hibernate.format_sql");
		String showSql = ConfigHelper.getProperty("hibernate.show_sql");
		Configuration configuration = new Configuration();
		
		if(StringHelper.isNotEmpty(driver)) {
			configuration.setProperty("hibernate.connection.driver_class", driver);
		}
		if(StringHelper.isNotEmpty(url)) {
			configuration.setProperty("hibernate.connection.url", url);
		}
		if(StringHelper.isNotEmpty(username)) {
			configuration.setProperty("hibernate.connection.username", username);
		}
		if(StringHelper.isNotEmpty(password)) {
			configuration.setProperty("hibernate.connection.password", password);
		}
		if(StringHelper.isNotEmpty(dialect)) {
			configuration.setProperty("hibernate.dialect", dialect);
		}
		if(StringHelper.isNotEmpty(formatSql)) {
			configuration.setProperty("hibernate.format_sql", formatSql);
		}
		if(StringHelper.isNotEmpty(showSql)) {
			configuration.setProperty("hibernate.show_sql", showSql);
		}
		sessionFactory = configuration.configure().buildSessionFactory();
	}
	
	public static Session getSession(SessionFactory sf) {
		Session session = (Session)TransactionObjectHolder.get();
		if(session == null) {
			/*
			 * 此处的执行路径一般是ioc容器提前注入了sessionFactory，所以直接从当前线程中获取session
			 * 否则openSession需要手动提交事务
			 */
			if(sf != null) return sf.getCurrentSession();
			if(log.isDebugEnabled()) {
				log.debug("could not found sessionFactory.");
			}
			return getSessionFactory().openSession();
		} else {
			if(log.isDebugEnabled()) {
				log.debug("found thread-bound session=" + session.hashCode());
			}
			return session;
		}
	}
	
	/**
	 * 使用默认的hibernate配置文件构造sessionFactory对象
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			synchronized (HibernateHelper.class) {
				initialize();
			}
		}
		return sessionFactory;
	}
}
