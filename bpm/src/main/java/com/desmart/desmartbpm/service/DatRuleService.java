package com.desmart.desmartbpm.service;

import java.util.LinkedList;
import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;

public interface DatRuleService {

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
	List<DatRule> getPreRulesLikeRuleName(String activityId);

	/**
	 * 根据ruleid删除rule
	 * @param datRule
	 * @return
	 */
	int deleteDatRule(DatRule datRule);

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
	public ServerResponse loadGatewaySet(String activityBpdId,String snapshotId,String bpdId,String activityType);
}
