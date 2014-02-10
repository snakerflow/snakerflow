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

import java.util.ArrayList;
import java.util.List;

import org.snaker.engine.Action;
import org.snaker.engine.core.Execution;

/**
 * 节点元素（存在输入输出的变迁）
 * @author yuqs
 * @version 1.0
 */
public class NodeModel extends BaseModel implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2377317472320109317L;
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
	 * 默认执行方法，子类如无特殊情况，可直接使用该方法
	 * @param order
	 * @param args
	 * @return
	 */
	public void execute(Execution execution) {
		for(TransitionModel tm : getOutputs()) {
			tm.setEnabled(true);
			tm.execute(execution);
		}
	}
	
	/**
	 * 根据父节点模型、当前节点模型判断是否可退回。可退回条件：
	 * 1、满足中间无fork、join、subprocess模型
	 * 2、满足父节点模型如果为任务模型时，参与类型为any
	 * @param parent
	 * @param current
	 * @return
	 */
	public boolean canRejected(NodeModel parent) {
		if(parent instanceof TaskModel && ((TaskModel)parent).getPerformType().equals(TaskModel.TYPE_ALL)) {
			return false;
		}
		for(TransitionModel tm : parent.getOutputs()) {
			NodeModel target = tm.getTarget();
			if(target.getName().equals(this.getName())) {
				return true;
			}
			if(target instanceof ForkModel 
					|| target instanceof JoinModel 
					|| target instanceof SubProcessModel) {
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
}
