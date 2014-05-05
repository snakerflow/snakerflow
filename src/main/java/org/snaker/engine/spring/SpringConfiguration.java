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
package org.snaker.engine.spring;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.SnakerException;
import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.core.ServiceContext;
import org.springframework.context.ApplicationContext;

/**
 * Spring的配置对象
 * @author yuqs
 * @since 1.5
 */
public class SpringConfiguration extends Configuration {
	/**
	 * Spring上下文
	 */
	private ApplicationContext applicationContext;
	
	public SpringConfiguration(ApplicationContext ctx) {
		super.initSpringContext(ctx);
		this.applicationContext = ctx;
	}
	
	/**
	 * 重新实现 {@link Configuration}的构建引擎方法
	 * 设置SpringContext，并主动创建 {@link SpringSnakerEngine}对象
	 */
	public SnakerEngine buildSnakerEngine() throws SnakerException {
		ServiceContext.setContext(new SpringContext(this.applicationContext));
		return super.buildSnakerEngine();
	}

	protected SnakerEngine getEngine() {
		return new SpringSnakerEngine();
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
