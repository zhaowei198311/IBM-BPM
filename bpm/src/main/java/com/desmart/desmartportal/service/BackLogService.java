package com.desmart.desmartportal.service;

import java.util.Date;
import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.github.pagehelper.PageInfo;

public interface BackLogService {
	/**
	 * 根据条件查询待办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByCondition(
			Date startTime,
			Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize);
}
