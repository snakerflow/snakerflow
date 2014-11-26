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

package test.task.model;

import org.junit.Before;
import org.junit.Test;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.model.TaskModel;
import org.snaker.engine.test.TestSnakerBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试模型操作
 * @author yuqs
 * @since 2.0
 */
public class TestModel extends TestSnakerBase {
    @Override
    protected SnakerEngine getEngine() {
        return  new Configuration().initProperties("snaker1.properties").buildSnakerEngine();
    }

    @Before
    public void before() {
        processId = engine.process().deploy(StreamHelper.getStreamFromClasspath("test/task/simple/process.snaker"));
    }

    @Test
    public void test() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("task1.operator", new String[]{"1"});
        Order order = engine.startInstanceByName("simple", null, "2", args);
        System.out.println("order=" + order);
        List<Task> tasks = queryService.getActiveTasks(new QueryFilter().setOrderId(order.getId()));
        for(Task task : tasks) {
            TaskModel model = engine.task().getTaskModel(task.getId());
            System.out.println(model.getName());
            List<TaskModel> models = model.getNextModels(TaskModel.class);
            for(TaskModel tm : models) {
                System.out.println(tm.getName());
            }
        }
        List<TaskModel> models = engine.process().getProcessById(processId).getModel().getModels(TaskModel.class);
            for(TaskModel tm : models) {
                System.out.println(tm.getName());
            }
    }

}
