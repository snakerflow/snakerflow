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
package test.task.actor;

import org.junit.Test;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestActor extends TestSnakerBase {
	@Test
	public void test() {
		//engine.task().addTaskActor("13b9edb451e5453394f7980ff4ab7ca9", new String[]{"test1", "test2"});
		engine.task().removeTaskActor("13b9edb451e5453394f7980ff4ab7ca9", "2");
	}
}
