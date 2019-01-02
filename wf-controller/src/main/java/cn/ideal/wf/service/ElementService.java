package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.Element;

public interface ElementService extends PageService<Element> {

	int save(Element element);
	
	int update(Element element);
	
	int updateStatus(Element element);
	
	Element find(Long emId);
	
	List<Element> findValidAll();
	
	List<Element> findValidAllWithTable(Long tbId);
	
	List<Element> findValidAllWithTable(Long tbId,String scope);

}
