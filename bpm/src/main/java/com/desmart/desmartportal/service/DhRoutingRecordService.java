package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartportal.entity.BpmRoutingData;

import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;

public interface DhRoutingRecordService {

	/**
	 * 根据条件查询流转信息
	 * @param dhRoutingRecord
	 * @return
	 */
	List<DhRoutingRecord> getDhRoutingRecordListByCondition(DhRoutingRecord dhRoutingRecord);


	/**
	 * 提交任务后生成流转信息
	 * @param taskInstance  任务实例
	 * @param bpmRoutingData  预测的下个环节信息
	 * @param  willTokenMove token是否移动，当token移动记录 activityTo
	 * @return
	 */
	ServerResponse<DhRoutingRecord> generateSubmitTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmRoutingData bpmRoutingData,
																						boolean willTokenMove);

	/**
	 * 驳回任务后记录流转信息
	 * @param taskInstance  任务实例
	 * @param targetNode  驳回的节点信息
	 * @return
	 */
	DhRoutingRecord generateRejectTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmActivityMeta targetNode);

	/**
	 * 为取回操作生成流转信息
	 * @param finishedTaskInstance
	 * @return
	 */
	DhRoutingRecord generateRevokeTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance finishedTaskInstance);

	/**
	 * 根据流程实例主键加载流转记录
	 * @param insUid
	 * @return
	 */
	ServerResponse loadDhRoutingRecords(String insUid);

	/**
	 * 获得指定流程实例，在指定节点上，指定人员的最近一条流转记录
	 * @param insUid  流程实例主键
	 * @param taskNode  任务节点
	 * @param userUid  用户主键
	 * @return
	 */
	DhRoutingRecord getNearlyRoutingRecordOnTaskNode(String insUid, BpmActivityMeta taskNode, String userUid);
}
