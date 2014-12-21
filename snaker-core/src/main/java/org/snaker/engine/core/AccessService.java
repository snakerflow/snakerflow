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
package org.snaker.engine.core;

import org.snaker.engine.Completion;
import org.snaker.engine.DBAccess;
import org.snaker.engine.impl.GeneralCompletion;

/**
 * 作为抽象父类，提供给子类access实现方式
 * @author yuqs
 * @since 1.0
 */
public abstract class AccessService {
	/**
	 * 状态；活动状态
	 */
	public static final Integer STATE_ACTIVE = 1;
	/**
	 * 状态：结束状态
	 */
	public static final Integer STATE_FINISH = 0;
	/**
	 * 状态：终止状态
	 */
	public static final Integer STATE_TERMINATION = 2;
	/**
	 * 数据库的access
	 */
	protected DBAccess access;
    /**
     * 完成触发接口
     */
    private Completion completion = null;
	/**
	 * 获取DBAccess，供子类使用
	 */
	public DBAccess access() {
		return access;
	}
	/**
	 * setter
	 * @param access 访问对象
	 */
	public void setAccess(DBAccess access) {
		this.access = access; 
	}

    /**
     * setter
     * @param completion 完成对象
     */
    public void setCompletion(Completion completion) {
        this.completion = completion;
    }
    public Completion getCompletion() {
        if(completion != null) {
            return completion;
        }

        completion = ServiceContext.find(Completion.class);
        if(completion == null) {
            ServiceContext.put(Completion.class.getName(), GeneralCompletion.class);
            completion = ServiceContext.find(Completion.class);
        }
        return completion;
    }
}
