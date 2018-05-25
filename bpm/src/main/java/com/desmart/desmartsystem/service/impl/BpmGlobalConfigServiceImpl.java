package com.desmart.desmartsystem.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.dao.BpmGlobalConfigMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class BpmGlobalConfigServiceImpl implements BpmGlobalConfigService {
	private static final Logger LOG = LoggerFactory.getLogger(BpmGlobalConfigServiceImpl.class);
	
    @Autowired
    private BpmGlobalConfigMapper sysGlobalConfigMapper;

    @Override
    public BpmGlobalConfig getFirstActConfig() {
        List<BpmGlobalConfig> configList = sysGlobalConfigMapper.queryActiveConfig();
        return configList.size() > 0 ? configList.get(0) : null;
    }

	@Override
	public ServerResponse<PageInfo<List<BpmGlobalConfig>>> getFirstActConfig(Integer pageNum, Integer pageSize) {
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<BpmGlobalConfig> resultList = sysGlobalConfigMapper.queryActiveConfig();
			PageInfo<List<BpmGlobalConfig>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public int updateByPrimaryKeySelective(BpmGlobalConfig record) {
		return sysGlobalConfigMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int insert(BpmGlobalConfig record) {
		return sysGlobalConfigMapper.insert(record);
	}
}