package cn.ideal.wfpf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wfpf.dao.SysMapper;
import cn.ideal.wfpf.model.SysConfiguration;
import cn.ideal.wfpf.service.SysService;

@Service
public class SysServiceImpl implements SysService {
	@Autowired
	private SysMapper sysMapper;
	
	@Override
	public List<SysConfiguration> findAll() {
		return sysMapper.findAll();
	}

}
