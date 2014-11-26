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
package org.snaker.engine.cache;

import org.snaker.engine.SnakerException;

/**
 * cache异常
 * @author yuqs
 * @since 1.3
 */
public class CacheException extends SnakerException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5329674600226403430L;
	/**
     * 创建cache异常
     */
    public CacheException() {
        super();
    }

    /**
     * 创建cache异常
     * @param message
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * 创建cache异常
     * @param cause
     */
    public CacheException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建cache异常
     * @param message
     * @param cause
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
