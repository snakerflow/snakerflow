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
package test.decision.expression;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.entity.Order;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 测试决策分支流程1：决策节点decision使用expr属性决定后置路线
 * @author yuqs
 * @version 1.0
 */
public class TestDecision1 extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper
						.getStreamFromClasspath("test/decision/expression/process.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		//args.put("task1.operator", new String[]{"1","2"});
		args.put("task2.operator", new String[]{"1"});
		//args.put("task3.operator", new String[]{"1","2"});
		args.put("content", "toTask2");
		Order order = engine.startInstanceById(processId, "2", args);
		System.out.println(order);
	}
}
