package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.FormDataUtil;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
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

	@Override
	public ServerResponse<List<BpmActivityMeta>> showRouteBar(String insUid, String activityId, String departNo,
			String companyNum, String formData) {
		// TODO Auto-generated method stub
		
		BpmActivityMeta bpmActivityMeta=bpmActivityMetaMapper.queryByPrimaryKey(activityId);
		//根据表单字段查
		DhProcessInstance dhProcessInstance = dhProcessInstanceMapper.selectByPrimaryKey(insUid);
		String insInitUser=dhProcessInstance.getInsInitUser(); //流程发起人
		String insDate=dhProcessInstance.getInsData();//实例表单
		
		JSONObject formJson=new JSONObject();
		if(StringUtils.isNotBlank(formData)) {
			formJson=FormDataUtil.formDataCombine(JSONObject.parseObject(formData), JSONObject.parseObject(insDate));
		}
		List<BpmActivityMeta> activityMetaList=getNextActivities(bpmActivityMeta,formJson); 
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
			List<String> idList = ArrayUtil.getIdListFromDhActivityAssignList(assignList); // 角色或部门

			String userUid = "";
			String userName = "";

			switch (assignTypeEnum) {
			case ROLE:
			case ROLE_AND_DEPARTMENT:
			case ROLE_AND_COMPANY:
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setRoleUid(ArrayUtil.toArrayString(idList));
				if(StringUtils.isNotBlank(departNo)) {
					roleUser.setDepartUid(departNo);
				}
				if(StringUtils.isNotBlank(companyNum)) {
					roleUser.setCompanyCode(companyNum);
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
				sysTeamMember.setTeamUid(ArrayUtil.toArrayString(idList));

				List<SysTeamMember> sysTeamMembers = sysTeamMemberMapper.selectTeamUser(sysTeamMember);
				for (SysTeamMember sysTeamMember2 : sysTeamMembers) {
					userUid += sysTeamMember2.getUserUid() + ";";
					userName += sysTeamMember2.getUserName() + ";";
				}

				break;
			case LEADER_OF_PRE_ACTIVITY_USER:

				break;
			case USERS:
				List<SysUser> users = sysUserMapper.listByPrimaryKeyList(idList);
				for (SysUser sysUser : users) {
					userUid += sysUser.getUserUid() + ";";
					userName += sysUser.getUserName() + ";";
				}
				break;
			case PROCESS_CREATOR:
				
				break;
			case BY_FIELD:
				if (assignList.size() > 0) {
					String field = assignList.get(0).getActaAssignId();
					dhActivityConf.setHandleField(field);
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
        
        for (BpmActivityMeta gatewayMeta : gatewayList) {
            DhGatewayLine lineSelective = new DhGatewayLine();
            lineSelective.setActivityId(gatewayMeta.getActivityId());
            List<DhGatewayLine> lines = dhGatewayLineService.getGateWayLinesByCondition(lineSelective);
            // 找出这个规则需要的所有表单字段
            // List<String> variableNeeded
            // formData.getString(var)
            // 
            
            
        }
	    
	    return result;
	}
	
}
