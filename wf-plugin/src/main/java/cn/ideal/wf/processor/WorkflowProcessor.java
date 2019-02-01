package cn.ideal.wf.processor;
/**
 * 流程的推进不依赖节点的选择
 * 主动寻找预先配置的流程节
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.action.Action;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;

@Service
public class WorkflowProcessor implements Processor {
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
    @Autowired
    @Qualifier("TerminateAction")
    private Action terminateAction;
    @Autowired
    @Qualifier("EndAction")
    private Action endAction;
    @Autowired
    @Qualifier("CallbackAction")
    private Action callbackAction;
    @Autowired
    @Qualifier("PostPhoneAction")
    private Action postPhoneAction;
    @Autowired
    @Qualifier("DispatchAction")
    private Action dispatchAction;
    @Autowired
    @Qualifier("ReturnAction")
    private Action returnAction;
    
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
	public boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);			
		}		
		
		WorkflowNode nextNode = this.findNextNode(wfId,bizId);
		if(nextNode == null) {
			res =this.endFlow(bizId,wfId);
			if(!res) throw new Exception("流程办结失败");						
		}else{
			List<WorkflowUser> wfuLst = null;
			//支持平台演示和实际运用两种方式获得续办用户
			if(("dev").equals(userScope)){
				WorkflowUser user = new WorkflowUser(1l,1l);
				user.setUserName("admin");
				user.setUnitName("平台");
				wfuLst = new ArrayList<WorkflowUser>();
				wfuLst.add(user);
			}else{
				wfuLst =  this.findUsersForNode(nextNode);
			}
			res = this.pushFlow(bizId, wfId,nextNode, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));	
			if(!res) throw new Exception("流程创建失败");					
		}		
				
		return res;
	}

	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu,
			WorkflowNode node) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);			
		}		
					
		if(node == null) {
			res = this.endFlow(bizId,wfId);
			if(!res) throw new Exception("流程办结失败");						
		}else{
			List<WorkflowUser> wfuLst =  this.findUsersForNode(node);
			res = this.pushFlow(bizId,wfId, node, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));	
			if(!res) throw new Exception("没有办理人无法创建流程");							
		}		
				
		return res;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu,
			WorkflowNode node, WorkflowUser... nextWfu) throws Exception {
		boolean res = true;
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);			
		}		
				
		if(node == null) {
			res = this.endFlow(bizId,wfId);
			if(!res) throw new Exception("流程办结失败");	
		}else{
			res = this.pushFlow(bizId,wfId, node, nextWfu);	
			if(!res) throw new Exception("没有办理人无法创建流程");						
		}		
				
		return res;
	}

	@Override
	public List<WorkflowUser> findUsersForNode(WorkflowNode node) throws Exception{
		if(node == null) return null;
		List<WorkflowUser> wfuLst = null;
			
		switch(node.getuType()){			
			case WFConstants.WF_NODE_TYPE_USER:
				if(node.getUsers() == null)  throw new Exception("没有办理人无法创建流程");	
				wfuLst = new ArrayList<WorkflowUser>(node.getUsers());									
				break;
			case WFConstants.WF_NODE_TYPE_ROLE:
				if(node.getRole() == null) throw new Exception("没有设置角色无法创建流程");
				wfuLst = platformService.findUsersByRoleIdAndOrgId(node.getRole().getRoleId(), node.getRole().getUnitId());												
				break;
		}
		
		return wfuLst;
	}

	@Override
	public WorkflowNode findNextNode(Long wfId, Long bizId) {
		WorkflowFlow wf = workflowFlowService.findDoingFlow(bizId,wfId);
		List<WorkflowNode> wfns = workflowNodeService.findNextNodes(wf.getNodeName(), wf.getWfId());
		if(wfns.size() > 0) return wfns.get(0);
		return null;
	}
	
	private boolean endFlow(Long bizId,Long wfId){		
		boolean res = workflowFlowService.endFlow(bizId,wfId);
		if(res){
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			wfts.setModifiedDate(new Date());
			wfts.setFinishedDate(new Date());
			wfts.setAction(WFConstants.WF_ACTION_END);
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			workflowTableService.endTableSummary(wfts);
			return true;
		}
		
		return false;
	}
	
	private boolean pushFlow(Long bizId, Long wfId,WorkflowNode node, WorkflowUser ...nextWfu) throws Exception{
		boolean res = workflowFlowService.endAndAddFlow(bizId,wfId,node,nextWfu);
		if(res){
			String curUserName = "";
			for(WorkflowUser user : nextWfu) curUserName += user.getUserName() + ",";	
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			wfts.setCurUserName(curUserName);
			wfts.setModifiedDate(new Date());
			//任何动作都反应在action字段上
			wfts.setAction(node.getNodeName());	
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			res = workflowTableService.synchTableSummary(wfts);
			return res;
		}
		return false;
	}

	@Override
	public boolean actionPass(Long wfId, Long bizId, WorkflowUser wfu,
			String action) throws Exception {
		Object obj = appContext.getBean(action);
		if(obj instanceof Action){
			return ((Action)obj).action(bizId,wfId, wfu);
		}
		
		return false;
	}

	@Override
	public boolean actionPass(Long wfId, Long bizId, WorkflowUser wfu,
			String action, WorkflowUser... users) throws Exception {
		Object obj = appContext.getBean(action);
		if(obj instanceof Action){
			return ((Action)obj).action(bizId,wfId, wfu, users);
		}
		
		return false;
	}



	@Override
	public String findNodeName(Long wfId, Long bizId) {
		WorkflowBrief wfb = workflowBriefService.find(bizId,wfId);
		if(wfb == null) return WFConstants.WF_NODE_STRAT;
		else if(wfb.getFinishedDate() != null) return null;
		return wfb.getNodeName();
	}
}
