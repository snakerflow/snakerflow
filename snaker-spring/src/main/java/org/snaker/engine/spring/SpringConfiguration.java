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
package org.snaker.engine.spring;

import org.snaker.engine.SnakerException;
import org.snaker.engine.cfg.Configuration;
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
		super(new SpringContext(ctx));
		this.applicationContext = ctx;
	}
	
	public void parser() throws SnakerException {
		super.parser();
	}
	
	public boolean isCMB() {
		return true;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
