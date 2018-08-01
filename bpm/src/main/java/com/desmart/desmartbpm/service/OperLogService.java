package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhOperLog;
import com.github.pagehelper.PageInfo;

public interface OperLogService {

	int save(DhOperLog operLog);

	/**
	 * 根据条件分页查询日志记录集合
	 * @param dhOperLog
	 * @return
	 */
	public ServerResponse<PageInfo<List<DhOperLog>>> pageOperLogByCondition(DhOperLog dhOperLog,Integer pageNum,Integer pageSize);	
}
