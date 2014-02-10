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

import org.snaker.engine.INoGenerator;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.impl.DefaultNoGenerator;

/**
 * 流程定义process元素
 * @author yuqs
 * @version 1.0
 */
public class ProcessModel extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9000210138346445915L;
	/**
	 * 节点元素集合
	 */
	private List<NodeModel> nodes = new ArrayList<NodeModel>();
	/**
	 * 流程实例启动url
	 */
	private String instanceUrl;
	/**
	 * 是否存在子流程
	 */
	private boolean isExistSub = false;
	/**
	 * 期望完成时间
	 */
	private String expireTime;
	/**
	 * 实例编号生成的class
	 */
	private String instanceNoClass;
	/**
	 * 实例编号生成器对象
	 */
	private INoGenerator generator;
	
	/**
	 * 返回当前流程定义的所有工作任务节点模型
	 * @return
	 */
	public List<WorkModel> getWorkModels() {
		List<WorkModel> models = new ArrayList<WorkModel>();
		for(NodeModel node : nodes) {
			if(node instanceof WorkModel) {
				models.add((WorkModel)node);
			}
		}
		return models;
	}

	/**
	 * 获取process定义的start节点模型
	 * @return
	 */
	public StartModel getStart() {
		for(NodeModel node : nodes) {
			if(node instanceof StartModel) {
				return (StartModel)node;
			}
		}
		return null;
	}
	
	/**
	 * 获取process定义的指定节点名称的节点模型
	 * @param nodeName
	 * @return
	 */
	public NodeModel getNode(String nodeName) {
		for(NodeModel node : nodes) {
			if(node.getName().equals(nodeName)) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * 判断当前模型的节点是否包含给定的节点名称参数
	 * @param nodeNames
	 * @return
	 */
	public <T> boolean containsNodeNames(Class<T> T, String... nodeNames) {
		for(NodeModel node : nodes) {
			if(!T.isInstance(node)) {
				continue;
			}
			for(String nodeName : nodeNames) {
				if(node.getName().equals(nodeName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isExistSub() {
		return isExistSub;
	}
	public void setExistSub(boolean isExistSub) {
		this.isExistSub = isExistSub;
	}
	
	public List<NodeModel> getNodes() {
		return nodes;
	}
	public void setNodes(List<NodeModel> nodes) {
		this.nodes = nodes;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public String getInstanceNoClass() {
		return instanceNoClass;
	}

	public void setInstanceNoClass(String instanceNoClass) {
		this.instanceNoClass = instanceNoClass;
		if(StringHelper.isNotEmpty(instanceNoClass)) {
			generator = (INoGenerator)ClassHelper.newInstance(instanceNoClass);
		}
	}
	
	public INoGenerator getGenerator() {
		return generator == null ? new DefaultNoGenerator() : generator;
	}

	public void setGenerator(INoGenerator generator) {
		this.generator = generator;
	}
}
