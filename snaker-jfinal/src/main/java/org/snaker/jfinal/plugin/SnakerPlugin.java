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
package org.snaker.jfinal.plugin;

import javax.sql.DataSource;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.cfg.Configuration;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

import java.util.Properties;

/**
 * 基于Jfinal的Snaker插件
 * 通过调用getEngine方法获取引擎的服务入口
 * @author yuqs
 * @since 2.0
 */
public class SnakerPlugin implements IPlugin {
	private static boolean isStarted = false;
	private static DataSource dataSource;
    private static Properties properties;
	private static IDataSourceProvider dataSourceProvider;
	private static SnakerEngine engine;
	
	/**
	 * 根据DataSource构造插件
	 * @param dataSource 数据源
	 */
	public SnakerPlugin(DataSource dataSource) {
		SnakerPlugin.dataSource = dataSource;
	}

    /**
     * 根据DataSource构造插件
     * @param dataSource 数据源
     * @param properties 属性
     */
    public SnakerPlugin(DataSource dataSource, Properties properties) {
        SnakerPlugin.dataSource = dataSource;
        SnakerPlugin.properties = properties;
    }
	
	/**
	 * 根据数据源提供者构造插件
	 * @param dataSourceProvider 数据源提供接口
     * @param properties 属性
	 */
	public SnakerPlugin(IDataSourceProvider dataSourceProvider, Properties properties) {
		SnakerPlugin.dataSourceProvider = dataSourceProvider;
        SnakerPlugin.properties = properties;
	}
	
	/**
	 * 启动插件
	 */
	public boolean start() {
		if (isStarted)
			return true;
		if (dataSourceProvider != null)
			dataSource = dataSourceProvider.getDataSource();
		if (dataSource == null)
			throw new RuntimeException("SnakerPlugin start error: SnakerPlugin need DataSource");
        Configuration config = new Configuration().initAccessDBObject(dataSource);
        if(properties != null) config.initProperties(properties);
		engine = config.buildSnakerEngine();
		isStarted = true;
		return true;
	}
	
	/**
	 * 获取snaker的流程引擎
	 * @return SnakerEngine
	 */
	public static SnakerEngine getEngine() {
		return engine;
	}

	/**
	 * 停止插件
	 */
	public boolean stop() {
		isStarted = false;
		return true;
	}
}
