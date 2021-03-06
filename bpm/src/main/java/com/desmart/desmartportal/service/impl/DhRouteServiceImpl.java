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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

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
import com.desmart.desmartbpm.dao.DhTaskHandlerMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DatRuleCondition;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhTaskHandler;
import com.desmart.desmartbpm.entity.ThreadBoolean;
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
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.service.SysUserService;
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
	private SysUserMapper sysUserMapper;
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
	private ThreadBoolean threadBoolean = new ThreadBoolean();
	@Autowired
    private DhRoutingRecordService dhRoutingRecordService;
	@Autowired
	private SysUserService sysUserService;


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

	    // 通过dhTaskInstance区别 是不是发起流程的第一个任务, 如果任务主键没有传，说明是第一个任务
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
        // 获得当前节点
		BpmActivityMeta currTaskNode = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		String insDate = currProcessInstance.getInsData();// 实例数据
		JSONObject newObj = new JSONObject();
		if (StringUtils.isNotBlank(formData)) {
			newObj = JSONObject.parseObject(formData);
		}
		JSONObject oldObj = JSONObject.parseObject(insDate).getJSONObject("formData");
        JSONObject mergedFormJson = FormDataUtil.formDataCombine(newObj, oldObj);

		// 获得下个环节的信息
        BpmRoutingData bpmRoutingData = getBpmRoutingData(currTaskNode, mergedFormJson);

        List<BpmActivityMeta> taskNodesOnSameDeepLevel = bpmRoutingData.getTaskNodesOnSameDeepLevel();
        // 如果任务实例不存在，上个处理人就是本人， 如果任务存在，上个处理人就是任务所有者
        String preTaskOwner = dhTaskInstance == null ? (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)
                : dhTaskInstance.getUsrUid();
		// 装配taskNodesOnSameDeepLevel的处理人
		for (BpmActivityMeta nextTaskNode : taskNodesOnSameDeepLevel) {
            List<SysUser> defaultTaskOwnerList = getDefaultTaskOwnerOfTaskNode(nextTaskNode, preTaskOwner, currProcessInstance,
                    FormDataUtil.getFormDataJsonFromProcessInstance(currProcessInstance));
            // 加入集合
            resultNodeList.add(nextTaskNode);
            nextTaskNode.setUserUid(DataListUtils.transformUserListToUserIdStr(defaultTaskOwnerList));
            nextTaskNode.setUserName(DataListUtils.transformUserListToUserNameStr(defaultTaskOwnerList));
            // 如果是系统任务，不允许选择可选处理人
            if (Const.Boolean.TRUE.equals(nextTaskNode.getDhActivityConf().getActcIsSystemTask())) {
				nextTaskNode.getDhActivityConf().setActcCanChooseUser(Const.Boolean.FALSE);
			}
        }
        // 为自身的子流程选择默认处理人
        List<BpmActivityMeta> nodesIdentitySubProcessOnSameDeepLevel = bpmRoutingData.getStartProcessNodesOnSameDeepLevel();
        for (BpmActivityMeta nodeIdentifyProcess : nodesIdentitySubProcessOnSameDeepLevel) {
            BpmActivityMeta firstTaskNode = nodeIdentifyProcess.getFirstTaskNode();
            // 从bpmRoutingData找到任务真实的父节点
			BpmActivityMeta realParentNodeOfFirstTaskNode = bpmRoutingData.getActIdAndNodeIdentitySubProcessMap().get(firstTaskNode.getParentActivityId());

            List<SysUser> defaultTaskOwnerList = getDefaultTaskOwnerOfFirstNodeOfProcess(currProcessInstance, firstTaskNode,
					realParentNodeOfFirstTaskNode);
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
		// 如果任务是系统任务，使用管理员
		if ("TRUE".equals(dhActivityConf.getActcIsSystemTask())) {
			return this.getAdminUserList();
		}

		String actcAssignType = dhActivityConf.getActcAssignType();
		DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
		if (assignTypeEnum == DhActivityConfAssignType.NONE) {
		    // 如果没有默认处理人，返回空集合
			return result;
		}

		if (assignTypeEnum == DhActivityConfAssignType.LEADER_OF_PRE_ACTIVITY_USER) {
            SysUser preTaskUser = sysUserMapper.queryByPrimaryKey(preTaskOwner);
            //根据用户查询上级用户
            result.addAll(sysUserService.searchByLeaderOfPreActivityUser(preTaskUser));
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
		switch (assignTypeEnum) {
			// 角色相关
			case ROLE:
				return sysUserService.searchByRoleUidList(objIdList);
			case ROLE_AND_DEPARTMENT:
				return sysUserService.searchByRoleUidListAndDepartment(objIdList,departNo);
			case ROLE_AND_COMPANY:
				return sysUserService.searchByRoleUidListAndCompany(objIdList,companyNum);
			// 角色组相关
			case TEAM:
				return sysUserService.searchByTeamUidList(objIdList);
			case TEAM_AND_DEPARTMENT:
				return sysUserService.searchbyTeamUidListAndDepartment(objIdList,departNo);
			case TEAM_AND_COMPANY:
				return sysUserService.searchByTeamUidListAndCompany(objIdList,companyNum);
			// 指定处理人
			case USERS:
				return sysUserService.searchByUserUids(objIdList);
			// 根据表单字段选
			case BY_FIELD:
                String field = objIdList.get(0);
				return sysUserService.searchByField(mergedFormData, field);
			default:
				break;
		}
        return result;
	}

	/**
	 * 获得管理员用户列表
	 * @return 集合中含有一个管理员数据
	 */
	private List<SysUser> getAdminUserList() {
		BpmGlobalConfig globalConfig = bpmGlobalConfigService.getFirstActConfig();
		if (globalConfig == null) {
			throw new PlatformException("获得系统管理员向信息失败");
		}
		if (StringUtils.isBlank(globalConfig.getBpmAdminName())) {
			throw new PlatformException("获得系统管理员向信息失败");
		}
		SysUser adminUser = sysUserMapper.queryByPrimaryKey(globalConfig.getBpmAdminName());
		if (adminUser == null) {
			throw new PlatformException("获得系统管理员向信息失败");
		}
		List<SysUser> result = new ArrayList<>();
		result.add(adminUser);
		return result;
	}

	/**
	 * 将包含重复id的字符串转换为用户列表，并去除重复<br/>
	 * @param tempIdStr  uid1;uid2uid3;
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
		return resultList == null ? new ArrayList<>() : resultList;
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

    /**
     * 从连接线集合中找到所有非默认的连接线
     * @param lineList
     * @return
     */
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
	 * 从连线集合中找到默认连线
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
	public ServerResponse<CommonBusinessObject> assembleCommonBusinessObject(CommonBusinessObject pubBo, JSONArray routeData) {
		if (pubBo == null) {
			pubBo = new CommonBusinessObject();
		}

		for (int i = 0; i < routeData.size(); i++) {
			JSONObject item = routeData.getJSONObject(i);
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
	public List<DhTaskHandler> saveTaskHandlerOfLoopTask(int insId, BpmRoutingData bpmRoutingData, CommonBusinessObject pubBo) {
	    List<DhTaskHandler> taskHandlerList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -2);
		Date nowDate = calendar.getTime(); // 记录的时间减去两秒
		// 获得预测到的所有任务
        List<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
        for (BpmActivityMeta normalNode : normalNodes) {
            String loopType = normalNode.getLoopType();
            if (DhTaskInstance.TYPE_SIMPLE_LOOP.equalsIgnoreCase(loopType)
                    || DhTaskInstance.TYPE_MULT_IINSTANCE_LOOP.equalsIgnoreCase(loopType)) {
                // 如果是多实例任务, 从pubBo中取出处理人
                String assignVarname = normalNode.getDhActivityConf().getActcAssignVariable();
                List<String> nextOwners = CommonBusinessObjectUtils.getNextOwners(assignVarname, pubBo);
                for (String userUid : nextOwners) {
                    DhTaskHandler dhTaskHandler = new DhTaskHandler();
                    dhTaskHandler.setHandleUid(EntityIdPrefix.DH_TASK_HANDLER + UUID.randomUUID().toString());
                    dhTaskHandler.setUserUid(userUid);
                    dhTaskHandler.setInsId(Long.valueOf(insId));
                    dhTaskHandler.setTaskActivityId(normalNode.getActivityId());
                    dhTaskHandler.setStatus("on");
					dhTaskHandler.setCreateTime(nowDate);
                    taskHandlerList.add(dhTaskHandler);
                }
            }
        }
		if (taskHandlerList.isEmpty()) {
        	return taskHandlerList;
		}
		// 删除以往的数据
		List<String> taskActivityIds = new ArrayList<>();
		for (DhTaskHandler item : taskHandlerList) {
			if (!taskActivityIds.contains(item.getTaskActivityId())) {
				taskActivityIds.add(item.getTaskActivityId());
			}
		}
		dhTaskHandlerMapper.deleteByInsIdAndTaskActivityIdList(insId, taskActivityIds);
		// 插入此次的数据
		dhTaskHandlerMapper.insertBatch(taskHandlerList);
        return taskHandlerList;
    }



	@Override
	public ServerResponse<List<SysUser>> getChoosableHandler(String insUid, String activityId, String departNo,
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
        String preTaskOwner = (dhTaskInstance == null) ? (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)
                : dhTaskInstance.getUsrUid();

		BpmActivityMeta taskNode = bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		DhActivityConf dhActivityConf = taskNode.getDhActivityConf();
        String actcAssignType = dhActivityConf.getActcChooseableHandlerType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);

        // 上个环节处理人的上级
        if (assignTypeEnum == DhActivityConfAssignType.LEADER_OF_PRE_ACTIVITY_USER) {
            SysUser preTaskUser = sysUserMapper.queryByPrimaryKey(preTaskOwner);
            return ServerResponse.createBySuccess(sysUserService.searchByLeaderOfPreActivityUser(preTaskUser));
        }

        // 流程发起人
        if (assignTypeEnum == DhActivityConfAssignType.PROCESS_CREATOR) {
            SysUser user = sysUserMapper.queryByPrimaryKey(dhProcessInstance.getInsInitUser());
            if (user != null) {
                resultList.add(user);
            }
            return ServerResponse.createBySuccess(resultList);
        }

        // 全体人员, 从设计角度不会访问这个service，而是自行处理
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
			return ServerResponse.createBySuccess(sysUserService.searchByRoleUidList(objIdList));
		case ROLE_AND_DEPARTMENT:
			return ServerResponse.createBySuccess(sysUserService.searchByRoleUidListAndDepartment(objIdList, departNo));
		case ROLE_AND_COMPANY:
			return ServerResponse.createBySuccess(sysUserService.searchByRoleUidListAndCompany(objIdList,companyNum));
		case TEAM:
			return ServerResponse.createBySuccess(sysUserService.searchByTeamUidList(objIdList));
		case TEAM_AND_DEPARTMENT:
			return ServerResponse.createBySuccess(sysUserService.searchbyTeamUidListAndDepartment(objIdList,departNo));
		case TEAM_AND_COMPANY:
			return ServerResponse.createBySuccess(sysUserService.searchByTeamUidListAndCompany(objIdList,companyNum));
		case USERS:
			return ServerResponse.createBySuccess(sysUserService.searchByUserUids(objIdList));
		case BY_FIELD:// 根据表单字段选
            JSONObject formJson = new JSONObject();
            JSONObject newObj = new JSONObject();
            if (StringUtils.isNotBlank(formData)) {
                newObj = JSONObject.parseObject(formData);
            }
            formJson = FormDataUtil.formDataCombine(newObj, JSONObject.parseObject(insDate).getJSONObject("formData"));
            String field = objIdList.get(0);
			return ServerResponse.createBySuccess(sysUserService.searchByField(formJson, field));
		case BY_TRIGGER:// 根据触发器选择
			String triUid = objIdList.get(0);
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			ServerResponse invokeTriggerResponse = dhTriggerService.invokeChooseUserTrigger(wac, insUid,
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServerResponse updateGatewayRouteResult(Integer insId, BpmRoutingData routingData) {
		List<BpmActivityMeta> gatewayNodes = routingData.getGatewayNodes();
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
	public ServerResponse<BpmActivityMeta> getPreActivity(DhProcessInstance dhProcessInstance, BpmActivityMeta bpmActivityMeta) {
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
        // 装配处理人信息，即当时任务的所有者
        preMeta.setUserUid(taskInstance.getUsrUid());
        SysUser preUser = sysUserMapper.queryByPrimaryKey(taskInstance.getUsrUid());
        preMeta.setUserName(preUser.getUserName());
		return ServerResponse.createBySuccess(preMeta);
	}


	/**
	 * 获得BpmRoutingData的底层方法
	 * @param sourceNode
	 * @param formData
	 * @return
	 */
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
					threadBoolean.setTrue();
                    List<BpmActivityMeta> directNextNodes = findDirectNextNodes(directNextNode);
                    for (BpmActivityMeta node : directNextNodes) {
                        result.includeAll(getNowActivity(node, formData));
                    }
                } else if ("gatewayOr".equals(activityType)) {
                    // 包容网关
					if (threadBoolean.getValue()) {
						// 如果经过包容网关前经过了并行网关， 不再往后找
						return result;
					}
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

	/**
	 * 获得BpmRoutingData的底层方法
	 * @param nowActivity
	 * @param formData
	 * @return
	 */
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
				threadBoolean.setTrue();
                List<BpmActivityMeta> directNextNodes = findDirectNextNodes(nowActivity);
                for (BpmActivityMeta node : directNextNodes) {
                    result.includeAll(getNowActivity(node, formData));
                }
            } else if ("gatewayOr".equals(activityType)) {
                // 包容网关
				if (threadBoolean.getValue()) {
					// 如果经过包容网关前经过了并行网关， 不再往后找
					return result;
				}
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
	 * @param subProcessNode 代表子流程的节点
	 * @return
	 */
	private List<BpmActivityMeta> findDirectNextNodesOfStartNodeBySubProcessNode(BpmActivityMeta subProcessNode) {
		BpmActivityMeta startNode = bpmActivityMetaService.getStartNodeOfSubProcess(subProcessNode);
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
		DhGatewayRouteResult routeResult = dhGatewayRouteResultMapper.queryByInsIdAndActivityBpdId(insId, activityBpdId);
		if (routeResult == null) {
			return ServerResponse.createBySuccess(UUID.randomUUID().toString());
		}
		return ServerResponse.createBySuccess(routeResult.getRouteResult());

	}


	public boolean checkRouteData(BpmActivityMeta currTaskNode, JSONArray routeData, BpmRoutingData routingData) {
	    // 保存activityId 与 处理人的对应关系
        Map<String, String> actIdAndUserMap = new HashMap<>();
	    for (int i = 0; i < routeData.size(); i++) {
            JSONObject item = routeData.getJSONObject(i);
            String userUid = item.getString("userUid");
            if (StringUtils.isBlank(userUid)) {
                return false;
            }
            actIdAndUserMap.put(item.getString("activityId"), userUid);
        }
        /*
        routingData.getTaskNodesOnSameDeepLevel();
	    routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel();
	    这两个集合里的节点需要分配处理人
	    */
        for (BpmActivityMeta taskNode : routingData.getTaskNodesOnSameDeepLevel()) {
            if (actIdAndUserMap.get(taskNode.getActivityId()) == null) {
                return false;
            }
        }
        for (BpmActivityMeta taskNode : routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel()) {
            if (actIdAndUserMap.get(taskNode.getActivityId()) == null) {
                return false;
            }
        }
        return true;
	}

	@Override
	public boolean willFinishTaskMoveToken(DhTaskInstance currTask, int insId) {
        String taskType = currTask.getTaskType();
        if (taskType.endsWith("Add") || taskType.equals(DhTaskInstance.TYPE_TRANSFER)) {
            // 加签的任务, 抄送任务
            return false;
        } else if (taskType.equals(DhTaskInstance.TYPE_NORMAL)) {
            // 普通任务
            return true;
        }

        // 查找指定任务编号和任务类型的任务
		// 当前流程实例在这个环节的任务
        DhTaskInstance selective = new DhTaskInstance();
        selective.setInsUid(currTask.getInsUid());
        selective.setTaskType(taskType);
        selective.setTaskActivityId(currTask.getTaskActivityId());
        List<DhTaskInstance> matchedTasks = dhTaskInstanceMapper.selectAllTask(selective);
        List<DhTaskHandler> handlerList = dhTaskHandlerMapper.listByInsIdAndTaskActivityId(insId, currTask.getTaskActivityId());
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
		// 判断实际TOKEN是否移动了
		try { // 等待半秒给引擎处理处理时间
			Thread.sleep(500);
		} catch (InterruptedException e) {
			log.error(e);
		}
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfigService.getFirstActConfig());
		// 获得流程详细信息
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

		// 为startProcessNodesOnOtherDeepLevel的第一个任务分配处理人
        List<BpmActivityMeta> startProcessNodesOnOtherDeepLevel = routingData.getStartProcessNodesOnOtherDeepLevel();
        for (BpmActivityMeta nodeIdentifySubProcess : startProcessNodesOnOtherDeepLevel) {
            // 找到发起节点
            BpmActivityMeta firstTaskNode = nodeIdentifySubProcess.getFirstTaskNode();
            if (firstTaskNode == null) {
            	// 当firstTaskNode为空，说明这个子流程没有人工环节或者这个子流程的人工环节被提取到上级去了，已经分配了处理人
				continue;
			}
			// 从bpmRoutingData找到任务真实的父节点
			BpmActivityMeta realParentNodeOfFirstTaskNode = routingData.getActIdAndNodeIdentitySubProcessMap().get(firstTaskNode.getParentActivityId());

            List<SysUser> defaultTaskOwners = getDefaultTaskOwnerOfFirstNodeOfProcess(currProcessInstance, firstTaskNode, realParentNodeOfFirstTaskNode);
            String actcAssignVariable = firstTaskNode.getDhActivityConf().getActcAssignVariable();
            if (defaultTaskOwners.isEmpty()) {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, adminUidList);
            } else {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo,
                        DataListUtils.transformUserListToUserIdList(defaultTaskOwners));
            }
        }

        // 为其他已创建的流程的任务计算处理人
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
	@Override
	public CommonBusinessObject assembleTaskOwnerForSystemTask(DhTaskInstance currTask, DhProcessInstance currProcessInstance,
																	  CommonBusinessObject pubBo, BpmRoutingData routingData) {
		// 管理员作为没有发起人时的处理人
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		List<String> adminUidList = new ArrayList<>();
		adminUidList.add(bpmGlobalConfig.getBpmAdminName());

		// 所有代表子流程的节点
        List<BpmActivityMeta> nodesIdentityStartSubProcess = routingData.getStartProcessNodesOnSameDeepLevel();
        nodesIdentityStartSubProcess.addAll(routingData.getStartProcessNodesOnOtherDeepLevel());

        for (BpmActivityMeta nodeIdentifySubProcess : nodesIdentityStartSubProcess) {
            // 找到发起节点
            BpmActivityMeta firstTaskNode = nodeIdentifySubProcess.getFirstTaskNode();
            if (firstTaskNode == null) {
            	continue;
			}
			// 从bpmRoutingData找到任务真实的父节点
			BpmActivityMeta realParentNodeOfFirstTaskNode = routingData.getActIdAndNodeIdentitySubProcessMap().get(firstTaskNode.getParentActivityId());

            List<SysUser> defaultTaskOwners = getDefaultTaskOwnerOfFirstNodeOfProcess(currProcessInstance, firstTaskNode, realParentNodeOfFirstTaskNode);
            String actcAssignVariable = firstTaskNode.getDhActivityConf().getActcAssignVariable();
            if (defaultTaskOwners.isEmpty()) {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo, adminUidList);
            } else {
                CommonBusinessObjectUtils.setNextOwners(actcAssignVariable, pubBo,
                        DataListUtils.transformUserListToUserIdList(defaultTaskOwners));
            }
        }
		// 所有不是子流程第一个任务的需要装配的任务
        List<BpmActivityMeta> taskNodesExcludeFirstTask = new ArrayList<>();
		taskNodesExcludeFirstTask.addAll(routingData.getTaskNodesOnSameDeepLevel());
        taskNodesExcludeFirstTask.addAll(routingData.getTaskNodesOnOtherDeepLevel());
        if (!taskNodesExcludeFirstTask.isEmpty()) {
            for (BpmActivityMeta taskNode : taskNodesExcludeFirstTask) {
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
	 * @param currProcessInstance  当前流程实例————当前流程实例的任务提交引发了，这个子流程的创建
	 * @param firstTaskNodeOfSubProcess  代表子流程的节点下的第一个任务
	 * @param nodeIdentitySubProcess  代表子流程的节点
	 * @return 当没有默认处理人时返回空集合
	 */
    private List<SysUser> getDefaultTaskOwnerOfFirstNodeOfProcess(DhProcessInstance currProcessInstance, BpmActivityMeta firstTaskNodeOfSubProcess,
																  BpmActivityMeta nodeIdentitySubProcess) {
		List<SysUser> result = new ArrayList<>();
		// 获得nodeIdentitySubProcess这个流程的父级流程， 父流程的创建人、组织、部门编号作为选人的条件
        DhProcessInstance parentProcessInstance = getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(currProcessInstance,
				nodeIdentitySubProcess);
        if (parentProcessInstance == null) {
            // 当找不到的时候说明他的父流程实例还没有被创建，在提交后才会被创建
			parentProcessInstance = currProcessInstance;
        }
        DhActivityConf dhActivityConf = firstTaskNodeOfSubProcess.getDhActivityConf();
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
        selective.setActivityId(firstTaskNodeOfSubProcess.getSourceActivityId());
        selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
        List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.isEmpty()) {
            return result;
        }
        String departNo = parentProcessInstance.getDepartNo();
        String companyNum = parentProcessInstance.getCompanyNumber();
        // 获得被分配[人|角色|角色组]的 主键数据
        List<String> objIdList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);
        switch (assignTypeEnum) {
            // 角色相关
            case ROLE:
                return sysUserService.searchByRoleUidList(objIdList);
            case ROLE_AND_DEPARTMENT:
                return sysUserService.searchByRoleUidListAndDepartment(objIdList, departNo);
            case ROLE_AND_COMPANY:
                return sysUserService.searchByRoleUidListAndCompany(objIdList, companyNum);
            // 角色组相关
            case TEAM:
                return sysUserService.searchByTeamUidList(objIdList);
            case TEAM_AND_DEPARTMENT:
                return sysUserService.searchbyTeamUidListAndDepartment(objIdList, departNo);
            case TEAM_AND_COMPANY:
                return sysUserService.searchByTeamUidListAndCompany(objIdList, companyNum);
            // 指定处理人
            case USERS:
                return sysUserService.searchByUserUids(objIdList);
            // 根据表单字段选，
            case BY_FIELD:
                String field = objIdList.get(0);
                return sysUserService.searchByField(FormDataUtil.getFormDataJsonFromProcessInstance(parentProcessInstance), field);
            default:
                break;
        }
        return result;
	}


	public DhProcessInstance getParentProcessInstanceByCurrProcessInstanceAndNodeIdentifyProcess(DhProcessInstance currProcessInstance,
																								  BpmActivityMeta nodeIdentifyProcess) {
        /*  根据代表子流程的节点的父级节点，
            当这个节点是"0" 说明子流程的父级流程是主流程
            当这个节点是其它值，去查询哪个流程的TOKEN_ACTIVITY_ID是这个值即为子流程的父流程
         */
        String parentActivityId = nodeIdentifyProcess.getParentActivityId();
        if (BpmActivityMeta.PARENT_ACTIVITY_ID_OF_MAIN_PROCESS.equals(parentActivityId)) {
			// 这个代表子流程的节点位于主流程上
			if (currProcessInstance.getInsStatusId().intValue() == DhProcessInstance.STATUS_ID_DRAFT) {
				// 如果当前流程是草稿状态， 父级流程就是当前流程
				return currProcessInstance;
			} else {
				// 如果流程是已经发起的，父级流程就是主流程
				return dhProcessInstanceMapper.getMainProcessByInsId(currProcessInstance.getInsId());
			}
        } else {
			// 代表流程的节点不在主流程上，根据nodeIdentitySubProcesss的 parentActivityId 找tokenId是这个节点的流程
			return dhProcessInstanceMapper.getByInsIdAndTokenActivityId(currProcessInstance.getInsId(), parentActivityId);
		}

    }


    @Override
    public BpmRoutingData getBpmRoutingData(BpmActivityMeta sourceNode, JSONObject formData) {
    	formData = formData == null ? new JSONObject() : formData;
        BpmRoutingData routingData = getRoutingDataOfNextActivityTo(sourceNode, formData);
		routingData.removeAllDuplicate(); // 去除重复的元素
        threadBoolean.setFalse();
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
        	// 遍历每一个代表子流程的节点
			Map<String, BpmActivityMeta> actIdAndNodeIdentitySubProcessMap = new HashMap<>();
            for (BpmActivityMeta nodeIdentitySubProcess : routingData.getStartProcessNodes()) {
				actIdAndNodeIdentitySubProcessMap.put(nodeIdentitySubProcess.getActivityId(), nodeIdentitySubProcess);
                for (BpmActivityMeta taskNode : normalNodes) {
                    // 从所有的任务节点中，找到新
                    if (taskNode.getParentActivityId().equals(nodeIdentitySubProcess.getActivityId())) {
                        nodeIdentitySubProcess.setFirstTaskNode(taskNode);
                        normalNodes.remove(taskNode);
                        break;
                    }
                }

                if (nodeIdentitySubProcess.getParentActivityId().equals(sourceNode.getParentActivityId())) {
                    // 代表子流程的节点与任务节点平级
                    routingData.getStartProcessNodesOnSameDeepLevel().add(nodeIdentitySubProcess);
                    if (nodeIdentitySubProcess.getFirstTaskNode() != null) {
						routingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel().add(nodeIdentitySubProcess.getFirstTaskNode());
					}
                } else {
                    // 代表子流程的节点与任务节点不平级
                    routingData.getStartProcessNodesOnOtherDeepLevel().add(nodeIdentitySubProcess);
					if (nodeIdentitySubProcess.getFirstTaskNode() != null) {
						routingData.getFirstTaskNodesOfStartProcessOnOtherDeepLevel().add(nodeIdentitySubProcess.getFirstTaskNode());
					}
                }
            }
            routingData.setActIdAndNodeIdentitySubProcessMap(actIdAndNodeIdentitySubProcessMap);
        }// 装配完成子流程的起始任务节点，剩下的是与起始节点不平级的任务节点
        routingData.getTaskNodesOnOtherDeepLevel().addAll(normalNodes);
        // 为如果同级的子流程没有任务，就给他任务
        assembleTaskNodeForProcessWillStartOnSameLevel(routingData);
        return routingData;
    }

    /**
     * 为与起始节点平级的子流程发起人装配任务节点<br/>
	 * 将其子流程下的任务提取上来
	 * @param bpmRoutingData
	 */
    private void assembleTaskNodeForProcessWillStartOnSameLevel(BpmRoutingData bpmRoutingData) {
        List<BpmActivityMeta> startProcessNodesOnSameDeepLevel = bpmRoutingData.getStartProcessNodesOnSameDeepLevel();
        if (startProcessNodesOnSameDeepLevel.isEmpty()) {
            return;
        }
        List<BpmActivityMeta> nodesLackOfTask = new ArrayList<>();
        Map<String, BpmActivityMeta> parentIdAndSelfMap = new HashMap<>();
        for (BpmActivityMeta nodeIdentitySubProcess : startProcessNodesOnSameDeepLevel) {
            parentIdAndSelfMap.put(nodeIdentitySubProcess.getParentActivityId(), nodeIdentitySubProcess);
            if (nodeIdentitySubProcess.getFirstTaskNode() == null) {
                nodesLackOfTask.add(nodeIdentitySubProcess);
            }
        }
        if (nodesLackOfTask.isEmpty()) {
            return;
        }
        // 记录父流程节点与自身的关系
		for (BpmActivityMeta startProcessNodesOnOtherDeepLevel : bpmRoutingData.getStartProcessNodesOnOtherDeepLevel()) {
			parentIdAndSelfMap.put(startProcessNodesOnOtherDeepLevel.getParentActivityId(), startProcessNodesOnOtherDeepLevel);
		}

        List<BpmActivityMeta> firstTaskNodesOfStartProcessOnSameDeepLevel = bpmRoutingData.getFirstTaskNodesOfStartProcessOnSameDeepLevel();
        List<BpmActivityMeta> firstTaskNodesOfStartProcessOnOtherDeepLevel = bpmRoutingData.getFirstTaskNodesOfStartProcessOnOtherDeepLevel();

        for (BpmActivityMeta nodeLackOfTask : nodesLackOfTask) {
            BpmActivityMeta taskNode = fetchTaskNodeFromChild(nodeLackOfTask, parentIdAndSelfMap);
            nodeLackOfTask.setFirstTaskNode(taskNode); // 为缺少环节的流程设置流程处理人
            firstTaskNodesOfStartProcessOnSameDeepLevel.add(taskNode);
            firstTaskNodesOfStartProcessOnOtherDeepLevel.remove(taskNode);
        }

    }

	/**
	 * 将子流程下的第一个任务，转到缺少第一个任务的上级流程
	 * @param nodeLackOfTask
	 * @param activityIdAndProcessNodeMap
	 * @return
	 */
	private BpmActivityMeta fetchTaskNodeFromChild(BpmActivityMeta nodeLackOfTask, Map<String, BpmActivityMeta> activityIdAndProcessNodeMap) {
        String activityId = nodeLackOfTask.getActivityId();
        // 找到这个节点的子流程
        BpmActivityMeta sonProcessNode = activityIdAndProcessNodeMap.get(activityId);
        if (sonProcessNode == null) {
            throw new PlatformException("查找新建子流程的任务节点失败");
        }
        BpmActivityMeta taskNode = sonProcessNode.getFirstTaskNode();
        if (taskNode != null) {
            sonProcessNode.setFirstTaskNode(null); // 将找到的这个子流程的第一个任务置空
            return taskNode;
        } else {
            return fetchTaskNodeFromChild(sonProcessNode, activityIdAndProcessNodeMap);
        }

    }

    @Override
	public ServerResponse choosableHandlerMove(String insUid, String activityId, String departNo,
			String companyNum, String formData, HttpServletRequest request, String taskUid,
			String userUidArrStr,String condition) {
		List<SysUser> userList = getChoosableHandler(insUid, activityId, departNo, companyNum, formData, request, taskUid).getData();
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

	public boolean isFirstTaskOfSubProcessAndWasRejected(BpmActivityMeta taskNode, DhProcessInstance processInstance) {
		if (DhProcessInstance.INS_PARENT_OF_MAIN_PROCESS.equals(processInstance.getInsParent())) {
			// 如果节点是主流程上的，返回false
			return false;
		}
        BpmActivityMeta processNode = bpmActivityMetaMapper.queryByPrimaryKey(processInstance.getTokenActivityId());
        BpmActivityMeta firstUserTaskMetaOfSubProcess = bpmActivityMetaService.getFirstUserTaskNodeOfSubProcess(processNode);
        // 校验是不是子流程第一个节点
        if (!taskNode.getActivityId().equals(firstUserTaskMetaOfSubProcess.getActivityId())) {
            return false;
        }
        // 校验是不是回退回来的
        DhRoutingRecord latestRecord = dhRoutingRecordService.getLatestRoutingRecordByActivityToAndInsUid(taskNode.getActivityId(), processInstance.getInsUid());
        if (latestRecord == null) {
            return false;
        }
        if (DhRoutingRecord.ROUTE_TYPE_REJECT_TASK.equals(latestRecord.getRouteType())
                || DhRoutingRecord.ROUTE_TYPE_TRUN_OFF_TASK.equals(latestRecord.getRouteType())) {
            // 如果是驳回的或者撤转的
            return true;
        }
        return false;
    }

	public boolean isFirstTaskOfSubProcess(BpmActivityMeta taskNode, DhProcessInstance processInstance) {
		if (DhProcessInstance.INS_PARENT_OF_MAIN_PROCESS.equals(processInstance.getInsParent())) {
			// 如果节点是主流程上的，返回false
			return false;
		}
		BpmActivityMeta processNode = bpmActivityMetaMapper.queryByPrimaryKey(processInstance.getTokenActivityId());
		BpmActivityMeta firstUserTaskMetaOfSubProcess = bpmActivityMetaService.getFirstUserTaskNodeOfSubProcess(processNode);
		// 校验是不是子流程第一个节点
		return taskNode.getActivityId().equals(firstUserTaskMetaOfSubProcess.getActivityId());
	}

	@Override
	public ServerResponse<BpmActivityMeta> getActualFirstUserTaskNodeOfMainProcess(String proAppId, String proUid, String proVerUid) {
		BpmActivityMeta startNode = bpmActivityMetaService.getStartNodeOfMainProcess(proAppId, proUid, proVerUid);
		BpmRoutingData bpmRoutingData = getBpmRoutingData(startNode, null);
		// 获得预测的下个人员环节
		List<BpmActivityMeta> normalNodes = bpmRoutingData.getNormalNodes();
		if (CollectionUtils.isEmpty(normalNodes)) {
			return ServerResponse.createByErrorMessage("查找流程的第一个人员环节出错：找不到");
		} else if (normalNodes.size() > 1) {
			return ServerResponse.createByErrorMessage("查找流程的第一个人员环节出错：找到多个环节");
		}
		return ServerResponse.createBySuccess(normalNodes.iterator().next());
	}

	@Override
	public ServerResponse<List<SysUser>> getInteriorNotifyUserOfActivity(BpmActivityMeta bpmActivityMeta,DhProcessInstance dhProcessInstance) {
		// 初始化返回值，设置为空集合
	    List<SysUser> result = new ArrayList<>();
	    DhActivityConf dhActivityConf = bpmActivityMeta.getDhActivityConf();
	    String actcAssignType = dhActivityConf.getActcAssignType();
		DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
		DhActivityAssign selective = new DhActivityAssign();
		selective.setActivityId(bpmActivityMeta.getSourceActivityId());
		selective.setActaType(DhActivityAssignType.SEND_MAIL_ON_TASK_NODE.getCode());
		List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.isEmpty()) {
            return ServerResponse.createByErrorMessage("内部通知人配置异常");
        }
        String companyNum = dhProcessInstance.getCompanyNumber();
		// 获得被分配[人|角色]的 主键数据
		List<String> objIdList = ArrayUtil.getIdListFromDhActivityAssignList(assignList);
		switch (assignTypeEnum) {
			// 角色相关
			case ROLE:
				return ServerResponse.createBySuccess(sysUserService.searchByRoleUidList(objIdList));
			case ROLE_AND_COMPANY:
				return ServerResponse.createBySuccess(sysUserService.searchByRoleUidListAndCompany(objIdList,companyNum));
			// 指定处理人
			case USERS:
				return ServerResponse.createBySuccess(sysUserService.searchByUserUids(objIdList));
			default:
				break;
		}
        return ServerResponse.createBySuccess(result);
	}


}
