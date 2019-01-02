package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.Page;

public interface PageService<E> {

	List<E> findAll();
	
	List<E> findAll(Page<E> page);
}
