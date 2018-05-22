package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhApprovalOpinion;

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
}
