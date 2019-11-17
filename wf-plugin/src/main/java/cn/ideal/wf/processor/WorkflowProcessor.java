package cn.ideal.wf.processor;
/**
 * 流程的推进不依赖节点的选择
 * 主动寻找预先配置的流程节
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.action.Utils;
import cn.ideal.wf.answeringaction.Answer;
import cn.ideal.wf.cache.factory.TableBriefCacheFactory;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.notification.Note;
import cn.ideal.wf.notification.Notifier;
import cn.ideal.wf.service.WorkflowAnswerService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowStepService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.impl.JdbcSQLExecutor;

import com.alibaba.druid.util.StringUtils;

@Service
public class WorkflowProcessor extends Utils implements Processor {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowProcessor.class);

	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowStepService workflowStepService;
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
    @Autowired
    private WorkflowAnswerService workflowAnswerService;
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
	public boolean doAction(Long tbId, Long bizId, Long wfId,WorkflowUser wfu, Notifier notifier) throws Exception {
		LOGGER.info("================流程推进===============");
		boolean res = true;			
		WorkflowTableBrief wftb = workflowTableService.findByIds(tbId, wfId);
		if(wftb == null) wftb = workflowTableService.findDefinationTableBrief(tbId, wfId);
		
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowAction action = new WorkflowAction();
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);	
			action.setActionCodeName("PassAction");			
		}else{			
			WorkflowNode curNode = workflowNodeService.findNode(wfb.getNodeName(), wfId);	
			
			//如果当前节点已经办理完毕，则流程推进否则继续处理本节点
			WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);	
			boolean isPush = this.pushFlow(wfb, wftbSummary, curNode, wfu,notifier);
			if(!isPush) return true;
			
			action = curNode.getAction();
		}
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();
		if(("dev").equals(userScope)){			
			wfuLst.add(wfu);
		}		
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof cn.ideal.wf.action.Action){
			cn.ideal.wf.action.Action wfa = (cn.ideal.wf.action.Action)obj;
			res = wfa.action(bizId, wfId, wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));						
			if(!res) throw new Exception("流程推进失败");			
		}
		
		this.doNotification(notifier, tbId, bizId,wfu);	
		return res;
	}

	
	/**
	 * 由外部传入续办节点
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doAction(Long tbId, Long bizId, Long wfId, WorkflowUser wfu,Long nodeId, Notifier notifier) throws Exception {
		LOGGER.info("================由外部传入续办节点流程推进===============");
		boolean res = true;		
		WorkflowTableBrief wftb = workflowTableService.findByIds(tbId, wfId);
		if(wftb == null) wftb = workflowTableService.findDefinationTableBrief(tbId, wfId);
		
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowNode node = workflowNodeService.findNode(nodeId);
		WorkflowAction action = null;
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START,wfu);				
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
		if(obj instanceof cn.ideal.wf.action.PassAction){
			cn.ideal.wf.action.PassAction wfa = (cn.ideal.wf.action.PassAction)obj;
			res = wfa.action(bizId, wfId, wfu,node, wfuAry);
			if(!res) throw new Exception("流程推进失败");			
		}				
		this.doNotification(notifier, tbId, bizId, wfu);
		
		return res;
	}

	/**
	 * 由外部传入续办节点和节点办理人
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doAction(Long tbId, Long bizId, Long wfId, WorkflowUser wfu,Long nodeId, Notifier notifier, WorkflowUser... nextWfu) throws Exception {
		LOGGER.info("================由外部传入续办节点和节点办理人流程推进===============");
		boolean res = true;		
		WorkflowTableBrief wftb = workflowTableService.findByIds(tbId, wfId);
		if(wftb == null) wftb = workflowTableService.findDefinationTableBrief(tbId, wfId);
		
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		WorkflowNode node = workflowNodeService.findNode(nodeId);
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
			wfuLst = new ArrayList<WorkflowUser>();
			wfuLst.add(wfu);
			nextWfu = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}
		Object obj = appContext.getBean(action.getActionCodeName());
		if(obj instanceof cn.ideal.wf.action.PassAction){
			cn.ideal.wf.action.PassAction wfa = (cn.ideal.wf.action.PassAction)obj;
			res = wfa.action(bizId, wfId, wfu,node, nextWfu);
			if(!res) throw new Exception("流程推进失败");			
		}	
		this.doNotification(notifier, tbId, bizId,wfu);	
		return res;
	}

	/**
	 * 行为类操作
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public boolean doButton(Long tbId, Long bizId,Long wfId, WorkflowUser wfu,String buttonName, Notifier notifier) throws Exception {
		LOGGER.info("================行为类操作流程推进===============");
		boolean res = true;		
		WorkflowTableBrief wftb = workflowTableService.findByIds(tbId, wfId);
		if(wftb == null) wftb = workflowTableService.findDefinationTableBrief(tbId, wfId);
		
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
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
		if(obj instanceof cn.ideal.wf.action.Action){
			cn.ideal.wf.action.Action wfa = (cn.ideal.wf.action.Action)obj;
			res = wfa.action(bizId, wfId, wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
		}
			
		return res;
	}
	
	/**
	 * 行为消息类操作
	 */
	@Override
	public boolean doButton(Long tbId, Long bizId, Long wfId,WorkflowUser wfu,String buttonName, String msg, Notifier notifier) throws Exception {
		LOGGER.info("================行为消息类流程推进===============");
		boolean res = true;		
		WorkflowTableBrief wftb = workflowTableService.findByIds(tbId, wfId);
		if(wftb == null) wftb = workflowTableService.findDefinationTableBrief(tbId, wfId);
		
		//判断是否有审批意见需要迁移
		jdbcSQLExecutor.migrateComments(bizId, tbId, wftb.getName(), wfu);
		
		WorkflowBrief wfb = workflowBriefService.findDoingFlow(bizId,wfId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wfb == null){
			throw new Exception("流程操作失败");
		}
		WorkflowStep wfs = workflowStepService.findDoingflowStep(bizId, wfId, wfu.getUserId());
		
		//支持平台演示和实际运用两种方式获得续办用户
		List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();
		if(("dev").equals(userScope)){				
			wfuLst.add(wfu);
		}		
		Object obj = appContext.getBean(buttonName);
		if(obj instanceof cn.ideal.wf.action.Action){
			cn.ideal.wf.action.Action wfa = (cn.ideal.wf.action.Action)obj;
			res = wfa.action(bizId, wfId, wfu, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			if(!res) throw new Exception("流程推进失败");
			else{
				//将消息插入推进者记录中
				workflowStepService.pushMsg(wfs.getStepId(), msg);
			}
		}
			
		return res;
	}

	
	/**
	 * 应答类操作
	 */
	@Override
	public boolean doAnswer(Answer answer, WorkflowUser wfu,String buttonName,String mode) throws Exception {
		LOGGER.info("================应答类操作===============");
		boolean res = true;
		Object obj = appContext.getBean(buttonName);
		if(obj instanceof cn.ideal.wf.answeringaction.DispenseAction){
			cn.ideal.wf.answeringaction.Action wfa = (cn.ideal.wf.answeringaction.DispenseAction)obj;
			switch (mode){
			case WFConstants.WF_MSG_DISPENSE:	
				res = wfa.dispense(answer,wfu);
				break;
			case WFConstants.WF_MSG_RESPONSE:
				res = wfa.response(answer, wfu);
				break;
			}
			if(!res) throw new Exception("应答类操作失败");
		}
		
		return res;
	}
	
	@Override
	public Map<String,String> findCurrentNode(Long wfId, Long bizId,WorkflowUser wfu) {
		Map<String,String> node = new HashMap<String,String>();
		//常规推进方式
		node.put("flowpush", "push");
		if(bizId == null) {
			node.put("nodeName", WFConstants.WF_NODE_STRAT);
			node.put("lastUser", Boolean.TRUE.toString());
			return node;
		}
		//流程不存在仅作保存动作
		if(wfId == null) {
			node.put("nodeName", WFConstants.WF_NODE_STRAT);
			node.put("lastUser", Boolean.TRUE.toString());
			return node;
		}
		WorkflowBrief wfb = workflowBriefService.find(bizId,wfId);
		if(wfb == null) {
			node.put("nodeName", WFConstants.WF_NODE_STRAT);
			node.put("lastUser", Boolean.TRUE.toString());
			return node;
		}
		else if(wfb.getFinishedDate() != null) return node;
		else if(wfu != null && !wfb.getDispatchUserId().contains(wfu.getUserId()+",")) return node;
		node.put("nodeName", wfb.getNodeName());
		//判断当前节点的办理人是不是最后一个办理人
		WorkflowNode curNode = workflowNodeService.findNode(wfb.getNodeName(), wfId);	
		switch(curNode.getnType()){
		case WFConstants.WF_NODE_TYPE_SINGLE:
			node.put("lastUser", Boolean.TRUE.toString());
			break;
		case WFConstants.WF_NODE_TYPE_SERIAL:
			List<WorkflowStep> steps = workflowStepService.findUNFinshedWrokflowStep(wfb.getBizId(), wfb.getWfId());
			if(steps.size() > 1) node.put("lastUser", Boolean.FALSE.toString());
			else node.put("lastUser", Boolean.TRUE.toString());
			break;
		case WFConstants.WF_NODE_TYPE_PARALLEL:
			int i=0;
			for(String userId : wfb.getDispatchUserId().split(",")){
				if(StringUtils.isEmpty(userId)) continue;
				else i++;
			}
			if(i > 1) node.put("lastUser", Boolean.FALSE.toString());
			else node.put("lastUser", Boolean.TRUE.toString());
			break;
		}
		
		//获取节点的推进方式
		if(curNode.getAction() != null){
			//自动处理方式
			if(curNode.getAction().getActionCodeName().toLowerCase().contains("auto")){
				node.put("flowpush", "auto");
			}
		}
		
		return node;
	}

	/**
	 * 处理是否需要推进流程
	 * @return
	 */
	private boolean pushFlow(WorkflowBrief wfb,WorkflowTableSummary wftbSummary, WorkflowNode curNode, WorkflowUser wfu,Notifier notifier) throws Exception{
		String userId = null;
		List<WorkflowStep> steps = null;
		switch(curNode.getnType()){
		case WFConstants.WF_NODE_TYPE_SINGLE:
			return true;
		case WFConstants.WF_NODE_TYPE_SERIAL:			
			userId = wfb.getDispatchUserId().substring(1,wfb.getDispatchUserId().length()-1);
			steps = workflowStepService.findUNFinshedWrokflowStep(wfb.getBizId(), wfb.getWfId());
			if(steps.size() > 1){
				try{
					WorkflowStep curStep = null,nextStep = null;
					Long i=0l;
					for(WorkflowStep step : steps){							
						if(userId.equals(step.getDispatchUserId().toString())) {
							curStep = step;
							i=step.getSerial()+1;
						}
						if(i.compareTo(step.getSerial()) == 0){
							nextStep = step;
						}
					}								
					//结束当前办理人
					workflowStepService.endFlowStep(curStep.getStepId(),wfu);
					//唤醒下一个办理人
					workflowStepService.wakeFlowStep(nextStep.getStepId());
					//更新流程概述
					wfb.setOldDispatchUserId("," + curStep.getDispatchUserId() + ",");
					wfb.setDispatchUserId("," + nextStep.getDispatchUserId() + ",");
					wfb.setStepId(nextStep.getStepId());
					wfb.setModifiedDate(new Date());
					wfb.setUnitId(nextStep.getUnitId());
					workflowBriefService.updateFlowBrief(wfb);
					//更新业务概述
					wftbSummary.setModifiedDate(wfb.getModifiedDate());
					wftbSummary.setCurUserId(","+nextStep.getDispatchUserId()+",");
					wftbSummary.setCurUserName(","+nextStep.getDispatchUserName()+",");
					workflowTableService.synchTableSummary(wftbSummary);
					WorkflowUser nextWfu = new WorkflowUser();
					nextWfu.setUserId(nextStep.getDispatchUserId());
					nextWfu.setUserName(nextStep.getDispatchUserName());
					nextWfu.setUnitId(nextStep.getUnitId());
					nextWfu.setUnitName(nextStep.getUnitName());
					this.doNotification(notifier, wftbSummary.getTbId(), wftbSummary.getBizId(), nextWfu);
					return false;
				}catch(Exception e){
					throw new Exception("串行流程处理异常：" + e.getMessage());
				}
				
			}else {
				return true;
			}			
		case WFConstants.WF_NODE_TYPE_PARALLEL:
			userId = wfu.getUserId().toString();
			steps = workflowStepService.findUNFinshedWrokflowStep(wfb.getBizId(), wfb.getWfId());
			if(steps.size() > 1){
				try{
					WorkflowStep curStep = null;				
					for(WorkflowStep step : steps){					
						if(userId.equals(step.getDispatchUserId().toString())) {
							curStep = step;
							break;
						}						
					}									
					//结束当前办理人
					workflowStepService.endFlowStep(curStep.getStepId(),wfu);					
					//更新流程概述
					wfb.setOldDispatchUserId("," + curStep.getDispatchUserId() + ",");
					wfb.setDispatchUserId(wfb.getDispatchUserId().replace("," + curStep.getDispatchUserId() + ",", ","));					
					wfb.setModifiedDate(new Date());					
					workflowBriefService.updateFlowBrief(wfb);
					//更新业务概述
					wftbSummary.setModifiedDate(wfb.getModifiedDate());
					wftbSummary.setCurUserId(wftbSummary.getCurUserId().replace(","+curStep.getDispatchUserId()+",",","));
					wftbSummary.setCurUserName(wftbSummary.getCurUserId().replace(","+curStep.getDispatchUserName()+",",","));
					workflowTableService.synchTableSummary(wftbSummary);
					return false;
				}catch(Exception e){
					throw new Exception("并行流程处理异常：" + e.getMessage());
				}
				
			}else {
				return true;
			}				
		}
		
		return true;
	}
	
	/**
	 * 发送通知
	 * @param notifier
	 * @param bizId
	 * @param tbId
	 * @param wfId
	 * @param sender
	 */
	private void doNotification(Notifier notifier,Long tbId, Long bizId,WorkflowUser sender){
		if(notifier != null){
			WorkflowTableSummary wftbSummary = workflowTableService.findTableSummary(tbId, bizId);
			Note note = new Note();
			note.setSummaryId(wftbSummary.getSummaryId());
			note.setBizId(bizId);
			note.setTbId(tbId);
			note.setCreatedDate(new Date());
			note.setSender(sender);
			List<WorkflowStep> steps = workflowStepService.findDoingflowSteps(bizId, wftbSummary.getWfId());
			List<WorkflowUser> receivers = new LinkedList<WorkflowUser>();
			for(WorkflowStep step : steps){
				WorkflowUser user = new WorkflowUser();
				user.setUserId(step.getDispatchUserId());
				user.setUserName(step.getDispatchUserName());
				user.setUnitId(step.getUnitId());
				user.setUnitName(step.getUnitName());
				receivers.add(user);
				note.setNodeName(step.getNodeName());
			}
			note.setReceiver(receivers);
			note.setContent(wftbSummary.getTitle());
			notifier.notify(note);
		}
	}


	
}
