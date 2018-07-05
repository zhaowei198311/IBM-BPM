package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.BpmFindNextNodeException;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.CommonBusinessObjectUtils;
import com.desmart.common.util.DataListUtils;
import com.desmart.common.util.FormDataUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.common.util.ProcessDataUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhTaskHandlerMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhTaskHandler;
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
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartportal.entity.DhGatewayRouteResult;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRouteService;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.dao.SysRoleUserMapper;
import com.desmart.desmartsystem.dao.SysTeamMemberMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.entity.SysTeamMember;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
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
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;


	@Override
	public ServerResponse<List<BpmActivityMeta>> showRouteBar(String taskUid, String insUid, String activityId,
			String departNo, String companyNum, String formData) {
	    List<BpmActivityMeta> resultNodeList = new ArrayList<>();

        if (StringUtils.isBlank(companyNum) || StringUtils.isBlank(departNo) || StringUtils.isBlank(activityId)) {
            return ServerResponse.createByErrorMessage("缺少必要参数");
        }
	    DhProcessInstance currProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (currProcessInstance == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }

        // 未发起的流程，预先装配页面传来的部门和公司编码
        if (currProcessInstance.getInsId() == -1) {
            currProcessInstance.setCompanyNumber(companyNum);
            currProcessInstance.setDepartNo(departNo);
        }

	    // dhTaskInstance区别 是不是发起流程的第一个任务
		DhTaskInstance dhTaskInstance = null;
        if (StringUtils.isNotBlank(taskUid)) {
            // 如果当前任务存在
            dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
            if (dhTaskInstance == null) {
                return ServerResponse.createByErrorMessage("任务不存在");
            }
            if (!dhTaskInstance.getTaskActivityId().equals(activityId)) {
                return ServerResponse.createByErrorMessage("任务节点与传入节点不匹配");
            }
        }
		BpmActivityMeta taskNode = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		String insDate = currProcessInstance.getInsData();// 实例数据
		JSONObject newObj = new JSONObject();
		if (StringUtils.isNotBlank(formData)) {
			newObj = JSONObject.parseObject(formData);
		}
		JSONObject oldObj = JSONObject.parseObject(insDate).getJSONObject("formData");
        JSONObject mergedFormJson = FormDataUtil.formDataCombine(newObj, oldObj);

		// 获得下个环节的信息，如果不需要为下个环节选人，不会列出
        BpmRoutingData routingData = this.getBpmRoutingData(taskNode, mergedFormJson);

        List<BpmActivityMeta> taskNodesOnSameDeepLevel = routingData.getTaskNodesOnSameDeepLevel();
        // 如果任务实例不存在，上个处理人就是本人， 如果任务存在，上个处理人就是任务所有者
        String preTaskOwner = dhTaskInstance == null ? (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)
                : dhTaskInstance.getUsrUid();

		for (BpmActivityMeta meta : taskNodesOnSameDeepLevel) {
            List<SysUser> defaultTaskOwnerList = getDefaultTaskOwnerOfTaskNode(meta, preTaskOwner, currProcessInstance,
                    FormDataUtil.getFormDataJsonFromProcessInstance(currProcessInstance));
            // 加入集合
            resultNodeList.add(meta);
            meta.setUserUid(DataListUtils.transformUserListToUserIdStr(defaultTaskOwnerList));
            meta.setUserName(DataListUtils.transformUserListToUserNameStr(defaultTaskOwnerList));
        }
        // 为自己的子流程选择默认处理人
        List<BpmActivityMeta> startProcessNodesOnSameDeepLevel = routingData.getStartProcessNodesOnSameDeepLevel();
        for (BpmActivityMeta nodeIdentifyProcess : startProcessNodesOnSameDeepLevel) {
            BpmActivityMeta firstTaskNode = nodeIdentifyProcess.getFirstTaskNode();
            List<SysUser> defaultTaskOwnerList = getDefaultTaskOwnerOfFirstNodeOfProcess(currProcessInstance, firstTaskNode,
                    nodeIdentifyProcess);
            // 加入集合
            resultNodeList.add(firstTaskNode);
            firstTaskNode.setUserUid(DataListUtils.transformUserListToUserIdStr(defaultTaskOwnerList));
            firstTaskNode.setUserName(DataListUtils.transformUserListToUserNameStr(defaultTaskOwnerList));
        }
        return ServerResponse.createBySuccess(resultNodeList);
	}

    @Override
	public List<SysUser> getDefaultTaskOwnerOfTaskNode(BpmActivityMeta taskNode, String preTaskOwner, DhProcessInstance dhProcessInstance, JSONObject mergedFormData) {
		// 初始化返回值，设置为空集合
	    List<SysUser> result = new ArrayList<>();

		DhActivityConf dhActivityConf = taskNode.getDhActivityConf();
		String actcAssignType = dhActivityConf.getActcAssignType();
		DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
		if (assignTypeEnum == DhActivityConfAssignType.NONE) {
		    // 如果没有默认处理人，返回空集合
			return result;
		}

		if (assignTypeEnum == DhActivityConfAssignType.LEADER_OF_PRE_ACTIVITY_USER) {
            SysUser preTaskUser = sysUserMapper.queryByPrimaryKey(preTaskOwner);
            SysUser leaderOfPreTaskUser = sysUserMapper.queryByPrimaryKey(preTaskUser.getManagernumber());
            if(leaderOfPreTaskUser != null) {
                result.add(leaderOfPreTaskUser);
            }
            return result;
        }

        if (assignTypeEnum == DhActivityConfAssignType.PROCESS_CREATOR) {
            SysUser user = sysUserMapper.queryByPrimaryKey(dhProcessInstance.getInsInitUser());
            if (user != null) {
                result.add(user);
            }
            return result;
        }

        // 其余 的情况需要数据库关联表中有匹配的数据才能生效
		// 获得数据库中保存的  DH_ACTIVITY_ASSIGN 默认处理人
		DhActivityAssign selective = new DhActivityAssign();
		selective.setActivityId(taskNode.getSourceActivityId());
		selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
		List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.isEmpty()) {
            return result;
        }

        String departNo = dhProcessInstance.getDepartNo();
        String companyNum = dhProcessInstance.getCompanyNumber();
		// 获得被分配[人|角色|角色组]的 主键数据
		List<String> objIdList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);
		String tempIdStr = "";
		switch (assignTypeEnum) {
			// 角色相关
			case ROLE:
			case ROLE_AND_DEPARTMENT:
			case ROLE_AND_COMPANY:
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setRoleUid(ArrayUtil.toArrayString(objIdList));
				if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
					roleUser.setCompanyCode(companyNum);
				}
				if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
                    StringBuffer str = new StringBuffer(departNo);
                    String str2 = recursionSelectDepartMent(departNo, str);
                    roleUser.setDepartUid(str2);
				}
				List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
				for (SysRoleUser sysRoleUser : roleUsers) {
					tempIdStr += sysRoleUser.getUserUid() + ";";
				}
				break;
			// 角色组相关
			case TEAM:
			case TEAM_AND_DEPARTMENT:
			case TEAM_AND_COMPANY:
				SysTeamMember sysTeamMember = new SysTeamMember();
				if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_COMPANY)) {
				    sysTeamMember.setCompanyCode(companyNum);
				}
				if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_DEPARTMENT)) {
                    StringBuffer str = new StringBuffer(departNo);
                    String str2 = recursionSelectDepartMent(departNo, str);
                    sysTeamMember.setDepartUid(str2);
				}
				sysTeamMember.setTeamUid(ArrayUtil.toArrayString(objIdList));
				List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
				for (SysTeamMember member : sysTeamMembers) {
					tempIdStr += member.getUserUid() + ";";
				}
				break;
			// 指定处理人
			case USERS:
				List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(objIdList);
				for (SysUser sysUser : userList) {
				    if (!result.contains(sysUser)) {
				        result.add(sysUser);
                    }
				}
				break;
			// 根据表单字段选
			case BY_FIELD:
                String field = objIdList.get(0);
                JSONObject fieldJson = mergedFormData.getJSONObject(field);
                if (fieldJson == null) {
                    return result;
                }
                String idValue = fieldJson.getString("value");
                if (idValue == null) {
                    return result;
                }
                String[] strArr = idValue.split(";");
                List<String> tempValueList = new ArrayList<>();
                for (String str : strArr) {
                    if (StringUtils.isNotBlank(str)) {
                        tempValueList.add(str.trim());
                    }
                }
                if (tempValueList.isEmpty()) {
                    return result;
                }
                List<SysUser> sysUsers = sysUserMapper.listByPrimaryKeyList(tempValueList);
                if (sysUsers.isEmpty()) {
                    return result;
                }
                for (SysUser sysUser : sysUsers) {
                    if (!result.contains(sysUser)) {
                        result.add(sysUser);
                    }
                }
                break;
			default:
				break;
		}
		if (StringUtils.isNotBlank(tempIdStr)) {
            result = transformTempIdStrToUserList(tempIdStr);
        }
        return result;
	}



	/**
	 * 将包含重复id的字符串转换为用户列表，并去除重复
	 * @return
	 */
	private List<SysUser> transformTempIdStrToUserList(String tempIdStr) {
		List<SysUser> resultList = null;
		if (StringUtils.isNotBlank(tempIdStr)) {
			// 去重
			List<String> tempList = new ArrayList<>();
			String[] temArr = tempIdStr.split(";");
			for (String str : temArr) {
				if (StringUtils.isNotBlank(str) && !tempList.contains(str)) {
					// 对id去重后添加
					tempList.add(str);
				}
			}
			if (!tempList.isEmpty()) {
				resultList = sysUserMapper.listByPrimaryKeyList(tempList);

			}
		}
		return resultList == null ? new ArrayList<SysUser>() : resultList;
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

	public Set<BpmActivityMeta> getActualNextActivities(BpmActivityMeta sourceActivityMeta, JSONObject formData) {
        BpmRoutingData routingData = getRoutingDataOfNextActivityTo(sourceActivityMeta, formData);
        return routingData.getNormalNodes();
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
	public List<DhTaskHandler> saveTaskHandlerOfLoopTask(int insId, JSONArray routeData) {
	    List<DhTaskHandler> taskHandlerList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -2);
		Date nowDate = calendar.getTime(); // 记录的时间减去两秒
        for (int i = 0; i < routeData.size(); i++) {
            JSONObject item = (JSONObject) routeData.get(i);
            String activityId = item.getString("activityId");
            String userUids = item.getString("userUid");
            String loopType = item.getString("loopType");

            if (DhTaskInstance.TYPE_SIMPLE_LOOP.equalsIgnoreCase(loopType)
                    || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equalsIgnoreCase(loopType)) {
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
					dhTaskHandler.setCreateTime(nowDate);
                    taskHandlerList.add(dhTaskHandler);
                }
            }
        }
		if (taskHandlerList.isEmpty()) {
        	return taskHandlerList;
		}
		List<String> taskActivityIds = new ArrayList<>();
		for (DhTaskHandler item : taskHandlerList) {
			if (!taskActivityIds.contains(item.getTaskActivityId())) {
				taskActivityIds.add(item.getTaskActivityId());
			}
		}
		dhTaskHandlerMapper.deleteByInsIdAndTaskActivityIdList(insId, taskActivityIds);
		dhTaskHandlerMapper.insertBatch(taskHandlerList);
        return taskHandlerList;
    }



	@Override
	public ServerResponse<List<SysUser>> choosableHandler(String insUid, String activityId, String departNo,
			String companyNum, String formData, HttpServletRequest request, String taskUid) {
        if (StringUtils.isBlank(companyNum) || StringUtils.isBlank(departNo) || StringUtils.isBlank(activityId)) {
            return ServerResponse.createByErrorMessage("缺少必要参数");
        }
        DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
        if (insUid == null) {
            return ServerResponse.createByErrorMessage("流程实例不存在");
        }
        // dhTaskInstance区别 是不是发起流程的第一个任务
        DhTaskInstance dhTaskInstance = null;

        if (StringUtils.isNotBlank(taskUid)) {
            // 如果当前任务存在
            dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
            if (dhTaskInstance == null) {
                return ServerResponse.createByErrorMessage("任务不存在");
            }

        }

		List<SysUser> resultList = new ArrayList<>();
        // 如果任务实例不存在，上个处理人就是本人， 如果任务存在，上个处理人就是任务所有者
        String preTaskOwner = dhTaskInstance == null ? (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)
                : dhTaskInstance.getUsrUid();

		BpmActivityMeta taskNode = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		DhActivityConf dhActivityConf = taskNode.getDhActivityConf();
        String actcAssignType = dhActivityConf.getActcChooseableHandlerType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);

        // 上个环节处理人的上级
        if (assignTypeEnum == DhActivityConfAssignType.LEADER_OF_PRE_ACTIVITY_USER) {
            SysUser preTaskUser = sysUserMapper.queryByPrimaryKey(preTaskOwner);
            SysUser leaderOfPreTaskUser = sysUserMapper.queryByPrimaryKey(preTaskUser.getManagernumber());
            if (leaderOfPreTaskUser != null) {
                resultList.add(leaderOfPreTaskUser);
            }
            return ServerResponse.createBySuccess(resultList);
        }

        // 流程发起人
        if (assignTypeEnum == DhActivityConfAssignType.PROCESS_CREATOR) {
            SysUser user = sysUserMapper.queryByPrimaryKey(dhProcessInstance.getInsInitUser());
            if (user != null) {
                resultList.add(user);
            }
            return ServerResponse.createBySuccess(resultList);
        }

        // 全体人员
        if (assignTypeEnum == DhActivityConfAssignType.ALL_USER) {
            return ServerResponse.createBySuccess(resultList);
        }
        String insDate = dhProcessInstance.getInsData();// 实例表单

		DhActivityAssign selective = new DhActivityAssign();
		selective.setActivityId(taskNode.getSourceActivityId());
		selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
		List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
		List<String> objIdList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);

        String tempIdStr = "";  // 保存重复的员工id, ";"分隔，";"结尾
		switch (assignTypeEnum) {
		case ROLE:
		case ROLE_AND_DEPARTMENT:
		case ROLE_AND_COMPANY:
			SysRoleUser roleUser = new SysRoleUser();
			roleUser.setRoleUid(ArrayUtil.toArrayString(objIdList));
			if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
				roleUser.setCompanyCode(companyNum);
			}
			if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
                StringBuffer str = new StringBuffer(departNo);
                String str2 = recursionSelectDepartMent(departNo,str);
                roleUser.setDepartUid(str2);
			}
			// 查询出来SysRoleUser的userUid属性是这样的： uid1;uid1;uid2;uid2
			List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
            for (SysRoleUser sysRoleUser : roleUsers) {
                tempIdStr += sysRoleUser.getUserUid() + ";";
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
                String str2 = recursionSelectDepartMent(departNo,str);
                sysTeamMember.setDepartUid(str2);
			}
			sysTeamMember.setTeamUid(ArrayUtil.toArrayString(objIdList));
			List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
			for (SysTeamMember teamMember : sysTeamMembers) {
                tempIdStr += teamMember.getUserUid() + ";";
			}
			break;
		case USERS:
			List<SysUser> userItem = sysUserMapper.listByPrimaryKeyList(objIdList);
			for (SysUser sysUser : userItem) {
				if (!resultList.contains(sysUser)) {
				    SysUser user = new SysUser();
				    user.setUserUid(sysUser.getUserUid());
				    user.setUserName(sysUser.getUserName());
				    resultList.add(sysUser);
                }
			}
			break;
		case BY_FIELD:// 根据表单字段选
            JSONObject formJson = new JSONObject();
            JSONObject newObj = new JSONObject();
            if (StringUtils.isNotBlank(formData)) {
                newObj = JSONObject.parseObject(formData);
            }
            formJson = FormDataUtil.formDataCombine(newObj, JSONObject.parseObject(insDate).getJSONObject("formData"));
            String field = objIdList.get(0);
            JSONObject fieldJson = formJson.getJSONObject(field);
            if (fieldJson == null) {
                return ServerResponse.createBySuccess(resultList);
            }
            String idValue = fieldJson.getString("value");
            if (idValue == null) {
                return ServerResponse.createBySuccess(resultList);
            }
            String[] strArr = idValue.split(";");
            List<String> tempValueList = new ArrayList<>();
            for (String str : strArr) {
                if (StringUtils.isNotBlank(str)) {
                    tempValueList.add(str.trim());
                }
            }
            if (tempValueList.isEmpty()) {
                return ServerResponse.createBySuccess(resultList);
            }
            List<SysUser> sysUsers = sysUserMapper.listByPrimaryKeyList(tempValueList);
            for (SysUser sysUser : sysUsers) {
                if (!resultList.contains(sysUser)) {
                    resultList.add(sysUser);
                }
            }
			break;
		case BY_TRIGGER:// 根据触发器选择
			String triUid = objIdList.get(0);
			WebApplicationContext webApplicationContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getServletContext());
			ServerResponse invokeTriggerResponse = dhTriggerService.invokeChooseUserTrigger(webApplicationContext, insUid,
					triUid);
			if (invokeTriggerResponse.isSuccess()) {
				List<String> userIds = (List<String>) invokeTriggerResponse.getData();
				List<SysUser> userList1 = sysUserMapper.listByPrimaryKeyList(userIds);
				for (SysUser sysUser : userList1) {
                    if (!resultList.contains(sysUser)) {
                        resultList.add(sysUser);
                    }
				}
			} else {
                return ServerResponse.createBySuccess(resultList);
			}
			break;
		default:
			break;
		}
		if (!tempIdStr.isEmpty()) {
            resultList = transformTempIdStrToUserList(tempIdStr);
        }

		return ServerResponse.createBySuccess(resultList);
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
		// 查找流转到这个环节的流转记录，上个环节处理人为流转记录的生成人
		List<DhRoutingRecord> preRoutingRecordList = dhRoutingRecordMapper
				.listPreRoutingRecord(dhProcessInstance.getInsUid(), bpmActivityMeta.getActivityId());
		if (preRoutingRecordList.isEmpty()) {
			return ServerResponse.createByErrorMessage("获取上个环节失败");
		}
		DhRoutingRecord preRoutingRecord = preRoutingRecordList.get(0);
		// 找到上个环节
		BpmActivityMeta preMeta = bpmActivityMetaMapper.queryByPrimaryKey(preRoutingRecord.getActivityId());
		// 找到路由记录对应的任务
        DhTaskInstance taskInstance = dhTaskInstanceMapper.selectByPrimaryKey(preRoutingRecord.getTaskUid());
        // 装配处理人信息
        preMeta.setUserUid(taskInstance.getUsrUid());
		SysUser preUser = sysUserMapper.queryByPrimaryKey(taskInstance.getUsrUid());
		preMeta.setUserName(preUser.getUserName());
		return ServerResponse.createBySuccess(preMeta);
	}

	@Override
	public ServerResponse<BpmActivityMeta> getPreActivityByDiagram(DhProcessInstance dhProcessInstance,
                                                                   BpmActivityMeta sourceNode) {
		List<DhRoutingRecord> preRoutingRecordList = dhRoutingRecordMapper
				.listPreRoutingRecord(dhProcessInstance.getInsUid(), sourceNode.getActivityId());
		// 找出其中是submitTask的记录
        DhRoutingRecord routingRecordWhenSubmit = null;
        for (DhRoutingRecord routingRecord : preRoutingRecordList) {
            if (DhRoutingRecord.ROUTE_TYPE_SUBMIT_TASK.equals(routingRecord.getRouteType())
                    || DhRoutingRecord.ROUTE_TYPE_AUTOCOMMIT.equals(routingRecord.getRouteType())) {
                // 提交或自动提交的记录满足条件
                routingRecordWhenSubmit = routingRecord;
                break;
            }
        }
        if (routingRecordWhenSubmit == null) {
            return ServerResponse.createByErrorMessage("获取上个环节失败");
        }
        DhTaskInstance taskInstance = dhTaskInstanceMapper.selectByPrimaryKey(routingRecordWhenSubmit.getTaskUid());
        BpmActivityMeta preMeta = bpmActivityMetaMapper.queryByPrimaryKey(routingRecordWhenSubmit.getActivityId());
        // 装配处理人信息
        preMeta.setUserUid(taskInstance.getUsrUid());
        SysUser preUser = sysUserMapper.queryByPrimaryKey(taskInstance.getUsrUid());
        preMeta.setUserName(preUser.getUserName());
		return ServerResponse.createBySuccess(preMeta);
	}




    private BpmRoutingData getRoutingDataOfNextActivityTo(BpmActivityMeta sourceNode, JSONObject formData) {
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
	    // 保存activityId 与 处理人的对应关系
        Map<String, String> assignMap = new HashMap<>();
	    for (int i = 0; i < routeData.size(); i++) {
            JSONObject item = routeData.getJSONObject(i);
            assignMap.put(item.getString("activityId"), item.getString("userUid"));
        }
        /*
        routingData.getTaskNodesOnSameDeepLevel();
	    routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel();
	    这两个集合里的节点需要分配处理人
	    */
        for (BpmActivityMeta taskNode : routingData.getTaskNodesOnSameDeepLevel()) {
            if (assignMap.get(taskNode.getActivityId()) == null
                    || StringUtils.isBlank(assignMap.get(taskNode.getActivityId()))) {
                return false;
            }
        }
        for (BpmActivityMeta taskNode : routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel()) {
            if (assignMap.get(taskNode.getActivityId()) == null
                    || StringUtils.isBlank(assignMap.get(taskNode.getActivityId()))) {
                return false;
            }
        }
        return true;
	}

	public boolean willFinishTaskMoveToken(DhTaskInstance currTask) {
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
        selective.setInsUid(currTask.getInsUid());
        selective.setTaskType(taskType);
        selective.setTaskActivityId(currTask.getTaskActivityId());
        // 当前流程实例在这个环节的任务
        DhProcessInstance processInstance = dhProcessInstanceMapper.selectByPrimaryKey(currTask.getInsUid());
        List<DhTaskInstance> matchedTasks = dhTaskInstanceMapper.selectAllTask(selective);
        List<DhTaskHandler> handlerList = dhTaskHandlerMapper.listByInsIdAndTaskActivityId(processInstance.getInsId(), currTask.getTaskActivityId());
        Date handlerRecordCteateTime = handlerList.get(0).getCreateTime(); // DhTaskHandler表的数据保存时间
        // 过滤掉在记录时间之前的任务
        Iterator<DhTaskInstance> it = matchedTasks.iterator();
        while (it.hasNext()) {
            DhTaskInstance taskInstance = it.next();
            if (taskInstance.getTaskInitDate().getTime() < handlerRecordCteateTime.getTime()) {
                it.remove();
            }
        }

        int count = 0;
        for (DhTaskInstance task : matchedTasks) {
            if (DhTaskInstance.STATUS_CLOSED.equals(task.getTaskStatus())) {
                count++;
            }
        }
        if (count == handlerList.size()) {
            // 如果都完成了简单循环会签任务
            return true;
        } else {
            return false;
        }

	}

	@Override
	public ServerResponse<JSONObject> didTokenMove(int insId, BpmRoutingData routingData) {
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfigService.getFirstActConfig());
        HttpReturnStatus processDataReturnStatus = bpmProcessUtil.getProcessData(insId);
        if (HttpReturnStatusUtil.isErrorResult(processDataReturnStatus)) {
            return ServerResponse.createByErrorMessage("查看流程状态失败");
        }
        JSONObject processData = JSON.parseObject(processDataReturnStatus.getMsg());
        boolean moved = false;
        if (routingData.getNormalNodes().size() > 0) {
            // 预测有后续节点，查看执行树上是否包含后续任务节点信息
            moved = ProcessDataUtil.isExecutionTreeContainsFlowObjectId(routingData.getNormalNodes().iterator().next().getActivityBpdId(),
                    processData);
        } else {
            // 预测没有后续节点，查看流程实例是否已经执行完毕
            moved = ProcessDataUtil.isProcessFinished(processData);
        }
        if (moved) {
            return ServerResponse.createBySuccess(processData);
        } else {
            return ServerResponse.createBySuccess();
        }
    }


    @Override
    public CommonBusinessObject assembleTaskOwnerForNodesCannotChoose(DhTaskInstance currTask, DhProcessInstance currProcessInstance,
                                                                      CommonBusinessObject pubBo, BpmRoutingData routingData) {
        // 管理员作为没有发起人时的处理人
	    BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
        List<String> adminUidList = new ArrayList<>();
        adminUidList.add(bpmGlobalConfig.getBpmAdminName());

		// 找到满足条件的所有子流程, 代表子流程的节点和作为出发点的节点不是平级的
        List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel = routingData.getStartProcessNodesOnOtherDeepLevel();
        for (BpmActivityMeta nodeIdentifySubProcess : startProcessNodesOnOtherDeepLevel) {
            // 找到发起节点
            BpmActivityMeta firstTaskNode = nodeIdentifySubProcess.getFirstTaskNode();
            List<SysUser> defaultTaskOwners = getDefaultTaskOwnerOfFirstNodeOfProcess(currProcessInstance, firstTaskNode, nodeIdentifySubProcess);
            String actcAssignVariable = firstTaskNode.getDhActivityConf().getActcAssignVariable();
            if (defaultTaskOwners.isEmpty()) {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, adminUidList);
            } else {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo,
                        DataListUtils.transformUserListToUserIdList(defaultTaskOwners));
            }
        }

        // 为其他任务计算处理人
        List<BpmActivityMeta> taskNodesOnOtherDeepLevel = routingData.getTaskNodesOnOtherDeepLevel();
        if (!taskNodesOnOtherDeepLevel.isEmpty()) {
            for (BpmActivityMeta taskNode : taskNodesOnOtherDeepLevel) {
                // todo yao 需要修正为对的流程实例
                List<SysUser> defaultOwnerList = this.getDefaultTaskOwnerOfTaskNode(taskNode, currTask.getUsrUid(), currProcessInstance,
                        FormDataUtil.getFormDataJsonFromProcessInstance(currProcessInstance));
                List<String> taskOwnerIdList = adminUidList;
                if (!defaultOwnerList.isEmpty()) {
                    // 如果有处理人，则不使用默认处理人
                    taskOwnerIdList = DataListUtils.transformUserListToUserIdList(defaultOwnerList);
                }
                // 获得相应的变量和signCount
                String assignVariable = taskNode.getDhActivityConf().getActcAssignVariable();
                CommonBusinessObjectUtils.setNextOwners(assignVariable, pubBo, taskOwnerIdList);
                // 看是否需要signCount
                if (!BpmActivityMeta.LOOP_TYPE_NONE.equals(taskNode.getLoopType())) {
                    String signCountVariable = taskNode.getDhActivityConf().getSignCountVarname();
                    CommonBusinessObjectUtils.setOwnerSignCount(signCountVariable, pubBo, taskOwnerIdList.size());
                }
            }
        }
        return pubBo;
    }

	/**
	 * 计算流程起草环节的默认处理人， 当没有时返回空集合
	 * @param currProcessInstance  当前流程实例————当前流程实例的任务提交引发了，改子流程的创建
	 * @param firstTaskNode  起草环节
	 * @param nodeIdentifyProcess  代表这个流程的节点
	 * @return 当没有默认处理人时返回空集合
	 */
    private List<SysUser> getDefaultTaskOwnerOfFirstNodeOfProcess(DhProcessInstance currProcessInstance, BpmActivityMeta firstTaskNode,
																  BpmActivityMeta nodeIdentifyProcess) {
		List<SysUser> result = new ArrayList<>();
		// 先获取这个任务节点所属的尚未创建的流程的父级流程
        DhProcessInstance parentProcessInstance = getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(currProcessInstance,
                nodeIdentifyProcess);
        if (parentProcessInstance == null) {
            log.error("查找父流程实例失败：当前流程：" + currProcessInstance.getInsUid()
                    + " 代表子流程的节点id：" + nodeIdentifyProcess.getActivityId());
            return result;
        }
        DhActivityConf dhActivityConf = firstTaskNode.getDhActivityConf();
        String actcAssignType = dhActivityConf.getActcAssignType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
        if (assignTypeEnum == DhActivityConfAssignType.NONE
                || assignTypeEnum == DhActivityConfAssignType.LEADER_OF_PRE_ACTIVITY_USER) {
            // 没有分配，或分配的是上个环节的处理人时，返回空集合
            return result;
        }
        // 如果是流程发起人，设为父流程发起人
        if (assignTypeEnum == DhActivityConfAssignType.PROCESS_CREATOR) {
            SysUser user = sysUserMapper.queryByPrimaryKey(parentProcessInstance.getInsInitUser());
            if (user != null) {
                result.add(user);
            }
            return result;
        }
        // 其余 的情况需要数据库关联表中有匹配的数据才能生效
        // 获得数据库中保存的  DH_ACTIVITY_ASSIGN 默认处理人
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(firstTaskNode.getSourceActivityId());
        selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
        List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.isEmpty()) {
            return result;
        }
        String departNo = parentProcessInstance.getDepartNo();
        String companyNum = parentProcessInstance.getCompanyNumber();
        // 获得被分配[人|角色|角色组]的 主键数据
        List<String> objIdList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);
        String tempIdStr = "";
        switch (assignTypeEnum) {
            // 角色相关
            case ROLE:
            case ROLE_AND_DEPARTMENT:
            case ROLE_AND_COMPANY:
                SysRoleUser roleUser = new SysRoleUser();
                roleUser.setRoleUid(ArrayUtil.toArrayString(objIdList));
                if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
                    roleUser.setCompanyCode(companyNum);
                }
                if (assignTypeEnum.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
                    StringBuffer str = new StringBuffer(departNo);
                    String str2 = recursionSelectDepartMent(departNo, str);
                    roleUser.setDepartUid(str2);
                }
                List<SysRoleUser> roleUsers = sysRoleUserMapper.selectByRoleUser(roleUser);
                for (SysRoleUser sysRoleUser : roleUsers) {
                    tempIdStr += sysRoleUser.getUserUid() + ";";
                }
                break;
            // 角色组相关
            case TEAM:
            case TEAM_AND_DEPARTMENT:
            case TEAM_AND_COMPANY:
                SysTeamMember sysTeamMember = new SysTeamMember();
                if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_COMPANY)) {
                    sysTeamMember.setCompanyCode(companyNum);
                }
                if (assignTypeEnum.equals(DhActivityConfAssignType.TEAM_AND_DEPARTMENT)) {
                    StringBuffer str = new StringBuffer(departNo);
                    String str2 = recursionSelectDepartMent(departNo, str);
                    sysTeamMember.setDepartUid(str2);
                }
                sysTeamMember.setTeamUid(ArrayUtil.toArrayString(objIdList));
                List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
                for (SysTeamMember member : sysTeamMembers) {
                    tempIdStr += member.getUserUid() + ";";
                }
                break;
            // 指定处理人
            case USERS:
                List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(objIdList);
                for (SysUser sysUser : userList) {
                    if (!result.contains(sysUser)) {
                        result.add(sysUser);
                    }
                }
                break;
            // 根据表单字段选，
            case BY_FIELD:
                String field = objIdList.get(0);
                JSONObject fieldJson = FormDataUtil.getFormDataJsonFromProcessInstance(parentProcessInstance).getJSONObject(field);
                if (fieldJson == null) {
                    return result;
                }
                String idValue = fieldJson.getString("value");
                if (idValue == null) {
                    return result;
                }
                String[] strArr = idValue.split(";");
                List<String> tempValueList = new ArrayList<>();
                for (String str : strArr) {
                    if (StringUtils.isNotBlank(str)) {
                        tempValueList.add(str.trim());
                    }
                }
                if (tempValueList.isEmpty()) {
                    return result;
                }
                List<SysUser> sysUsers = sysUserMapper.listByPrimaryKeyList(tempValueList);
                if (sysUsers.isEmpty()) {
                    return result;
                }
                for (SysUser sysUser : sysUsers) {
                    if (!result.contains(sysUser)) {
                        result.add(sysUser);
                    }
                }
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(tempIdStr)) {
            result = transformTempIdStrToUserList(tempIdStr);
        }
        return result;
	}


	public DhProcessInstance getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(DhProcessInstance currProcessInstance,
																								  BpmActivityMeta nodeIdentifyProcess) {
        /* 代表子流程的节点的父级节点，
            当这个节点是"0" 说明子流程的父级流程是主流程
            当这个节点是其它值，去查询哪个流程的TOKEN_ACTIVITY_ID是这个值即为子流程的父流程
         */
        String parentActivityId = nodeIdentifyProcess.getParentActivityId();
        if ("0".equals(parentActivityId)) {
            // 返回主流程
            if (currProcessInstance.getInsStatusId().intValue() == DhProcessInstance.STATUS_ID_DRAFT) {
                // 如果当前流程是草稿状态， 父级流程就是当前流程
                return currProcessInstance;
            } else {
                return dhProcessInstanceMapper.getMainProcessByInsId(currProcessInstance.getInsId());
            }
        }
        // 返回TOKEN_ACTIVITY_ID是此值的流程
        return dhProcessInstanceMapper.getByInsIdAndTokenActivityId(currProcessInstance.getInsId(), parentActivityId);
    }


    @Override
    public BpmRoutingData getBpmRoutingData(BpmActivityMeta sourceNode, JSONObject formData) {
        BpmRoutingData routingData = this.getRoutingDataOfNextActivityTo(sourceNode, formData);
        /*  将noramalNodes整理为4类数据
         *  1. 与sourceNode平级的userTaskNode(要选人)
         *  2. 与sourceNode平级的 processStartNode对应的userTaskNode （要选人）
         *  3. 其他startNode下个的userTask （不可选人）
         *  4. 其他普通的userTask （不可选人）
         *
         *  startProcessNodes 分为两类
         *  1. 与起始任务平级的 代表子流程的节点
         *  2. 与起始任务不平级的 代表子流程的节点
         */
        if (routingData.getNormalNodes().isEmpty()) {
            return routingData;
        }
        List<BpmActivityMeta> normalNodes = DataListUtils.cloneList(new ArrayList<>(routingData.getNormalNodes()), BpmActivityMeta.class);
        Iterator<BpmActivityMeta> iterator = normalNodes.iterator();
        // 先选择出平级的任务节点
        // 选择出 平级的子流程的起草节点
        // 选择出 不平级的子流程的起草节点
        // 剩下的是第4类节点
        while (iterator.hasNext()) {
            BpmActivityMeta taskNode = iterator.next();
            if (taskNode.getParentActivityId().equals(sourceNode.getParentActivityId())) {
                routingData.getTaskNodesOnSameDeepLevel().add(taskNode);
                iterator.remove();
            }
        } // 选出了平级节点

        if (!routingData.getStartProcessNodes().isEmpty()) {
            for (BpmActivityMeta processNode : routingData.getStartProcessNodes()) {
                for (BpmActivityMeta taskNode : normalNodes) {
                    // 找到子流程的第一个任务节点
                    if (taskNode.getParentActivityId().equals(processNode.getActivityId())) {
                        processNode.setFirstTaskNode(taskNode);
                        normalNodes.remove(taskNode);
                        break;
                    }
                }

                if (processNode.getParentActivityId().equals(sourceNode.getParentActivityId())) {
                    // 代表子流程的节点与任务节点平级
                    routingData.getStartProcessNodesOnSameDeepLevel().add(processNode);
                    routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel().add(processNode.getFirstTaskNode());
                } else {
                    // 代表子流程的节点与任务节点不平级
                    routingData.getStartProcessNodesOnOtherDeepLevel().add(processNode);
                    routingData.getFirstTaskNodesOfStartProcessOnOtherDeepLevel().add(processNode.getFirstTaskNode());
                }
            }
        }// 装配完成子流程的起始任务节点，剩下的是与起始节点不平级的任务节点
        routingData.getTaskNodesOnOtherDeepLevel().addAll(normalNodes);
        return routingData;
    }

	@Override
	public ServerResponse choosableHandlerMove(String insUid, String activityId, String departNo,
			String companyNum, String formData, HttpServletRequest request, String taskUid,
			String userUidArrStr,String condition) {
		List<SysUser> userList = choosableHandler(insUid, activityId, departNo, companyNum, formData, request, taskUid).getData();
		List<SysUser> returnUserList = new ArrayList<>();
		for(SysUser user:userList) {
			//判断用户是否为已选处理人
			if(userUidArrStr!=null && userUidArrStr!="" && userUidArrStr.indexOf(user.getUserUid())!=-1) {
				continue;
			}
			//判断用户是否为
			if(condition!=null && condition!="" && user.getUserUid().indexOf(condition)==-1 
					&& user.getUserName().indexOf(condition)==-1) {
				continue;
			}
			returnUserList.add(user);
		}
		return ServerResponse.createBySuccess(returnUserList);
	}
}
