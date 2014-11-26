package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Process;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程定义查询测试
 * @author yuqs
 * @since 1.0
 */
public class TestQueryProcess extends TestSnakerBase {
	@Test
	public void test() {
		System.out.println(engine.process().getProcesss(null));
		System.out.println(engine.process().getProcesss(new Page<Process>(), 
				new QueryFilter().setName("subprocess1")));
		System.out.println(engine.process().getProcessByVersion("subprocess1", 0));
		System.out.println(engine.process().getProcessByName("subprocess1"));
	}
}
