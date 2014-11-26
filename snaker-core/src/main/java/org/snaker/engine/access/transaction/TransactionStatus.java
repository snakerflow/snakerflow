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
package org.snaker.engine.access.transaction;

/**
 * 事务状态
 * @author yuqs
 * @since 1.0
 */
public class TransactionStatus {
	/**
	 * 事务对象，此处支持两类：
	 * Connection：JDBC方式对象类型
	 * Session：Hibernate方式对象类型
	 */
	private Object transaction;
	/**
	 * 是否为新的事务，考虑到业务层互相调用导致事务提前commit
	 */
	private final boolean newTransaction;
	
	public TransactionStatus(Object transaction, boolean newTransaction) {
		this.transaction = transaction;
		this.newTransaction = newTransaction;
	}

	public Object getTransaction() {
		return transaction;
	}

	public void setTransaction(Object transaction) {
		this.transaction = transaction;
	}

	/**
	 * 判断是否为新的事务对象，用于业务嵌套时commit判断是否对事务进行提交
	 */
	public boolean isNewTransaction() {
		return newTransaction;
	}
}
