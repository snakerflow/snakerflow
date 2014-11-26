/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.snaker.nutz.access;

import java.sql.Connection;

import org.nutz.trans.Trans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.transaction.TransactionInterceptor;
import org.snaker.engine.access.transaction.TransactionStatus;

/**
 * Nutz事务拦截器
 * @author yuqs
 * @since 2.0
 */
public class NutzTransactionInterceptor extends TransactionInterceptor {
    private static final Logger log = LoggerFactory.getLogger(NutzTransactionInterceptor.class);
    public void initialize(Object accessObject) {
        //ignore
    }

    protected TransactionStatus getTransaction() {
        try {
            boolean isNew = false;
            if(Trans.get() == null) {
                Trans.begin(Connection.TRANSACTION_REPEATABLE_READ);
                isNew = true;
            }
            return new TransactionStatus(Trans.get(), isNew);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void commit(TransactionStatus status) {
        try {
            Trans.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                Trans.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    protected void rollback(TransactionStatus status) {
        try {
            Trans.rollback();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                Trans.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
