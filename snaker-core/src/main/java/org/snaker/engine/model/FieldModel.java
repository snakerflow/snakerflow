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

package org.snaker.engine.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段模型类
 * @author yuqs
 * @since 2.0
 */
public class FieldModel extends BaseModel {
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段模型对应的属性key/value数据
     */
    private Map<String, String> attrMap = new HashMap<String, String>();
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    /**
     * 向属性集合添加key/value数据
     * @param name 属性名称
     * @param value 属性值
     */
    public void addAttr(String name, String value) {
        this.attrMap.put(name, value);
    }

    /**
     * 获取属性集合
     * @return 属性集合
     */
    public Map<String, String> getAttrMap() {
        return this.attrMap;
    }

    /**
     * 设置属性集合
     * @param attrMap 属性集合
     */
    public void setAttrMap(Map<String, String> attrMap) {
        this.attrMap = attrMap;
    }
}
