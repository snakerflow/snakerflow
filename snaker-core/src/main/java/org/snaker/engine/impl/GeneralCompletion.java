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

package org.snaker.engine.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.Completion;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;

/**
 * 默认的任务、实例完成时触发的动作
 * @author yuqs
 * @since 2.2.0
 */
public class GeneralCompletion implements Completion {
    private static final Logger log = LoggerFactory.getLogger(GeneralCompletion.class);

    public void complete(HistoryTask task) {
        log.info("The task[{}] has been user[{}] has completed", task.getId(), task.getOperator());
    }

    public void complete(HistoryOrder order) {
        log.info("The order[{}] has completed", order.getId());
    }
}
