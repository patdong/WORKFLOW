package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.Element;

public interface ElementService extends PageService<Element> {

	Element save(Element element);
	
	Element update(Element element);
	
	int updateStatus(Element element);
	
	Element find(Long emId);
	
	List<Element> findValidAll();
	
	List<Element> findValidAllWithTable(Long tbId);
	
	List<Element> findValidAllWithTable(Long tbId,String scope);

}
