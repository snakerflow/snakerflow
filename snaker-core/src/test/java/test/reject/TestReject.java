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
package test.reject;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestReject extends TestSnakerBase {
	@Before
	public void before() {
		processId = engine.process().deploy(StreamHelper
						.getStreamFromClasspath("test/reject/reject.snaker"));
		engine.startInstanceById(processId);
	}
	
	@Test
	public void test() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("number", 2);
		//engine.executeTask("f4a7a9b486ca41d3a2ebb1ecc0af75a9", null, args);
		//engine.executeAndJumpTask("737a9d4118594d69a918ed20daf347cb", null, args, "task1");
	}
}
