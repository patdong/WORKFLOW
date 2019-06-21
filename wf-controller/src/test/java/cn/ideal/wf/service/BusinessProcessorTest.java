package cn.ideal.wf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.ideal.wf.App;
import cn.ideal.wf.model.WorkflowRole;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.processor.BusinessProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class BusinessProcessorTest extends TestCase{

	@Autowired
	private BusinessProcessor businessProcessor;
	
	@Test
	public void createUserDefinationTest() {
		HttpServletRequest request = null;
		Long tbId = 5l;
		Map<Long,List<WorkflowUser>> users = new HashMap<Long, List<WorkflowUser>>();		
		Map<Long,WorkflowRole> roles = new HashMap<Long,WorkflowRole>();
		
		List<WorkflowUser> wfus = new ArrayList<WorkflowUser>();
		WorkflowUser user = new WorkflowUser();
		user.setUnitId(1l);
		user.setUnitName("系统");
		user.setUserId(1l);
		user.setUserName("admin");
		wfus.add(user);
		users.put(1l, wfus);
		
		wfus = new ArrayList<WorkflowUser>();
		user = new WorkflowUser();
		user.setUnitId(1l);
		user.setUnitName("系统");
		user.setUserId(2l);
		user.setUserName("郭佟燕");
		wfus.add(user);
		users.put(2l, wfus);
		
		businessProcessor.createUserDefination(request, tbId, users, roles);
	}
}
