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

import org.snaker.engine.model.CustomModel;
import org.snaker.engine.model.NodeModel;
import org.snaker.engine.parser.AbstractNodeParser;
import org.w3c.dom.Element;

/**
 * 自定义节点解析器
 * @author yuqs
 * @version 1.0
 */
public class CustomParser extends AbstractNodeParser {
	@Override
	protected void parseNode(NodeModel node, Element element) {
		CustomModel custom = (CustomModel)node;
		custom.setClazz(element.getAttribute(ATTR_CLAZZ));
		custom.setMethodName(element.getAttribute(ATTR_METHODNAME));
		custom.setArgs(element.getAttribute(ATTR_ARGS));
		custom.setVar(element.getAttribute(ATTR_VAR));
	}
	
	@Override
	protected NodeModel newModel() {
		return new CustomModel();
	}

}
