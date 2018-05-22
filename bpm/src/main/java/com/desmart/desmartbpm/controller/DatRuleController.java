package com.desmart.desmartbpm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DatRuleService;
import com.desmart.desmartbpm.util.DateUtil;
import com.desmart.desmartbpm.util.UUIDTool;

@Controller
@RequestMapping("/datRule")
public class DatRuleController {

	@Resource
	private DatRuleService datRuleServiceImpl;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;

	@RequestMapping("/loadGatewaySet")
	@ResponseBody
	public ServerResponse loadGatewaySet(String activityBpdId,String snapshotId,String bpdId,String activityType) {
		List<BpmActivityMeta> list = 
				bpmActivityMetaServiceImpl.getBpmActivityMetaByActivityType(activityBpdId, snapshotId, bpdId, activityType);		
		return ServerResponse.createBySuccess(list);
	}

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	@RequestMapping("/addDatRule")
	@ResponseBody
	public ServerResponse addDatRule(@RequestBody List<DatRuleCondition> itemList1, @RequestParam String activityId,
			 @RequestParam String type,
			@RequestParam String activityType) throws ParseException {
		//activityId = "sa";
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//String routeVarname="result_0,result_5";
		String ruleName =type+"_rule_"+activityId+"_";
		// activityType = "gatewayor";
		if(itemList1!=null && itemList1.size()>0) {
		Map<String, List<DatRuleCondition>> map = groupListToMapByResult(itemList1);
		//检查更新activity_meta及规则数据
		check_Update_MetaAndRule(map,ruleName,activityType,activityId,type);
		Integer num = 0;
		Integer count = 0;
		for (List<DatRuleCondition> itemList : map.values()) {
			DatRule datRule = null;
			ruleName = type+"_rule_"+activityId+"_" + itemList.get(0).getResult();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateTime = simpleDateFormat.format(new Date());
				datRule = new DatRule();
				datRule.setRuleName(ruleName);
				datRule.setRuleId(EntityIdPrefix.DAT_RULE + UUIDTool.getUUID());
				datRule.setBizType("wfCondCtrl");
				datRule.setEditMode("STD");// 不明确
				datRule.setIsActivate("on");
				datRule.setReturnType("text");
				datRule.setRuleStatus("on");
				datRule.setCreateTime(dateTime);
				datRule.setStartTime(dateTime);
				datRule.setEndTime(
						simpleDateFormat.format(DateUtil.getDateAddYear(simpleDateFormat.parse(dateTime), 182)));
				datRule.setCreator(creator);
				datRule.setRuleType("PARAMS");
				datRule.setRuleVersion(1);// 规则版本
				String ruleId = datRule.getRuleId();

				LinkedList<DatRuleCondition> linkedList = new LinkedList<DatRuleCondition>();
				for (DatRuleCondition datRuleCondition : itemList) {
					datRuleCondition.setLeftValueType("");
					String[] arr = datRuleCondition.getConditionOperator().split(":");
					datRuleCondition.setConditionOperator(arr[0]);
					datRuleCondition.setConditionOperatorName(arr[1]);
					datRuleCondition.setCreateTime(dateTime);
					datRuleCondition.setCreator(creator);
					;
					datRuleCondition.setRuleId(ruleId);
					datRuleCondition.setRuleStatus("on");
					datRuleCondition.setConditionId("rulecond:" + UUIDTool.getUUID());
					if (datRuleCondition.getRuleVersion() == null || "".equals(datRuleCondition.getRuleVersion())) {
						datRuleCondition.setRuleVersion(0);// 设置优先级默认为0
					}
					linkedList.addFirst(datRuleCondition);
				}
				List<Map.Entry<String, List<DatRuleCondition>>> list2 = groupListToMap(linkedList);
				StringBuffer sb = new StringBuffer("Map(");
				for (int j = 0; j < list2.size(); j++) {
					Entry<String, List<DatRuleCondition>> entry = list2.get(j);
					StringBuffer sonSb = new StringBuffer("(");
					for (int b = 0; b < entry.getValue().size(); b++) {
						DatRuleCondition datRuleCondition2 = entry.getValue().get(b);
						sonSb.append("this['");
						sonSb.append(datRuleCondition2.getLeftValue());
						sonSb.append("']");
						sonSb.append(datRuleCondition2.getValueOperator());
						sonSb.append(datRuleCondition2.getRightValue());
						if ((b + 1) < entry.getValue().size()) {
							sonSb.append(datRuleCondition2.getConditionOperator());
						}
					}
					if ((j + 1) == list2.size()) {
						sonSb.append(")");
					} else if ((j + 1) < list2.size()) {
						sonSb.append(")" + entry.getValue().get(entry.getValue().size() - 1).getConditionOperator());
					}
					sb.append(sonSb.toString());
				}
				sb.append(")");
				datRule.setRuleProcess(sb.toString());
				num += datRuleServiceImpl.insertToDatRule(datRule);
				count += datRuleServiceImpl.inserToDatRuleCondition(itemList);
		}
		if (num > 0) {
			if (count > 0) {
				// 根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
				List<DatRuleCondition> datRuleConditionList = datRuleServiceImpl
						.loadDatruleConditionInRuleId(activityId);
				// 根据当前activityId查询当前流程和当前type所有predictRules展示,需要按时间排序
				List<DatRule> predictRules = datRuleServiceImpl.getPreRulesLikeRuleName(activityId);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("dataList", datRuleConditionList);
				data.put("predictRule", predictRules);
				return ServerResponse.createBySuccess("保存成功！", data);
			} else {
				return ServerResponse.createByErrorMessage("保存失败！网络繁忙");
			}
		} else {
			return ServerResponse.createByErrorMessage("保存失败！网络繁忙");
		}
	}else {
		DatRule datRule =null;
		int num=0;
		List<DatRule> datRules = datRuleServiceImpl.getPreRulesLikeRuleName(activityId);
		for (DatRule datRule2 : datRules) {
			datRule = datRule2;
			num += datRuleServiceImpl.deleteDatRule(datRule);
			datRuleServiceImpl.deleteDatRuleCondition(datRule);
		}
		
		if(datRule!=null) {
		if(num>0) {
			//根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
    		List<DatRuleCondition> datRuleConditionList
    		=datRuleServiceImpl.loadDatruleConditionInRuleId(activityId);
    		//根据当前activityId查询当前流程和当前type所有predictRules展示,需要按时间排序
    		List<DatRule> predictRules = datRuleServiceImpl.getPreRulesLikeRuleName(activityId);
    		Map<String, Object> data = new HashMap<String, Object>();
    		data.put("dataList", datRuleConditionList);
    		data.put("predictRule", predictRules);
			return ServerResponse.createBySuccess("保存成功！", data);
		}else {
			return ServerResponse.createByErrorMessage("保存失败！网络繁忙");
			}
		}else {
			return ServerResponse.createByErrorMessage("保存失败！请先新建网关规则条件");
		}
	}
}
	private void check_Update_MetaAndRule(Map<String, List<DatRuleCondition>> map, String ruleName
			, String activityType, String activityId,String type) {
		// TODO Auto-generated method stub
		BpmActivityMeta bpmActivityMeta = new BpmActivityMeta();
		bpmActivityMeta.setActivityId(activityId);
		bpmActivityMeta.setActivityType(activityType);
		bpmActivityMeta.setType(type);
		//不能修改
		//int count = datRuleServiceImpl.updateActivityMeta(bpmActivityMeta);
		//if(count>0) {
		List<DatRule> datRules = datRuleServiceImpl.getPreRulesLikeRuleName(activityId);
		for (DatRule datRule : datRules) {
			int num =datRuleServiceImpl.deleteDatRuleCondition(datRule); 
			if(num>0) {
			    datRuleServiceImpl.deleteDatRule(datRule);
			}
		}
		//}
		//return 1;
	}

