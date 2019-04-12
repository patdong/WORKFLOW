package cn.ideal.wfpf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.ideal.wfpf.dao.TableMapper;
import cn.ideal.wfpf.model.Element;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.model.TableLayout;
import cn.ideal.wfpf.service.ElementService;
import cn.ideal.wfpf.service.TableService;
import cn.ideal.wfpf.service.WorkflowService;
import cn.ideal.wfpf.sqlengine.SQLExecutor;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private TableMapper tableMapper;
	@Autowired
	private ElementService elementService;
	@Autowired
	private WorkflowService wfService;
	
	@Value("${workflow.wfpf.database.executor}")
    String executorName;
		
	private SQLExecutor sqlExecutor;
	
	@Autowired
    public void setSQLExecutor(ApplicationContext context) {
		sqlExecutor = (SQLExecutor) context.getBean(executorName);
    }

	
	@Override
	public TableBrief saveTableBrief(TableBrief obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		int idx = tableMapper.saveTableBrief(obj);
		if(idx == 1) {
			TableLayout tl = new TableLayout();
			tl.setTbId(obj.getTbId());
			tl.setCols(2l);
			tl.setScope("表体");
			idx = tableMapper.saveTableLayout(tl);
			if(idx == 1) return obj;
		}
		return null;
	}

	@Override
	public List<TableBrief> findAll() {
		return tableMapper.findAll();
	}

	@Override
	public List<TableBrief> findAll(Page<TableBrief> page) {
		return tableMapper.findAPage(page.getCurFirstRecord(),page.getCurLastRecord(),Page.pageSize);
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
		if(objs == null || objs.length == 0) return true;
		Long seq = tableMapper.findMaxSeq(objs[0].getTbId());
		if(seq == null) seq = 0l;
		
		for(TableElement te : objs){
			TableElement item = this.findTableElement(te.getTbId(),te.getEmId());
			if(item == null) {
				Element em = elementService.find(te.getEmId());
				//标签
				TableElement label = new TableElement();
				label.setTbId(te.getTbId());
				label.setNewLabelName(em.getLabelName());				
				label.setNewFieldType("标签");
				label.setScope(te.getScope());
				label.setList("无效");
				label.setSeq(++seq);
				label.setCreatedDate(new Date());
				teLst.add(label);
				//字段
				te.setNewLabelName(em.getLabelName());
				te.setNewFunctionName(em.getFunctionName());
				te.setNewHiddenFieldName(em.getHiddenFieldName());
				te.setNewDataContent(em.getDataContent());
				te.setNewFieldType(em.getFieldType());
				te.setNewFieldDataType(em.getFieldDataType());
				te.setNewLength(em.getLength());
				te.setList("无效");
				te.setSeq(++seq);
				te.setCreatedDate(new Date());
				teLst.add(te);
			}
		}
		if(teLst.size() == 0) return false;
		int idx = tableMapper.saveBatchTableElement(teLst);		
		if(idx == 1) return true;
		return false;
	}

	@Override
	public List<TableElement> findTableAllElements(Long tbId) {
		return tableMapper.findTableAllElements(tbId,null);
	}

	@Override
	public TableElement findTableElement(Long tbId, Long emId) {
		return tableMapper.findTableElement(tbId, emId);
	}

	@Override
	public boolean moveUp(Long tbId, Long id) {
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,null);
		int i=0;
		TableElement curTe = null;
		for(TableElement te : teLst){
			if(te.getId().equals(id)) {
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
	public boolean moveDown(Long tbId, Long id) {
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,null);
		int i=0;
		TableElement curTe = null;
		for(TableElement te : teLst){
			if(te.getId().equals(id)) {
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
	public List<TableElement> findTableAllElements(Long tbId, String scope) {
		if(StringUtils.isEmpty(scope)) scope = "表体";
		return tableMapper.findTableAllElements(tbId, scope);
	}

	@Override
	public boolean deleteElement(Long id) {
		tableMapper.deleteTableElement(id);
		return true;
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
	public boolean updateTableElementList(Long tbId, Long[] emIds,Long[] newEmIds) {
		tableMapper.resetTableElementList(tbId);
		int idx = tableMapper.updateTableElementList(tbId,emIds);
		if(idx > 0 && newEmIds.length > 0) {			
			List<TableElement> teLst = new ArrayList<TableElement>();
			for(Long id : newEmIds){
				Long seq = 99l;
				Element em = elementService.find(id);
				TableElement te = new TableElement();
				te.setTbId(tbId);
				te.setEmId(em.getEmId());
				te.setNewLabelName(em.getLabelName());
				te.setNewFunctionName(em.getFunctionName());
				te.setNewHiddenFieldName(em.getHiddenFieldName());
				te.setNewDataContent(em.getDataContent());
				te.setNewFieldType(em.getFieldType());
				te.setNewFieldDataType(em.getFieldDataType());
				te.setNewLength(em.getLength());
				te.setScope("列表");
				te.setSeq(seq);
				te.setList("有效");
				te.setStatus("有效");
				te.setCreatedDate(new Date());
				teLst.add(te);
				seq++;
				
			}			
			idx = tableMapper.saveBatchTableElement(teLst);			
			return true;
		}
		return false;
	}

	@Override
	public List<TableElement> findElementsOnList(Long tbId) {
		return tableMapper.findElementsOnList(tbId);
	}

	/**
	 * 生成库表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean createTable(Long tbId, String tableName) throws Exception {
		tableName = "tb_"+tableName;
		//判断设定的表名是否已经存在
		boolean tableExist = sqlExecutor.isExist(tableName);
		if(tableExist) {
			throw new Exception("表名已存在!");
		}
		//判断是新建表还是重命名表
		TableBrief tb = tableMapper.find(tbId);
		if(tb.getName() == null) sqlExecutor.createTable(tbId,tableName);
		else sqlExecutor.rename(tableName,tb.getName());
		tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setName(tableName);
		tb = this.updateTableBrief(tb);
		if(tb != null) return true;
		return false;
		
	}

	@Override
	public List<TableBrief> findAllWithTableName() {
		return tableMapper.findAllWithTableName();
	}

	/**
	 * 获取表单及表单列表上的字段
	 */
	@Override
	public List<TableElement> findTableAllElementsWithListLevelElements(Long tbId) {
		List<TableElement> tes = tableMapper.findTableAllFields(tbId);
		tes.addAll(tableMapper.findTableListLevelElements(tbId));
		return tes;
	}

	/**
	 * 设置表元素
	 */
	@Override
	public boolean updateTableElement(TableElement obj) {
		int idx = 0;
		
		String[] valAry = obj.getNewFieldDataType().split(",");
		if(valAry.length == 2){
			obj.setNewFieldDataType(valAry[1]);
			obj.setStbId(Long.parseLong(valAry[0]));
		}
		if(obj.getId() == null) {
			Element em = new Element();
			if(!(obj.getNewFieldType().contains("标签") || obj.getNewFieldType().contains("子表单") || obj.getNewFieldType().contains("组件"))){				
				em.setCreatedDate(new Date());
				em.setDataContent(obj.getNewDataContent());
				em.setFieldDataType(obj.getNewFieldDataType());
				em.setFieldName(obj.getFieldName());
				em.setFieldType(obj.getNewFieldType());
				em.setFunctionBelongTo(obj.getFunctionBelongTo());
				em.setFunctionName(obj.getNewFunctionName());
				em.setGrade("自定义");
				em.setHiddenFieldName(obj.getNewHiddenFieldName());
				em.setLabelName(obj.getNewLabelName());
				em.setLength(obj.getNewLength());
				em = elementService.save(em);
			}
			Long seq = tableMapper.findMaxSeq(obj.getTbId());
			if(seq == null) seq = 0l;
			obj.setSeq(seq+1);
			obj.setCreatedDate(new Date());
			obj.setEmId(em.getEmId());
			idx = tableMapper.saveTableElement(obj);
		}
		else idx = tableMapper.updateTableElement(obj);
		if(idx > 0) return true;
		return false;
	}	

	/**
	 * 获得流程节点被设置的字段
	 */
	@Override
	public List<TableElement> findTableAllElementsOnNode(Long wfId,
			Long nodeId, Long tbId) {
		return tableMapper.findTableAllElementsOnNode(wfId, nodeId, tbId);		
	}

	/**
	 * 设置表单在节点上的属性
	 */
	@Override
	public boolean setTableFieldsOnNode(Long wfId, Long nodeId, Long[] ids) {
		int idx = tableMapper.saveTableElementOnNode(wfId, nodeId, ids);
		if(idx > 0) return true;
		return false;
	}

	/**
	 * 设置表单状态
	 */
	@Override
	public boolean setStatus(Long tbId, boolean status) {
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		if(status) tb.setStatus("有效");
		else tb.setStatus("无效");
		int idx = tableMapper.updateTableBrief(tb);
		if(idx > 0) return true;
		return false;
	}

	/**
	 * 获得所有没有和在用流程建立过关联的表
	 */
	@Override
	public List<TableBrief> findAllWithTableNameNoRelated() {
		return tableMapper.findAllWithTableNameNoRelated();
	}
	
	/**
	 * 删除指定的表单包括表结构
	 */
	@Override
	public boolean deleteTable(Long tbId) {
		TableBrief tb = tableMapper.find(tbId);
		if(tb != null){
			tableMapper.deleteTableBrief(tbId);
			tableMapper.deleteLayout(tbId, null);
			tableMapper.deleteTableElementByTbId(tb.getTbId());
			if(tb.getTableName() != null){
				try{					
					sqlExecutor.dropTable(tb.getTableName());
				}catch(Exception e){}
			}
		}
		return true;
	}

	
	
	/**
	 * 设置表单布局
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean saveLayout(Long tbId, Long headCols, Long bodyCols,Long footCols) {		
		TableLayout tl = tableMapper.findLayout(tbId, "表头");
		if(tl != null) tableMapper.deleteLayout(tbId, "表头");
		if(headCols != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表头");
				tl.setTbId(tbId);
			}			
			tl.setCols(headCols);						
			tableMapper.saveTableLayout(tl);
		}
		
		tl = tableMapper.findLayout(tbId, "表体");
		if(tl != null) tableMapper.deleteLayout(tbId, "表体");
		if(bodyCols != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表体");
				tl.setTbId(tbId);
			}			
			tl.setCols(bodyCols);						
			tableMapper.saveTableLayout(tl);
		}
		
		tl = tableMapper.findLayout(tbId, "表尾");
		if(tl != null) tableMapper.deleteLayout(tbId, "表尾");
		if(footCols != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表尾");
				tl.setTbId(tbId);
			}			
			tl.setCols(footCols);						
			tableMapper.saveTableLayout(tl);
		}
		
		return true;
	}


	/**
	 * 获取当前表配置的所有元素
	 */
	@Override
	public List<TableElement> findTableAllFields(Long tbId) {
		return tableMapper.findTableAllFields(tbId);
	}


	/**
	 * 获取当前表可用的子表
	 */
	@Override
	public List<TableBrief> findAllSubTables(Long tbId) {
		return tableMapper.findAllSubTables(tbId);
	}


	/**
	 * 设置子表
	 */
	@Override
	public boolean setSubTable(Long tbId, String scope, Long stbId) {
		int idx = tableMapper.setSubTable(tbId, scope, stbId);
		if(idx == 1) return true;
		return false;
	}


	/**
	 * 根据模板类型返回所有的表单
	 */
	@Override
	public List<TableBrief> findTableBriefWithTemplate(String template) {
		return tableMapper.findTableBriefWithTemplate(template);
	}


	/**
	 * 仅供表单创建数据库表的时候的字段检测
	 */
	@Override
	public List<TableElement> findTableFieldsToDBCheck(Long tbId) {
		return tableMapper.findTableFieldsToDBCheck(tbId);
	}


	/**
	 * 拷贝指定表单信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean copy(Long tbId) {
		//拷贝tableBrief
		TableBrief tb = this.find(tbId);
		TableBrief copyTb = new TableBrief();
		copyTb.setCols(tb.getCols());
		copyTb.setCreatedDate(new Date());
		copyTb.setName(tb.getName());
		copyTb.setTableName(tb.getTableName()+"_拷贝");
		copyTb.setStatus(tb.getStatus());
		copyTb.setTemplate(tb.getTemplate());
		int idx = tableMapper.saveTableBrief(copyTb);
		if(idx == 1){
			//拷贝tableLayout
			for(TableLayout tl : tb.getLayout()){
				tl.setTbId(copyTb.getTbId());
				tableMapper.saveTableLayout(tl);
			}
			for(TableElement te : this.findTableAllElements(tbId)){
				te.setTbId(copyTb.getTbId());
				te.setCreatedDate(new Date());
				this.saveTableElement(te);
			}			
		}
		
		return true;
	}


	@Override
	public boolean removeBinding(Long tbId) {
		TableBrief tb = tableMapper.find(tbId);
		int idx = tableMapper.removeBinding(tbId);
		if(idx > 0) {
			return wfService.removeBinding(tb.getWfId());			
		}
		return false;
	}


	@Override
	public boolean dropTable(Long tbId) throws Exception {
		TableBrief tb = tableMapper.find(tbId);
		try{					
			sqlExecutor.dropTable(tb.getName());			
			tb.setTbId(tbId);
			tb.setName(null);
			int idx = tableMapper.updateTableBriefToNull(tb);
			if(idx == 1) return true;
			else return false;
		}catch(Exception e){
			throw e;
		}
				
	}

}
 