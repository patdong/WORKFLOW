package cn.ideal.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.dao.ElementMapper;
import cn.ideal.wf.model.Element;
import cn.ideal.wf.model.Page;
import cn.ideal.wf.service.ElementService;

@Service
public class ElementServiceImpl implements ElementService{

	@Autowired
	private ElementMapper elementMapper;
	
	@Override
	public int save(Element element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Element element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateStatus(Element element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Element find(Long emId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Element> findAll() {
		return elementMapper.findAll();
	}

	@Override
	public List<Element> findAll(Page<Element> page) {
		return elementMapper.findAPage(page.getCurFirstRecord(),Page.pageSize);
	}

	@Override
	public List<Element> findValidAll() {
		return elementMapper.findValidAll();
	}

	@Override
	public List<Element> findValidAllWithTable(Long tbId) {
		return elementMapper.findValidAllWithTable(tbId,null);
	}

	@Override
	public List<Element> findValidAllWithTable(Long tbId, String scope) {
		if(StringUtils.isEmpty(scope)) scope = "body";
		return elementMapper.findValidAllWithTable(tbId,scope);
	}
}
