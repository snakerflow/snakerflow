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

package org.snaker.jfinal;

import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * Jfinal帮助类
 * @author yuqs
 * @since 2.0
 */
public class JfinalHelper {
    private static final String CONFIG_NAME = "jfinal.configName";
    private static String configName = ConfigHelper.getProperty(CONFIG_NAME);
    private static Config config = null;
    public static Config getConfig() {
        if(config == null) {
            synchronized (JfinalHelper.class) {
                if(config == null) {
                    if(StringHelper.isNotEmpty(configName)) {
                        config = DbKit.getConfig(configName);
                    }
                    if(config == null) {
                        config = DbKit.getConfig();
                    }
                }
            }
        }
        return config;
    }
}
