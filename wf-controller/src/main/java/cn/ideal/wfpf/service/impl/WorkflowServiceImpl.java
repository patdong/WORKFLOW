package cn.ideal.wfpf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wfpf.model.WFPFWorkflow;
import cn.ideal.wfpf.dao.WorkflowMapper;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService{

	@Autowired
	private WorkflowMapper workflowMapper;
	
	@Override
	public WFPFWorkflow save(WFPFWorkflow obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		int idx = workflowMapper.save(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	public WFPFWorkflow update(WFPFWorkflow obj) {
		//重名判断
		if(obj.getWfName() != null){
			List<WFPFWorkflow> wfpfs = workflowMapper.findByWFName(obj.getWfId(),obj.getWfName());
			if(wfpfs.size() > 0) return null;	
		}
		int idx = workflowMapper.update(obj);
		if(idx > 0) return obj;
		return null;
	}	

	@Override
	public WFPFWorkflow find(Long key) {
		return workflowMapper.find(key);
	}	

	@Override
	public List<WFPFWorkflow> findAll() {
		return workflowMapper.findAll();
	}

	@Override
	public List<WFPFWorkflow> findAll(Page<WFPFWorkflow> page) {
		return workflowMapper.findAPage(page.getCurFirstRecord(),page.getCurLastRecord(),Page.pageSize);
	}

	@Override
	public List<WFPFWorkflow> findAllBlindTable() {
		return workflowMapper.findAllBlindTable();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean removeBinding(Long wfId) {
		int idx = workflowMapper.removeBinding(wfId);
		if(idx == 1) {
			workflowMapper.deleteTableElementOnNode(wfId);
			return true;
		}
		return false;
	}

	@Override
	public boolean setStatus(Long wfId, boolean status) {
		WFPFWorkflow wf = new WFPFWorkflow();
		wf.setWfId(wfId);
		if(status) wf.setStatus("有效");
		else wf.setStatus("无效");
		int idx = workflowMapper.update(wf);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean delete(Long wfId) {
		workflowMapper.delete(wfId);
		return true;
	}
}
