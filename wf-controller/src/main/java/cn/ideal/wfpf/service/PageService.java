package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.Page;

public interface PageService<E> {

	List<E> findAll();
	
	List<E> findAll(Page<E> page);
}
