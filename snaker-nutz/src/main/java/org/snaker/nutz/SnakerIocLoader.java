package org.snaker.nutz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.nutz.ioc.IocLoader;
import org.nutz.ioc.IocLoading;
import org.nutz.ioc.ObjectLoadException;
import org.nutz.ioc.meta.IocObject;
import org.nutz.ioc.meta.IocValue;
import org.nutz.json.Json;
import org.nutz.lang.Streams;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.NutResource;
import org.nutz.resource.Scans;
import org.snaker.engine.DBAccess;
import org.snaker.engine.SnakerEngine;
import org.snaker.engine.cfg.Configuration;
import org.snaker.engine.entity.Process;
import org.snaker.engine.impl.SimpleContext;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.parser.ModelParser;
import org.snaker.nutz.access.NutzAccess;
import org.snaker.nutz.access.NutzTransactionInterceptor;

/**
 * 用法: 假设使用ComboIocLoader及流程文件位于flows目录.在@IocBy中声明 "*xxxxxx.xxx.x.x.SnakerIocLoader", "flows"
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class SnakerIocLoader implements IocLoader {
	
	private static final Log log = Logs.get();
	protected IocObject iobj;
	protected String name = "snakerEngine";
	protected String dataSourceBeanName = "dataSource";
	protected String factoryMethodName = "buildSnaker";
	protected static String querySql = "select * from wf_process where name=? order by version desc";

	public SnakerIocLoader(String...paths) {
    	// 手工构建一个nutz的ioc bean定义
        iobj = new IocObject();
        iobj.setFactory(SnakerIocLoader.class.getName() +"#" + factoryMethodName); // 调用本类的buildSnaker方法
        iobj.setType(SnakerEngine.class); // 反馈类型
        IocValue ds = new IocValue();
        ds.setType(IocValue.TYPE_REFER);
        ds.setValue(dataSourceBeanName); // 引用数据源
        iobj.addArg(ds);
        for (String path : paths) {
        	IocValue p = new IocValue();
        	p.setValue(path);
        	iobj.addArg(p);
		}
        if (log.isDebugEnabled())
        	log.debug("snakerflow bean will define as\n" + Json.toJson(iobj));
    }
	
	public static SnakerEngine buildSnaker(DataSource ds) throws IOException {
		return buildSnaker(ds, new Object[]{});
	}

    public static SnakerEngine buildSnaker(DataSource ds, String path) throws IOException {
    	return buildSnaker(ds, new Object[]{path});
    }
    
    protected static SnakerEngine buildSnaker(DataSource ds, Object...paths) throws IOException {
    	// 首先,我们构建一个snaker的上下文
    	SimpleContext ctx = new SimpleContext();
    	// 将集成nutz所必须的两个类,关联之. 这样使用者仅需要声明本IocLoader即可完成全部配置
    	ctx.put(NutzAccess.class.getName(), NutzAccess.class);
    	ctx.put(NutzTransactionInterceptor.class.getName(), NutzTransactionInterceptor.class);
    	// 开始构建sanker的配置对象
    	Configuration cnf = new Configuration(ctx);
        cnf.initAccessDBObject(ds);
        SnakerEngine engine = cnf.buildSnakerEngine();
        // 如果用户声明了流程描述文件的路径,加载之
        if (paths != null) {
        	for (Object path : paths) {
				for(NutResource re : Scans.me().scan(String.valueOf(path))) {
					if (log.isDebugEnabled())
						log.debug("Found snakerflow xml > " + re.getName());
					//*********************************************************
					// 这部分属于hack的部分, 因为snaker并不识别相同的流程配置,而是简单地作为新流程
					// 所以,需要自行查询一下是不是数据相同,不一样的时候才deploy
					byte[] data = Streams.readBytesAndClose(re.getInputStream());
					ProcessModel model = ModelParser.parse(data);
					List<Process> list = ctx.find(DBAccess.class).queryList(Process.class, querySql, model.getName());
					if (!list.isEmpty()) {
						Process p = list.get(0);
						byte[] cnt = p.getDBContent();
						if (cnt != null && Arrays.equals(cnt, data)) {
							log.debug("Same  snakerflow xml > " + re.getName() + " skiped");
							continue;
						}
					}
					//*********************************************************
					// 同名的流程数据有更新或这是全新的流程,部署之
					engine.process().deploy(new ByteArrayInputStream(data));
				}
			}
        }
        // 工厂方法完成, snaker引擎已经准备好了,可以返回了
        return engine;
    }

	public String[] getName() {
		// 这个类仅需要维护一个ioc bean的定义,所以就是单个name的数组咯
		return new String[]{name};
	}

	public boolean has(String name) {
		return this.name.equals(name); // 就一个ioc bean定义嘛
	}

	public IocObject load(IocLoading il, String name) throws ObjectLoadException {
		if (this.name.equals(name))
			return iobj;
		throw new ObjectLoadException("Object '" + name + "' without define!");
	}
}
