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

package test.nutz;

import org.junit.Before;
import org.junit.Test;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试nutz整合
 * @author yuqs
 * @since 2.0
 */
public class TestNutz {
    private SnakerEngine engine;
    @Before
    public void before() {
        Ioc ioc = new NutIoc(new JsonLoader("ioc.js"));
        SnakerFacets facets = ioc.get(SnakerFacets.class, "snaker");
        engine = facets.getEngine();
    }

    @Test
    public void test() {
        engine.process().deploy(StreamHelper.getStreamFromClasspath("process.snaker"));
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("task1.operator", new String[]{"1"});
        Order order = engine.startInstanceByName("simple", 0, "2", args);
        System.out.println("order=" + order);
        List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
        for(Task task : tasks) {
            engine.executeTask(task.getId(), "1", args);
        }
    }
}
