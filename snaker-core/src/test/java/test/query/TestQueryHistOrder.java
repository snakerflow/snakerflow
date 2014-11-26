package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程实例查询测试
 * @author yuqs
 * @since 1.0
 */
public class TestQueryHistOrder extends TestSnakerBase {
	@Test
	public void test() {
		System.out.println(engine.query().getHistoryOrders(
				new QueryFilter().setCreateTimeStart("2014-01-01").setName("simple").setState(0).setProcessType("预算管理流程1")));
		System.out.println(engine.query().getHistoryOrders(new Page<HistoryOrder>(), new QueryFilter()));
	}
}
