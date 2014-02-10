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
package org.snaker.engine.access.spring;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.AbstractDBAccess;
import org.snaker.engine.access.Page;
import org.snaker.engine.DBAccess;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;
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
 * @version 1.0
 */
public class SpringJdbcAccess extends AbstractDBAccess implements DBAccess {
	private static final Logger log = LoggerFactory.getLogger(SpringJdbcAccess.class);
	private LobHandler lobHandler;
	private JdbcTemplate template;

	@Override
	public void saveProcess(final Process process) {
		super.saveProcess(process);
		if(process.getBytes() != null) {
			template.execute(PROCESS_UPDATE_BLOB, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				@Override
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

	@Override
	public void updateProcess(final Process process) {
		super.updateProcess(process);
		if(process.getBytes() != null) {
			template.execute(PROCESS_UPDATE_BLOB, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
				@Override
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

	@Override
	public boolean isORM() {
		return false;
	}

	@Override
	public void saveOrUpdate(Map<String, Object> map) {
		String sql = (String)map.get(KEY_SQL);
		Object[] args = (Object[])map.get(KEY_ARGS);
		int[] type = (int[])map.get(KEY_TYPE);
		if(log.isDebugEnabled()) {
			log.debug("增删改数据(Spring托管事务)=\n" + sql);
		}
		template.update(sql, args, type);
	}

	@Override
	public <T> T queryObject(Class<T> T, String sql, Object... args) {
		if(log.isDebugEnabled()) {
			log.debug("查询单条数据=\n" + sql);
		}
		try {
			return template.queryForObject(sql, args, new BeanPropertyRowMapper<T>(T));
		} catch(Exception e) {
			log.error("查询单条数据=\n" + e.getMessage());
			return null;
		}
	}

	@Override
	public <T> List<T> queryList(Class<T> T, String sql, Object... args) {
		if(log.isDebugEnabled()) {
			log.debug("查询多条数据=\n" + sql);
		}
		return template.query(sql, args, new BeanPropertyRowMapper<T>(T));
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
		if(log.isDebugEnabled()) {
			log.debug("查询分页countSQL=\n" + countSQL);
			log.debug("查询分页querySQL=\n" + querySQL);
		}

		List<T> tasks = null;
		long count = 0L;
		try {
			count = template.queryForLong(countSQL, args);
			tasks = template.query(querySQL, args, new BeanPropertyRowMapper<T>(T));
			if(tasks == null) tasks = Collections.emptyList();
			page.setResult(tasks);
			page.setTotalCount(ClassHelper.castLong(count));
			return page.getResult();
		} catch(RuntimeException e) {
			log.error("查询失败" + e.getMessage());
			return Collections.emptyList();
		}
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
}
