package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.model.TableLayout;

@Mapper
public interface TableMapper {

	int saveTableBrief(TableBrief obj);

	int updateTableBrief(TableBrief obj);
	
	TableBrief find(Long tbId);
	
	List<TableBrief> findAll();
	
	List<TableBrief> findAllWithTableName();
	
	List<TableBrief> findAPage(@Param("recordNumber") Long recordNumber,@Param("recordLastNumber") Long recordLastNumber,@Param("pageSize") Long  pageSize);
	
    int saveTableElement(TableElement obj);
	
	int saveBatchTableElement(List<TableElement> objs);
	
	List<TableElement> findTableAllElements(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<TableElement> findTableAllFields(@Param("tbId") Long tbId);
	
	TableElement findTableElement(@Param("tbId") Long tbId, @Param("emId") Long emId);
	
	int updateTableElementSeq(TableElement obj);
	
	void deleteTableElement(@Param("id") Long id);
	
	int updateTableElementList(@Param("tbId") Long tbId,@Param("emIds") Long[]  emIds);
	
	int resetTableElementList(Long tbId);
	
	List<TableElement> findElementsOnList(Long tbId);
	
	//获取列表级的元素
	List<TableElement> findTableListLevelElements(Long tbId);
	
	int updateTableElement(TableElement obj);
	
	List<TableElement> findTableAllElementsOnNode(@Param("wfId") Long wfId, @Param("nodeId") Long nodeId,@Param("tbId") Long tbId);
	
	int saveTableElementOnNode(@Param("wfId") Long wfId, @Param("nodeId") Long nodeId,@Param("ids") Long[] ids);
	
	List<TableBrief> findAllWithTableNameNoRelated();
	
	void deleteTableBrief(Long tbId);
	
	int saveTableLayout(TableLayout tl);
	
	TableLayout findTableLayoutWithScope(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	Long findMaxSeq(Long tbId);
	
	void deleteLayout(Long tbId);
	
	List<TableBrief> findAllSubTables(Long tbId);
	
	int setSubTable(@Param("tbId") Long tbId,@Param("scope") String scope,@Param("stbId") Long stbId);
	
	TableBrief findSubTable(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<TableBrief> findTableBriefWithTemplate(String template);
	
	List<TableElement> findTableFieldsToDBCheck(Long tbId);
	
}
