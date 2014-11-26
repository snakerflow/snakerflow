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

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.cfg.Configuration;

import javax.sql.DataSource;

/**
 * 获取SnakerEngine的门面类
 * @author yuqs
 * @since 2.0
 */
public class SnakerFacets {
    private SnakerEngine engine;

    public SnakerFacets(DataSource dataSource) {
        engine = new Configuration().initAccessDBObject(dataSource)
                .buildSnakerEngine();
    }

    public SnakerEngine getEngine() {
        return engine;
    }
}
