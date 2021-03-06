package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.TableBizTemplate;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;

public interface TableService extends PageService<TableBrief> {

	TableBrief saveTableBrief(TableBrief obj);
	
	TableBrief find(Long tbId);
	
	TableElement saveTableElement(TableElement obj);
	
	boolean saveTableElement(Long tbId, String scope, Long[] emIds);
	
	List<TableBrief> findAllWithTableName();
	
	List<TableBrief> findAllWithTableNameNoRelated();
	
	List<TableElement> findTableAllElements(Long tbId);
	
	List<TableElement> findTableAllElements(Long tbId,String scope);
	
	List<TableElement> findTableAllElementsWithListLevelElements(Long tbId);
	
	TableElement findTableElement(Long id);
	
	boolean moveUp(Long tbId, Long id,String scope);
	
	boolean moveDown(Long tbId, Long id,String scope);
	
	boolean updateTableElementSeq(TableElement obj);
	
	boolean deleteElement(Long id);
	
	boolean setTableName(Long tbId,String tableName);
	
	TableBrief updateTableBrief(TableBrief obj);
	
	boolean updateTableElementList(Long tbId, Long[] ids);
	
	List<TableElement> findElementsOnList(Long tbId);
	
	boolean createTable(Long tbId, String tableName) throws Exception;
	
	boolean updateTableElement(TableElement obj);
	
	List<TableElement> findTableAllElementsOnNode(Long wfId, Long nodeId,Long tbId);
	
	boolean setTableFieldsOnNode(Long wfId, Long nodeId, List<TableElement> teLst);
	
	boolean setStatus(Long tbId, boolean status);
		
	boolean deleteTable(Long tbId);
	
	boolean saveLayout(Long tbId, String[] head, String[] body,String[] foot);
	
	List<TableElement> findTableAllFields(Long tbId);
	
	List<TableBrief> findAllSubTables(Long tbId);
	
	boolean setSubTable(Long tbId, String scope,Long stbId);
	
	List<TableBrief> findTableBriefWithTemplate(String template);
	
	List<TableElement> findTableFieldsToDBCheck(Long tbId);
	
	boolean copy(Long tbId);
	
	boolean removeBinding(Long tbId);
	
	boolean dropTable(Long tbId) throws Exception;
	
	List<TableBizTemplate> findBizTemplates();
	
	boolean setTableAlias(Long tbId,String alias);
}
