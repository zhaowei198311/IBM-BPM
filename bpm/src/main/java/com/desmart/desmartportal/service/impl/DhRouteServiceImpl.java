package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DatRuleConditionService;
import com.desmart.desmartbpm.service.DatRuleService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DroolsEngineService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartportal.dao.DhGatewayRouteResultMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhGatewayRouteResult;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.ArrayUtil;

@Service
public class DhRouteServiceImpl implements DhRouteService {
	private Logger log = Logger.getLogger(DhRouteServiceImpl.class);
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	@Autowired
	private DhActivityAssignMapper dhActivityAssignMapper;

	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;

	@Autowired
	private BpmActivityMetaMapper bpmActivityMetaMapper;

	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysTeamMemberMapper sysTeamMemberMapper;

	@Autowired
	private DhGatewayLineService dhGatewayLineService;
	@Autowired
	private DatRuleConditionService datRuleConditionService;
	@Autowired
	private DroolsEngineService droolsEngineService;
	@Autowired
	private DatRuleService datRuleService;
	@Autowired
	private DhGatewayRouteResultMapper dhGatewayRouteResultMapper;


	@Override
	public ServerResponse<List<BpmActivityMeta>> showRouteBar(String insUid, String activityId, String departNo,
			String companyNum, String formData) {
		// TODO Auto-generated method stub

		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		// 根据表单字段查
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insInitUser = dhProcessInstance.getInsInitUser(); // 流程发起人
		String insDate = dhProcessInstance.getInsData();// 实例表单

		JSONObject formJson = new JSONObject();
		
		
		JSONObject newObj = new JSONObject();
		if (StringUtils.isNotBlank(formData)) {
			newObj=JSONObject.parseObject(insDate);
		}
		formJson = FormDataUtil.formDataCombine(newObj,JSONObject.parseObject(insDate).getJSONObject("formData"));
		List<BpmActivityMeta> activityMetaList = getNextActivities(bpmActivityMeta, formJson);
		// 环节配置获取
		for (BpmActivityMeta activityMeta : activityMetaList) {
			DhActivityConf dhActivityConf = activityMeta.getDhActivityConf();
			String actcAssignType = dhActivityConf.getActcAssignType();
			DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
			String activity_id = dhActivityConf.getActivityId();
			if (assignTypeEnum == null) {
				return ServerResponse.createByErrorMessage("处理人类型不符合要求");
			}
			if (assignTypeEnum == DhActivityConfAssignType.NONE) {
				return ServerResponse.createBySuccess(activityMetaList);
			}
			DhActivityAssign selective = new DhActivityAssign();
			selective.setActivityId(activity_id);
			selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
			List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
			if (assignList.size() == 0) {
				return ServerResponse.createByErrorMessage("缺少处理人信息");
			}
			List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);

			String userUid = "";
			String userName = "";

			switch (assignTypeEnum) {
			case ROLE:
			case ROLE_AND_DEPARTMENT:
			case ROLE_AND_COMPANY:
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setRoleUid(ArrayUtil.toArrayString(idList));

				if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
					if (StringUtils.isNotBlank(departNo)) {
						roleUser.setCompanyCode(companyNum);
					}
				}
				if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
					if (StringUtils.isNotBlank(companyNum)) {
						roleUser.setDepartUid(departNo);
					}
				}

				List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
				for (SysRoleUser sysRoleUser : roleUsers) {
					userUid += sysRoleUser.getUserUid() + ";";
					userName += sysRoleUser.getUserName() + ";";
				}
				break;
			case TEAM:

			case TEAM_AND_DEPARTMENT:

			case TEAM_AND_COMPANY:
				SysTeamMember sysTeamMember = new SysTeamMember();

