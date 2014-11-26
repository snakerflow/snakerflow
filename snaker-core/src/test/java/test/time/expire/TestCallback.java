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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.entity.Task;
import org.snaker.engine.scheduling.JobCallback;

/**
 * @author yuqs
 * @since 1.0
 */
public class TestCallback implements JobCallback {
	private static final Logger log = LoggerFactory.getLogger(TestCallback.class);
	public void callback(String taskId, List<Task> newTasks) {
		log.info("callback taskId=" + taskId);
		log.info("newTasks=" + newTasks);
	}
}
