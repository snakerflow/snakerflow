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
package test.modules;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.entity.Order;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 请假流程
 * @author yuqs
 * @version 1.0
 */
public class TestLeave extends TestSnakerBase {
	//@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper
						.getStreamFromClasspath("test/modules/leave.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("apply.operator", "1");
		args.put("approveDept.operator", "1");
		args.put("approveBoss.operator", "1");
		args.put("day", new Integer(3));
//		Order order = engine.startInstanceById(processId, "1", args);
//		System.out.println(order);
//		engine.executeTask("b758ba54e1a547a29751ac73a41cabb2", "1", args);
		engine.executeAndJumpTask("95f80ee297a24b1f9a6e4bda72fe6d36", "1", args, null);
	}
}
