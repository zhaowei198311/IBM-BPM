package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DatRule;

@Repository
public interface DatRuleMapper {
	/**
	 * 根据条件查询DatRule数据
	 * @param datRule
	 * @return
	 */
	DatRule getDatRuleByCondition(DatRule datRule);

	/**
	 * 新增datrule
	 * @param datRule
	 * @return
	 */
	Integer insertToDatRule(DatRule datRule);

	/**
	 * 更新组合规则，并更新版本
	 * @param datRule
	 * @return
	 */
	Integer updateDatRule(DatRule datRule);
	
	/**
	 * 根据当前activityId查询当前流程所有predictRules展示,需要按时间排序
	 * @param activityId
	 * @return
	 */
	List<DatRule> getPreRulesLikeRuleName(@Param("activityId")String activityId);
	
	/**
	 * 根据ruleid删除rule
	 * @param datRule
	 * @return
	 */
	int deleteDatRule(DatRule datRule);

	int batchInsertDatRule(List<DatRule> datRule);
	

}
