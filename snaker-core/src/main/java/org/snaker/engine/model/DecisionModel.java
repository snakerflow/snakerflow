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
package org.snaker.engine.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.DecisionHandler;
import org.snaker.engine.Expression;
import org.snaker.engine.SnakerException;
import org.snaker.engine.core.Execution;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * 决策定义decision元素
 * @author yuqs
 * @since 1.0
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
	/**
	 * 决策处理类实例
	 */
	private DecisionHandler decide;
	/**
	 * 表达式解析器
	 */
	private transient Expression expression;
	
	public void exec(Execution execution) {
		log.info(execution.getOrder().getId() + "->decision execution.getArgs():" + execution.getArgs());
		if(expression == null) {
			expression = ServiceContext.getContext().find(Expression.class);
		}
		log.info("expression is " + expression);
		if(expression == null) throw new SnakerException("表达式解析器为空，请检查配置.");
		String next = null;
		if(StringHelper.isNotEmpty(expr)) {
			next = expression.eval(String.class, expr, execution.getArgs());
		} else if(decide != null) {
			next = decide.decide(execution);
		}
		log.info(execution.getOrder().getId() + "->decision expression[expr=" + expr + "] return result:" + next);
		boolean isfound = false;
		for(TransitionModel tm : getOutputs()) {
			if(StringHelper.isEmpty(next)) {
				String expr = tm.getExpr();
				if(StringHelper.isNotEmpty(expr) && expression.eval(Boolean.class, expr, execution.getArgs())) {
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
		if(!isfound) throw new SnakerException(execution.getOrder().getId() + "->decision节点无法确定下一步执行路线");
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
		if(StringHelper.isNotEmpty(handleClass)) {
			decide = (DecisionHandler)ClassHelper.newInstance(handleClass);
		}
	}
}
