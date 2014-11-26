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

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.SnakerException;

/**
 * JTA事务拦截器
 * @author yuqs
 * @since 1.0
 */
public class JtaTransactionInterceptor extends TransactionInterceptor {
	private static final Logger log = LoggerFactory.getLogger(JtaTransactionInterceptor.class);
	
	public void initialize(Object accessObject) {
		//ignore
	}

	protected TransactionStatus getTransaction() {
		UserTransaction userTransaction = JtaTransactionHelper
				.lookupJeeUserTransaction();
		int status = JtaTransactionHelper
				.getUserTransactionStatus(userTransaction);
    	if(log.isInfoEnabled()) {
    		log.info("begin transaction=" + status);
    	}
		if (status == Status.STATUS_ACTIVE) {
			return new TransactionStatus(null, false);
		}

		if ((status != Status.STATUS_NO_TRANSACTION)
				&& (status != Status.STATUS_COMMITTED)
				&& (status != Status.STATUS_ROLLEDBACK)) {
			throw new SnakerException("无效的事务状态:" + status);
		}

		Transaction suspendedTransaction = null;
		if ((status == Status.STATUS_ACTIVE)
				|| (status == Status.STATUS_COMMITTED)
				|| (status == Status.STATUS_ROLLEDBACK)) {
			suspendedTransaction = JtaTransactionHelper.suspend();
		}

		try {
			JtaTransactionHelper.begin();
			return new TransactionStatus(null, true);
		} catch (RuntimeException e) {
			throw e;
		} finally {
			if (suspendedTransaction != null) {
				JtaTransactionHelper.resume(suspendedTransaction);
			}
		}
	}

	protected void commit(TransactionStatus status) {
    	if(log.isInfoEnabled()) {
    		log.info("commit transaction=");
    	}
		JtaTransactionHelper.commit();
	}

	protected void rollback(TransactionStatus status) {
		UserTransaction userTransaction = JtaTransactionHelper
				.lookupJeeUserTransaction();
		int txStatus = JtaTransactionHelper
				.getUserTransactionStatus(userTransaction);
    	if(log.isInfoEnabled()) {
    		log.info("rollback transaction=" + txStatus);
    	}
		if ((txStatus != Status.STATUS_NO_TRANSACTION)
				&& (txStatus != Status.STATUS_COMMITTED)
				&& (txStatus != Status.STATUS_ROLLEDBACK)) {
			JtaTransactionHelper.rollback();
		}
	}
}
