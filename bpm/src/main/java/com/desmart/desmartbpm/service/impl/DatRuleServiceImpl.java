package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.dao.DatRuleMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DatRuleConditionService;
import com.desmart.desmartbpm.service.DatRuleService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartbpm.util.UUIDTool;

@Service
public class DatRuleServiceImpl implements DatRuleService {

	@Resource
	private DatRuleMapper datRuleMapper;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	@Autowired
	private DhGatewayLineService dhGatewayLineServiceImpl;
	@Autowired
	private DatRuleConditionService datRuleConditionServiceImpl;
	
	@Override
	public DatRule getDatRuleByKey(String ruleId) {

		return datRuleMapper.getDatRuleByKey(ruleId);
	}

	@Override
	public Integer insertToDatRule(DatRule datRule) {

		return datRuleMapper.insertToDatRule(datRule);
	}
	
	@Override
	public Integer updateDatRule(DatRule datRule) {

		return datRuleMapper.updateDatRule(datRule);
	}

	@Override
	public List<DatRule> getPreRulesLikeRuleName(String activityId) {

		return datRuleMapper.getPreRulesLikeRuleName(activityId);
	}
	
	@Override
	public int deleteDatRule(DatRule datRule) {

		return datRuleMapper.deleteDatRule(datRule);
	}

