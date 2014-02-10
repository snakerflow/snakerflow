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
package org.snaker.engine.parser;

import java.util.List;

import org.snaker.engine.helper.XmlHelper;
import org.snaker.engine.model.NodeModel;
import org.snaker.engine.model.TransitionModel;
import org.w3c.dom.Element;

/**
 * 抽象dom节点解析类
 * 完成通用的属性、变迁解析
 * @author yuqs
 * @version 1.0
 */
public abstract class AbstractNodeParser implements NodeParser {
	/**
	 * 模型对象
	 */
	protected NodeModel model;
	
	/**
	 * 实现NodeParser接口的parse函数
	 * 由子类产生各自的模型对象，设置常用的名称属性，并且解析子节点transition，构造TransitionModel模型对象
	 */
	public void parse(Element element) {
		model = newModel();
		model.setName(element.getAttribute(ATTR_NAME));
		model.setDisplayName(element.getAttribute(ATTR_DISPLAYNAME));
		model.setLayout(element.getAttribute(ATTR_LAYOUT));
		
		List<Element> transitions = XmlHelper.elements(element, NODE_TRANSITION);
		for(Element te : transitions) {
			TransitionModel transition = new TransitionModel();
			transition.setName(te.getAttribute(ATTR_NAME));
			transition.setDisplayName(te.getAttribute(ATTR_DISPLAYNAME));
			transition.setTo(te.getAttribute(ATTR_TO));
			transition.setExpr(te.getAttribute(ATTR_EXPR));
			transition.setG(te.getAttribute(ATTR_G));
			transition.setOffset(te.getAttribute(ATTR_OFFSET));
			transition.setSource(model);
			model.getOutputs().add(transition);
		}
		
		parseNode(model, element);
	}
	
	/**
	 * 子类可覆盖此方法，完成特定的解析
	 * @param model
	 * @param element
	 */
	protected void parseNode(NodeModel model, Element element) {
		
	}
	
	/**
	 * 抽象方法，由子类产生各自的模型对象
	 * @return
	 */
	protected abstract NodeModel newModel();

	/**
	 * 返回模型对象
	 */
	@Override
	public NodeModel getModel() {
		return model;
	}
}
