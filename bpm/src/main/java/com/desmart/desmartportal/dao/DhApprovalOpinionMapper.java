package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhApprovalOpinion;
@Repository
public interface DhApprovalOpinionMapper {

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
	
	
	
}
