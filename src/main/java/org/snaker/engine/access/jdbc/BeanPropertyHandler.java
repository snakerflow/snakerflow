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
package org.snaker.engine.access.jdbc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.handlers.AbstractListHandler;
import org.snaker.engine.SnakerException;
import org.snaker.engine.helper.ClassHelper;

/**
 * 该类主要解决数据库字段与类属性之间的转换存在下划线的情况(如：taskId->task_id)
 * 该类主要参考Spring JDBC的rowmapper方式
 * dbutils使用BeanHandler、BeanListHandler来处理返回集与bean的转换
 * 这里统一使用BeanPropertyHandler，当返回单条记录时，使用JdbcHelper的requiredSingleResult做处理
 * @author yuqs
 * @version 1.0
 * @param <T>
 */
public class BeanPropertyHandler<T> extends AbstractListHandler<T> {
	/**
	 * 需要映射的bean对象的class类型
	 */
	private Class<T> mappedClass;
	/**
	 * 映射的字段
	 */
	private Map<String, PropertyDescriptor> mappedFields;

	/**
	 * 构造函数，根据bean对象的class类型初始化mappedFields
	 * @param mappedClass
	 */
	public BeanPropertyHandler(Class<T> mappedClass) {
		initialize(mappedClass);
	}

	/**
	 * ResultSet结果集处理
	 */
	@Override
	protected T handleRow(ResultSet rs) throws SQLException {
		/**
		 * 根据bean的class类型实例化为对象
		 */
		T mappedObject = ClassHelper.instantiate(mappedClass);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		/**
		 * 对ResultSet结果集字段进行循环
		 */
		for (int index = 1; index <= columnCount; index++) {
			/**
			 * 根据字段索引index获取字段名称
			 */
			String column = JdbcHelper.lookupColumnName(rsmd, index);
			/**
			 * 根据映射字段集合返回字段名称对应的属性描述符对象
			 */
			PropertyDescriptor pd = this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
			if (pd != null) {
				try {
					/**
					 * 根据字段index、属性类型返回字段值
					 */
					Object value = JdbcHelper.getResultSetValue(rs, index, pd.getPropertyType());
					try {
						/**
						 * 使用apache-beanutils设置对象的属性
						 */
						BeanUtils.setProperty(mappedObject, pd.getName(), value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return mappedObject;
	}

	/**
	 * 根据bean对象的class初始化字段映射集合
	 * @param mappedClass
	 */
	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		PropertyDescriptor[] pds = null;
		try {
			/**
			 * 返回bean的属性描述对象数组
			 */
			pds = propertyDescriptors(mappedClass);
		} catch (SQLException e) {
			throw new SnakerException(e.getMessage(), e.getCause());
		}
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
			}
		}
	}

	/**
	 * 属性名称转换为下划线，如taskId->task_id
	 * @param name
	 * @return
	 */
	private static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 由Introspector返回指定类型的BeanInfo对象，再返回需要的属性描述对象数组PropertyDescriptor[]
	 * @param c
	 * @return PropertyDescriptor[]
	 * @throws SQLException
	 */
	private PropertyDescriptor[] propertyDescriptors(Class<?> c)
			throws SQLException {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(c);
		} catch (IntrospectionException e) {
			throw new SQLException("Bean introspection failed: "
					+ e.getMessage());
		}

		return beanInfo.getPropertyDescriptors();
	}
}
