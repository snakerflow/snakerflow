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
package org.snaker.engine;

import java.util.List;

import org.snaker.engine.entity.TaskActor;

/**
 * 任务访问策略类
 * 用于判断给定的操作人员是否允许执行某个任务
 * @author yuqs
 * @since 1.4
 */
public interface TaskAccessStrategy {
	/**
	 * 根据操作人id、参与者集合判断是否允许访问所属任务
	 * @param operator 操作人id
	 * @param actors 参与者列表 传递至该接口的实现类中的参与者都是为非空
	 * @return boolean 是否允许访问
	 */
	boolean isAllowed(String operator, List<TaskActor> actors); 
}
