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

/**
 * 短整型类型
 * @author yuqs
 * @since 3.0
 */
public class ShortType implements VariableType {
  private static final long serialVersionUID = 1L;

  public String getTypeName() {
    return "short";
  }

  public Object getValue(ValueFields valueFields) {
    if(valueFields.getLongValue() != null) {
      return new Short(valueFields.getLongValue().shortValue());      
    }
    return null;
  }

  public void setValue(Object value, ValueFields valueFields) {
    if (value != null) {
      valueFields.setLongValue(((Short) value).longValue());
    } else {
      valueFields.setLongValue(null);
    }      
  }

  public boolean isAbleToStore(Object value) {
    if (value==null) {
      return true;
    }
    return Short.class.isAssignableFrom(value.getClass())
           || short.class.isAssignableFrom(value.getClass());
  }
}
