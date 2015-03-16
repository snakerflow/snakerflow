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
package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Task;
import org.snaker.engine.entity.WorkItem;
import org.snaker.engine.test.TestSnakerBase;

import java.util.List;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestQueryTask extends TestSnakerBase {
	@Test
	public void test() {
		List<Task> tasks = queryService.getActiveTasks(new Page<Task>(),
				new QueryFilter());
		for(Task task : tasks) {
			System.out.println(task);
			System.out.println(task.getVariableMap());
		}
//		System.out.println(queryService.getWorkItems(new Page<WorkItem>(),
//				new QueryFilter().setOperator("1").setOrderId("36c0228fcfa740d5b62682dc954eaecd")));
//		List<WorkItem> workItems = queryService.getWorkItems(new Page<WorkItem>(),
//				new QueryFilter());
//		for(WorkItem item : workItems) {
//			System.out.println(item);
//			System.out.println(item.getVariables());
//		}
	}
}
