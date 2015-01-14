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

package org.snaker.engine.access.hibernate4;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.snaker.engine.DBAccess;
import org.snaker.engine.SnakerException;
import org.snaker.engine.access.ScriptRunner;
import org.snaker.engine.access.hibernate.HibernateAccess;
import org.snaker.engine.access.jdbc.JdbcHelper;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * hibernate4方式的数据库访问
 * @author yuqs
 * @since 2.0
 */
public class Hibernate4Access extends HibernateAccess implements DBAccess {
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 取得hibernate的connection对象
     */
    protected Connection getConnection() throws SQLException {
        if (sessionFactory instanceof SessionFactoryImpl) {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            ConnectionProvider provider = sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class);
            if(provider != null) return provider.getConnection();
        }
        return null;
    }

    public Blob createBlob(byte[] bytes) {
        return getSession().getLobHelper().createBlob(bytes);
    }

    public void runScript() {
        getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                if(JdbcHelper.isExec(conn)) {
                    return;
                }
                try {
                    String databaseType = JdbcHelper.getDatabaseType(conn);
                    String schema = "db/core/schema-" + databaseType + ".sql";
                    ScriptRunner runner = new ScriptRunner(conn, true);
                    runner.runScript(schema);
                } catch (Exception e) {
                    throw new SnakerException(e);
                }
            }
        });
    }
}
