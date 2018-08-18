package com.desmart.desmartbpm.service;

import java.util.Date;
import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTurnTaskRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;

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
	/**
	 * 批量抄送用户任务
	 * @param dhTaskUidList
	 * @param dhTurnTaskRecord
	 * @return
	 */
	public ServerResponse batchTransferTaskInstanceByUser(List<String> dhTaskUidList,
			DhTurnTaskRecord dhTurnTaskRecord);
	/**
	 * 抄送用户所有任务
	 * @param dhTurnTaskRecord
	 * @param dhTaskInstance
	 * @param isAgent
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public ServerResponse allTransferTaskInstanceByUser(DhTurnTaskRecord dhTurnTaskRecord,
			DhTaskInstance dhTaskInstance, String isAgent, Date startTime, Date endTime);
}
