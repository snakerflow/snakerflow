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
package test.concurrency;

import java.util.HashMap;
import java.util.Map;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.entity.Process;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 测试并发
 * @author yuqs
 * @version 1.0
 */
public class TestConcurrent extends TestSnakerBase {
	public static void main(String[] a) {
		TestConcurrent con = new TestConcurrent();
		Process process = con.engine.process().getProcess("simple");
		for(int i = 0; i < 50; i++) {
			new Thread(new StartProcess(con.engine, process.getId())).start();
		}
	}
}

class StartProcess implements Runnable {
	private SnakerEngine engine;
	private String processId;
	public StartProcess(SnakerEngine engine, String processId) {
		this.engine = engine;
		this.processId = processId;
	}
	@Override
	public void run() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("task1.operator", new String[]{"1"});
		try{
		engine.startInstanceById(processId, "2", args);//simple流程
		}catch(Exception e) {
		e.printStackTrace();	
		}
	}
	
}