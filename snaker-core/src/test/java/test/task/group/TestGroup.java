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
package test.task.group;

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
 * 测试该类时，确认是否配置了自定义的访问策略，请检查snaker.xml中的配置
 * @author yuqs
 * @since 1.4
 */
public class TestGroup extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/group/process.snaker"));
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"role1"});
		Order order = engine.startInstanceByName("group", 0, "2", args);
		System.out.println("order=" + order);
		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		for(Task task : tasks) {
			//操作人改为test时，角色对应test，会无权处理
			engine.executeTask(task.getId(), "test1", args);
		}
	}
}
