package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.WorkItem;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程实例查询测试
 * @author yuqs
 * @version 1.0
 */
public class TestQueryCCOrder extends TestSnakerBase {
	@Test
	public void test() {
		Page<WorkItem> page = new Page<WorkItem>();
		System.out.println(engine.query().getCCWorks(page, new QueryFilter().setState(1)));
	}
}
