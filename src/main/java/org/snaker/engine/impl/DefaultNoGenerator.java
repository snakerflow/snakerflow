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
package org.snaker.engine.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.snaker.engine.INoGenerator;
import org.snaker.engine.model.ProcessModel;

/**
 * 默认的流程实例编号生成器
 * 编号生成规则为:yyyy-mm-dd-hh-mi-ss-ms-random
 * @author yuqs
 * @version 1.0
 */
public class DefaultNoGenerator implements INoGenerator {
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd-HH:mm:ss-SSS");
	@Override
	public String generate(ProcessModel model) {
		String time = df.format(new Date());
		Random random = new Random();
		return time + "-" + random.nextInt(1000);
	}
}
