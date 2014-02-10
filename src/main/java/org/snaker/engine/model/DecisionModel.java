/* Copyright 2012-2013 the original author or authors.
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
package org.snaker.engine.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.DecisionHandler;
import org.snaker.engine.core.Execution;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.ExprHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * 决策定义decision元素
 * @author yuqs
 * @version 1.0
 */
public class DecisionModel extends NodeModel {
	private static final Logger log = LoggerFactory.getLogger(DecisionModel.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -806621814645169999L;
	/**
	 * 决策选择表达式串（需要表达式引擎解析）
	 */
	private String expr;
	/**
	 * 决策处理类，对于复杂的分支条件，可通过handleClass来处理
	 */
	private String handleClass;
	
	@Override
	public void execute(Execution execution) {
		log.info(execution.getOrder().getId() + "->decision execution.getArgs():" + execution.getArgs());
		String next = null;
		if(StringHelper.isNotEmpty(expr)) {
			next = ExprHelper.evalString(execution.getArgs(), expr);
		} else if(StringHelper.isNotEmpty(handleClass)) {
			DecisionHandler handler = (DecisionHandler)ClassHelper.newInstance(handleClass);
			if(handler != null) {
				next = handler.decide(execution);
			}
		}
		log.info(execution.getOrder().getId() + "->decision expression[expr=" + expr + "] return result:" + next);
		boolean isfound = false;
		for(TransitionModel tm : getOutputs()) {
			if(StringHelper.isEmpty(next)) {
				String expr = tm.getExpr();
				if(StringHelper.isNotEmpty(expr) && ExprHelper.evalBoolean(execution.getArgs(), expr)) {
					tm.setEnabled(true);
					tm.execute(execution);
					isfound = true;
				}
			} else {
				if(tm.getName().equals(next)) {
					tm.setEnabled(true);
					tm.execute(execution);
					isfound = true;
				}
			}
		}
		if(!isfound) log.warn(execution.getOrder().getId() + "->decision can't find next transition.");
	}
	
	public String getExpr() {
		return expr;
	}
	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getHandleClass() {
		return handleClass;
	}

	public void setHandleClass(String handleClass) {
		this.handleClass = handleClass;
	}
}
