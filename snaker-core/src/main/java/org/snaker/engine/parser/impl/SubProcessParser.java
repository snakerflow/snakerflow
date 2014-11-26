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
package org.snaker.engine.parser.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.NodeModel;
import org.snaker.engine.model.SubProcessModel;
import org.snaker.engine.parser.AbstractNodeParser;
import org.w3c.dom.Element;

/**
 * 子流程节点解析类
 * @author yuqs
 * @since 1.0
 */
public class SubProcessParser extends AbstractNodeParser {
	/**
	 * 产生SubProcessModel模型对象
	 */
	protected NodeModel newModel() {
		return new SubProcessModel();
	}
	
	/**
	 * 解析decisition节点的特有属性expr
	 */
	protected void parseNode(NodeModel node, Element element) {
		SubProcessModel model = (SubProcessModel)node;
		model.setProcessName(element.getAttribute(ATTR_PROCESSNAME));
		String version = element.getAttribute(ATTR_VERSION);
		int ver = 0;
        if(NumberUtils.isNumber(version)) {
        	ver = Integer.parseInt(version);
        }
		model.setVersion(ver);
		String form = element.getAttribute(ATTR_FORM);
		if(StringHelper.isNotEmpty(form)) {
			model.setForm(form);
		} else {
			model.setForm(ConfigHelper.getProperty("subprocessurl"));
		}
	}
}
