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
package org.snaker.engine.parser;

import org.snaker.engine.model.NodeModel;
import org.w3c.dom.Element;

/**
 * 节点解析接口
 * @author yuqs
 * @since 1.0
 */
public interface NodeParser {
	/**
	 * 变迁节点名称
	 */
	public static final String NODE_TRANSITION = "transition";
	
	/**
	 * 节点属性名称
	 */
	public static final String ATTR_NAME = "name";
	public static final String ATTR_DISPLAYNAME = "displayName";
	public static final String ATTR_INSTANCEURL = "instanceUrl";
	public static final String ATTR_INSTANCENOCLASS = "instanceNoClass";
	public static final String ATTR_EXPR = "expr";
	public static final String ATTR_HANDLECLASS = "handleClass";
	public static final String ATTR_FORM = "form";
    public static final String ATTR_FIELD = "field";
    public static final String ATTR_VALUE = "value";
    public static final String ATTR_ATTR = "attr";
    public static final String ATTR_TYPE= "type";
	public static final String ATTR_ASSIGNEE = "assignee";
	public static final String ATTR_ASSIGNEE_HANDLER = "assignmentHandler";
	public static final String ATTR_PERFORMTYPE = "performType";
	public static final String ATTR_TASKTYPE = "taskType";
	public static final String ATTR_TO = "to";
	public static final String ATTR_PROCESSNAME = "processName";
	public static final String ATTR_VERSION = "version";
	public static final String ATTR_EXPIRETIME = "expireTime";
	public static final String ATTR_AUTOEXECUTE = "autoExecute";
	public static final String ATTR_CALLBACK = "callback";
	public static final String ATTR_REMINDERTIME = "reminderTime";
	public static final String ATTR_REMINDERREPEAT = "reminderRepeat";
    public static final String ATTR_CLAZZ = "clazz";
    public static final String ATTR_METHODNAME = "methodName";
    public static final String ATTR_ARGS = "args";
    public static final String ATTR_VAR = "var";
    public static final String ATTR_LAYOUT = "layout";
    public static final String ATTR_G = "g";
    public static final String ATTR_OFFSET = "offset";
    public static final String ATTR_PREINTERCEPTORS = "preInterceptors";
    public static final String ATTR_POSTINTERCEPTORS = "postInterceptors";
	
	/**
	 * 节点dom元素解析方法，由实现类完成解析
	 * @param element dom元素
	 */
	public void parse(Element element);
	
	/**
	 * 解析成功后，提供返回NodeModel模型对象
	 * @return 节点模型
	 */
	public NodeModel getModel();
}
