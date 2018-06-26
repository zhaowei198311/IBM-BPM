package com.desmart.desmartportal.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.entity.DhTaskInstance;

public interface DhApprovalOpinionService {
	/**
	 * 根据条件查询DhApprovalOpinion，默认按照审批序号正序排序
	 * 包含联合查询结果
	 * @param dhApprovalOpinion
	 */
	public List<DhApprovalOpinion> loadDhApprovalOpinionListByCondition(DhApprovalOpinion dhApprovalOpinion);
	/**
	 * 新增
	 * @param dhApprovalOpinion
	 * @return
	 */
	public Integer insert(DhApprovalOpinion dhApprovalOpinion);
	/**
	 * 根据条件查询DhApprovalOpinion，默认按照审批序号正序排序
	 * 无联合查询数据
	 * @param dhApprovalOpinion
	 * @return
	 */
	public List<DhApprovalOpinion> getDhApprovalObinionList(DhApprovalOpinion dhApprovalOpinion);
	/**
	 * 插入审批意见
	 * @param dhApprovalOpinion
	 * @return
	 */
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion);

	/**
	 * 为提交任务插入审批意见，如果不需要填审批意见，返回成功
	 * @param currTask  当前任务
	 * @param dhActivityConf  当前节点配置
	 * @param dataJson  提交上来的data
	 * @return
	 */
	ServerResponse saveDhApprovalOpinionWhenSubmitTask(DhTaskInstance currTask, DhActivityConf dhActivityConf, JSONObject dataJson);

	/**
	 * 为驳回操作插入审批意见
	 * @param currTask 当前任务
	 * @param dataJson 提交上来的data
	 * @return
	 */
	ServerResponse saveDhApprovalOpinionWhenRejectTask(DhTaskInstance currTask, JSONObject dataJson);

	/**
	 * 为完成加签任务保存审批意见
	 * @param currTask 当前任务
	 * @param content  审批意见
	 * @return
	 */
	ServerResponse saveDhApprovalOpinionWhenFinishAdd(DhTaskInstance currTask, String content);

}
