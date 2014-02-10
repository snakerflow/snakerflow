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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.AbstractDBAccess;
import org.snaker.engine.access.Page;
import org.snaker.engine.DBAccess;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * JDBC方式的数据库访问
 * 在无事务控制的情况下，使用cglib的拦截器+ThreadLocale控制
 * @see org.snaker.engine.access.transaction.DataSourceTransactionInterceptor
 * @author yuqs
 * @version 1.0
 */
public class JdbcAccess extends AbstractDBAccess implements DBAccess {
	private static final Logger log = LoggerFactory.getLogger(JdbcAccess.class);
    /**
     * dbutils的QueryRunner对象
     */
    private QueryRunner runner = new QueryRunner();
    
	/**
	 * jdbc的数据源
	 */
    private DataSource dataSource;
	
	/**
	 * setter
	 * @param dataSource
	 */
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
    
	@Override
	public void initialize(Object accessObject) {
		if(accessObject == null) return;
		if(accessObject instanceof DataSource) {
			this.dataSource = (DataSource)accessObject;
		}
	}

	/**
     * 返回数据库连接对象
     * @return
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
    	return JdbcHelper.getConnection(dataSource);
    }

	/**
	 * 使用原生JDBC操作BLOB字段
	 */
	@Override
	public void saveProcess(Process process) {
		super.saveProcess(process);
		if(process.getBytes() != null) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			try {
				conn = getConnection();
				pstmt = conn.prepareStatement(PROCESS_UPDATE_BLOB);
				pstmt.setBytes(1, process.getBytes());
				pstmt.setString(2, process.getId());
				pstmt.execute();
			} catch (Exception e) {
				throw new SnakerException(e.getMessage(), e.getCause());
			} finally {
				try {
					JdbcHelper.close(pstmt);
				} catch (SQLException e) {
					throw new SnakerException(e.getMessage(), e.getCause());
				}
			}
		}
	}

	/**
	 * 使用原生JDBC操作BLOB字段
	 */
	@Override
	public void updateProcess(Process process) {
		super.updateProcess(process);
		if(process.getBytes() != null) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			try {
				conn = getConnection();
				pstmt = conn.prepareStatement(PROCESS_UPDATE_BLOB);
				pstmt.setBytes(1, process.getBytes());
				pstmt.setString(2, process.getId());
				pstmt.execute();
			} catch (Exception e) {
				throw new SnakerException(e.getMessage(), e.getCause());
			} finally {
				try {
					JdbcHelper.close(pstmt);
				} catch (SQLException e) {
					throw new SnakerException(e.getMessage(), e.getCause());
				}
			}
		}
	}

    /**
     * 查询指定列
     * @param column 结果集的列索引号
     * @param sql sql语句
     * @param params 查询参数
     * @return 指定列的结果对象
     */
    public Object query(int column, String sql, Object... params) {
    	Object result;
        try {
        	if(log.isDebugEnabled()) {
        		log.debug("查询单列数据=\n" + sql);
        	}
            result = runner.query(getConnection(), sql, new ScalarHandler<Object>(column), params);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    
	@Override
	public boolean isORM() {
		return false;
	}

	@Override
	public void saveOrUpdate(Map<String, Object> map) {
		String sql = (String)map.get(KEY_SQL);
		Object[] args = (Object[])map.get(KEY_ARGS);
        try {
        	if(log.isDebugEnabled()) {
        		log.debug("增删改数据(需手动提交事务)=\n" + sql);
        	}
            runner.update(getConnection(), sql, args);
        } catch (SQLException e) {
        	log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
	}

	@Override
	public <T> T queryObject(Class<T> T, String sql, Object... args) {
    	List<T> result = null;
        try {
        	if(log.isDebugEnabled()) {
        		log.debug("查询单条记录=\n" + sql);
        	}
        	result = runner.query(getConnection(), sql, new BeanPropertyHandler<T>(T), args);
        	return JdbcHelper.requiredSingleResult(result);
        } catch (SQLException e) {
        	log.error(e.getMessage(), e);
            return null;
        }
	}

	@Override
	public <T> List<T> queryList(Class<T> T, String sql, Object... args) {
        try {
        	if(log.isDebugEnabled()) {
        		log.debug("查询单条记录=\n" + sql);
        	}
        	return runner.query(getConnection(), sql, new BeanPropertyHandler<T>(T), args);
        } catch (SQLException e) {
        	log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
	}
	
	@Override
	public <T> List<T> queryList(Page<T> page, Class<T> T, String sql, Object... args) {
		String countSQL = "select count(1) from (" + sql + ") c ";
		String querySQL = sql;
		if(page.isOrderBySetted()) {
			querySQL = sql + StringHelper.buildPageOrder(page.getOrder(), page.getOrderBy());
		}
		//判断是否需要分页（根据pageSize判断）
		if(page.getPageSize() != Page.NON_PAGE) {
			querySQL = getDialect().getPageSql(querySQL, page.getPageNo(), page.getPageSize());
		}
		try {
			Object count = query(1, countSQL, args);
			List<T> list = runner.query(getConnection(), sql, new BeanPropertyHandler<T>(T), args);
			if(list == null) list = Collections.emptyList();
			page.setResult(list);
			page.setTotalCount(ClassHelper.castLong(count));
			return list;
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return Collections.emptyList();
		}
	}
}
