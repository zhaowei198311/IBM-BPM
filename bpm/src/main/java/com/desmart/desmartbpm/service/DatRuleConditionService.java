package com.desmart.desmartbpm.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;

public interface DatRuleConditionService {
	/**
	 * 新增datrulecondition,批量新增
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
	 * 根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
	 * @param activityId
	 * @return
	 */
	List<DatRuleCondition> getDatruleConditionInRuleId(String activityId);

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
	 * 新增
	 * @param datRuleCondition
	 * @return
	 */
	Integer insert(DatRuleCondition datRuleCondition);

	/**
	 * 根据activityId查询datrulecondition集合,按照rule_id、group_name排序
	 * @param activityId
	 * @return
	 */
	List<DatRuleCondition> getDatruleConditionByActivityId(String activityId);

	/**
	 * 批量删除
	 * @param datRuleConditions
	 * @return
	 */
	Integer batchDelete(List<DatRuleCondition> datRuleConditions);
	/**
	 * 修改规则条件
	 * @param datRuleCondition
	 * @return
	 */
	ServerResponse update(DatRuleCondition datRuleCondition);

	/**
	 * 根据规则id列表找到所有 ruleCondition
	 * @param ruleIdList
	 * @return
	 */
	List<DatRuleCondition> listByRuleIdList(List<String> ruleIdList);
}
