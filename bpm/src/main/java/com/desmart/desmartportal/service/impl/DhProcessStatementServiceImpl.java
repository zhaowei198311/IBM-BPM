package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.dao.DhProcessStatementMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessStatementService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class DhProcessStatementServiceImpl implements DhProcessStatementService{

	
	@Autowired
	private DhProcessStatementMapper dhProcessStatementMapper;
	
	@Override
	public ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(Map<String, String> parameter, Integer pageNum,
			Integer pageSize) {
		try {
			PageHelper.startPage(pageNum, pageSize,"task_init_date desc");
			List<DhTaskInstance> resultList = dhProcessStatementMapper.selectAllTask(parameter);
			PageInfo<List<DhTaskInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public List<DhTaskInstance> selectAllTask(Map<String, String> parameter) {
		// TODO Auto-generated method stub
		return dhProcessStatementMapper.selectAllTask(parameter);
	}
}
