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
package org.snaker.engine.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ExpressionFactory;

import org.snaker.engine.Expression;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * Juel 表达式引擎实现
 * @author yuqs
 * @since 1.2
 */
public class JuelExpression implements Expression {
	ExpressionFactory factory = new ExpressionFactoryImpl();
	
	@SuppressWarnings("unchecked")
	public <T> T eval(Class<T> T, String expr, Map<String, Object> args) {
		SimpleContext context = new SimpleContext();
		for(Entry<String, Object> entry : args.entrySet()) {
			context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), Object.class));
		}
		return (T)factory.createValueExpression(context, expr, T).getValue(context);
	}
}
