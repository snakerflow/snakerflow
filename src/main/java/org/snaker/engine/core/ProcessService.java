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

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.IProcessService;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.Page;
import org.snaker.engine.cache.Cache;
import org.snaker.engine.cache.CacheManager;
import org.snaker.engine.cache.CacheManagerAware;
import org.snaker.engine.cache.memory.MemoryCacheManager;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.parser.ModelParser;

/**
 * 流程定义业务类
 * @author yuqs
 * @version 1.0
 */
public class ProcessService extends AccessService implements IProcessService, CacheManagerAware {
	private static final Logger log = LoggerFactory.getLogger(ProcessService.class);
	/**
	 * cache manager
	 */
	private CacheManager cacheManager;
	private Cache<String, Process> entityCache;
	private Cache<String, String> nameCache;
	/**
	 * 由DBAccess实现类持久化order对象
	 */
	public void saveProcess(Process process) {
		access().saveProcess(process);
	}
	
	/**
	 * 由DBAccess实现类根据id或name获取process对象
	 */
	public Process getProcess(String idName) {
		Process entity = null;
		Cache<String, String> nameCache = getAvailableNameCache();
		Cache<String, Process> entityCache = getAvailableEntityCache();
		if(entityCache != null) {
			entity = entityCache.get(idName);
		}
		if(entity == null && nameCache != null && entityCache != null) {
			String name = nameCache.get(idName);
			if(StringHelper.isNotEmpty(name)) {
				entity = entityCache.get(idName);
			}
		}
		if(entity == null) {
			entity = access().getProcess(idName);
			AssertHelper.notNull(entity);
			if(entity.getModel() == null && entity.getDBContent() != null) {
				entity.setModel(ModelParser.parse(entity.getDBContent()));
			}
			getAvailableEntityCache().put(entity.getName(), entity);
			getAvailableNameCache().put(entity.getId(), entity.getName());
		}
		return entity;
	}
	
	/**
	 * 根据流程定义xml的输入流解析为字节数组，保存至数据库中，并且push到模型容器中
	 * @param input
	 */
	public String deploy(InputStream input) {
		try {
			byte[] bytes = StreamHelper.readBytes(input);
			ProcessModel model = ModelParser.parse(bytes);
			Process process = getProcess(model.getName());
			boolean isNew = (process == null);
			if(process == null) {
				process = new Process();
				process.setId(StringHelper.getPrimaryKey());
			}
			process.setName(model.getName());
			process.setDisplayName(model.getDisplayName());
			process.setState(STATE_ACTIVE);
			process.setInstanceUrl(model.getInstanceUrl());
			process.setModel(model);
			process.setBytes(bytes);
			if(isNew) {
				saveProcess(process);
			} else {
				access().updateProcess(process);
			}
			getAvailableEntityCache().put(process.getName(), process);
			getAvailableNameCache().put(process.getId(), process.getName());
			return process.getId();
		} catch(Exception e) {
			log.error(e.getMessage());
			throw new SnakerException(e.getMessage(), e.getCause());
		}
	}
	
	public List<Process> getProcesss(Page<Process> page, String name, Integer state) {
		if(page == null) throw new SnakerException("分页对象不能为空.");
		return access().getProcesss(page, name, state);
	}

	public List<Process> getAllProcess() {
		return access().getAllProcess();
	}

	public void undeploy(String processId) {
		Process process = access().getProcess(processId);
		process.setState(STATE_FINISH);
		process.setName(process.getName() + "." + processId);
		access().updateProcess(process);
	}

	public void setCacheManager(CacheManager cacheManager) {
		if(cacheManager == null) {
			this.cacheManager = new MemoryCacheManager();
		} else {
			this.cacheManager = cacheManager;
		}
	}
	
    private Cache<String, Process> getAvailableEntityCache() {
        Cache<String, Process> entityCache = getEntityCache();
		if(entityCache == null && this.cacheManager != null) {
			entityCache = this.cacheManager.getCache(getClass().getName() + ".entity");
		}
        return entityCache;
    }
    
    private Cache<String, String> getAvailableNameCache() {
        Cache<String, String> nameCache = getNameCache();
		if(nameCache == null && this.cacheManager != null) {
			nameCache = this.cacheManager.getCache(getClass().getName() + ".name");
		}
        return nameCache;
    }

	public Cache<String, Process> getEntityCache() {
		return entityCache;
	}

	public void setEntityCache(Cache<String, Process> entityCache) {
		this.entityCache = entityCache;
	}

	public Cache<String, String> getNameCache() {
		return nameCache;
	}

	public void setNameCache(Cache<String, String> nameCache) {
		this.nameCache = nameCache;
	}
}
