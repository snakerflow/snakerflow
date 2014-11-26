/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.snaker.engine;

import org.snaker.engine.core.Execution;
import org.snaker.engine.model.TaskModel;

/**
 * 分配参与者的处理抽象类
 * @author yuqs
 * @since 2.1.0
 */
public abstract class Assignment implements AssignmentHandler {
    /**
     * 分配参与者方法，可获取到当前的任务模型、执行对象
     * @param model 任务模型
     * @param execution 执行对象
     * @return Object 参与者对象
     */
    public abstract Object assign(TaskModel model, Execution execution);

    public Object assign(Execution execution) {
        return assign(null, execution);
    }
}
