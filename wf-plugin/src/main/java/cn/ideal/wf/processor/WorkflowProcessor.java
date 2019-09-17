package cn.ideal.wf.processor;
/**
 * 流程的推进不依赖节点的选择
 * 主动寻找预先配置的流程节
 */
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.action.Action;
import cn.ideal.wf.action.PassAction;
import cn.ideal.wf.action.Utils;
import cn.ideal.wf.cache.factory.TableBriefCacheFactory;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.impl.JdbcSQLExecutor;

@Service
public class WorkflowProcessor extends Utils implements Processor {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowProcessor.class);

	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowBriefService workflowBriefService;
    @Autowired
    private WorkflowNodeService workflowNodeService;
    @Autowired
    private WorkflowTableService workflowTableService;
    @Autowired
    private ApplicationContext appContext;
    @Autowired
	private JdbcSQLExecutor jdbcSQLExecutor;
    @Autowired
	private TableBriefCacheFactory tableBriefCacheFactory;
    
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
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doAction(Long tbId, Long bizId, WorkflowUser wfu) throws Exception {
		LOGGER.info("================流程推进===============");
		boolean res = true;
		WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);		
		WorkflowTableBrief wftb = workflowTableService.findDefinationTableBrief(tbId, wftbSummary.getWfId());
		if(wftb == null) wftb = workflowTableService.find(tbId);
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wftb.getWfId());
		WorkflowAction action = new WorkflowAction();
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wftb.getWfId(), WFConstants.WF_NODE_START,wfu);	
			action.setActionCodeName("PassAction");			
		}else{
			WorkflowNode curNode = workflowNodeService.findNode(wfb.getNodeName(), wftb.getWfId());
			action = curNode.getAction();
		}
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();
		if(("dev").equals(userScope)){
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(wfu);
		}		
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof Action){
			Action wfa = (Action)obj;
			res = wfa.action(bizId, wftb.getWfId(), wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}

	
	/**
	 * 由外部传入续办节点
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doAction(Long tbId, Long bizId, WorkflowUser wfu,Long nodeId) throws Exception {
		boolean res = true;
		WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);
		WorkflowTableBrief wftb = workflowTableService.findDefinationTableBrief(tbId, wftbSummary.getWfId());
		if(wftb == null) wftb = workflowTableService.find(tbId);
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wftb.getWfId());
		WorkflowNode node = workflowNodeService.findNode(nodeId);
		WorkflowAction action = null;
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wftb.getWfId(), WFConstants.WF_NODE_START,wfu);				
		}
		
		action = new WorkflowAction();
		action.setActionCodeName("PassAction");
		//支持平台演示和实际运用两种方式获得续办用户
		WorkflowUser[] wfuAry = null;
		if(("dev").equals(userScope)){			
			List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(wfu);
			wfuAry = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}		
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof PassAction){
			PassAction wfa = (PassAction)obj;
			res = wfa.action(bizId, wftb.getWfId(), wfu,node, wfuAry);
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}

	/**
	 * 由外部传入续办节点和节点办理人
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doAction(Long tbId, Long bizId, WorkflowUser wfu,Long nodeId, WorkflowUser... nextWfu) throws Exception {
		boolean res = true;
		WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);
		WorkflowTableBrief wftb = workflowTableService.findDefinationTableBrief(tbId, wftbSummary.getWfId());
		if(wftb == null) wftb = workflowTableService.find(tbId);
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wftb.getWfId());
		WorkflowNode node = workflowNodeService.findNode(nodeId);
		WorkflowAction action = null;
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wftb.getWfId(), WFConstants.WF_NODE_START,wfu);				
		}
		
		action = new WorkflowAction();
		action.setActionCodeName("PassAction");
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = null;
		if(("dev").equals(userScope)){			
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(wfu);
			nextWfu = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof PassAction){
			PassAction wfa = (PassAction)obj;
			res = wfa.action(bizId, wftb.getWfId(), wfu,node, nextWfu);
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}


	@Override
	public String findNodeName(Long wfId, Long bizId,WorkflowUser wfu) {
		if(bizId == null) return WFConstants.WF_NODE_STRAT;
		//流程不存在仅作保存动作
		if(wfId == null) return WFConstants.WF_NODE_STRAT;
		WorkflowBrief wfb = workflowBriefService.find(bizId,wfId);
		if(wfb == null) return WFConstants.WF_NODE_STRAT;
		else if(wfb.getFinishedDate() != null) return null;
		else if(wfu != null && !wfb.getDispatchUserId().contains(wfu.getUserId()+",")) return null;
		return wfb.getNodeName();
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doButton(Long tbId, Long bizId, WorkflowUser wfu,String buttonName) throws Exception {
		boolean res = true;
		WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);
		WorkflowTableBrief wftb = workflowTableService.findDefinationTableBrief(tbId, wftbSummary.getWfId());
		if(wftb == null) wftb = workflowTableService.find(tbId);
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wftb.getWfId());
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			throw new Exception("流程操作失败");
		}
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();
		if(("dev").equals(userScope)){				
			wfuLst.add(wfu);
		}		
		Object obj = appContext.getBean(buttonName);
		if(obj instanceof Action){
			Action wfa = (Action)obj;
			res = wfa.action(bizId, wftb.getWfId(), wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}	
			
		return res;
	}


}
