package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.Element;

@Mapper
public interface ElementMapper {
	
	int save(Element element);
	
	int update(Element element);
	
	int updateStatus(Element element);
	
	Element find(Long emId);
	
	List<Element> findAll();

	List<Element> findValidAll();
	
	List<Element> findValidAllWithTable(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<Element> findAPage(@Param("recordNumber") Long recordNumber,@Param("pageSize") Long  pageSize);	
}
