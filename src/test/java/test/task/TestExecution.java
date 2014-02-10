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
package test.task;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @version 1.0
 */
public class TestExecution extends TestSnakerBase {
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		//engine.executeTask("ad0857146f6145549c36c1264474b8e1", "1", args);
	}
}
