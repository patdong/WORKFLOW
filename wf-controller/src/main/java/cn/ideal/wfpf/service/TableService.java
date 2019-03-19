package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;

public interface TableService extends PageService<TableBrief> {

	TableBrief saveTableBrief(TableBrief obj);
	
	TableBrief find(Long tbId);
	
	TableElement saveTableElement(TableElement obj);
	
	boolean saveTableElement(TableElement[] objs);
	
	List<TableBrief> findAllWithTableName();
	
	List<TableBrief> findAllWithTableNameNoRelated();
	
	List<TableElement> findTableAllElements(Long tbId);
	
	List<TableElement> findTableAllElements(Long tbId,String scope);
	
	List<TableElement> findTableAllElementsWithSpecialElements(Long tbId);
	
	TableElement findTableElement(Long tbId, Long emId);
	
	boolean moveUp(Long tbId, Long emId);
	
	boolean moveDown(Long tbId, Long emId);
	
	boolean updateTableElementSeq(TableElement obj);
	
	boolean delete(Long tbId, Long emId);
	
	boolean setTableName(Long tbId,String tableName);
	
	TableBrief updateTableBrief(TableBrief obj);
	
	boolean updateTableElementList(Long tbId, Long[] emIds,Long[] newEmIds);
	
	List<TableElement> findElementsOnList(Long tbId);
	
	boolean createTable(Long tbId, String tableName) throws Exception;
	
	boolean updateTableElement(TableElement obj);
	
	TableElement[][] findTableAllElements(Long tbId,String scope,String style);
	
	List<TableElement> findTableAllElementsOnNode(Long wfId, Long nodeId,Long tbId);
	
	boolean setTableFieldsOnNode(Long wfId, Long nodeId, Long[] emIds);
	
	boolean setStatus(Long tbId, boolean status);
		
	boolean delete(Long tbId);
}
