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
package org.snaker.engine.parser.impl;

import org.snaker.engine.model.NodeModel;
import org.snaker.engine.model.TaskModel;
import org.snaker.engine.parser.AbstractNodeParser;
import org.w3c.dom.Element;

/**
 * 任务节点解析类
 * @author yuqs
 * @version 1.0
 */
public class TaskParser extends AbstractNodeParser {
	/**
	 * 由于任务节点需要解析form、assignee属性，这里覆盖抽象类方法实现
	 */
	@Override
	protected void parseNode(NodeModel node, Element element) {
		TaskModel task = (TaskModel)node;
		task.setUrl(element.getAttribute(ATTR_FORM));
		task.setAssignee(element.getAttribute(ATTR_ASSIGNEE));
		task.setExpireTime(element.getAttribute(ATTR_EXPIRETIME));
		task.setPerformType(element.getAttribute(ATTR_TYPE));
		task.setInterceptors(element.getAttribute(ATTR_INTERCEPTORS));
	}

	/**
	 * 产生TaskModel模型对象
	 */
	@Override
	protected NodeModel newModel() {
		return new TaskModel();
	}
}
