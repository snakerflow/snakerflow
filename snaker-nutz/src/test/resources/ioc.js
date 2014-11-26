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

var ioc = {
	dataSource : {
		type : "org.apache.commons.dbcp.BasicDataSource",
		events : { depose : "close" },
		fields : {
			driverClassName : "com.mysql.jdbc.Driver",
			url : "jdbc:mysql://127.0.0.1:3306/snaker?useUnicode=true&characterEncoding=utf-8",
			username : "root",
			password : "root",
			initialSize : 5,
			maxActive : 50,
			maxIdle : 10,
			defaultAutoCommit : false,
			timeBetweenEvictionRunsMillis : 3600000,
			minEvictableIdleTimeMillis : 3600000
		}
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [ { refer : "dataSource" } ],
		fields : { sqlManager : { refer : "sql" } }
	},
	snaker : {
		type : "test.nutz.SnakerFacets",
		args : [ { refer : "dataSource" } ]
	}
}