	@RequestMapping("/loadConditionArr")
	@ResponseBody
	public ServerResponse loadConditionArr(@RequestBody List<BpmActivityMeta> activitys) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (BpmActivityMeta activity : activitys) {
			activity = datRuleServiceImpl.loadActivityMetaByCondition(activity);
			Map<String, Object> son = new HashMap<String, Object>();
			if(activity.getActivityType()!=null&&!"".equals(activity.getActivityType())) {//判断是否已经添加规则数据
				//根据当前activityId查询当前流程所有dat_rule_condition展示,需要按照分组名排序
				List<DatRuleCondition> datRuleConditionList
				=datRuleServiceImpl.loadDatruleConditionInRuleId(activity.getActivityId());
				//根据当前activityId查询当前流程和当前type所有predictRules展示,需要按时间排序
				List<DatRule> predictRules = datRuleServiceImpl.getPreRulesLikeRuleName(activity.getActivityId());
				son.put("DataList", datRuleConditionList);
				son.put("PredictRule", predictRules);
				map.put("gatewayKey", son);
			}
		}
		return ServerResponse.createBySuccess(map);
	}

	/**
	 * 根据result分组，不同result为不同datRule
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, List<DatRuleCondition>> groupListToMapByResult(List<DatRuleCondition> list) {
		// TODO Auto-generated method stub
		Map<String, List<DatRuleCondition>> map = new HashMap<>();
		for (DatRuleCondition datRuleCondition : list) {
			if(datRuleCondition!=null) {
			String key = datRuleCondition.getResult();
			List<DatRuleCondition> sonList = new ArrayList<>();
			for (DatRuleCondition son : list) {
				if(son!=null) {
				if (key != null) {
					if (key.equals(son.getResult())) {
						sonList.add(son);
					}
				} else {
					sonList.add(son);
				}
				}
			}
			map.put(key, sonList);
		  }
		}
		return map;
	}

	/**
	 * 循环分组，降序map
	 * 
	 * @param list
	 * @return
	 */
	private List<Map.Entry<String, List<DatRuleCondition>>> groupListToMap(List<DatRuleCondition> list) {
		// TODO Auto-generated method stub
		Map<String, List<DatRuleCondition>> map = new HashMap<>();
		for (DatRuleCondition datRuleCondition : list) {
			String key = datRuleCondition.getConditionGroupName();
			if("".equals(key)) {
				key=null;
			}
			List<DatRuleCondition> sonList = new ArrayList<>();
			for (DatRuleCondition son : list) {
				if (key != null) {
					if (key.equals(son.getConditionGroupName())) {
						sonList.add(son);
					}
				} else if(key==null){
					if(son.getConditionGroupName()==null||"".equals(son.getConditionGroupName())) {
					sonList.add(son);
					}
				}
			}
			// 根据ruleVersion优先级别排序
			Collections.sort(sonList, new Comparator<DatRuleCondition>() {

				@Override
				public int compare(DatRuleCondition o1, DatRuleCondition o2) {
					// TODO Auto-generated method stub
					if (o1.getRuleVersion() > o2.getRuleVersion()) {
						return -1;
					}
					if (o1.getRuleVersion() == o2.getRuleVersion()) {
						return 0;
					}
					return 1;
				}
			});
			map.put(key, sonList);
		}
		// 按分组名key排序，降序map
		List<Map.Entry<String, List<DatRuleCondition>>> list1 = new ArrayList<Map.Entry<String, List<DatRuleCondition>>>(
				map.entrySet());
		Collections.sort(list1, new Comparator<Map.Entry<String, List<DatRuleCondition>>>() {
			// 降序排序
			public int compare(Entry<String, List<DatRuleCondition>> o1, Entry<String, List<DatRuleCondition>> o2) {
				if (o2.getKey() != null && o1.getKey() != null) {
					return o2.getKey().compareTo(o1.getKey());
				} else {
					if (o2.getKey() == null) {
						return 1;
					} else {
						return -1;
					}
				}
			}

		});

		return list1;
	}

}
