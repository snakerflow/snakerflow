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
package test.time.expire;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.entity.Order;
import org.snaker.engine.helper.DateHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestExpire extends TestSnakerBase {
	private static final String PROCESSNAME = "expire";
	@Before
	public void before() {
		engine.process().deploy(StreamHelper.getStreamFromClasspath("test/time/expire/process.snaker"));
	}
	
	@Test
	public void test() {
		System.out.println(DateHelper.parseTime(new DateTime(2014, 4, 6, 16, 41).toDate()));
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		args.put("task1.expireTime", new DateTime(2014, 4, 15, 9, 0).toDate());
		args.put("task1.reminderTime", new DateTime(2014, 4, 15, 8, 57).toDate());
		Order order = engine.startInstanceByName(PROCESSNAME, null, "2", args);
		System.out.println("order=" + order);
//		List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
//		for(Task task : tasks) {
//			engine.executeTask(task.getId(), "1", args);
//		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
