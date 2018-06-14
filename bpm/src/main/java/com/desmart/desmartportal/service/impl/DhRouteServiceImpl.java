package com.desmart.desmartportal.service.impl;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.desmart.common.exception.BpmFindNextNodeException;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.dao.DhTaskHandlerMapper;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartportal.entity.*;
import com.desmart.desmartsystem.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DatRuleConditionService;
import com.desmart.desmartbpm.service.DatRuleService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartbpm.service.DhTriggerService;
import com.desmart.desmartbpm.service.DroolsEngineService;
import com.desmart.desmartportal.dao.DhGatewayRouteResultMapper;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.dao.DhRoutingRecordMapper;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
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
	private DhActivityConfMapper dhActivityConfMapper;

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
	@Autowired
	private DhRoutingRecordMapper dhRoutingRecordMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private DhTriggerService dhTriggerService;
	@Autowired
	private SysDepartmentMapper sysDepartmentMapper;
	@Autowired
    private DhTaskHandlerMapper dhTaskHandlerMapper;

	@Override
	public ServerResponse<List<BpmActivityMeta>> showRouteBar(String taskUid, String insUid, String activityId,
			String departNo, String companyNum, String formData) {
		// 如果当前任务已添加会签任务，则不能执行提交操作
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		if (dhTaskInstance != null) {
			if (DhTaskInstance.STATUS_WAIT_ADD.equals(dhTaskInstance.getTaskStatus())) {
				return ServerResponse.createByErrorMessage("当前任务已经暂挂，请等会签人审批结束!");
			}
		}
		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		// 根据表单字段查
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		
		String insInitUser = dhProcessInstance.getInsInitUser(); // 流程发起人
		String insDate = dhProcessInstance.getInsData();// 实例表单

		JSONObject formJson = new JSONObject();
		JSONObject newObj = new JSONObject();
		if (StringUtils.isNotBlank(formData)) {
			newObj = JSONObject.parseObject(formData);
		}
		JSONObject oldObj = JSONObject.parseObject(insDate).getJSONObject("formData");
		formJson = FormDataUtil.formDataCombine(newObj, oldObj);
		List<BpmActivityMeta> activityMetaList = getNextActivitiesForRoutingBar(bpmActivityMeta, formJson);
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
				// return ServerResponse.createByErrorMessage("缺少处理人信息");
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
						StringBuffer str = new StringBuffer(departNo);
						String result = recursionSelectDepartMent(departNo,str);
						
						roleUser.setDepartUid(result);
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
						if (StringUtils.isNotBlank(companyNum)) {
							StringBuffer str = new StringBuffer(departNo);
							String result = recursionSelectDepartMent(departNo,str);
							sysTeamMember.setDepartUid(result);
						}
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

				ServerResponse<BpmActivityMeta> serverResponse = getPreActivity(dhProcessInstance, bpmActivityMeta);
				BpmActivityMeta activityMetaNew = serverResponse.getData();
				if (activityMetaNew != null) {
					String activityMetaUserUid = activityMetaNew.getUserUid();
					SysUser previousActivityUser = sysUserMapper.queryByPrimaryKey(activityMetaUserUid);// 上个环节处理人
					SysUser leader = sysUserMapper.queryByPrimaryKey(previousActivityUser.getUserId());// 上个环节处理人上级
					userUid += leader.getManagernumber() + ";";
					userName += leader.getUserName() + ";";
				}
				break;
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
			case BY_FIELD:// 根据表单字段选
				if (assignList.size() > 0) {
					String field = assignList.get(0).getActaAssignId();
					dhActivityConf.setHandleField(field);
					JSONObject jsonObject = formJson.getJSONObject(field);
					idList = new ArrayList<String>();
					if (jsonObject != null) {
						String value = jsonObject.getString("value");
						idList.add(value);
					}
					if (idList.size() > 0) {
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

			// 去除重复的值
			String newUserUid = "";
			String newUserName = "";
			if (StringUtils.isNotBlank(userUid)) {
				String userUidArray[] = userUid.split(";");
				String userNameArray[] = userName.split(";");
				Set<String> set = new HashSet<String>();
				for (int i = 0; i < userUidArray.length; i++) {
					String userUidNext = userUidArray[i];
					String useNameNext = userNameArray[i];
					if (set.add(userUidNext)) {
						newUserUid += userUidNext + ";";
						newUserName += useNameNext + ";";
					}

				}
			}
			activityMeta.setUserUid(newUserUid);
			activityMeta.setUserName(newUserName);
		}
		return ServerResponse.createBySuccess(activityMetaList);
	}

	private String recursionSelectDepartMent(String departNo,StringBuffer str) {
		SysDepartment department = sysDepartmentMapper.getSysDepartmentByDepartNo(departNo);
		if(department==null||"".equals(department.getDepartParent())) {
			return str.toString();
		}else {
			str.append(","+department.getDepartParent());
			return recursionSelectDepartMent(department.getDepartParent(), str);
		}
	}

	@Override
	public List<BpmActivityMeta> getNextActivitiesForRoutingBar(BpmActivityMeta sourceActivityMeta, JSONObject formData) {
		List<BpmActivityMeta> result = new ArrayList<>();
		BpmRoutingData routingData = getRoutingDataOfNextActivityTo(sourceActivityMeta, formData);
        if (routingData.getMainEndNodes().size() > 0) {
			// 如果主流程结束，返回空集合
			return result;
		}
		// 如果source节点所属的流程结束了，不需要选后面的人，不然需要选择后面的人
        if (routingData.getEndProcessNodes().size() == 0) {
            result = new ArrayList<>(routingData.getNormalNodes());
        } else if (!"0".equals(sourceActivityMeta.getParentActivityId())) {

            BpmActivityMeta parentMeta = new BpmActivityMeta(sourceActivityMeta.getParentActivityId());
            if (routingData.getEndProcessNodes().contains(parentMeta)) {
                // 如果sourceMeta对应的流程已经结束，不需要选择下个环节的人

            } else {
                result = new ArrayList<>(routingData.getNormalNodes());
            }
        }
		return result;
	}

	@Override
	public List<BpmActivityMeta> getNextActiviesForRoutingRecord(BpmActivityMeta sourceActivityMeta, BpmRoutingData routingData) {
        List<BpmActivityMeta> result = new ArrayList<>();
        if (routingData.getMainEndNodes().size() > 0) {
            // 如果主流程结束，返回空集合
            return result;
        }
        // 如果source节点所属的流程结束了，不需要选后面的人，不然需要选择后面的人
        if (routingData.getEndProcessNodes().size() == 0) {
            result = new ArrayList<>(routingData.getNormalNodes());
        } else if (!"0".equals(sourceActivityMeta.getParentActivityId())) {

            BpmActivityMeta parentMeta = new BpmActivityMeta(sourceActivityMeta.getParentActivityId());
            if (routingData.getEndProcessNodes().contains(parentMeta)) {
                // 如果sourceMeta对应的流程已经结束，不需要选择下个环节的人

            } else {
                result = new ArrayList<>(routingData.getNormalNodes());
            }
        }
        return result;
    }

	public Set<BpmActivityMeta> getActualNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData) {
        BpmRoutingData routingData = getRoutingDataOfNextActivityTo(sourceActivityMeta, formData);
        return routingData.getNormalNodes();
    }

	/**
	 * 将连线连接的人工环节加入移除列表，如果连线连接的不是人工环节，连接点后续的人工环节加入移除列表
	 * 
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

	/**
	 * 从表单中取出网关判断需要的参数
	 * 
	 * @param needvarNameList
	 * @param varTypeMap
	 * @param formData
	 * @return
	 */
	private org.json.JSONObject assembleJsonParam(Set<String> needvarNameList, Map<String, String> varTypeMap,
			JSONObject formData) {
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

	/**
	 * 从所有连线中过的默认连线
	 * 
	 * @param lineList
	 * @return
	 */
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
	 * 
	 * @param meta
	 * @return
	 */
	private boolean isHumanActivity(BpmActivityMeta meta) {
		if ("activity".equals(meta.getActivityType()) && "UserTask".equals(meta.getBpmTaskType())
				&& "activity".equals(meta.getType())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ServerResponse<CommonBusinessObject> assembleCommonBusinessObject(CommonBusinessObject pubBo,
			JSONArray routeData) {
		if (pubBo == null) {
			pubBo = new CommonBusinessObject();
		}

		for (int i = 0; i < routeData.size(); i++) {
			JSONObject item = (JSONObject) routeData.get(i);
			String activityId = item.getString("activityId");
			String userUids = item.getString("userUid");
			String assignVarName = item.getString("assignVarName");
			String signCountVarName = item.getString("signCountVarName");
			String loopType = item.getString("loopType");

			if (StringUtils.isBlank(activityId) || StringUtils.isBlank(userUids) || StringUtils.isBlank(assignVarName)
					|| StringUtils.isBlank(signCountVarName) || StringUtils.isBlank(loopType)) {
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
			if (BpmActivityMeta.LOOP_TYPE_SIMPLE_LOOP.equals(loopType)
					|| BpmActivityMeta.LOOP_TYPE_MULTI_INSTANCE_LOOP.equals(loopType)) {
				CommonBusinessObjectUtils.setOwnerSignCount(signCountVarName, pubBo, userIdList.size());
			}

		}
		return ServerResponse.createBySuccess(pubBo);
	}

	@Override
	public List<DhTaskHandler> getTaskHandlerOfSimpleLoopTask (int insId, JSONArray routeData) {
	    List<DhTaskHandler> dhTaskHandlers = new ArrayList<>();
        for (int i = 0; i < routeData.size(); i++) {
            JSONObject item = (JSONObject) routeData.get(i);
            String activityId = item.getString("activityId");
            String userUids = item.getString("userUid");
            String loopType = item.getString("loopType");

            if (DhTaskInstance.TYPE_SIMPLE_LOOP.equals(loopType)) {
                List<String> userIdList = Arrays.asList(userUids.split(";"));
                List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(userIdList);
                if (userIdList.size() != userList.size()) {
                    throw new PlatformException("处理人信息异常");
                }
                for (String userUid : userIdList) {
                    DhTaskHandler dhTaskHandler = new DhTaskHandler();
                    dhTaskHandler.setHandleUid(EntityIdPrefix.DH_TASK_HANDLER + UUID.randomUUID().toString());
                    dhTaskHandler.setUserUid(userUid);
                    dhTaskHandler.setInsId(Long.valueOf(insId));
                    dhTaskHandler.setTaskActivityId(activityId);
                    dhTaskHandler.setStatus("on");
                    dhTaskHandlers.add(dhTaskHandler);
                }
            }
        }
        return dhTaskHandlers;
    }



	@Override
	public ServerResponse<List<SysUser>> choosableHandler(String insUid, String activityId, String departNo,
			String companyNum, String formData, HttpServletRequest request) {
		List<SysUser> userListToBeReturned = new ArrayList<SysUser>();

		BpmActivityMeta bpmActivityMeta = bpmActivityMetaMapper.queryByPrimaryKey(activityId);

		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
		// 根据表单字段查
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insInitUser = dhProcessInstance.getInsInitUser(); // 流程发起人
		String insDate = dhProcessInstance.getInsData();// 实例表单

		JSONObject formJson = new JSONObject();
		JSONObject newObj = new JSONObject();
		if (StringUtils.isNotBlank(formData)) {
			newObj = JSONObject.parseObject(formData);
		}
		formJson = FormDataUtil.formDataCombine(newObj, JSONObject.parseObject(insDate).getJSONObject("formData"));

		String actcAssignType = dhActivityConf.getActcChooseableHandlerType();
		DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
		String activityIdnew = dhActivityConf.getActivityId();
		if (assignTypeEnum == null) {
			return ServerResponse.createByErrorMessage("处理人类型不符合要求");
		}
		if (assignTypeEnum == DhActivityConfAssignType.NONE) {
			return ServerResponse.createBySuccess(userListToBeReturned);
		}
		DhActivityAssign selective = new DhActivityAssign();
		selective.setActivityId(activityIdnew);
		selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
		List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
		if (assignList.size() == 0) {
			// return ServerResponse.createByErrorMessage("缺少处理人信息");
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
					StringBuffer str = new StringBuffer(departNo);
					String result = recursionSelectDepartMent(departNo,str);
					roleUser.setDepartUid(result);
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
					StringBuffer str = new StringBuffer(departNo);
					String result = recursionSelectDepartMent(departNo,str);
					sysTeamMember.setDepartUid(result);
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
			ServerResponse<BpmActivityMeta> serverResponse = getPreActivity(dhProcessInstance, bpmActivityMeta);
			BpmActivityMeta activityMeta = serverResponse.getData();
			String activityMetaUserUid = activityMeta.getUserUid();
			SysUser previousActivityUser = sysUserMapper.queryByPrimaryKey(activityMetaUserUid);// 上个环节处理人
			SysUser leader = sysUserMapper.queryByPrimaryKey(previousActivityUser.getUserId());// 上个环节处理人上级
			userUid += leader.getManagernumber() + ";";
			userName += leader.getUserName() + ";";
			break;
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
		case BY_FIELD:// 根据表单字段选
			if (assignList.size() > 0) {
				String field = assignList.get(0).getActaAssignId();
				dhActivityConf.setHandleField(field);
				JSONObject jsonObject = formJson.getJSONObject(field);
				idList = new ArrayList<String>();
				if (jsonObject != null) {
					String value = jsonObject.getString("value");
					idList.add(value);
				}
				if (idList.size() > 0) {
					List<SysUser> users = sysUserMapper.listByPrimaryKeyList(idList);
					for (SysUser sysUser : users) {
						userUid += sysUser.getUserUid() + ";";
						userName += sysUser.getUserName() + ";";
					}
				}
			}
			break;
		case BY_TRIGGER:// 根据触发器选择
			String triUid = idList.get(0);
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getServletContext());
			ServerResponse serverResponse2 = dhTriggerService.invokeChooseUserTrigger(webApplicationContext, insUid,
					triUid);
			if (serverResponse2.isSuccess()) {
				List<String> userIds = (List<String>) serverResponse2.getData();
				List<SysUser> userList1 = sysUserMapper.listByPrimaryKeyList(userIds);
				for (SysUser sysUser : userList1) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
			} else {
				return serverResponse2;
			}
			break;
		default:
			break;
		}

		// 去除重复的值
		String newUserUid = "";
		String newUserName = "";
		if (StringUtils.isNotBlank(userUid)) {
			String userUidArray[] = userUid.split(";");
			String userNameArray[] = userName.split(";");
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < userUidArray.length; i++) {
				String userUidNext = userUidArray[i];
				String useNameNext = userNameArray[i];
				if (set.add(userUidNext)) {
					SysUser sysUser = new SysUser();
					sysUser.setUserUid(userUidNext);
					sysUser.setUserName(useNameNext);
					userListToBeReturned.add(sysUser);
				}
			}
		}
		return ServerResponse.createBySuccess(userListToBeReturned);
	}

    @Override
    public ServerResponse updateGatewayRouteResult(Integer insId, BpmRoutingData routingData) {
        Set<BpmActivityMeta> gatewayNodes = routingData.getGatewayNodes();
        // 删除这些网关对应的路由结果
        for (BpmActivityMeta gatewayNode : gatewayNodes) {
            dhGatewayRouteResultMapper.deleteByInsIdAndActivityBpdId(insId, gatewayNode.getActivityBpdId());
        }
        // 将新的结果插入
        if (routingData.getRouteResults().size() > 0) {
            for (DhGatewayRouteResult routeResult : routingData.getRouteResults()) {
                routeResult.setInsId(insId);
                dhGatewayRouteResultMapper.save(routeResult);
            }
        }
        return ServerResponse.createBySuccess();
    }

	@Override
	public ServerResponse<BpmActivityMeta> getPreActivity(DhProcessInstance dhProcessInstance,
			BpmActivityMeta bpmActivityMeta) {
		// 查找流转到这个环节的流转记录
		List<DhRoutingRecord> preRoutingRecordList = dhRoutingRecordMapper
				.listPreRoutingRecord(dhProcessInstance.getInsUid(), bpmActivityMeta.getActivityId());
		if (preRoutingRecordList.size() == 0) {
			return ServerResponse.createByErrorMessage("获取上个环节失败");
		}
		DhRoutingRecord preRoutingRecord = preRoutingRecordList.get(0);
		BpmActivityMeta preMeta = bpmActivityMetaMapper.queryByPrimaryKey(preRoutingRecord.getActivityId());
		// 从任务列表中找，这个流程实例的 这个节点的任务，并且任务处理人是路由记录中的人
		List<DhTaskInstance> taskList = dhTaskInstanceMapper.listTaskByCondition(dhProcessInstance.getInsUid(),
				preMeta.getActivityBpdId(), preRoutingRecord.getUserUid());
		if (taskList.size() == 0) {
			return ServerResponse.createByErrorMessage("获取上个环节失败");
		}
		preMeta.setUserUid(preRoutingRecord.getUserUid());
		SysUser preUser = sysUserMapper.queryByPrimaryKey(preRoutingRecord.getUserUid());
		preMeta.setUserName(preUser.getUserName());
		return ServerResponse.createBySuccess(preMeta);
	}

	@Override
    public BpmRoutingData getRoutingDataOfNextActivityTo(BpmActivityMeta sourceNode, JSONObject formData) {
        BpmRoutingData result = new BpmRoutingData();
        result.setSourceNode(sourceNode);

        List<BpmActivityMeta> directNextNodeList = findDirectNextNodes(sourceNode);

        // 遍历处理与源节点直接关联的节点
        for (BpmActivityMeta directNextNode : directNextNodeList) {
            String type = directNextNode.getType();
            String activityType = directNextNode.getActivityType();
            String bpmTaskType = directNextNode.getBpmTaskType();

            if ("activity".equals(activityType) && BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(bpmTaskType) && "activity".equals(type)) {
                // 人员服务
                result.addNormalNode(directNextNode);
            } else if ("activity".equals(activityType) && (BpmActivityMeta.BPM_TASK_TYPE_SUB_PROCESS.equals(bpmTaskType) || BpmActivityMeta.BPM_TASK_TYPE_CALLED_PROCESS.equals(bpmTaskType))
                    && "activity".equals(type)) {
                // 子流程
                result.addStartProcessNode(directNextNode);
                List<BpmActivityMeta> sonNodes = findDirectNextNodesOfStartNodeBySubProcessNode(directNextNode);
                for (BpmActivityMeta sonNode : sonNodes) {
                    result.includeAll(getNowActivity(sonNode, formData));
                }
            } else if ("gateway".equals(type)) {
                if ("gateway".equals(activityType)) {
                    // 排他网关
                    result.addGatewayNode(directNextNode);
                    // 根据规则判断网关走向
                    DhGatewayLine outLine = getUniqueOutLineByGatewayNodeAndFormData(directNextNode, formData);
                    // 根据输出线，封装DhGatewayRouteResult
                    DhGatewayRouteResult routeResult = assembleDhGatewayRouteResultWithOutInsId(outLine);
                    if (routeResult != null) {
                        result.addRouteResult(routeResult);
                    }
                    // 获得连接点对应的节点
                    BpmActivityMeta node = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(outLine.getToActivityBpdId(),
                            directNextNode.getParentActivityId(), directNextNode.getSnapshotId());
                    result.includeAll(getNowActivity(node, formData));
                } else if ("gatewayAnd".equals(activityType)) {
                    // 并行网关
                    List<BpmActivityMeta> directNextNodes = findDirectNextNodes(directNextNode);
                    for (BpmActivityMeta node : directNextNodes) {
                        result.includeAll(getNowActivity(node, formData));
                    }
                } else if ("gatewayOr".equals(activityType)) {
                    // 包容网关
                    List<BpmActivityMeta> directNextNodes = findDirectNextNodes(directNextNode);
                    for (BpmActivityMeta node : directNextNodes) {
                        result.includeAll(getNowActivity(node, formData));
                    }
                } else {
                    throw new BpmFindNextNodeException("环节类型异常，环节主键：" + directNextNode.getActivityId());
                }
            }else if ("end".equals(activityType)) {
                if (!"0".equals(sourceNode.getParentActivityId())) {
                    // 子流程结束
                    // 获得父节点，找到父节点，遍历父节点的下个节点
                    BpmActivityMeta endProcessNode = bpmActivityMetaService.queryByPrimaryKey(directNextNode.getParentActivityId());
                    result.addEndProcessNode(endProcessNode);
                    List<BpmActivityMeta> directNextNodes = findDirectNextNodes(endProcessNode);
                    for (BpmActivityMeta node : directNextNodes) {
                        result.includeAll(getNowActivity(node, formData));
                    }

                } else {
                    // 主流程结束
                    result.addMainEndNode(sourceNode);
                }
            } else {
                throw new BpmFindNextNodeException("环节类型异常，环节主键：" + directNextNode.getActivityId());
            }

        }
        return result;
    }

    public BpmRoutingData getNowActivity (BpmActivityMeta nowActivity, JSONObject formData) {
        BpmRoutingData result = new BpmRoutingData();
        String type = nowActivity.getType();
        String activityType = nowActivity.getActivityType();
        String bpmTaskType = nowActivity.getBpmTaskType();

        if ("activity".equals(activityType) && BpmActivityMeta.BPM_TASK_TYPE_USER_TASK.equals(bpmTaskType) && "activity".equals(type)) {
            // 人员服务
            result.addNormalNode(nowActivity);
        } else if ("activity".equals(activityType) && (BpmActivityMeta.BPM_TASK_TYPE_SUB_PROCESS.equals(bpmTaskType) || BpmActivityMeta.BPM_TASK_TYPE_CALLED_PROCESS.equals(bpmTaskType))
                && "activity".equals(type)) {
            // 子流程, 在需要创建的子流程中加入节点
            result.addStartProcessNode(nowActivity);
            List<BpmActivityMeta> sonNodes = findDirectNextNodesOfStartNodeBySubProcessNode(nowActivity);
            for (BpmActivityMeta sonNode : sonNodes) {
                result.includeAll(getNowActivity(sonNode, formData));
            }

        } else if ("gateway".equals(type)) {
            if ("gateway".equals(activityType)) {
                // 排他网关
                result.addGatewayNode(nowActivity);
                // 根据规则判断网关走向
                DhGatewayLine outLine = getUniqueOutLineByGatewayNodeAndFormData(nowActivity, formData);
                // 根据输出线，封装DhGatewayRouteResult
                DhGatewayRouteResult routeResult = assembleDhGatewayRouteResultWithOutInsId(outLine);
                if (routeResult != null) {
                    result.addRouteResult(routeResult);
                }
                // 获得连接点对应的节点
                BpmActivityMeta node = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(outLine.getToActivityBpdId(),
                        nowActivity.getParentActivityId(), nowActivity.getSnapshotId());
                result.includeAll(getNowActivity(node, formData));
            } else if ("gatewayAnd".equals(activityType)) {
                // 并行网关
                List<BpmActivityMeta> directNextNodes = findDirectNextNodes(nowActivity);
                for (BpmActivityMeta node : directNextNodes) {
                    result.includeAll(getNowActivity(node, formData));
                }
            } else if ("gatewayOr".equals(activityType)) {
                // 包容网关
                List<BpmActivityMeta> directNextNodes = findDirectNextNodes(nowActivity);
                for (BpmActivityMeta node : directNextNodes) {
                    result.includeAll(getNowActivity(node, formData));
                }
            } else {
                throw new BpmFindNextNodeException("环节类型异常，环节主键：" + nowActivity.getActivityId());
            }
        }else if ("end".equals(activityType)) {
            if (!"0".equals(nowActivity.getParentActivityId())) {
                // 子流程结束
                // 获得父节点，找到父节点，遍历父节点的下个节点
                BpmActivityMeta endProcessNode = bpmActivityMetaService.queryByPrimaryKey(nowActivity.getParentActivityId());
                result.addEndProcessNode(endProcessNode);
                List<BpmActivityMeta> directNextNodes = findDirectNextNodes(endProcessNode);
                for (BpmActivityMeta sonNode : directNextNodes) {
                    result.includeAll(getNowActivity(sonNode, formData));
                }
            } else {
                // 主流程结束
                result.addMainEndNode(nowActivity);
            }

        } else {
            throw new BpmFindNextNodeException("环节类型异常，环节主键：" + nowActivity.getActivityId());
        }

        return result;
    }

	/**
	 * 根据代表子流程的节点，获得它的start节点直接相连的节点
	 * 
	 * @return
	 */
	private List<BpmActivityMeta> findDirectNextNodesOfStartNodeBySubProcessNode(BpmActivityMeta subProcessNode) {
		BpmActivityMeta startNode = bpmActivityMetaService.getStartMetaOfSubProcess(subProcessNode);
		return findDirectNextNodes(startNode);
	}

	private List<BpmActivityMeta> findDirectNextNodes(BpmActivityMeta sourceNode) {
		List<BpmActivityMeta> directNextNodeList = new ArrayList<>();
		String activityToStr = sourceNode.getActivityTo();
		if (StringUtils.isBlank(activityToStr)) {
			throw new BpmFindNextNodeException("找到下个环节异常，节点id：" + sourceNode.getActivityId());
		}

		String[] toActivityBpdIds = activityToStr.split(",");
		// 直接关联的节点集合
		for (String activityBpdId : toActivityBpdIds) {
			BpmActivityMeta meta = bpmActivityMetaService.getByActBpdIdAndParentActIdAndProVerUid(activityBpdId,
					sourceNode.getParentActivityId(), sourceNode.getSnapshotId());
			if (meta == null) {
				throw new BpmFindNextNodeException("找不到指定环节, activityBpdId: " + activityBpdId + ", parentActivityId: "
						+ sourceNode.getParentActivityId());
			}
			directNextNodeList.add(meta);
		}
		return directNextNodeList;
	}

	/**
	 * 根据网关环节和表单内容给出唯一的输出连接线
	 * 
	 * @param gatewayNode
	 * @param formData
	 * @return
	 */
	public DhGatewayLine getUniqueOutLineByGatewayNodeAndFormData(BpmActivityMeta gatewayNode, JSONObject formData) {
		DhGatewayLine lineSelective = new DhGatewayLine();
		// 获得源网关的id
		lineSelective.setActivityId(gatewayNode.getSourceActivityId());
		List<DhGatewayLine> lines = dhGatewayLineService.getGateWayLinesByCondition(lineSelective);
		// 获得相关的条件
		List<DatRuleCondition> conditions = datRuleConditionService
				.getDatruleConditionByActivityId(gatewayNode.getSourceActivityId());
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
			log.error("从表单中获取变量发生错误，网关id: " + gatewayNode.getActivityId());
			// 返回默认连线
			return getDefaultLine(lines);
		}

		// 计算每种规则的结果
		List<DhGatewayLine> unDefaultLines = getUnDefaultLines(lines);

		for (DhGatewayLine line : unDefaultLines) {
			String ruleId = line.getRuleId();
			DatRule datRule = datRuleService.getDatRuleByKey(ruleId);
			Map<String, Object> ruleResult = null;
			try {
				ruleResult = droolsEngineService.execute(param, datRule);
				if (ruleResult.get("state") != null && ruleResult.get("state").equals(true)) {
					// 规则运算成功, 返回满足规则的连线
					return line;
				}
			} catch (Exception e) {
				log.error("规则运算异常,规则编号：" + ruleId, e);
			}
		}

		// 如果没有满足规则的连线，返回默认连线
		return getDefaultLine(lines);
	}

	private DhGatewayRouteResult assembleDhGatewayRouteResultWithOutInsId(DhGatewayLine line) {
		if (line == null || "TRUE".equals(line.getIsDefault())) {
			return null;
		}
		DhGatewayRouteResult routeResult = new DhGatewayRouteResult();
		routeResult.setRouteResultUid(EntityIdPrefix.DH_GATEWAY_ROUTE_RESULT + UUID.randomUUID().toString());
		routeResult.setStatus("on");
		routeResult.setActivityBpdId(line.getGatewayActivityBpdId()); // 网关的activityBpdId
		routeResult.setRouteResult(line.getRouteResult());
		routeResult.setCreateTime(new Date());
		routeResult.setUpdateTime(new Date());
		return routeResult;
	}

	@Override
	public ServerResponse<String> getDhGatewayRouteResult(Integer insId, String activityBpdId) {
		if (insId == null || StringUtils.isBlank(activityBpdId)) {
			return ServerResponse.createBySuccess(UUID.randomUUID().toString());
		}
		DhGatewayRouteResult routeResult = dhGatewayRouteResultMapper.queryByInsIdAndActivityBpdId(insId,
				activityBpdId);
		if (routeResult == null) {
			return ServerResponse.createBySuccess(UUID.randomUUID().toString());
		}
		return ServerResponse.createBySuccess(routeResult.getRouteResult());

	}


	public boolean checkRouteData(BpmActivityMeta currTaskNode, JSONArray routeData, BpmRoutingData routingData) {
        Map<String, String> assignMap = new HashMap<>();
	    for (int i=0; i<routeData.size(); i++) {
            JSONObject item = routeData.getJSONObject(i);
            assignMap.put(item.getString("activityId"), item.getString("userUid"));
        }


		// 所有当前任务节点平级的普通任务节点都有处理人
		Set<BpmActivityMeta> normalNodes = routingData.getNormalNodes();
		if (normalNodes.isEmpty()) {
			// 当后续没有人工环节，返回true
			return true;
		}
        for (Iterator<BpmActivityMeta> it = normalNodes.iterator(); it.hasNext();) {
            BpmActivityMeta normalNode = it.next();
            if (normalNode.getParentActivityId().equals(currTaskNode.getParentActivityId())) {
                // 指定节点的处理人是否是空
                if (StringUtils.isBlank(assignMap.get(normalNode.getActivityId()))) {
                    return false;
                }
            }
        }

		// 如果有新的子流程，当前任务节点平级的 开始子流程节点 其下的第一个普通任务有处理人
        Set<BpmActivityMeta> startProcessNodes = routingData.getStartProcessNodes();
		if (startProcessNodes.isEmpty()) {
		    return true;
        }
        // 找出与当前任务节点平级的子流程节点
        List<BpmActivityMeta> brotherSubProcessNodes = new ArrayList<>();
        for (Iterator<BpmActivityMeta> it = startProcessNodes.iterator(); it.hasNext();) {
            BpmActivityMeta startProcessNode = it.next();
            if (startProcessNode.getParentActivityId().equals(currTaskNode.getParentActivityId())) {
                brotherSubProcessNodes.add(startProcessNode);
            }
        }
        List<BpmActivityMeta> normalNodesNeedCheck = new ArrayList<>(); // 需要分配人的子流程下的任务
        for (BpmActivityMeta brotherSubProcessNode : brotherSubProcessNodes) {
            for (BpmActivityMeta item : normalNodes) {
                if (item.getParentActivityId().equals(brotherSubProcessNode.getActivityId())) {
                    normalNodesNeedCheck.add(item);
                }
            }
        }
        for (BpmActivityMeta item : normalNodesNeedCheck) {
            if (StringUtils.isBlank(assignMap.get(item.getActivityId()))) {
                return false;
            }
        }
        return true;
	}

	public boolean willFinishTaskMoveToken(DhTaskInstance currTask) {
        // DhTaskInstance.TYPE_NORMAL;
        String taskType = currTask.getTaskType();
        if (taskType.endsWith("Add") || taskType.equals(DhTaskInstance.TYPE_TRANSFER)) {
            // 加签的会签任务, 传阅任务
            return false;
        } else if (taskType.equals(DhTaskInstance.TYPE_NORMAL)) {
            // 普通任务
            return true;
        }

        // 查找指定任务编号和任务类型的任务
        int taskId = currTask.getTaskId(); // 任务编号
        DhTaskInstance selective = new DhTaskInstance();
        selective.setTaskId(taskId);
        selective.setTaskType(taskType);
        List<DhTaskInstance> matchedTasks = dhTaskInstanceMapper.selectAllTask(selective);
        if (taskType.equals(DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP)) {
            // 多实例会签任务
            boolean allTaskFinished = true;
            for (Iterator<DhTaskInstance> it = matchedTasks.iterator(); it.hasNext();) {
                DhTaskInstance task = it.next();
                if (!task.getTaskUid().equals(currTask.getTaskUid()) && !DhTaskInstance.STATUS_CLOSED.equals(task.getTaskStatus())) {
                    // 如果有其他同编号的多实例会签任务没有完成，就返回false
                    return false;
                }
            }
        } else if (taskType.equals(DhTaskInstance.TYPE_SIMPLE_LOOP)) {
            // 简单循环会签任务
            // 查询出简单循环任务
            DhProcessInstance processInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
            // 查询出这个简单循环会签有几个人
            List<DhTaskHandler> list = dhTaskHandlerMapper.listByInsIdAndTaskActivityId(processInstance.getInsId(), currTask.getTaskActivityId());
            int count = 0;
            for (Iterator<DhTaskInstance> it = matchedTasks.iterator(); it.hasNext();) {
                DhTaskInstance task = it.next();
                if (DhTaskInstance.STATUS_CLOSED.equals(task.getTaskStatus())) {
                    count++;
                }
            }
            if (count >= list.size() - 1) {
                // 如果其他人都完成了简单循环会签任务
                return true;
            } else {
                return false;
            }
        }
        return false;
	}

    public int updateDhTaskHandlerOfSimpleLoopTask(List<DhTaskHandler> list) {
	    int insId = 0;
	    List<String> taskActivityIds = new ArrayList<>();
        for (DhTaskHandler item : list) {
            if (!taskActivityIds.contains(item.getTaskActivityId())) {
                insId = item.getInsId().intValue();
                taskActivityIds.add(item.getTaskActivityId());
            }
        }
        dhTaskHandlerMapper.deleteByInsIdAndTaskActivityIdList(insId, taskActivityIds);
        return dhTaskHandlerMapper.insertBatch(list);
    }

    @Override
    public CommonBusinessObject assembleInitUserOfSubProcess(DhProcessInstance currProcessInstance, CommonBusinessObject pubBo, BpmRoutingData routingData) {
		// 找到满足条件的所有子流程, 代表子流程的节点和作为出发点的节点不是平级的
        List<BpmActivityMeta> processNodesToAssemble = findNodesIdentifySubProcessNeedToAssemble(routingData);
        for (BpmActivityMeta nodeIdentifySubProcess : processNodesToAssemble) {
            // 找到发起节点
            BpmActivityMeta firstTaskNode = null;
            Set<BpmActivityMeta> normalNodes = routingData.getNormalNodes();
            for (Iterator<BpmActivityMeta> it = normalNodes.iterator(); it.hasNext();) {
                BpmActivityMeta normalNode = it.next();
                if (normalNode.getParentActivityId().equals(nodeIdentifySubProcess.getActivityId())) {
                    firstTaskNode = normalNode;
                    break;
                }
            }
            BpmGlobalConfig bpmGlobalConfig = new BpmGlobalConfig();
            List<String> adminUidList = new ArrayList<>();
            adminUidList.add(bpmGlobalConfig.getBpmAdminName());
            DhActivityConf activityConf = firstTaskNode.getDhActivityConf();
            // 分配默认处理人
            String actcAssignType = activityConf.getActcAssignType();
            String actcAssignVariable = activityConf.getActcAssignVariable();
            DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
            if (assignTypeEnum == null || assignTypeEnum == DhActivityConfAssignType.NONE) {
                log.info("为子流程创建处理人异常");
                // 找不到默认处理人，分配给管理员
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, adminUidList);
                continue;
            }
            DhActivityAssign selective = new DhActivityAssign();
            selective.setActivityId(activityConf.getActivityId());
            selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
            List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
            List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);
            String departNo = currProcessInstance.getDepartNo();
            String companyNum = currProcessInstance.getCompanyNumber();
            String userUidStr = "";
            switch (assignTypeEnum) {
                case ROLE:
                case ROLE_AND_DEPARTMENT:
                case ROLE_AND_COMPANY:
                    SysRoleUser roleUser = new SysRoleUser();
                    roleUser.setRoleUid(ArrayUtil.toArrayString(idList));

                    if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
                        roleUser.setCompanyCode(companyNum);
                    }
                    if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
                        StringBuffer str = new StringBuffer(departNo);
                        String result = recursionSelectDepartMent(departNo,str);
                        roleUser.setDepartUid(result);
                    }
                    List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
                    for (SysRoleUser sysRoleUser : roleUsers) {
                        userUidStr += sysRoleUser.getUserUid() + ";";
                    }
                    break;
                case TEAM:
                case TEAM_AND_DEPARTMENT:
                case TEAM_AND_COMPANY:
                    SysTeamMember sysTeamMember = new SysTeamMember();
                    if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_COMPANY)) {
                        sysTeamMember.setCompanyCode(companyNum);
                    }
                    if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_DEPARTMENT)) {
                        StringBuffer str = new StringBuffer(departNo);
                        String result = recursionSelectDepartMent(departNo, str);
                        sysTeamMember.setDepartUid(result);
                    }
                    sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));
                    List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
                    for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
                        userUidStr += sysTeamMember2.getUserUid() + ";";
                    }
                    break;
                case LEADER_OF_PRE_ACTIVITY_USER:
                    break;
                case USERS:
                    List<SysUser> userItem = sysUserMapper.listByPrimaryKeyList(idList);
                    for (SysUser sysUser : userItem) {
                        userUidStr += sysUser.getUserUid() + ";";
                    }
                    break;
                case PROCESS_CREATOR:
                    // 流程发起人, 则使用触发次流程的那个流程的流程发起人
                    userUidStr += currProcessInstance.getInsInitUser() + ";";
                    break;
                case BY_FIELD:// 根据表单字段选
                    if (assignList.size() > 0) {
                        String field = assignList.get(0).getActaAssignId();
                        JSONObject obj = JSON.parseObject(currProcessInstance.getInsData());
                        JSONObject formData = obj.getJSONObject("formData");
                        String value = FormDataUtil.getStringValue(field, formData);
                        if (value != null) {
                            userUidStr += value;
                        }
                    }
                    break;
                default:
                    break;
            }
            if (userUidStr.length() == 0) {
                // 分配给管理员
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, adminUidList);
                continue;
            }
            // 分配给userUidStr对应的人
            List<String> initUser = new ArrayList<>();
            initUser.add(userUidStr.split(";")[0]);
            CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, initUser);
        }
        return pubBo;
    }

    /**
     * 根据routingData得到需要被分配发起人的子流程节点，代表子流程的节点和作为出发点的节点不是平级的
     * @param routingData
     * @return
     */
    private List<BpmActivityMeta> findNodesIdentifySubProcessNeedToAssemble(BpmRoutingData routingData) {
        List<BpmActivityMeta> result = new ArrayList<>();
        Set<BpmActivityMeta> startProcessNodes = routingData.getStartProcessNodes();
        if (startProcessNodes.isEmpty()) {
            return result;
        }
        BpmActivityMeta sourceNode = routingData.getSourceNode();

        for (Iterator<BpmActivityMeta> it = startProcessNodes.iterator(); it.hasNext();) {
            BpmActivityMeta startProcessNode = it.next();
            if (!startProcessNode.getParentActivityId().equals(sourceNode.getParentActivityId())) {
                result.add(startProcessNode);
            }
        }
        return result;
    }

}
