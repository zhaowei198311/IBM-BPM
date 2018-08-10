package com.desmart.desmartbpm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
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
import com.desmart.desmartbpm.util.UUIDTool;
import com.desmart.desmartportal.common.Const;

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
	public ServerResponse loadGatewaySet(String proAppId,String snapshotId,String bpdId,String activityType) {
		return datRuleServiceImpl.loadGatewaySet(proAppId, snapshotId, bpdId, activityType);
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
	 
	@RequestMapping("/saveDatRule")
	@ResponseBody
	public ServerResponse saveDatRule(DatRuleCondition datRuleCondition,String activityId
			,@RequestParam(value="oldRuleId",required=false)String oldRuleId) {
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = simpleDateFormat.format(new Date());
		datRuleCondition.setCreator(creator);
		datRuleCondition.setCreateTime(dateTime);
		datRuleCondition.setLeftValueType("");
		String[] arr = datRuleCondition.getConditionOperator().split(":");
		datRuleCondition.setConditionOperator(arr[0]);
		datRuleCondition.setConditionOperatorName(arr[1]);
		datRuleCondition.setRuleStatus("on");
		if (datRuleCondition.getRuleVersion() == null) {
			datRuleCondition.setRuleVersion(0);// 设置优先级默认为0
		}
	    	return datRuleServiceImpl.saveOrUpdateDatRule(datRuleCondition,activityId,oldRuleId);
	   }
	
	@RequestMapping("/deleteDatRule")
	@ResponseBody
	public ServerResponse deleteDatRule(@RequestBody List<DatRuleCondition> datRuleConditions
			,@RequestParam("activityId")String activityId) {
		return datRuleServiceImpl.deleteDatRule(datRuleConditions,activityId);
	}
	@RequestMapping("/loadRightDetailsList")
	@ResponseBody
	public ServerResponse loadRightDetailsList(String activityId) {
		List<Map<String, Object>> rightDetailsList = datRuleServiceImpl.loadRightDetailsList(activityId);
		return ServerResponse.createBySuccess(rightDetailsList);
	}
	
	
}
