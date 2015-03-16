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
package org.snaker.engine.entity.var;

import java.io.Serializable;

/**
 * 变量类型接口
 * @author yuqs
 * @since 3.0
 */
public interface VariableType extends Serializable {
    /**
     * name of variable type (limited to 100 characters length)
     */
    String getTypeName();

    /**
     * @return whether this variable type can store the specified value.
     */
    boolean isAbleToStore(Object value);

    /**
     * Stores the specified value in the supplied {@link ValueFields}.
     */
    void setValue(Object value, ValueFields valueFields);

    /**
     * @return the value of a variable based on the specified {@link ValueFields}.
     */
    Object getValue(ValueFields valueFields);
}
