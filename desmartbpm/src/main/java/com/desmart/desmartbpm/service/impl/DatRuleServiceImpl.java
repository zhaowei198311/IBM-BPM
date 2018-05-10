package com.desmart.desmartbpm.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DatRuleMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.service.DatRuleService;
@Service
public class DatRuleServiceImpl implements DatRuleService {

	@Resource
	private DatRuleMapper datRuleMapper;
	
	@Override
	public DatRule getDatRuleByCondition(DatRule datRule) {
		// TODO Auto-generated method stub
		return datRuleMapper.getDatRuleByCondition(datRule);
	}

	@Override
	public Integer insertToDatRule(DatRule datRule) {
		// TODO Auto-generated method stub
		return datRuleMapper.insertToDatRule(datRule);
	}

	@Override
	public int inserToDatRuleCondition(List<DatRuleCondition> datRuleCondition) {
		// TODO Auto-generated method stub
		return datRuleMapper.inserToDatRuleCondition(datRuleCondition);
	}

	@Override
	public LinkedList<DatRuleCondition> getDatruleConditionByRuleId(String ruleId) {
		// TODO Auto-generated method stub
		return datRuleMapper.getDatruleConditionByRuleId(ruleId);
	}

	@Override
	public Integer updateDatRule(DatRule datRule) {
		// TODO Auto-generated method stub
		return datRuleMapper.updateDatRule(datRule);
	}

	@Override
	public List<DatRuleCondition> getDatruleConditionInRuleId(String activityId) {
		// TODO Auto-generated method stub
		return datRuleMapper.getDatruleConditionInRuleId(activityId);
	}

	@Override
	public List<DatRule> getPreRulesLikeRuleName(String activityId) {
		// TODO Auto-generated method stub
		return datRuleMapper.getPreRulesLikeRuleName(activityId);
	}

	@Override
	public int deleteDatRuleCondition(DatRule datRule) {
		// TODO Auto-generated method stub
		return datRuleMapper.deleteDatRuleCondition(datRule);
	}

	@Override
	public List<DatRuleCondition> loadDatruleConditionInRuleId(String activityId) {
		// TODO Auto-generated method stub
		return datRuleMapper.loadDatruleConditionInRuleId(activityId);
	}

	@Override
	public int deleteDatRule(DatRule datRule) {
		// TODO Auto-generated method stub
		return datRuleMapper.deleteDatRule(datRule);
	}

	@Override
	public int updateActivityMeta(BpmActivityMeta bpmActivityMeta) {
		// TODO Auto-generated method stub
		return datRuleMapper.updateActivityMeta(bpmActivityMeta);
	}

	@Override
	public BpmActivityMeta loadActivityMetaByCondition(BpmActivityMeta activity) {
		// TODO Auto-generated method stub
		return datRuleMapper.loadActivityMetaByCondition(activity);
	}

}
