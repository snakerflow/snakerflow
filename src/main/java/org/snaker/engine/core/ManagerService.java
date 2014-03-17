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
package org.snaker.engine.core;

import java.util.List;

import org.snaker.engine.IManagerService;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * @author yuqs
 * @version 1.0
 */
public class ManagerService extends AccessService implements IManagerService {
	public void saveOrUpdate(Surrogate surrogate) {
		AssertHelper.notNull(surrogate);
		if(StringHelper.isEmpty(surrogate.getId())) {
			surrogate.setId(StringHelper.getPrimaryKey());
			access().saveSurrogate(surrogate);
		} else {
			access().updateSurrogate(surrogate);
		}
	}

	public void deleteSurrogate(String id) {
		Surrogate surrogate = getSurrogate(id);
		AssertHelper.notNull(surrogate);
		access().deleteSurrogate(surrogate);
	}

	public Surrogate getSurrogate(String id) {
		return access().getSurrogate(id);
	}
	
	public List<Surrogate> getSurrogate(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getSurrogate(null, filter);
	}

	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getSurrogate(page, filter);
	}
	
	public String getSurrogate(String operator, String processName) {
		AssertHelper.notEmpty(operator);
		QueryFilter filter = new QueryFilter().setOperator(operator);
		if(StringHelper.isNotEmpty(processName)) {
			filter.setName(processName);
		}
		List<Surrogate> surrogates = getSurrogate(filter);
		for(Surrogate surrogate : surrogates) {
			//TODO 判断当前时间是否介于sdate、edate之间，如果是，则需要递归代理人是否还存在委托的情况
		}
		return "";
	}
}
