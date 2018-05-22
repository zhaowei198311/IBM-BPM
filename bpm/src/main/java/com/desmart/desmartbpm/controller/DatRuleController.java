package com.desmart.desmartbpm.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DatRuleConditionService;
import com.desmart.desmartbpm.service.DatRuleService;

@Controller
@RequestMapping("/datRule")
public class DatRuleController {

	@Resource
	private DatRuleService datRuleServiceImpl;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	@Autowired
	private DatRuleConditionService datRuleConditionServiceImpl;

	@RequestMapping("/loadGatewaySet")
	@ResponseBody
	public ServerResponse loadGatewaySet(String activityBpdId,String snapshotId,String bpdId,String activityType) {
		return datRuleServiceImpl.loadGatewaySet(activityBpdId, snapshotId, bpdId, activityType);
	}

	@RequestMapping("/addDatRule")
	@ResponseBody
	public ServerResponse addDatRule(@RequestBody List<DatRuleCondition> itemList1, @RequestParam String activityId,
			 @RequestParam String type,
			@RequestParam String activityType) throws ParseException {
	    return datRuleServiceImpl.addDatRule(itemList1, activityId, type, activityType);
	}
	
	@RequestMapping("/loadConditionArr")
	@ResponseBody
	public ServerResponse loadConditionArr(@RequestBody List<BpmActivityMeta> activitys) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (BpmActivityMeta activity : activitys) {
			activity = bpmActivityMetaServiceImpl.queryByPrimaryKey(activity.getActivityId());
			Map<String, Object> son = new HashMap<String, Object>();
			if(activity.getActivityType()!=null&&!"".equals(activity.getActivityType())) {//判断是否已经添加规则数据
				//根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
				List<DatRuleCondition> datRuleConditionList
				=datRuleConditionServiceImpl.loadDatruleConditionInRuleId(activity.getActivityId());
				//根据当前activityId查询当前流程和当前type所有predictRules展示,需要按时间排序
				List<DatRule> predictRules = datRuleServiceImpl.getPreRulesLikeRuleName(activity.getActivityId());
				son.put("DataList", datRuleConditionList);
				son.put("PredictRule", predictRules);
				map.put("gatewayKey", son);
			}
		}
		return ServerResponse.createBySuccess(map);
	}


}
