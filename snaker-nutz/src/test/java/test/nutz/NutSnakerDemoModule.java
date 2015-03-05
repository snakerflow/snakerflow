package test.nutz;

import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * 这是个为了演示配置方法的类. 无需在snaker.xml中声明nutz相关的类.
 * @author wendal(wendal19885@gmail.com)
 *
 */
@IocBy(type=ComboIocProvider.class, args={"*js", "ioc/", // 其他json ioc配置文件
										  "*anno", "net.wendal.snaker.demo", //需要扫描的package 
										  "*org.snaker.nutz.SnakerIocLoader", "flows"}) // 假设流程配置文件在flows目录下
public class NutSnakerDemoModule {
}
