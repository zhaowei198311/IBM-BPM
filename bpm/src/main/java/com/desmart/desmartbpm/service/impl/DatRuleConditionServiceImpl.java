package com.desmart.desmartbpm.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DatRuleConditionMapper;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.service.DatRuleConditionService;
import com.desmart.desmartbpm.service.DatRuleService;
@Service
public class DatRuleConditionServiceImpl implements DatRuleConditionService {

	@Autowired
	private DatRuleConditionMapper datRuleConditionMapper;

	@Override
	public int inserToDatRuleCondition(List<DatRuleCondition> datRuleCondition) {

		return datRuleConditionMapper.inserToDatRuleCondition(datRuleCondition);
	}

	@Override
	public LinkedList<DatRuleCondition> getDatruleConditionByRuleId(String ruleId) {

		return datRuleConditionMapper.getDatruleConditionByRuleId(ruleId);
	}

	@Override
	public List<DatRuleCondition> getDatruleConditionInRuleId(String activityId) {

		return datRuleConditionMapper.getDatruleConditionInRuleId(activityId);
	}

	@Override
	public int deleteDatRuleCondition(DatRule datRule) {

		return datRuleConditionMapper.deleteDatRuleCondition(datRule);
	}

	@Override
	public List<DatRuleCondition> loadDatruleConditionInRuleId(String activityId) {

		return datRuleConditionMapper.loadDatruleConditionInRuleId(activityId);
	}

	@Override
	public Integer insert(DatRuleCondition datRuleCondition) {

		return datRuleConditionMapper.insert(datRuleCondition);
	}

	@Override
	public List<DatRuleCondition> getDatruleConditionByActivityId(String activityId) {

		return datRuleConditionMapper.getDatruleConditionByActivityId(activityId);
	}

	@Override
	public Integer batchDelete(List<DatRuleCondition> datRuleConditions) {

		return datRuleConditionMapper.batchDelete(datRuleConditions);
	}

	@Override
	public ServerResponse update(DatRuleCondition datRuleCondition) {
		Integer count = 0;
		count = datRuleConditionMapper.update(datRuleCondition);
		if(count>0) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	

}
