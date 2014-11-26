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
package test.subprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 测试子流程的fork-join流程
 * start->subprocess1----->end
 *    |___subprocess2_______|
 * @author yuqs
 * @since 1.0
 */
public class TestSubProcess2 extends TestSnakerBase {
	@Before
	public void before() {
		engine.process().deploy(StreamHelper
				.getStreamFromClasspath("test/subprocess/child.snaker"));
		processId = engine.process().deploy(StreamHelper
						.getStreamFromClasspath("test/subprocess/subprocess2.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		Order order = engine.startInstanceById(processId, "2", args);
		System.out.println(order);
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			engine.executeTask(task.getId(), "1", args);
		}
	}
}
