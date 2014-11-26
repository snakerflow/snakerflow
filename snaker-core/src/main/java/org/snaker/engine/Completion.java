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

import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;

/**
 * 任务、实例完成时触发动作的接口
 * @author yuqs
 * @since 2.2.0
 */
public interface Completion {
    /**
     * 任务完成触发执行
     * @param task 任务对象
     */
    public void complete(HistoryTask task);

    /**
     * 实例完成触发执行
     * @param order 实例对象
     */
    public void complete(HistoryOrder order);
}
