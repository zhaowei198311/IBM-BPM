package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DataForSkipFromReject;
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
	 * 保存流转信息
	 * @param dhRoutingRecord
	 * @return
	 */
	int saveDhRoutingRecord(DhRoutingRecord dhRoutingRecord);

	/**
	 * 提交任务后生成流转信息
	 * @param taskInstance  任务实例
	 * @param bpmRoutingData  预测的下个环节信息
	 * @param  willTokenMove token是否移动，当token移动记录 activityTo
	 * @return
	 */
	DhRoutingRecord generateSubmitTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance taskInstance, BpmRoutingData bpmRoutingData,
																						boolean willTokenMove);

	/**
	 * 为系统任务生成流转记录
	 * @param taskNode 任务节点
	 * @param currTask  当前任务实例
	 * @param systemUser  系统提交员id
	 * @param bpmRoutingData 流转信息
	 * @return
	 */
	DhRoutingRecord generateSystemTaskRoutingRecord(BpmActivityMeta taskNode, DhTaskInstance currTask,
													String systemUser, BpmRoutingData bpmRoutingData);

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
	DhRoutingRecord generateRevokeTaskRoutingRecord(DhTaskInstance finishedTaskInstance);

	/**
	 * 为加签生成流转信息
	 * @param currTaskInstance
	 * @return
	 */
	DhRoutingRecord generateAddTaskRoutingRecord(DhTaskInstance currTaskInstance);

	/**
	 * 为完成加签生成流转信息
	 * @param currTaskInstance
	 * @return
	 */
	DhRoutingRecord generateFinishAddTaskRoutingRecord(DhTaskInstance currTaskInstance);

	/**
	 * 为跳转到驳回的节点生成流转信息
	 * @param dataForSkipFromReject
	 * @return
	 */
	DhRoutingRecord generateSkipFromRejectRoutingRecord(DataForSkipFromReject dataForSkipFromReject);

	/**
	 * 为自动提交生成流转信息
	 * @param currTaskInstance
	 * @param tagetActivityMeta
	 * @param adminUid  管理员id
	 * @return
	 */
	DhRoutingRecord generateAutoCommitRoutingRecord(DhTaskInstance currTaskInstance, BpmActivityMeta tagetActivityMeta
			, String adminUid);

	/**
	 * 根据流程实例主键加载流转记录
	 * @param insUid
	 * @return
	 */
	ServerResponse loadDhRoutingRecords(String insUid);


	/**
	 * 获得流程实例的所有流转记录，倒序排序
	 * @param insUid 实例主键
	 * @return
	 */
	List<DhRoutingRecord> getAllRoutingRecordOfProcessInstance(String insUid);

	/**
	 * 提交指定任务的流转记录，如果任务对应的流转记录中不是提交，返回null
	 * @param taskUid
	 * @return
	 */
	DhRoutingRecord getSubmitRoutingRecordOfTask(String taskUid);

	/**
	 * 根据任务主键查找对应的流转记录，可能有多条（提交又取回的情况有一条提交记录，一条取回记录是关联的task）
	 * @param taskUid
	 * @return
	 */
	List<DhRoutingRecord> getRoutingRecordOfTask(String taskUid);


	/**
	 * 为撤转流程实例生成流转信息
	 * @param currTaskInstance
	 * @param tagetActivityMeta
	 * @return
	 */
	DhRoutingRecord generateTrunOffTaskRoutingRecordByTaskAndRoutingData(DhTaskInstance currTaskInstance,
			BpmActivityMeta tagetActivityMeta);
}
