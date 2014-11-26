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

import net.sf.ehcache.Element;

import org.snaker.engine.cache.Cache;
import org.snaker.engine.cache.CacheException;
import org.snaker.engine.helper.AssertHelper;

/**
 * ehcache实现
 * @author yuqs
 * @since 1.3
 */
public class EhCache<K, V> implements Cache<K, V> {
	/**
	 * Ehcache对象
	 */
	private net.sf.ehcache.Ehcache cache;
	public EhCache(net.sf.ehcache.Ehcache cache) {
		AssertHelper.notNull(cache);
		this.cache = cache;
	}
	
	@SuppressWarnings("unchecked")
	public V get(K key) throws CacheException {
		if(key == null) return null;
        try {
            Element element = cache.get(key);
            if (element == null) {
                return null;
            } else {
                return (V) element.getObjectValue();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	public V put(K key, V value) throws CacheException {
        try {
            V previous = get(key);
            Element element = new Element(key, value);
            cache.put(element);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	public V remove(K key) throws CacheException {
        try {
            V previous = get(key);
            cache.remove(key);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	public void clear() throws CacheException {
        try {
            cache.removeAll();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
}
