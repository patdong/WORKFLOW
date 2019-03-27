package cn.ideal.wfpf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wfpf.dao.WorkflowMapper;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService{

	@Autowired
	private WorkflowMapper workflowMapper;
	
	@Override
	public Workflow save(Workflow obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		int idx = workflowMapper.save(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	public Workflow update(Workflow obj) {
		int idx = workflowMapper.update(obj);
		if(idx > 0) return obj;
		return null;
	}	

	@Override
	public Workflow find(Long key) {
		return workflowMapper.find(key);
	}	

	@Override
	public List<Workflow> findAll() {
		return workflowMapper.findAll();
	}

	@Override
	public List<Workflow> findAll(Page<Workflow> page) {
		return workflowMapper.findAPage(page.getCurFirstRecord(),page.getCurLastRecord(),Page.pageSize);
	}

	@Override
	public List<Workflow> findAllBlindTable() {
		return workflowMapper.findAllBlindTable();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public Workflow removeBinding(Workflow obj) {
		int idx = workflowMapper.removeBinding(obj);
		if(idx > 0) {
			workflowMapper.deleteTableElementOnNode(obj.getWfId());
			return this.find(obj.getWfId());
		}
		return null;
	}

	@Override
	public boolean setStatus(Long wfId, boolean status) {
		Workflow wf = new Workflow();
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
