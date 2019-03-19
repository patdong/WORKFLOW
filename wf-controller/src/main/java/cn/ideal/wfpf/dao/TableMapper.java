package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;

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
	
	TableElement findTableElement(@Param("tbId") Long tbId, @Param("emId") Long emId);
	
	int updateTableElementSeq(TableElement obj);
	
	void deleteTableElement(@Param("tbId") Long tbId, @Param("emId") Long emId);
	
	int updateTableElementList(@Param("tbId") Long tbId,@Param("emIds") Long[]  emIds);
	
	int resetTableElementList(Long tbId);
	
	List<TableElement> findElementsOnList(Long tbId);
	
	List<TableElement> findTableSpecialElements(Long tbId);
	
	int updateTableElement(TableElement obj);
	
	List<TableElement> findTableAllElementsOnNode(@Param("wfId") Long wfId, @Param("nodeId") Long nodeId,@Param("tbId") Long tbId);
	
	int saveTableElementOnNode(@Param("wfId") Long wfId, @Param("nodeId") Long nodeId,@Param("emIds") Long[] emIds);
	
	List<TableBrief> findAllWithTableNameNoRelated();
	
	void deleteTableBrief(Long tbId);
}
