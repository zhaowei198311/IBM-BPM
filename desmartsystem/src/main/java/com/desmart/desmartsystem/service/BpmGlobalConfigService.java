package com.desmart.desmartsystem.service;

import java.util.List;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.github.pagehelper.PageInfo;

/**
 * 全局配置服务
 */
public interface BpmGlobalConfigService {
    /**
     * 获得所有启用的全局配置
     */
	ServerResponse<PageInfo<List<BpmGlobalConfig>>> getFirstActConfig(Integer pageNum, Integer pageSize);
	
	BpmGlobalConfig getFirstActConfig();
	
	int updateByPrimaryKeySelective(BpmGlobalConfig record);

	int insert(BpmGlobalConfig record);
}