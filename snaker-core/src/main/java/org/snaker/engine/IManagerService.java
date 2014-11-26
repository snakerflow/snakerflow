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
package org.snaker.engine;

import java.util.List;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Surrogate;

/**
 * 管理服务接口,用于流程管理控制服务
 * 委托管理
 * 时限控制
 * @author yuqs
 * @since 1.4
 */
public interface IManagerService {
	/**
	 * 保存或更新委托代理对象
	 * @param surrogate 委托代理对象
	 */
	public void saveOrUpdate(Surrogate surrogate);
	
	/**
	 * 删除委托代理对象
	 * @param id 委托代理主键id
	 */
	public void deleteSurrogate(String id);
	
	/**
	 * 根据主键id查询委托代理对象
	 * @param id 主键id
	 * @return surrogate 委托代理对象
	 */
	public Surrogate getSurrogate(String id);
	
	/**
	 * 根据过滤条件查询委托代理对象
	 * @param filter 查询过滤器
	 * @return List<Surrogate> 委托代理对象集合
	 */
	public List<Surrogate> getSurrogate(QueryFilter filter);
	
	/**
	 * 根据授权人、流程名称获取最终代理人
	 * 如存在user1->user2->user3，那么最终返回user3
	 * @param operator 授权人
	 * @param processName 流程名称
	 * @return String 代理人
	 */
	public String getSurrogate(String operator, String processName);
	
	/**
	 * 根据过滤条件查询委托代理对象
	 * @param page 分页对象
	 * @param filter 查询过滤器
	 * @return List<Surrogate> 委托代理对象集合
	 */
	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter);
}
