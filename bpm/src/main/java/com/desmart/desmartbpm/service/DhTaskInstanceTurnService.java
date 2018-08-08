package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTurnTaskRecord;

public interface DhTaskInstanceTurnService {
	
	/**
	 * 批量移交用户任务
	 * @param dhTaskUidList
	 * @param dhTurnTaskRecord
	 * @return
	 */
	public ServerResponse batchTurnTaskInstanceByUser(List<String> dhTaskUidList,DhTurnTaskRecord dhTurnTaskRecord);
	/**
	 * 移交用户所有任务
	 * @param dhTurnTaskRecord
	 * @return
	 */
	public ServerResponse allTurnTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord);
}
