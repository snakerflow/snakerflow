/* Copyright 2012-2014 the original author or authors.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.AbstractDBAccess;
import org.snaker.engine.DBAccess;
import org.snaker.engine.entity.Process;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * Spring jdbc方式的数据库访问操作
 * @author yuqs
 * @since 1.0
 */
public class SpringJdbcAccess extends AbstractDBAccess implements DBAccess {
	private static final Logger log = LoggerFactory.getLogger(SpringJdbcAccess.class);
	private LobHandler lobHandler;
	private JdbcTemplate template;

	public void saveProcess(final Process process) {
		super.saveProcess(process);
		if(process.getBytes() != null) {
			template.execute(PROCESS_UPDATE_BLOB, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				
				protected void setValues(PreparedStatement ps, LobCreator lobCreator)
						throws SQLException, DataAccessException {
					try {
						lobCreator.setBlobAsBytes(ps, 1, process.getBytes());
						StatementCreatorUtils.setParameterValue(ps, 2, Types.VARCHAR, process.getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public void updateProcess(final Process process) {
		super.updateProcess(process);
		if(process.getBytes() != null) {
			template.execute(PROCESS_UPDATE_BLOB, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				
				protected void setValues(PreparedStatement ps, LobCreator lobCreator)
						throws SQLException, DataAccessException {
					try {
						lobCreator.setBlobAsBytes(ps, 1, process.getBytes());
						StatementCreatorUtils.setParameterValue(ps, 2, Types.VARCHAR, process.getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public boolean isORM() {
		return false;
	}

	public void saveOrUpdate(Map<String, Object> map) {
		String sql = (String)map.get(KEY_SQL);
		Object[] args = (Object[])map.get(KEY_ARGS);
		int[] type = (int[])map.get(KEY_TYPE);
		if(log.isDebugEnabled()) {
			log.debug("增删改数据(Spring托管事务)=\n" + sql);
		}
		template.update(sql, args, type);
	}
	
	public Integer getLatestProcessVersion(String name) {
		String where = " where name = ?";
        Number number = template.queryForObject(QUERY_VERSION + where, new Object[]{name }, Integer.class);
        return (number != null ? number.intValue() : -1);
	}

	@SuppressWarnings("unchecked")
	public <T> T queryObject(Class<T> clazz, String sql, Object... args) {
		if(log.isDebugEnabled()) {
			log.debug("查询单条数据=\n" + sql);
		}
		try {
			return (T)template.queryForObject(sql, args, new BeanPropertyRowMapper(clazz));
		} catch(Exception e) {
			log.error("查询单条数据=\n" + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> queryList(Class<T> clazz, String sql, Object... args) {
		if(log.isDebugEnabled()) {
			log.debug("查询多条数据=\n" + sql);
		}
		return (List<T>)template.query(sql, args, new BeanPropertyRowMapper(clazz));
	}

    public Object queryCount(String sql, Object... args) {
        return template.queryForLong(sql, args);
    }
	
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public DataSource getDataSource() {
		return (this.template != null ? this.template.getDataSource() : null);
	}

	public void setDataSource(DataSource dataSource) {
		if (this.template == null || dataSource != this.template.getDataSource()) {
			this.template = new JdbcTemplate(dataSource);
		}
	}

    protected Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
