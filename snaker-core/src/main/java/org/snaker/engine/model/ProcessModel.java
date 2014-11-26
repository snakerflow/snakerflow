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

import java.util.ArrayList;
import java.util.List;

import org.snaker.engine.INoGenerator;
import org.snaker.engine.helper.ClassHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.impl.DefaultNoGenerator;

/**
 * 流程定义process元素
 * @author yuqs
 * @since 1.0
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
    private List<TaskModel> taskModels = new ArrayList<TaskModel>();
	/**
	 * 流程实例启动url
	 */
	private String instanceUrl;
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
     * lock
     */
    private final Object lock = new Object();
	
	/**
	 * 返回当前流程定义的所有工作任务节点模型
	 * @return
     * @deprecated
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
     * 获取所有的有序任务模型集合
     * @return List<TaskModel> 任务模型集合
     */
    public List<TaskModel> getTaskModels() {
        if(taskModels.isEmpty()) {
            synchronized (lock) {
                if(taskModels.isEmpty())
                    buildModels(taskModels, getStart().getNextModels(TaskModel.class), TaskModel.class);
            }
        }
        return taskModels;
    }

    /**
     * 根据指定的节点类型返回流程定义中所有模型对象
     * @param clazz 节点类型
     * @param <T> 泛型
     * @return 节点列表
     */
    public <T> List<T> getModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        buildModels(models, getStart().getNextModels(clazz), clazz);
        return models;
    }

    private <T> void buildModels(List<T> models, List<T> nextModels, Class<T> clazz) {
        for(T nextModel : nextModels) {
            if(!models.contains(nextModel)) {
                models.add(nextModel);
                buildModels(models, ((NodeModel)nextModel).getNextModels(clazz), clazz);
            }
        }
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
	 * @param nodeName 节点名称
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
	 * @param nodeNames 节点名称数组
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
