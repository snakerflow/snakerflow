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
package test.cc;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.snaker.engine.entity.Order;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @version 1.0
 */
public class TestCC extends TestSnakerBase {
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		Order order = engine.startInstanceByName("simple", 0, "2", args);
		engine.order().createCCOrder(order.getId(), "test");
//		engine.order().updateCCStatus("01b960b9d5df4be7b8565b9f64bc1856", "test");
//		engine.order().deleteCCOrder("01b960b9d5df4be7b8565b9f64bc1856", "test");
	}
}
