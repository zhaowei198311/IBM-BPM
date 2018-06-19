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
	 * 新增审批意见
	 * @param dhApprovalOpinion
	 * @return
	 */
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion);

	/**
	 * 保存提交任务时的审批记录
	 * @param currTask  当前任务
	 * @param dhActivityConf  当前节点配置
	 * @param dataJson  提交上来的data
	 * @return
	 */
	ServerResponse saveDhApprovalOpiionWhenSubmitTask(DhTaskInstance currTask, DhActivityConf dhActivityConf, JSONObject dataJson);
}
