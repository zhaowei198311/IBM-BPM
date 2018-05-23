package com.desmart.desmartbpm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.desmartbpm.dao.DhProcessInsManageMapper;
import com.desmart.desmartbpm.service.DhProcessInsManageService;

/**
 * 流程实例管理业务逻辑层实现类
 * @author loser_wu
 * @since 2018年5月22日
 */
@Service
@Transactional
public class DhProcessInsManageServiceImpl implements DhProcessInsManageService{
	@Autowired
	private DhProcessInsManageMapper dhProcessInsManageMapper;
}
