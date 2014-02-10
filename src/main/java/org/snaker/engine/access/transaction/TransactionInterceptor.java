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
package org.snaker.engine.access.transaction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.StringHelper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 事务拦截器，用于产生业务逻辑类的代理类
 * @author yuqs
 * @version 1.0
 */
public abstract class TransactionInterceptor implements MethodInterceptor {
	private static final Logger log = LoggerFactory.getLogger(TransactionInterceptor.class);
	/**
	 * 需要拦截的事务方法集合
	 */
	private static final List<String> txMethods = new ArrayList<String>();
	static {
		txMethods.add("start*");
		txMethods.add("execute*");
		txMethods.add("finish*");
		txMethods.add("terminate*");
		txMethods.add("take*");
		txMethods.add("create*");
		txMethods.add("save*");
		txMethods.add("delete*");
		txMethods.add("remove*");
		txMethods.add("update*");
		txMethods.add("deploy*");
		txMethods.add("undeploy*");
		txMethods.add("complete*");
		txMethods.add("assign*");
		txMethods.add("withdrawTask*");
		txMethods.add("add*");
		txMethods.add("get*");
	}
	
	/**
	 * 使用Cglib产生业务类的代理
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clazz) {
		return (T)Enhancer.create(clazz, this);
	}
	
	/**
	 * 方法执行的拦截器，拦截条件为：
	 * 1、数据库访问类是DBTransaction的实现类
	 * 2、方法名称匹配初始化的事务方法列表
	 */
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result = null;
		TransactionStatus status = null;
		if(isMatch(method.getName())) {
			if(log.isDebugEnabled()) {
				log.debug("intercept method is[name="  + method.getName() + "]");
			}
			try {
				status = getTransaction();
				AssertHelper.notNull(status);
				//调用具体无事务支持的业务逻辑
				result = proxy.invokeSuper(obj, args);
				//如果整个执行过程无异常抛出，则提交TransactionStatus持有的transaction对象
				if(status.isNewTransaction()) {
					commit(status);
				}
			} catch (Exception e) {
				rollback(status);
				throw new SnakerException(e.getMessage(), e.getCause());
			}
		} else {
			if(log.isDebugEnabled()) {
				log.debug("****don't intercept method is[name="  + method.getName() + "]");
			}
			result = proxy.invokeSuper(obj, args);
		}
		return result;
	}

	/**
	 * 根据方法名称，匹配所有初始化的需要事务拦截的方法
	 * @param methodName
	 * @return
	 */
	private boolean isMatch(String methodName) {
		for(String pattern : txMethods) {
			if(StringHelper.simpleMatch(pattern, methodName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 初始化事务拦截器，设置访问对象
	 * @param accessObject
	 */
	public abstract void initialize(Object accessObject);
	
	/**
	 * 返回当前事务状态
	 * @return
	 */
	protected abstract TransactionStatus getTransaction();
	
	/**
	 * 提交当前事务状态
	 * @param status
	 */
	protected abstract void commit(TransactionStatus status);
	
	/**
	 * 回滚当前事务状态
	 * @param status
	 */
	protected abstract void rollback(TransactionStatus status);
}
