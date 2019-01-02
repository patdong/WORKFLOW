package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.TableBrief;
import cn.ideal.wf.model.TableElement;

public interface TableService extends PageService<TableBrief> {

	TableBrief saveTableBrief(TableBrief obj);
	
	TableBrief find(Long tbId);
	
	TableElement saveTableElement(TableElement obj);
	
	boolean saveTableElement(TableElement[] objs);
	
	List<TableElement> findAllTableElements(Long tbId);
	
	List<TableElement> findAllTableElements(Long tbId,String scope);
	
	TableElement findTableElement(Long tbId, Long emId);
	
	boolean moveUp(Long tbId, Long emId);
	
	boolean moveDown(Long tbId, Long emId);
	
	boolean updateTableElementSeq(TableElement obj);
	
	void delete(Long tbId, Long emId);
	
	boolean setTableName(Long tbId,String tableName);
	
	TableBrief updateTableBrief(TableBrief obj);
	
	boolean updateTableElementList(Long tbId, Long[] emIds);
	
	List<TableElement> findTableList(Long tbId);
		
}
