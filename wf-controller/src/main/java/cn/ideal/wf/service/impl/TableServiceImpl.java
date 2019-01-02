package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.ideal.wf.dao.ElementMapper;
import cn.ideal.wf.dao.TableMapper;
import cn.ideal.wf.model.Element;
import cn.ideal.wf.model.Page;
import cn.ideal.wf.model.TableBrief;
import cn.ideal.wf.model.TableElement;
import cn.ideal.wf.service.TableService;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private TableMapper tableMapper;
	@Autowired
	private ElementMapper elementMapper;
	
	
	@Override
	public TableBrief saveTableBrief(TableBrief obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		int idx = tableMapper.saveTableBrief(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	public List<TableBrief> findAll() {
		return tableMapper.findAll();
	}

	@Override
	public List<TableBrief> findAll(Page<TableBrief> page) {
		return tableMapper.findAPage(page.getCurFirstRecord(),Page.pageSize);
	}

	@Override
	public TableBrief find(Long tbId) {
		return tableMapper.find(tbId);
	}

	@Override
	public TableElement saveTableElement(TableElement obj) {
		int idx = tableMapper.saveTableElement(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveTableElement(TableElement[] objs) {
		List<TableElement> teLst = new ArrayList<TableElement>();
		for(TableElement te : objs){
			TableElement item = this.findTableElement(te.getTbId(),te.getEmId());
			if(item == null) {
				Element em = elementMapper.find(te.getEmId());
				te.setNewLabelName(em.getLabelName());
				te.setNewFunctionName(em.getFunctionName());
				te.setNewHiddenFieldName(em.getHiddenFieldName());
				te.setNewDataContent(em.getDataContent());
				te.setNewFieldType(em.getFieldType());
				te.setNewFieldDataType(em.getFieldDataType());
				teLst.add(te);
			}
		}
		if(teLst.size() == 0) return false;
		int idx = tableMapper.saveBatchTableElement(teLst);		
		if(idx == 1) return true;
		return false;
	}

	@Override
	public List<TableElement> findAllTableElements(Long tbId) {
		return tableMapper.findAllTableElements(tbId,null);
	}

	@Override
	public TableElement findTableElement(Long tbId, Long emId) {
		return tableMapper.findTableElement(tbId, emId);
	}

	@Override
	public boolean moveUp(Long tbId, Long emId) {
		List<TableElement> teLst = tableMapper.findAllTableElements(tbId,null);
		int i=0;
		TableElement curTe = null;
		for(TableElement te : teLst){
			if(te.getEmId().equals(emId)) {
				curTe = te;
				break;
			}
			i++;
		}
		TableElement preTe = teLst.get(i-1);
		if(preTe != null && curTe != null){
			Long seq = curTe.getSeq();
			curTe.setSeq(preTe.getSeq());
			preTe.setSeq(seq);
			this.updateTableElementSeq(curTe);
			this.updateTableElementSeq(preTe);
		}
		return false;
	}

	@Override
	public boolean moveDown(Long tbId, Long emId) {
		List<TableElement> teLst = tableMapper.findAllTableElements(tbId,null);
		int i=0;
		TableElement curTe = null;
		for(TableElement te : teLst){
			if(te.getEmId().equals(emId)) {
				curTe = te;
				break;
			}
			i++;
		}
		TableElement sufTe = teLst.get(i+1);
		if(sufTe != null && curTe != null){
			Long seq = curTe.getSeq();
			curTe.setSeq(sufTe.getSeq());
			sufTe.setSeq(seq);
			this.updateTableElementSeq(curTe);
			this.updateTableElementSeq(sufTe);
		}
		return false;
	}

	@Override
	public boolean updateTableElementSeq(TableElement obj) {
		int idx = tableMapper.updateTableElementSeq(obj);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public List<TableElement> findAllTableElements(Long tbId, String scope) {
		if(StringUtils.isEmpty(scope)) scope = "body";
		return tableMapper.findAllTableElements(tbId, scope);
	}

	@Override
	public void delete(Long tbId, Long emId) {
		tableMapper.deleteTableElement(tbId, emId);
		
	}

	@Override
	public boolean setTableName(Long tbId,String tableName) {
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setTableName(tableName);
		int idx = tableMapper.updateTableBrief(tb);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public TableBrief updateTableBrief(TableBrief obj) {		
		int idx = tableMapper.updateTableBrief(obj);
		if(idx > 0) return this.find(obj.getTbId());
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean updateTableElementList(Long tbId, Long[] emIds) {
		tableMapper.resetTableElementList(tbId);
		int idx = tableMapper.updateTableElementList(tbId,emIds);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public List<TableElement> findTableList(Long tbId) {
		return tableMapper.findTableList(tbId);
	}

}
 