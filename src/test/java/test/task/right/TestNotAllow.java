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
package test.task.right;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 测试无权限执行任务
 * @author yuqs
 * @version 1.0
 */
public class TestNotAllow extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper
						.getStreamFromClasspath("test/task/right/process.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"2"});
		Order order = engine.startInstanceById(processId, "2", args);
		System.out.println(order);
		List<Task> tasks = queryService.getActiveTasks(order.getId());
		for(Task task : tasks) {
			engine.executeTask(task.getId(), SnakerEngine.ADMIN, args);
		}
	}
}
