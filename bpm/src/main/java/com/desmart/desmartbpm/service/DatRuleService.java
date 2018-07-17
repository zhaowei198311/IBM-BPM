package com.desmart.desmartbpm.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;

public interface DatRuleService {

	/**
	 * 根据主键查询DatRule数据
	 * @param datRule
	 * @return
	 */
	DatRule getDatRuleByKey(String ruleId);

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
	List<DatRule> getPreRulesLikeRuleName(String activityId);

	/**
	 * 根据ruleid删除rule
	 * @param datRule
	 * @return
	 */
	int deleteDatRule(DatRule datRule);

	/**
	 * 根据规则id集合批量删除
	 * @param ruleIdList
	 * @return
	 */
	int removeByRuleIdList(List<String> ruleIdList);

	/**
	 * 新增规则
	 * @param itemList1
	 * @param activityId
	 * @param type
	 * @param activityType
	 * @return
	 */
	public ServerResponse addDatRule(List<DatRuleCondition> itemList1,String activityId,
			 String type,String activityType);
	/**
	 * 初始化加载网关配置界面
	 */
	public ServerResponse loadGatewaySet(String proAppId,String snapshotId,String bpdId,String activityType);
	/**
	 * 修改网关规则，新增网关规则条件或者修改网关规则
	 * @param datRuleCondition
	 * @param activityId
	 * @param oldRuleId
	 * @return
	 */
	public ServerResponse saveOrUpdateDatRule(DatRuleCondition datRuleCondition,String activityId,String oldRuleId);
	/**
	 * 修改网关规则，删除网关规则条件
	 * @param datRuleConditions
	 * @param activityId
	 * @return
	 */
	public ServerResponse deleteDatRule(List<DatRuleCondition> datRuleConditions,String activityId);
	/**
	 * 加载右侧网关详细
	 * @param activityId
	 * @return
	 */
	public List<Map<String,Object>> loadRightDetailsList(String activityId);

	List<DatRule> listByRuleIdList(List<String> ruleIdList);
}
