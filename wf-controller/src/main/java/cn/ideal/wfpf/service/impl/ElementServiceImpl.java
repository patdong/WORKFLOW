package cn.ideal.wfpf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wfpf.dao.ElementMapper;
import cn.ideal.wfpf.model.Element;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.ElementService;

@Service
public class ElementServiceImpl implements ElementService{

	@Autowired
	private ElementMapper elementMapper;
	
	@Override
	public Element save(Element element) {
		element.setStatus("有效");
		element.setGrade("自定义");
		element.setCreatedDate(new Date());
		int idx = elementMapper.save(element);
		if(idx > 0) return element;
		return null;
	}

	@Override
	public Element update(Element element) {
		int idx = elementMapper.update(element);
		if(idx > 0) return element;
		return null;
	}

	@Override
	public int updateStatus(Element element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Element find(Long emId) {
		return elementMapper.find(emId);
	}

	@Override
	public List<Element> findAll() {
		return elementMapper.findAll();
	}

	@Override
	public List<Element> findAll(Page<Element> page) {
		return elementMapper.findAPage(page.getCurFirstRecord(),page.getCurLastRecord(),Page.pageSize);
	}

	@Override
	public List<Element> findValidAll() {
		return elementMapper.findValidAll();
	}
}
