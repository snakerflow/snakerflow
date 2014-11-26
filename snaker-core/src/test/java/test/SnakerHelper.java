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
package test;

import javax.sql.DataSource;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.jdbc.JdbcHelper;
import org.snaker.engine.cfg.Configuration;

/**
 * Snaker引擎帮助类
 * @author yuqs
 * @since 1.0
 */
public class SnakerHelper {
	private static final SnakerEngine engine;
	
	static {
		DataSource dataSource = JdbcHelper.getDataSource();
		engine = new Configuration()
			.initAccessDBObject(dataSource)
			.buildSnakerEngine();
	}
	
	public static SnakerEngine getEngine() {
		return engine;
	}
}
