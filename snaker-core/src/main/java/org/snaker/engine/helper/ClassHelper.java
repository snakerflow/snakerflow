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
package org.snaker.engine.helper;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作帮助类
 * @author yuqs
 * @since 1.0
 */
public class ClassHelper {
    private static final Logger log = LoggerFactory.getLogger(ClassHelper.class);
	/**
	 * 查询结果总记录数的类型转换
	 * @param count
	 * @return
	 */
	public static long castLong(Object count) {
		if(count == null) return -1L;
		if(count instanceof Long) {
			return (Long)count;
		} else if(count instanceof BigDecimal) {
			return ((BigDecimal)count).longValue();
		} else if(count instanceof Integer) {
			return ((Integer)count).longValue();
		} else if(count instanceof BigInteger) {
			return ((BigInteger)count).longValue();
		} else if(count instanceof Byte) {
			return ((Byte)count).longValue();
        } else if(count instanceof Short) {
            return ((Short)count).longValue();
		} else {
			return -1L;
		}
	}
    
    /**
     * 根据指定的类名称加载类
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoader.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    throw exc;
                }
            }
        }
    }
    
    /**
     * 实例化指定的类名称（全路径）
     * @param clazzStr
     * @return
     * @throws Exception
     */
    public static Object newInstance(String clazzStr) {
        try {
        	log.debug("loading class:" + clazzStr);
            Class<?> clazz = loadClass(clazzStr);
            return instantiate(clazz);
        } catch (ClassNotFoundException e) {
            log.error("Class not found.", e);
        } catch (Exception ex) {
        	log.error("类型实例化失败[class=" + clazzStr + "]\n" + ex.getMessage());
        }
        return null;
    }
    
    /**
     * 根据类的class实例化对象
     * @param clazz
     * @return
     */
	public static <T> T instantiate(Class<T> clazz) {
		if (clazz.isInterface()) {
			log.error("所传递的class类型参数为接口，无法实例化");
			return null;
		}
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			log.error("检查传递的class类型参数是否为抽象类?", ex.getCause());
		}
		return null;
	}
}
