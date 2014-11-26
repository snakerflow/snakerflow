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

package org.snaker.jfinal.access;

import com.jfinal.plugin.activerecord.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.access.transaction.DataSourceTransactionInterceptor;
import org.snaker.engine.access.transaction.TransactionInterceptor;
import org.snaker.engine.access.transaction.TransactionStatus;
import org.snaker.jfinal.JfinalHelper;

import java.sql.Connection;

/**
 * Jfinal事务拦截器
 * @author yuqs
 * @since 2.0
 */
public class JfinalTransactionInterceptor extends DataSourceTransactionInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TransactionInterceptor.class);
    public void initialize(Object accessObject) {
        //ignore
    }

    protected TransactionStatus getTransaction() {
        try {
            boolean isNew = false;
            Config config = JfinalHelper.getConfig();
            Connection conn = config.getThreadLocalConnection();
            if(conn == null) {
                conn = config.getConnection();
                config.setThreadLocalConnection(conn);
                conn.setAutoCommit(false);
                isNew = true;
            }
            return new TransactionStatus(conn, isNew);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void commit(TransactionStatus status) {
        super.commit(status);
        Config config = JfinalHelper.getConfig();
        config.removeThreadLocalConnection();
    }

    protected void rollback(TransactionStatus status) {
        super.rollback(status);
        Config config = JfinalHelper.getConfig();
        config.removeThreadLocalConnection();
    }
}