	/**
	 * 循环分组(按照groupName)，降序map
	 * 
	 * @param list
	 * @return
	 */
	private List<Map.Entry<String, List<DatRuleCondition>>> groupListToMap(List<DatRuleCondition> list) {

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
	
	@Override
	public ServerResponse loadGatewaySet(String proAppId, String snapshotId, String bpdId,
			String activityType) {

		List<BpmActivityMeta> list = 
				bpmActivityMetaServiceImpl.getBpmActivityMetaByActivityType(proAppId, snapshotId, bpdId, activityType);
		
		// 过滤掉外链流程的网关节点
		Iterator<BpmActivityMeta> iterator = list.iterator();
		while(iterator.hasNext()) {
		    BpmActivityMeta meta = iterator.next();
		    if (!meta.getActivityId().equals(meta.getSourceActivityId())) {
		        iterator.remove();
		    }
		}
		
		List<Map<String, Object>> rightDetailsList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list!=null&&list.size()>0) {
			BpmActivityMeta bpmActivityMeta = list.get(0);//默认展示第一个
			rightDetailsList = loadRightDetailsList(bpmActivityMeta.getActivityId());
			map.put("leftMenus", list);
			map.put("rightDetailsList", rightDetailsList);
		}
		return ServerResponse.createBySuccess(map);
	}
	@Override
	public List<Map<String,Object>> loadRightDetailsList(String activityId){
		List<Map<String,Object>> rightDetailsList = new ArrayList<Map<String,Object>>();
		DhGatewayLine dhGatewayLine = new DhGatewayLine();
		dhGatewayLine.setActivityId(activityId);
		List<DhGatewayLine> dhGatewayLines = dhGatewayLineServiceImpl.getGateWayLinesByCondition(dhGatewayLine);
		
		
		for (DhGatewayLine dhGatewayLine2 : dhGatewayLines) {//循环网关连接线集合
			if("FALSE".equals(dhGatewayLine2.getIsDefault())) {//不是默认连接线
				
				Map<String, Object> son = new HashMap<String, Object>();
					//根据ruleId查询predictRules展示
					DatRule datRule = getDatRuleByKey(dhGatewayLine2.getRuleId());
					//根据当前ruleId查询所有dat_rule_condition展示,需要按照分组名排序
					List<DatRuleCondition> datRuleConditions = datRuleConditionServiceImpl.getDatruleConditionByRuleId(dhGatewayLine2.getRuleId());
					
					son.put("DatConditionList", datRuleConditions);
					son.put("PredictRule", datRule);
					Map<String,Object> sonMap = new HashMap<String,Object>();
					String target = dhGatewayLine2.getToActivityName();
					sonMap.put(target, son);
					rightDetailsList.add(sonMap);
			}else {
				Map<String,Object> sonMap = new HashMap<String,Object>();
				String target = dhGatewayLine2.getToActivityName();
				sonMap.put(target, "default");
				rightDetailsList.add(sonMap);
			}
		}
		return rightDetailsList;
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public ServerResponse saveOrUpdateDatRule(DatRuleCondition datRuleCondition,String activityId
			,String oldRuleId) {

		LinkedList<DatRuleCondition> linkedList = new LinkedList<DatRuleCondition>();
		if(datRuleCondition.getConditionId()!=null&&!"".equals(datRuleCondition.getConditionId())) {
			ServerResponse updateReponse = datRuleConditionServiceImpl.update(datRuleCondition);
			if(!updateReponse.isSuccess()) {
				return ServerResponse.createByErrorMessage("修改网关规则异常！");
			}
		}
		linkedList = datRuleConditionServiceImpl.getDatruleConditionByRuleId(datRuleCondition.getRuleId());
		if(datRuleCondition.getConditionId()==null||"".equals(datRuleCondition.getConditionId())) {
			linkedList.addLast(datRuleCondition);
		}
		String ruleProcess = createRuleProcess(linkedList);
		DatRule datRule = new DatRule();
		datRule.setRuleId(datRuleCondition.getRuleId());
		datRule.setRuleProcess(ruleProcess);
		Integer count = datRuleMapper.updateDatRule(datRule);//修改
		if(count>0) {
			if(datRuleCondition.getConditionId()!=null&&!"".equals(datRuleCondition.getConditionId())) {
				if(!datRuleCondition.getRuleId().equals(oldRuleId)) {
					this.updateDatruleByDelete(oldRuleId);
				}
				List<DatRuleCondition> list = datRuleConditionServiceImpl.getDatruleConditionByActivityId(activityId);
				Map<String, Object> data = new HashMap<String,Object>();
				data.put("DatConditionList", list);
				data.put("PredictRule", datRule);
				data.put("oldRule", datRuleMapper.getDatRuleByKey(oldRuleId));
				return ServerResponse.createBySuccess("修改网关规则成功！", data);
			}else {
				datRuleCondition.setConditionId("rulecond:" + UUIDTool.getUUID());
				if(datRuleConditionServiceImpl.insert(datRuleCondition)>0) {
					List<DatRuleCondition> list = datRuleConditionServiceImpl.getDatruleConditionByActivityId(activityId);
					Map<String, Object> data = new HashMap<String,Object>();
					data.put("DatConditionList", list);
					data.put("PredictRule", datRule);

					return ServerResponse.createBySuccess("新增网关规则成功！", data);
				}else {
					return ServerResponse.createByErrorMessage("添加网关规则异常！");
				}
			}
		}else {
			return ServerResponse.createByErrorMessage("添加网关规则异常！");
		}
	}

	private String createRuleProcess(LinkedList<DatRuleCondition> linkedList) {
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
		return sb.toString();
	}

	@Override
	@Transactional(rollbackFor= {RuntimeException.class,Exception.class})
	public ServerResponse deleteDatRule(List<DatRuleCondition> datRuleConditions,String activityId) {

		Map<String, List<DatRuleCondition>> datRuleMap = groupListToMapByRuleId(datRuleConditions);
		Integer count = 0;
		for (String key : datRuleMap.keySet()) {
			datRuleConditionServiceImpl.batchDelete(datRuleConditions);
			count += updateDatruleByDelete(key);
		}
		if(count>0) {
			List<DatRuleCondition> list = datRuleConditionServiceImpl.getDatruleConditionByActivityId( activityId);
			List<DatRule> datRules = datRuleMapper.batchSelectDatRule(datRuleMap.keySet());
			Map<String, Object> data = new HashMap<String,Object>();
			data.put("DatConditionList", list);
			data.put("PredictRules", datRules);
			return ServerResponse.createBySuccess("删除成功", data);
		}else {
			return ServerResponse.createByErrorMessage("删除异常！");
		}
	}
	//删除调用的修改规则方法
	private Integer updateDatruleByDelete(String ruleId) {

		LinkedList<DatRuleCondition> linkedList = new LinkedList<DatRuleCondition>();
		linkedList = datRuleConditionServiceImpl.getDatruleConditionByRuleId(ruleId);
		DatRule datRule = new DatRule();
		if(linkedList.size()>0) {
			datRule.setRuleProcess(createRuleProcess(linkedList));
		}else {
			datRule.setRuleProcess(null);
		}
		datRule.setRuleId(ruleId);
		return datRuleMapper.updateDatRule(datRule);//修改
	}

	/**
	 * 根据ruleId分组
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, List<DatRuleCondition>> groupListToMapByRuleId(List<DatRuleCondition> list) {

		Map<String, List<DatRuleCondition>> map = new HashMap<>();
		for (DatRuleCondition datRuleCondition : list) {
			if(datRuleCondition!=null) {
			String key = datRuleCondition.getRuleId();
			List<DatRuleCondition> sonList = new ArrayList<>();
			for (DatRuleCondition son : list) {
				if(son!=null) {
				if (key != null) {
					if (key.equals(son.getRuleId())) {
						sonList.add(son);
					}
				}
				}
			}
			map.put(key, sonList);
		  }
		}
		return map;
	}

	@Override
	public List<DatRule> listByRuleIdList(List<String> ruleIdList) {
		if (ruleIdList == null || ruleIdList.isEmpty()) {
			return new ArrayList<>();
		}
		return datRuleMapper.listByRuleIds(ruleIdList);
	}

	@Override
	public int removeByRuleIdList(List<String> ruleIdList) {
		if (CollectionUtils.isEmpty(ruleIdList)) {
			return 0;
		}
		return datRuleMapper.deleteByRuleIds(ruleIdList);
	}

}
