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
package org.snaker.engine.model;

import java.lang.reflect.Method;
import java.util.Map;

import org.snaker.engine.SnakerException;
import org.snaker.engine.core.Execution;
import org.snaker.engine.handlers.IHandler;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.ReflectHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * 自定义模型
 * @author yuqs
 * @version 1.0
 */
public class CustomModel extends WorkModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8796192915721758769L;
	/**
	 * 需要执行的class类路径
	 */
	private String clazz;
	/**
	 * 需要执行的class对象的方法名称
	 */
	private String methodName;
	/**
	 * 执行方法时传递的参数表达式变量名称
	 */
	private String args;
	/**
	 * 执行的返回值变量
	 */
	private String var;
	/**
	 * 加载模型时初始化的对象实例
	 */
	private Object invokeObject;
	
	@Override
	public void execute(Execution execution) {
		if(invokeObject == null) {
			invokeObject = ClassHelper.newInstance(clazz);
		}
		if(invokeObject == null) {
			throw new SnakerException("自定义模型[class=" + clazz + "]实例化对象失败");
		}
		
		if(invokeObject instanceof IHandler) {
			IHandler handler = (IHandler)invokeObject;
			handler.handle(execution);
		} else {
			Method method = ReflectHelper.findMethod(invokeObject.getClass(), methodName);
			if(method == null) {
				throw new SnakerException("自定义模型[class=" + clazz + "]无法找到方法名称:" + methodName);
			}
			Object[] objects = getArgs(execution.getArgs(), args);
			Object returnValue = ReflectHelper.invoke(method, invokeObject, objects);
			if(StringHelper.isNotEmpty(var)) {
				execution.getArgs().put(var, returnValue);
			}
		}
		super.execute(execution);
	}
	
	/**
	 * 根据传递的执行参数、模型的参数列表返回实际的参数对象数组
	 * @param execArgs
	 * @param args
	 * @return
	 */
	private Object[] getArgs(Map<String, Object> execArgs, String args) {
		Object[] objects = null;
		if(StringHelper.isNotEmpty(args)) {
			String[] argArray = args.split(",");
			objects = new Object[argArray.length];
			for(int i = 0; i < argArray.length; i++) {
				objects[i] = execArgs.get(argArray[i]);
			}
		}
		return objects;
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
}
