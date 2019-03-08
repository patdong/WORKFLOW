package cn.ideal.wf.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.ideal.wf.App;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class NodeServiceTest extends TestCase{

	@Autowired
	private WorkflowNodeService nodeService;
	@Test
	public void testTree() {
			
	}
}
