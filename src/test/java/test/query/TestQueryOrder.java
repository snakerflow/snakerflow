package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.entity.Order;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程实例查询测试
 * @author yuqs
 * @version 1.0
 */
public class TestQueryOrder extends TestSnakerBase {
	@Test
	public void test() {
		Page<Order> page = new Page<Order>();
		System.out.println(engine.query().getActiveOrders());
		System.out.println(engine.query().getActiveOrders(page));
	}
}
