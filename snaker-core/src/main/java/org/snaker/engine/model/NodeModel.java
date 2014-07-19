/* Copyright 2013-2014 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.Action;
import org.snaker.engine.SnakerInterceptor;
import org.snaker.engine.core.Execution;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * 节点元素（存在输入输出的变迁）
 * @author yuqs
 * @version 1.0
 */
public abstract class NodeModel extends BaseModel implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2377317472320109317L;
	private static final Logger log = LoggerFactory.getLogger(NodeModel.class);
	/**
	 * 输入变迁集合
	 */
	private List<TransitionModel> inputs = new ArrayList<TransitionModel>();
	/**
	 * 输出变迁集合
	 */
	private List<TransitionModel> outputs = new ArrayList<TransitionModel>();
	/**
	 * layout
	 */
	private String layout;
	/**
	 * 局部前置拦截器
	 */
	private String preInterceptors;
	/**
	 * 局部后置拦截器
	 */
	private String postInterceptors;
	/**
	 * 前置局部拦截器实例集合
	 */
	private List<SnakerInterceptor> preInterceptorList = new ArrayList<SnakerInterceptor>();
	/**
	 * 后置局部拦截器实例集合
	 */
	private List<SnakerInterceptor> postInterceptorList = new ArrayList<SnakerInterceptor>();
	
	/**
	 * 具体节点模型需要完成的执行逻辑
	 * @param execution
	 */
	protected abstract void exec(Execution execution);
	
	/**
	 * 对执行逻辑增加前置、后置拦截处理
	 * @param execution
	 * @return
	 */
	public void execute(Execution execution) {
		intercept(preInterceptorList, execution);
		exec(execution);
		intercept(postInterceptorList, execution);
	}
	
	/**
	 * 运行变迁继续执行
	 * @param execution
	 */
	protected void runOutTransition(Execution execution) {
		for (TransitionModel tm : getOutputs()) {
			tm.setEnabled(true);
			tm.execute(execution);
		}
	}
	
	/**
	 * 拦截方法
	 * @param interceptorList
	 * @param execution
	 */
	private void intercept(List<SnakerInterceptor> interceptorList, Execution execution) {
		try {
			for(SnakerInterceptor interceptor : interceptorList) {
				interceptor.intercept(execution);
			}
		} catch(Exception e) {
			//拦截器执行过程中出现的异常不影响流程执行逻辑
			log.error("拦截器执行失败=" + e.getMessage());
		}
	}
	
	/**
	 * 根据父节点模型、当前节点模型判断是否可退回。可退回条件：
	 * 1、满足中间无fork、join、subprocess模型
	 * 2、满足父节点模型如果为任务模型时，参与类型为any
	 * @param parent
	 * @return
	 */
	public boolean canRejected(NodeModel parent) {
		if(parent instanceof TaskModel && !((TaskModel)parent).isPerformAny()) {
			return false;
		}
		for(TransitionModel tm : parent.getOutputs()) {
			NodeModel target = tm.getTarget();
			if(target.getName().equals(this.getName())) {
				return true;
			}
			if(target instanceof ForkModel 
					|| target instanceof JoinModel 
					|| target instanceof SubProcessModel
                    || target instanceof EndModel) {
				continue;
			}
			return canRejected(target);
		}
		return false;
	}
	
	public List<TransitionModel> getInputs() {
		return inputs;
	}
	public void setInputs(List<TransitionModel> inputs) {
		this.inputs = inputs;
	}
	public List<TransitionModel> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<TransitionModel> outputs) {
		this.outputs = outputs;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getPreInterceptors() {
		return preInterceptors;
	}

	public void setPreInterceptors(String preInterceptors) {
		this.preInterceptors = preInterceptors;
		if(StringHelper.isNotEmpty(preInterceptors)) {
			for(String interceptor : preInterceptors.split(",")) {
				SnakerInterceptor instance = (SnakerInterceptor)ClassHelper.newInstance(interceptor);
				if(instance != null) this.preInterceptorList.add(instance);
			}
		}
	}

	public String getPostInterceptors() {
		return postInterceptors;
	}

	public void setPostInterceptors(String postInterceptors) {
		this.postInterceptors = postInterceptors;
		if(StringHelper.isNotEmpty(postInterceptors)) {
			for(String interceptor : postInterceptors.split(",")) {
				SnakerInterceptor instance = (SnakerInterceptor)ClassHelper.newInstance(interceptor);
				if(instance != null) this.postInterceptorList.add(instance);
			}
		}
	}
}
