package cn.ideal.wfpf.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wfpf.dao.TableMapper;
import cn.ideal.wfpf.model.Element;
import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableBizTemplate;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.model.TableLayout;
import cn.ideal.wfpf.service.ElementService;
import cn.ideal.wfpf.service.NodeService;
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
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NodeService nodeService;
		
	private SQLExecutor sqlExecutor;
	
	@Autowired
    public void setSQLExecutor(ApplicationContext context) {
		try {
			String driver = jdbcTemplate.getDataSource().getConnection().getMetaData().getDriverName();
			if(driver.toLowerCase().contains("mysql")) sqlExecutor = (SQLExecutor) context.getBean("WFPFMYSQLExecutor");
			if(driver.toLowerCase().contains("oracle")) sqlExecutor = (SQLExecutor) context.getBean("WFPFORACLEExecutor");
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
    }

	
	@Override
	public TableBrief saveTableBrief(TableBrief obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		obj.setTemplate("表");
		int idx = tableMapper.saveTableBrief(obj);
		if(idx == 1) {
			TableLayout tl = new TableLayout();
			tl.setTbId(obj.getTbId());
			tl.setCols(2l);
			tl.setScope("表体");
			tl.setBorder("是");
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
		//为字段默认加上"f_"前缀，防止和数据库中的保留关键字产生冲突
		obj.setNewFieldName("f_"+obj.getNewFieldName());
		int idx = tableMapper.saveTableElement(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveTableElement(Long tbId, String scope, Long[] emIds) {
		List<TableElement> teLst = new ArrayList<TableElement>();
		if(emIds == null || emIds.length == 0) return true;
		Long seq = tableMapper.findMaxSeq(tbId);
		if(seq == null) seq = 0l;
		
		for(Long emId : emIds){
			Element em = elementService.find(emId);
			if(em != null) {				
				//标签
				TableElement label = new TableElement();
				label.setTbId(tbId);
				label.setNewLabelName(em.getLabelName());				
				label.setNewFieldType("标签");
				label.setScope(scope);
				label.setList("无效");
				label.setSeq(++seq);
				label.setCreatedDate(new Date());
				label.setPosition("右");
				teLst.add(label);
				//字段
				TableElement te = new TableElement();
				te.setTbId(tbId);
				te.setNewLabelName(em.getLabelName());
				//为字段默认加上"f_"前缀，防止和数据库中的保留关键字产生冲突
				te.setNewFieldName("f_"+em.getFieldName());
				te.setNewFunctionName(em.getFunctionName());
				te.setNewHiddenFieldName(em.getHiddenFieldName());
				te.setNewDataContent(em.getDataContent());
				te.setNewFieldType(em.getFieldType());
				te.setNewFieldDataType(em.getFieldDataType());
				te.setNewLength(em.getLength());
				te.setScope(scope);
				te.setList("无效");
				te.setSeq(++seq);
				te.setCreatedDate(new Date());
				te.setPosition("右");
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
	public TableElement findTableElement(Long id) {
		return tableMapper.findTableElement(id);
	}

	@Override
	public boolean moveUp(Long tbId, Long id ,String scope) {
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,scope);
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
	public boolean moveDown(Long tbId, Long id,String scope) {
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,scope);
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
		if(tbId == null) return false;
		List<TableBrief> tbs = tableMapper.findByTableName(tbId,tableName);
		//重名不支持
		if(tbs.size() > 0) return false;
		TableBrief oldTb = this.find(tbId);
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setTableName(tableName);
		if(oldTb.getAlias() == null) tb.setAlias(tableName);
		int idx = tableMapper.updateTableBrief(tb);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean setTableAlias(Long tbId,String alias) {
		if(tbId == null) return false;
		List<TableBrief> tbs = tableMapper.findByAlias(tbId,alias);
		//重名不支持
		if(tbs.size() > 0) return false;
		
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setAlias(alias);
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
	public boolean updateTableElementList(Long tbId, Long[] ids) {
		tableMapper.resetTableElementList(tbId);
		int idx = tableMapper.updateTableElementList(tbId,ids);
		if(idx > 0 ) return true;
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
		//tes.addAll(tableMapper.findTableListLevelElements(tbId));
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
		//为字段默认加上"f_"前缀，防止和数据库中的保留关键字产生冲突
		obj.setNewFieldName("f_"+obj.getNewFieldName());				
		if(obj.getId() == null){
			Long seq = tableMapper.findMaxSeq(obj.getTbId());
			if(seq == null) seq = 0l;
			obj.setSeq(seq+1);
			obj.setCreatedDate(new Date());					
			idx = tableMapper.saveTableElement(obj);
		}else{				
			idx = tableMapper.updateTableElement(obj);
		}			
			
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
	public boolean setTableFieldsOnNode(Long wfId, Long nodeId, List<TableElement> teLst) {
		String nodeName = null;
		if(nodeId.longValue() == 0l) nodeName = WFConstants.WF_NODE_START;
		else {
			Node node = nodeService.find(nodeId);
			if(node != null) nodeName = node.getNodename();
			else return false;
		}
				
		int idx = tableMapper.saveTableElementOnNode(wfId, nodeId,nodeName, teLst);
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
	public boolean saveLayout(Long tbId, String[] head, String[] body,String[] foot) {		
		TableLayout tl = tableMapper.findLayout(tbId, "表头");
		if(tl != null) tableMapper.deleteLayout(tbId, "表头");
		if(head != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表头");
				tl.setTbId(tbId);
			}			
			tl.setCols(Long.parseLong(head[0]));	
			tl.setBorder(head[1]);
			tableMapper.saveTableLayout(tl);
		}
		
		tl = tableMapper.findLayout(tbId, "表体");
		if(tl != null) tableMapper.deleteLayout(tbId, "表体");
		if(body != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表体");
				tl.setTbId(tbId);
			}			
			tl.setCols(Long.parseLong(body[0]));
			tl.setBorder(body[1]);
			tableMapper.saveTableLayout(tl);
		}
		
		tl = tableMapper.findLayout(tbId, "表尾");
		if(tl != null) tableMapper.deleteLayout(tbId, "表尾");
		if(foot != null){			
			if(tl == null) {
				tl = new TableLayout();
				tl.setScope("表尾");
				tl.setTbId(tbId);
			}			
			tl.setCols(Long.parseLong(foot[0]));
			tl.setBorder(foot[1]);
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


	@Override
	public List<TableBizTemplate> findBizTemplates() {
		return tableMapper.findAllBizTemplates();
	}

}
 