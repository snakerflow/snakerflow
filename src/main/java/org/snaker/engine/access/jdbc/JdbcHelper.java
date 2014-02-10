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

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.snaker.engine.access.transaction.TransactionObjectHolder;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.ConfigHelper;

/**
 * Jdbc操作帮助类
 * @author yuqs
 * @version 1.0
 */
public abstract class JdbcHelper {
	private static DataSource dataSource = null;
	/**
	 * 在没有任何dataSource注入的情况下，默认使用dbcp数据源
	 */
	private static void initialize() {
		String driver = ConfigHelper.getProperty("jdbc.driver");
		String url = ConfigHelper.getProperty("jdbc.url");
		String username = ConfigHelper.getProperty("jdbc.username");
		String password = ConfigHelper.getProperty("jdbc.password");
		int maxActive = ConfigHelper.getNumerProperty("jdbc.max.active");
		int maxIdle = ConfigHelper.getNumerProperty("jdbc.max.idle");
		AssertHelper.notNull(driver);
		AssertHelper.notNull(url);
		AssertHelper.notNull(username);
		AssertHelper.notNull(password);
		//初始化DBCP数据源
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		dataSource = ds;
	}
	
	/**
	 * 返回数据源dataSource
	 * @return
	 */
	public static DataSource getDataSource() {
		if(dataSource == null) {
			synchronized (JdbcHelper.class) {
				if(dataSource == null) {
					initialize();
				}
			}
		}
		AssertHelper.notNull(dataSource);
		return dataSource;
	}
	
	/**
     * 返回数据库连接对象
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(DataSource ds) throws SQLException {
    	//通过ThreadLocale中获取Connection，如果为空，则通过dataSource返回新的连接对象
    	Connection conn = (Connection)TransactionObjectHolder.get();
    	if(conn != null) return conn;
		if(ds != null) return ds.getConnection();
		return getDataSource().getConnection();
    }
	
	/**
	 * 根据返回的对象集合判断是否为单条记录，并返回
	 * 如果返回无记录，或者超过1条记录，则抛出异常
	 * @param results
	 * @return
	 */
	public static <T> T requiredSingleResult(Collection<T> results) {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			return null;
		}
		return results.iterator().next();
	}
	
	/**
	 * 根据元数据ResultSetMetaData、列索引columIndex获取列名称
	 * @param resultSetMetaData
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 */
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	
	/**
	 * 根据ResultSet结果集、index列索引、字段类型requiredType获取指定类型的对象值
	 * @param rs
	 * @param index
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
		if (requiredType == null) {
			return getResultSetValue(rs, index);
		}

		Object value = null;
		boolean wasNullCheck = false;

		if (String.class.equals(requiredType)) {
			value = rs.getString(index);
		}
		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
			value = rs.getBoolean(index);
			wasNullCheck = true;
		}
		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
			value = rs.getByte(index);
			wasNullCheck = true;
		}
		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
			value = rs.getShort(index);
			wasNullCheck = true;
		}
		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = rs.getInt(index);
			wasNullCheck = true;
		}
		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
			value = rs.getLong(index);
			wasNullCheck = true;
		}
		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
			value = rs.getFloat(index);
			wasNullCheck = true;
		}
		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
				Number.class.equals(requiredType)) {
			value = rs.getDouble(index);
			wasNullCheck = true;
		}
		else if (byte[].class.equals(requiredType)) {
			value = rs.getBytes(index);
		}
		else if (java.sql.Date.class.equals(requiredType)) {
			value = rs.getDate(index);
		}
		else if (java.sql.Time.class.equals(requiredType)) {
			value = rs.getTime(index);
		}
		else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
			value = rs.getTimestamp(index);
		}
		else if (BigDecimal.class.equals(requiredType)) {
			value = rs.getBigDecimal(index);
		}
		else if (Blob.class.equals(requiredType)) {
			value = rs.getBlob(index);
		}
		else if (Clob.class.equals(requiredType)) {
			value = rs.getClob(index);
		}
		else {
			value = getResultSetValue(rs, index);
		}

		if (wasNullCheck && value != null && rs.wasNull()) {
			value = null;
		}
		return value;
	}
	
	/**
	 * 对于特殊字段类型做特殊处理
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}
	
    /**
     * conn不为空时，关闭conn
     * @param conn
     * @throws SQLException
     */
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * rs不为空时，关闭rs
     * @param rs
     * @throws SQLException
     */
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    /**
     * stmt不为空时，关闭stmt
     * @param stmt
     * @throws SQLException
     */
    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }
}
