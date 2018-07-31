package com.desmart.desmartbpm.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DatRule;

@Repository
public interface DatRuleMapper {
	/**
	 * 根据主键查询DatRule数据
	 * @param ruleId
	 * @return
	 */
	DatRule getDatRuleByKey(@Param("ruleId")String ruleId);

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
	
	List<DatRule> batchSelectDatRule(@Param("keySet")Set<String> ruleIds);
	
	/**
	 * 根据ruleIds进行批量删除
	 * @Title: deleteByRuleIds  
	 * @Description:
	 * @param @param ruleIds
	 * @param @return  
	 * @return int  
	 * @throws
	 */
	int deleteByRuleIds(List<String> ruleIds);
	
	/**
	 * 根据ruleIds进行批量查询
	 * @Title: listByRuleIds  
	 * @Description:
	 * @param @param ruleIds
	 * @param @return  
	 * @return List<DatRule>  
	 * @throws
	 */
	List<DatRule> listByRuleIds(List<String> ruleIds);

	/**
	 * 批量更新规则的执行逻辑
	 * @param rules
	 * @return
	 */
	int batchUpdateRuleProcessByPrimaryKey(Collection<DatRule> rules);
}
