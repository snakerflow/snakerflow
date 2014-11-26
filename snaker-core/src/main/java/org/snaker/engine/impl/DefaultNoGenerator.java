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
package org.snaker.engine.impl;

import java.util.Random;

import org.joda.time.DateTime;
import org.snaker.engine.INoGenerator;
import org.snaker.engine.model.ProcessModel;

/**
 * 默认的流程实例编号生成器
 * 编号生成规则为:yyyyMMdd-HH:mm:ss-SSS-random
 * @author yuqs
 * @since 1.0
 */
public class DefaultNoGenerator implements INoGenerator {
	public String generate(ProcessModel model) {
		DateTime dateTime = new DateTime();
		String time = dateTime.toString("yyyyMMdd-HH:mm:ss-SSS");
		Random random = new Random();
		return time + "-" + random.nextInt(1000);
	}
}
