package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Order;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程实例查询测试
 * @author yuqs
 * @since 1.0
 */
public class TestQueryOrder extends TestSnakerBase {
	@Test
	public void test() {
		Page<Order> page = new Page<Order>();
		System.out.println(engine.query().getActiveOrders(
				new QueryFilter().setCreateTimeStart("2014-01-01").setProcessId("860e5edae536495a9f51937f435a1c01")));
		System.out.println(engine.query().getActiveOrders(page, new QueryFilter()));
        System.out.println(engine.query().getOrder("b2802224d75d4847ae5bfb0f7e621b8f"));
	}
}
