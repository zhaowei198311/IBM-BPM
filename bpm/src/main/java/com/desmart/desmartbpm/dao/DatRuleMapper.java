package com.desmart.desmartbpm.dao;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;

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
	 * 新增datrulecondition
	 * @param datRuleCondition
	 * @return
	 */
	int inserToDatRuleCondition(@Param("datRuleCondition")List<DatRuleCondition> datRuleCondition);

	/**
	 * 根据ruleId查询datrulecondition
	 * @param ruleId
	 * @return
	 */
	LinkedList<DatRuleCondition> getDatruleConditionByRuleId(String ruleId);

	/**
	 * 更新组合规则，并更新版本
	 * @param datRule
	 * @return
	 */
	Integer updateDatRule(DatRule datRule);
	
	/**
	 * 根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
	 * @param activityId
	 * @return
	 */
	List<DatRuleCondition> getDatruleConditionInRuleId(String activityId);

	/**
	 * 根据当前activityId查询当前流程所有predictRules展示,需要按时间排序
	 * @param activityId
	 * @return
	 */
	List<DatRule> getPreRulesLikeRuleName(@Param("activityId")String activityId);
	/**
	 * 根据ruleId删除condition
	 * @param datRule
	 * @return
	 */
	int deleteDatRuleCondition(DatRule datRule);
	/**
	 * 初始化加载时
	 * 根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
	 * @param activityId
	 * @return
	 */
	List<DatRuleCondition> loadDatruleConditionInRuleId(String activityId);
	/**
	 * 根据ruleid删除rule
	 * @param datRule
	 * @return
	 */
	int deleteDatRule(DatRule datRule);

	/**
	 * 修改activitymeta的type和activityType
	 * @param bpmActivityMeta
	 * @return
	 */
	int updateActivityMeta(BpmActivityMeta bpmActivityMeta);
	/**
	 * 根据条件查询activitymeta,查询结果唯一
	 * @param activity
	 * @return
	 */
	BpmActivityMeta loadActivityMetaByCondition(BpmActivityMeta activity);

}
