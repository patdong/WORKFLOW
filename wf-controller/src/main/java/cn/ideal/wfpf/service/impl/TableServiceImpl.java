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

import cn.ideal.wfpf.dao.ElementMapper;
import cn.ideal.wfpf.dao.TableMapper;
import cn.ideal.wfpf.model.Element;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.service.TableService;
import cn.ideal.wfpf.sqlengine.SQLExecutor;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private TableMapper tableMapper;
	@Autowired
	private ElementMapper elementMapper;
	
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
		if(idx == 1) return obj;
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
				te.setNewLength(em.getLength());
				te.setList("无效");
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
	public boolean moveUp(Long tbId, Long emId) {
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,null);
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
		List<TableElement> teLst = tableMapper.findTableAllElements(tbId,null);
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
	public List<TableElement> findTableAllElements(Long tbId, String scope) {
		if(StringUtils.isEmpty(scope)) scope = "body";
		return tableMapper.findTableAllElements(tbId, scope);
	}

	@Override
	public boolean delete(Long tbId, Long emId) {
		tableMapper.deleteTableElement(tbId, emId);
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
				Element em = elementMapper.find(id);
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
				te.setScope("/");
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

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
	public boolean createTable(Long tbId, String tableName) throws Exception {
		tableName = "tb_"+tableName;
		//判断设定的表名是否已经存在
		boolean tableExist = sqlExecutor.isExist(tableName);
		if(tableExist) {
			throw new Exception("表名已存在!");
		}
			
		sqlExecutor.createTable(tbId,tableName);
		TableBrief tb = new TableBrief();
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

	@Override
	public List<TableElement> findTableAllElementsWithSpecialElements(Long tbId) {
		List<TableElement> tes = tableMapper.findTableAllElements(tbId,null);
		tes.addAll(tableMapper.findTableSpecialElements(tbId));
		return tes;
	}

	@Override
	public boolean updateTableElement(TableElement obj) {
		int idx = tableMapper.updateTableElement(obj);
		if(idx > 0) return true;
		return false;
	}

	/**
	 * 将表格生成多维数组，此方法仅限表格展示的时候使用
	 * @param tbId
	 * @param scope
	 * @param style
	 * @return
	 */
	@Override
	public TableElement[][] findTableAllElements(Long tbId, String scope,String style) {
		if(StringUtils.isEmpty(style)) return null;
		Long column = 0l;
		try{
			column = Long.parseLong(style);
		}catch(Exception e){
			return null;
		}
		List<TableElement> ems = this.findTableAllElements(tbId, scope);
		//修复配置和实际设值之间的差异
		for(TableElement item : ems){
			if(item.getCols() > Long.parseLong(style)) item.setCols(column);
		}	
				
		TableElement[][] emary = new TableElement[ems.size()][column.intValue()];
		int row = 0,col = 0;
		for(TableElement item : ems){			
			if(col > (column-1)){
				row++;
				col = 0;				
			}
			if(emary[row][col] == null) {			
				emary[row][col] = item;
				//多行仅在同列的行上做扩展
				if(item.getRowes() > 1) {			
					for(int i=1;i<item.getRowes()-1;i++){
						emary[row+i][col] = new TableElement();
					}
				}
				//多列在同列多行上扩展
				if(item.getCols() > 1) {
					//修复配置和实际设值之间的差异，以style的设置为准
					if(col+item.getCols() > column) item.setCols(column-col);
					for(int j=0;j<item.getRowes()-1;j++){
						for(int i=col+1;i<col+item.getCols();i++){
							emary[row+j][i] = new TableElement();
						}
					}
				}
				col+=item.getCols();
			}else{
				int l=0;
				//判断当前行列是否有空位可以存放读到的数据
				for(l=col;l<column;l++){
					if(emary[row][l] != null){
						col++;
					}else{
						emary[row][l] = item;
						col+=item.getCols();
						break;
					}
				}
				//将读到的数据放到下一行
				if(l==column){
					emary[row+1][0] = item;
				}
			}
		}
		
		//修复配置和实际设值之间的差异
		TableElement te = null;
		row = 0;
		for(int i=0; i<emary.length; i++){
			col=0;	
			if(emary[i][0] != null) row++;
			for(int j=0;j<column;j++){
				if(emary[i][j] != null){
					te = emary[i][j];
					if(emary[i][j].getNewLabelName() == null) col++;
					else col += emary[i][j].getCols();
				}
			}
			if(col>0 && col<column) te.setCols(te.getCols()+column-col);
		}
		
		TableElement[][] rst = new TableElement[row][column.intValue()];
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				rst[i][j] = emary[i][j];
			}
		}
		return rst;
	}

	/**
	 * 获得流程节点被设置的字段
	 */
	@Override
	public List<TableElement> findTableAllElementsOnNode(Long wfId,
			Long nodeId, Long tbId) {
		return tableMapper.findTableAllElementsOnNode(wfId, nodeId, tbId);		
	}

	@Override
	public boolean setTableFieldsOnNode(Long wfId, Long nodeId, Long[] emIds) {
		int idx = tableMapper.saveTableElementOnNode(wfId, nodeId, emIds);
		if(idx > 0) return true;
		return false;
	}

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

	@Override
	public boolean delete(Long tbId) {
		TableBrief tb = tableMapper.find(tbId);
		if(tb != null){
			tableMapper.deleteTableBrief(tbId);
			if(tb.getTableName() != null){
				try{					
					sqlExecutor.dropTable(tb.getTableName());
				}catch(Exception e){}
			}
		}
		return true;
	}

}
 