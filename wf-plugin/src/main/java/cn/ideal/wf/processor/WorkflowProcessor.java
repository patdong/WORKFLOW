package cn.ideal.wf.processor;
/**
 * 流程的推进不依赖节点的选择
 * 主动寻找预先配置的流程节
 */
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.action.Action;
import cn.ideal.wf.action.PassAction;
import cn.ideal.wf.action.Utils;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;

@Service
public class WorkflowProcessor extends Utils implements Processor {
	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowBriefService workflowBriefService;
    @Autowired
    private WorkflowNodeService workflowNodeService;
    @Autowired
    private WorkflowTableService workflowTableService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private ApplicationContext appContext;
   
    
    @Value("${workflow.user.scope}")
    private String userScope;
    /**
     * 流程推进
     * 1.当前节点无后续节点，流程结束
     * 2.当前节点的后续节点是用户类型的，发送到用户
     * 3.当前节点的后续节点是角色类型的，发送的指定单位下的角色
     * 4.更新业务附表的行为
     */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean doAction(Long wfId, Long bizId, WorkflowUser wfu) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowAction action = new WorkflowAction();
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);	
			action.setActionCodeName("PassAction");			
		}else{
			WorkflowNode curNode = workflowNodeService.findNode(wfb.getNodeName(), wfId);
			action = curNode.getAction();
		}
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = null;
		if(("dev").equals(userScope)){
			WorkflowUser user = new WorkflowUser(1l,1l);
			user.setUserName("admin");
			user.setUnitName("平台");
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(user);
		}		
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof Action){
			Action wfa = (Action)obj;
			res = wfa.action(bizId, wfId, wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}

	
	/**
	 * 由外部传入续办节点
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean doAction(Long wfId, Long bizId, WorkflowUser wfu,WorkflowNode node) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowAction action = null;
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);				
		}
		
		action = new WorkflowAction();
		action.setActionCodeName("PassAction");
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = null;
		if(("dev").equals(userScope)){
			WorkflowUser user = new WorkflowUser(1l,1l);
			user.setUserName("admin");
			user.setUnitName("平台");
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(user);
		}		
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof PassAction){
			PassAction wfa = (PassAction)obj;
			res = wfa.action(bizId, wfId, wfu,node, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}

	/**
	 * 由外部传入续办节点和节点办理人
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean doAction(Long wfId, Long bizId, WorkflowUser wfu,WorkflowNode node, WorkflowUser... nextWfu) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowAction action = null;
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);				
		}
		
		action = new WorkflowAction();
		action.setActionCodeName("PassAction");
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = null;
		if(("dev").equals(userScope)){
			WorkflowUser user = new WorkflowUser(1l,1l);
			user.setUserName("admin");
			user.setUnitName("平台");
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(user);
			nextWfu = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof PassAction){
			PassAction wfa = (PassAction)obj;
			res = wfa.action(bizId, wfId, wfu,node, nextWfu);
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}


	@Override
	public String findNodeName(Long wfId, Long bizId) {
		WorkflowBrief wfb = workflowBriefService.find(bizId,wfId);
		if(wfb == null) return WFConstants.WF_NODE_STRAT;
		else if(wfb.getFinishedDate() != null) return null;
		return wfb.getNodeName();
	}


	@Override
	public boolean doButton(Long wfId, Long bizId, WorkflowUser wfu,String buttonName) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			throw new Exception("流程操作失败");
		}
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = null;
		if(("dev").equals(userScope)){
			WorkflowUser user = new WorkflowUser(1l,1l);
			user.setUserName("admin");
			user.setUnitName("平台");
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(user);
		}		
		Object obj = appContext.getBean(buttonName);
		if(obj instanceof Action){
			Action wfa = (Action)obj;
			res = wfa.action(bizId, wfId, wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}


}
