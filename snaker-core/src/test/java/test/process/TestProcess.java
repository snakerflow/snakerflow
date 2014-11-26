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
package test.process;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.test.TestSnakerBase;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestProcess extends TestSnakerBase {
	@Test
	public void test() {
		processId = engine.process().deploy(StreamHelper.
				getStreamFromClasspath("test/task/simple/process.snaker"));
		Process process = engine.process().getProcessById(processId);
		System.out.println("output 1="+process);
		process = engine.process().getProcessByVersion(process.getName(), process.getVersion());
		System.out.println("output 2="+process);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", "1");
		engine.startInstanceById(processId, "1", args);
		engine.process().undeploy(processId);
		//engine.startInstanceById(processId, "1", args);
	}
}