				if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_COMPANY)) {
					if (StringUtils.isNotBlank(departNo)) {
						sysTeamMember.setCompanyCode(companyNum);
					}
				}
				if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_DEPARTMENT)) {
					if (StringUtils.isNotBlank(companyNum)) {
						sysTeamMember.setDepartUid(departNo);
					}
				}

				sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));
				List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
					userUid += sysTeamMember2.getUserUid() + ";";
					userName += sysTeamMember2.getUserName() + ";";
				}
				break;
			case LEADER_OF_PRE_ACTIVITY_USER:
				
			case USERS:
				List<SysUser> userItem = sysUserMapper.listByPrimaryKeyList(idList);
				for (SysUser sysUser : userItem) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
				break;
			case PROCESS_CREATOR: // 流程发起人
				idList = new ArrayList<String>();
				idList.add(insInitUser);
				List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList);
				for (SysUser sysUser : userList) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
				break;
			case BY_FIELD://根据表单字段选
				if (assignList.size() > 0) {
					String field = assignList.get(0).getActaAssignId();
					dhActivityConf.setHandleField(field);
					JSONObject jsonObject=formJson.getJSONObject(field);
					idList=new ArrayList<String>(); 
					if (jsonObject!=null) {
						String value=jsonObject.getString("value");
						idList.add(value);
					}
					if(idList.size()>0) {
						List<SysUser> users = sysUserMapper.listByPrimaryKeyList(idList);
						for (SysUser sysUser : users) {
							userUid += sysUser.getUserUid() + ";";
							userName += sysUser.getUserName() + ";";
						}
					}
				}
				break;
			default:
				break;
			}
			activityMeta.setUserUid(userUid);
			activityMeta.setUserName(userName);
		}
		return ServerResponse.createBySuccess(activityMetaList);
	}

	@Override
	public List<BpmActivityMeta> getNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData) {
	    List<BpmActivityMeta> result = new ArrayList<>();
	    Map<String, Object> resultMap = bpmActivityMetaService.getNextToActivity(sourceActivityMeta, "");
	    List<BpmActivityMeta> normalList = (List<BpmActivityMeta>)resultMap.get("normal");
        List<BpmActivityMeta> gateAndlList = (List<BpmActivityMeta>)resultMap.get("gateAnd");
        List<BpmActivityMeta> endList = (List<BpmActivityMeta>)resultMap.get("end");
        List<BpmActivityMeta> gatewayList = (List<BpmActivityMeta>)resultMap.get("gateway");
	    
        result.addAll(normalList);
        result.addAll(gateAndlList);
        // 查看是否需要校验排他网关
        if (gatewayList.isEmpty()) {
            return result;
        }
        
        // 被排他网关排除的节点
        List<String> activityIdsToRemove = new ArrayList<>();
        
        // 遍历处理每个网关
        for (BpmActivityMeta gatewayMeta : gatewayList) {
            DhGatewayLine lineSelective = new DhGatewayLine();
            lineSelective.setActivityId(gatewayMeta.getActivityId());
            List<DhGatewayLine> lines = dhGatewayLineService.getGateWayLinesByCondition(lineSelective);
            // 获得相关的条件
            List<DatRuleCondition> conditions = datRuleConditionService.getDatruleConditionByActivityId(gatewayMeta.getActivityId());
            // 获得条件需要的表单中的变量
            Set<String> needVarnameSet = new HashSet<>();
            Map<String, String> varTypeMap = new HashMap<>();
            for (DatRuleCondition condition : conditions) {
                String varname = condition.getLeftValue();
                if (!needVarnameSet.contains(varname)) {
                    needVarnameSet.add(varname);
                    varTypeMap.put(varname, condition.getRightValueType());
                }
            }
            // 从表单中取出需要的参数
            org.json.JSONObject param = assembleJsonParam(needVarnameSet, varTypeMap, formData);
            
            if (param == null) {
                // 从表单中获取变量发生错误
                log.error("从表单中获取变量发生错误，网关id: " + gatewayMeta.getActivityId());
                // 走默认路径，需要排除非默认路径
                for (DhGatewayLine line : lines) {
                    if ("FALSE".equals(line.getIsDefault())){
                        addHumanActivityToRemoveList(activityIdsToRemove, line);
                    }
                }
                continue;
            }
            
            // 计算每种规则的结果
            List<DhGatewayLine> unDefaultLines = getUnDefaultLines(lines);
            String fittedLineId = null; // 满足规则的连接线id
            for (DhGatewayLine line : unDefaultLines) {
                String ruleId = line.getRuleId();
                DatRule datRule = datRuleService.getDatRuleByKey(ruleId);
                Map<String, Object> ruleResult = null;
                try {
                    ruleResult = droolsEngineService.execute(param, datRule);
                    if (ruleResult.get("state") != null && ruleResult.get("state").equals(true)) {
                        // 规则运算成功
                        fittedLineId = line.getGatewayLineUid();
                        break;
                    }
                } catch (Exception e) {
                   log.error("规则运算异常,规则编号：" + ruleId, e); 
                }
            }
            if (fittedLineId == null) {
                // 如果没有满足规则的连线，从人工环节列表中去除所有不是默认路线的节点
                for (DhGatewayLine line : unDefaultLines) {
                    addHumanActivityToRemoveList(activityIdsToRemove, line);
                }
            } else {
                // 如果有连线满足规则， 从人工环节列表中除去不是这个线相连的节点
                for (DhGatewayLine line : lines) {
                    if (!fittedLineId.equals(line.getGatewayLineUid())) {
                        addHumanActivityToRemoveList(activityIdsToRemove, line);
                    }
                }
            }
            
        } // 遍历处理网关结束
	    // 移除多余的节点
        Iterator<BpmActivityMeta> iterator = result.iterator();
        while (iterator.hasNext()) {
            BpmActivityMeta meta = iterator.next();
            if (activityIdsToRemove.contains(meta.getActivityId())) {
                iterator.remove();
            }
        }
	    return result;
	}

	/**
	 * 将连线连接的人工环节加入移除列表，如果连线连接的不是人工环节，连接点后续的人工环节加入移除列表
	 * @param activityIdsToRemove
	 * @param line
	 */
    private void addHumanActivityToRemoveList(List<String> activityIdsToRemove, DhGatewayLine line) {
        BpmActivityMeta activityMeta = bpmActivityMetaMapper.queryByPrimaryKey(line.getToActivityId());
        if (isHumanActivity(activityMeta)) {
            activityIdsToRemove.add(line.getToActivityId());
        } else {
            List<BpmActivityMeta> nextToMetaList = bpmActivityMetaService.getNextToActivity(activityMeta);
            if (nextToMetaList.size() > 0) {
                for (BpmActivityMeta item : nextToMetaList) {
                    activityIdsToRemove.add(item.getActivityId());
                }
            }
        }
    }
	
	private org.json.JSONObject assembleJsonParam(Set<String> needvarNameList, Map<String, String> varTypeMap, JSONObject formData) {
	    org.json.JSONObject param = new org.json.JSONObject();
	    for (String varname : needvarNameList) {
	        String stringValue = FormDataUtil.getStringValue(varname, formData);
	        if (stringValue == null) {
	            return null;
	        }
	        String type = varTypeMap.get(varname);
	        if (DatRuleCondition.RIGHT_VALUE_TYPE_STRING.equals(type)) {
	            // 如果是字符串
	            param.put(varname, stringValue);
	        } else {
	            // 如果是数字
	            try {
                    Double num = Double.parseDouble(stringValue);
                    param.put(varname, num);
                } catch (NumberFormatException e) {
                    return null;
                }
	        }
	    } 
	    return param;
	}

	private List<DhGatewayLine> getUnDefaultLines(List<DhGatewayLine> lineList) {
	    List<DhGatewayLine> unDefaultLines = new ArrayList<>();
	    Iterator<DhGatewayLine> iterator = lineList.iterator();
	    while (iterator.hasNext()) {
	        DhGatewayLine line = iterator.next();
	        if ("FALSE".equals(line.getIsDefault())) {
	            unDefaultLines.add(line);
	        }
	    }
	    return unDefaultLines;
	}

	private DhGatewayLine getDefaultLine(List<DhGatewayLine> lineList) {
        Iterator<DhGatewayLine> iterator = lineList.iterator();
        while (iterator.hasNext()) {
            DhGatewayLine line = iterator.next();
            if ("TRUE".equals(line.getIsDefault())) {
                return line;
            }
        }
        return null;
    }
	
	/**
	 * 判断节点是否人工服务节点
	 * @param meta
	 * @return
	 */
	private boolean isHumanActivity(BpmActivityMeta meta) {
	    if ("activity".equals(meta.getActivityType()) && "UserTask".equals(meta.getBpmTaskType()) && "activity".equals(meta.getType())) {
	        return true;
	    } else {
	        return false;
	    }
	}
	@Override
	public ServerResponse<CommonBusinessObject> assembleCommonBusinessObject(CommonBusinessObject pubBo, JSONArray routeData) {
        if (pubBo == null) {
            pubBo = new CommonBusinessObject();
        }
        
        for (int i=0; i<routeData.size(); i++) {
            JSONObject item = (JSONObject)routeData.get(i);
            String activityId = item.getString("activityId");
            String userUids = item.getString("userUid");
            String assignVarName = item.getString("assignVarName");
            String signCountVarName = item.getString("signCountVarName");
            String loopType = item.getString("loopType");
            
            if (StringUtils.isBlank(activityId) || StringUtils.isBlank(userUids) || StringUtils.isBlank(assignVarName) || StringUtils.isBlank(signCountVarName)
                    || StringUtils.isBlank(loopType)) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            
            List<String> userIdList = Arrays.asList(userUids.split(";"));
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(userIdList); 
            if (userIdList.size() != userList.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            // 设置处理人
            CommonBusinessObjectUtils.setNextOwners(assignVarName, pubBo, userIdList);
            // 如果是多实例会签或者简单循环会签就设置signCount值
            if (BpmActivityMeta.LOOP_TYPE_SIMPLE_LOOP.equals(loopType) || BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
                CommonBusinessObjectUtils.setOwnerSignCount(signCountVarName, pubBo, userIdList.size());
            }
            
        }
        
        return ServerResponse.createBySuccess(pubBo);
    }
	
	public ServerResponse updateGatewayRouteResult(BpmActivityMeta currActivityMeta, Integer insId, JSONObject formData) {
	    Map<String, Object> resultMap = bpmActivityMetaService.getNextToActivity(currActivityMeta, "");
	    List<BpmActivityMeta> gatewayList = (List<BpmActivityMeta>)resultMap.get("gateway");
	    
	    for (BpmActivityMeta gatewayMeta : gatewayList) {
	        DhGatewayLine lineSelective = new DhGatewayLine();
            lineSelective.setActivityId(gatewayMeta.getActivityId());
            List<DhGatewayLine> lines = dhGatewayLineService.getGateWayLinesByCondition(lineSelective);
            // 获得相关的条件
            List<DatRuleCondition> conditions = datRuleConditionService.getDatruleConditionByActivityId(gatewayMeta.getActivityId());
            // 获得条件需要的表单中的变量
            Set<String> needVarnameSet = new HashSet<>();
            Map<String, String> varTypeMap = new HashMap<>();
            for (DatRuleCondition condition : conditions) {
                String varname = condition.getLeftValue();
                if (!needVarnameSet.contains(varname)) {
                    needVarnameSet.add(varname);
                    varTypeMap.put(varname, condition.getRightValueType());
                }
            }
            // 从表单中取出需要的参数
            org.json.JSONObject param = assembleJsonParam(needVarnameSet, varTypeMap, formData);
            if (param == null) {
                // 从表单中获取变量发生错误, 不保存/更新值
                log.error("从表单中获取变量发生错误，网关id: " + gatewayMeta.getActivityId());
                continue;
            }
            // 计算每种规则的结果
            List<DhGatewayLine> unDefaultLines = getUnDefaultLines(lines);
            String fittedLineId = null; // 满足规则的连接线id
            for (DhGatewayLine line : unDefaultLines) {
                String ruleId = line.getRuleId();
                DatRule datRule = datRuleService.getDatRuleByKey(ruleId);
                Map<String, Object> ruleResult = null;
                try {
                    ruleResult = droolsEngineService.execute(param, datRule);
                    if (ruleResult.get("state") != null && ruleResult.get("state").equals(true)) {
                        // 规则运算成功，更新或新增结果
                        fittedLineId = line.getGatewayLineUid();
                        String outputValue = line.getRouteResult();
                        DhGatewayRouteResult routeResult = new DhGatewayRouteResult();
                        routeResult.setInsId(insId);
                        routeResult.setActivityBpdId(gatewayMeta.getActivityBpdId());
                        routeResult.setRouteResult(outputValue);
                        int updateCount = dhGatewayRouteResultMapper.updateRouteResultByInsIdAndActivityBpdId(routeResult);
                        if (updateCount == 0) {
                            routeResult.setRouteResultUid(EntityIdPrefix.DH_GATEWAY_ROUTE_RESULT + UUID.randomUUID().toString());
                            routeResult.setStatus("on");
                            dhGatewayRouteResultMapper.save(routeResult);
                        }
                        break;
                    }
                } catch (Exception e) {
                   log.error("规则运算异常,规则编号：" + ruleId, e); 
                }
            }
            if (fittedLineId == null) {
                // 删除表中相应的记录
                dhGatewayRouteResultMapper.deleteByInsIdAndActivityBpdId(insId, gatewayMeta.getActivityBpdId());
            }
	    } // 遍历网关结束
	    return ServerResponse.createBySuccess();
	}
	
}
