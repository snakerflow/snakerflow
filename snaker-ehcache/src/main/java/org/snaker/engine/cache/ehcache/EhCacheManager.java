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
package org.snaker.engine.cache.ehcache;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.cache.Cache;
import org.snaker.engine.cache.CacheException;
import org.snaker.engine.cache.CacheManager;
import org.snaker.engine.helper.ConfigHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;

/**
 * ehcache管理器实现
 * @author yuqs
 * @since 1.3
 */
public class EhCacheManager implements CacheManager {
	private static final Logger log = LoggerFactory.getLogger(EhCacheManager.class);
	protected net.sf.ehcache.CacheManager manager;
	private String configFile = "org/snaker/engine/cache/ehcache/ehcache.xml";
	
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        try {
            net.sf.ehcache.Ehcache cache = ensureCacheManager().getEhcache(name);
            if (cache == null) {
                if (log.isInfoEnabled()) {
                    log.info("Cache with name '{}' does not yet exist.  Creating now.", name);
                }
                this.manager.addCache(name);

                cache = manager.getCache(name);

                if (log.isInfoEnabled()) {
                    log.info("Added EhCache named [" + name + "]");
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Using existing EHCache named [" + cache.getName() + "]");
                }
            }
            return new EhCache<K, V>(cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
	}

    public void destroy() throws CacheException {
        try {
            manager.shutdown();
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to cleanly shutdown implicitly created CacheManager instance.  " +
                        "Ignoring (shutting down)...");
            }
        }
    }

    private net.sf.ehcache.CacheManager ensureCacheManager() {
        try {
            if (this.manager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("cacheManager property not set.  Constructing CacheManager instance... ");
                }
                String cacheConfig = ConfigHelper.getProperty("cache.config");
                if(StringHelper.isNotEmpty(cacheConfig)) {
                	configFile = cacheConfig;
                }
                InputStream input = StreamHelper.getStreamFromClasspath(configFile);
                this.manager = new net.sf.ehcache.CacheManager(input);
                if (log.isInfoEnabled()) {
                    log.info("instantiated Ehcache CacheManager instance.");
                }
                if (log.isDebugEnabled()) {
                    log.debug("implicit cacheManager created successfully.");
                }
            }
            return this.manager;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }
}
