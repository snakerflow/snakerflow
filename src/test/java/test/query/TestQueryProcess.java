package test.query;

import org.junit.Test;
import org.snaker.engine.access.Page;
import org.snaker.engine.entity.Process;
import org.snaker.engine.test.TestSnakerBase;

/**
 * 流程定义查询测试
 * @author yuqs
 * @version 1.0
 */
public class TestQueryProcess extends TestSnakerBase {
	@Test
	public void test() {
		Page<Process> page = new Page<Process>();
		engine.process().getProcesss(page, null, null);
		System.out.println(page);
	}
}
