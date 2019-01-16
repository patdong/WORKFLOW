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
	
	List<TableBrief> findAPage(@Param("recordNumber") Long recordNumber,@Param("pageSize") Long  pageSize);
	
    int saveTableElement(TableElement obj);
	
	int saveBatchTableElement(List<TableElement> objs);
	
	List<TableElement> findAllTableElements(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	TableElement findTableElement(@Param("tbId") Long tbId, @Param("emId") Long emId);
	
	int updateTableElementSeq(TableElement obj);
	
	void deleteTableElement(@Param("tbId") Long tbId, @Param("emId") Long emId);
	
	int updateTableElementList(@Param("tbId") Long tbId,@Param("emIds") Long[]  emIds);
	
	int resetTableElementList(Long tbId);
	
	List<TableElement> findElementsOnList(Long tbId);
	
}
