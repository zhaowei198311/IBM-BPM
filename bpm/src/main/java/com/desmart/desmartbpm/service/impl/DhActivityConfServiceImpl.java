package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.desmart.common.exception.PlatformException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhActivityRejectMapper;
import com.desmart.desmartbpm.dao.DhTriggerMapper;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhActivityReject;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.enums.DhActivityAssignAssignType;
import com.desmart.desmartbpm.enums.DhActivityAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfOuttimeNotifyType;
import com.desmart.desmartbpm.enums.DhActivityConfRejectType;
import com.desmart.desmartbpm.service.DhActivityConfService;
import com.desmart.desmartbpm.service.DhStepService;
import com.desmart.desmartsystem.dao.SysRoleMapper;
import com.desmart.desmartsystem.dao.SysTeamMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysRole;
import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.entity.SysUser;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

@Service
public class DhActivityConfServiceImpl implements DhActivityConfService {
    private static final Logger LOG = LoggerFactory.getLogger(DhActivityConfServiceImpl.class);

    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private DhActivityAssignMapper dhActivityAssignMapper;
    @Autowired
    private DhActivityRejectMapper dhActivityRejectMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysTeamMapper sysTeamMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private DhStepService dhStepService;
    @Autowired
    private DhTriggerMapper dhTriggerMapper;
    
    public ServerResponse getActivityConfData(String actcUid) {
        if(StringUtils.isBlank(actcUid)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        DhActivityConf dhActivityConf = dhActivityConfMapper.selectByPrimaryKey(actcUid);
        if (dhActivityConf == null) {
            return ServerResponse.createByErrorMessage("此环节不存在");
        }
        
        String actcAssignType = dhActivityConf.getActcAssignType();
        
        // 加载默认处理人信息
        ServerResponse serverResponse = loadHandleOfActivity(dhActivityConf);
        
        //加载可选处理人信息
        loadChosseAbleHandleOfActivity(dhActivityConf);
        
        // 加载退回环节信息
        loadRejectActivities(dhActivityConf);
        
        // 加载超时通知人信息
        loadOuttimeNotifyInfo(dhActivityConf);
        
        Map<String, Object> result = new HashMap<>();
        result.put("conf", dhActivityConf);
        
        // 获取该环节的所有步骤的信息
        ServerResponse<List<DhStep>> response = dhStepService.getStepOfDhActivityConf(dhActivityConf);
        result.put("stepList", response.getData());
        return ServerResponse.createBySuccess(result);
    }
    
    /**
     * 加载可选处理人信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse loadChosseAbleHandleOfActivity(DhActivityConf dhActivityConf) {
    	String actcAssignType = dhActivityConf.getActcChooseableHandlerType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
        String activityId = dhActivityConf.getActivityId();
        if (assignTypeEnum == null) {
            return ServerResponse.createByErrorMessage("处理人类型不符合要求");
        }
        /*if (assignTypeEnum == DhActivityConfAssignType.NONE) {
            return ServerResponse.createBySuccess();
        }*/
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
        List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.size() == 0) {
            return ServerResponse.createByErrorMessage("缺少处理人信息");
        }
        List<String> idList = getIdListFromDhActivityAssignList(assignList);
        String str = "";
        String strView = "";
        switch (assignTypeEnum) {
        case ROLE:
        case ROLE_AND_DEPARTMENT:
        case ROLE_AND_COMPANY:
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(idList); 
            for (SysRole role : roleList) {
                str += role.getRoleUid() + ";";
                strView += role.getRoleName() + ";";
            }
            dhActivityConf.setChooseableHandleRole(str);
            dhActivityConf.setChooseableHandleRoleView(strView);
            break;
        case TEAM:
        case TEAM_AND_DEPARTMENT:
        case TEAM_AND_COMPANY:
            List<SysTeam> sysTeamList = sysTeamMapper.listByPrimaryKeyList(idList); 
            for (SysTeam sysTeam : sysTeamList) {
                str += sysTeam.getTeamUid() + ";";
                strView += sysTeam.getTeamName() + ";";
            }
            dhActivityConf.setChooseableHandleTeam(str);
            dhActivityConf.setChooseableHandleTeamView(strView);
            break;
        case LEADER_OF_PRE_ACTIVITY_USER:
            
            break;
        case USERS:
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            for (SysUser sysUser : userList) {
                str += sysUser.getUserUid() + ";";
                strView += sysUser.getUserName() + ";";
            }
            dhActivityConf.setChooseableHandleUser(str);
            dhActivityConf.setChooseableHandleUserView(strView);
            break;
        case PROCESS_CREATOR:
            
            break;
        case BY_FIELD:
            if (assignList.size() > 0) {
                String field = assignList.get(0).getActaAssignId();
                dhActivityConf.setChooseableHandleField(field);
            }
            break;
        case BY_TRIGGER:
            DhTrigger dhTrigger = dhTriggerMapper.getByPrimaryKey(idList.get(0));
            str+=dhTrigger.getTriUid();
            strView+=dhTrigger.getTriTitle();
            dhActivityConf.setChooseableHandleTrigger(str);
            dhActivityConf.setChooseableHandleTriggerTitle(strView);
            break;
        default:
            break; 
        }
        return ServerResponse.createBySuccess();
	}





	@Transactional
    public ServerResponse updateDhActivityConf(DhActivityConf dhActivityConf) {
        if (StringUtils.isBlank(dhActivityConf.getActcUid())) {
            return ServerResponse.createByErrorMessage("缺少环节唯一id");
        }
        DhActivityConf activityInDb = dhActivityConfMapper.selectByPrimaryKey(dhActivityConf.getActcUid());
        if (activityInDb == null) {
            return ServerResponse.createByErrorMessage("此环节不存在");
        } 
        dhActivityConf.setActivityId(activityInDb.getActivityId());
        // 判断处理人类型并记录
        ServerResponse serverResponse = updateHandleOfActivity(dhActivityConf);
        if (!serverResponse.isSuccess()) {
            throw new PlatformException(serverResponse.getMsg());
        }
        
        // 判断可选处理人类型并记录
        if("TRUE".equals(dhActivityConf.getActcCanChooseUser())) {//判断是可选处理人
        	serverResponse = updateChooseAbleHandleOfActivity(dhActivityConf);
        	if (!serverResponse.isSuccess()) {
        		throw new PlatformException(serverResponse.getMsg());
        	}
        }else {
        	DhActivityAssign selective = new DhActivityAssign();
        	String activityId = dhActivityConf.getActivityId();
            selective.setActivityId(activityId);
            selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
            dhActivityAssignMapper.deleteBySelective(selective);
            
        }
        // 判断回退环节并记录
        serverResponse = updateRejectActivities(dhActivityConf);
        if (!serverResponse.isSuccess()) {
            throw new PlatformException(serverResponse.getMsg());
        }
        // 判断超时提醒并记录
        serverResponse = updateOuttimeNotifyInfo(dhActivityConf);
        if (!serverResponse.isSuccess()) {
            throw new PlatformException(serverResponse.getMsg());
        }
        
        String updator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhActivityConf.setUpdator(updator);
        dhActivityConfMapper.updateByPrimaryKey(dhActivityConf);
        
        return ServerResponse.createBySuccess();
    }

    @Override
    public int removeByActcUidList(List<String> actcUidList) {
        if (CollectionUtils.isEmpty(actcUidList)) {
            return 0;
        }
        return dhActivityConfMapper.deleteByActivityIds(actcUidList);
    }

    @Override
    public int insertBatch(List<DhActivityConf> dhActivityConfList) {
        if (CollectionUtils.isEmpty(dhActivityConfList)) {
            return 0;
        }
        return dhActivityConfMapper.insertBatch(dhActivityConfList);
    }


    /**
     * 为环节更新可选处理人信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse updateChooseAbleHandleOfActivity(DhActivityConf dhActivityConf) {
		// TODO Auto-generated method stub
    	String actcChooseableHandlerType = dhActivityConf.getActcChooseableHandlerType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcChooseableHandlerType);
        if (assignTypeEnum == null) {
            return ServerResponse.createByErrorMessage("处理人类型不符合要求");
        }
        String activityAssignType = null;  // 分配的类型
        String activityId = dhActivityConf.getActivityId();
        // 删除老的可选处理人记录
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
        dhActivityAssignMapper.deleteBySelective(selective);
        List<DhActivityAssign> assignList = new ArrayList<>(); 
        
        switch (assignTypeEnum) {
        case NONE:
            break;
        case ROLE:
        case ROLE_AND_DEPARTMENT:
        case ROLE_AND_COMPANY:
            activityAssignType = DhActivityAssignAssignType.ROLE.getCode();
            String chooseHandleRole = dhActivityConf.getChooseableHandleRole();
            if (StringUtils.isBlank(chooseHandleRole)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> roleIdList = Arrays.asList(chooseHandleRole.split(";"));
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(roleIdList);
            if (roleIdList.size() != roleList.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysRole role : roleList) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(activityAssignType);
                assign.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
                assign.setActaAssignId(role.getRoleUid());
                assignList.add(assign);
            }
            break;
        case TEAM:
        case TEAM_AND_DEPARTMENT:
        case TEAM_AND_COMPANY:
            activityAssignType = DhActivityAssignAssignType.TEAM.getCode();
            String chooseableHandleTeam2 = dhActivityConf.getChooseableHandleTeam();
            if (StringUtils.isBlank(chooseableHandleTeam2)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> teamIdList2 = Arrays.asList(chooseableHandleTeam2.split(";"));
            List<SysTeam> teamList2 = sysTeamMapper.listByPrimaryKeyList(teamIdList2);
            if (teamIdList2.size() != teamList2.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysTeam team : teamList2) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(activityAssignType);
                assign.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
                assign.setActaAssignId(team.getTeamUid());
                assignList.add(assign);
            }
            break;
        case LEADER_OF_PRE_ACTIVITY_USER:
            
            break;
        case USERS:
            String chooseableHandleUser = dhActivityConf.getChooseableHandleUser();
            if (StringUtils.isBlank(chooseableHandleUser)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> idList = Arrays.asList(chooseableHandleUser.split(";"));
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            if (idList.size() != userList.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysUser user : userList) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(DhActivityAssignAssignType.USER.getCode());
                assign.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
                assign.setActaAssignId(user.getUserUid());
                assignList.add(assign);
            }
            break;
        case PROCESS_CREATOR:
            break;
        case BY_FIELD:
            String chosseableHandleField = dhActivityConf.getChooseableHandleField();
            if (StringUtils.isBlank(chosseableHandleField)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            if (chosseableHandleField.length() > 60) {
                return ServerResponse.createByErrorMessage("处理人字段过长");
            }
            DhActivityAssign assign = new DhActivityAssign();
            assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
            assign.setActivityId(activityId);
            assign.setActaAssignType(DhActivityAssignAssignType.FIELD.getCode());
            assign.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
            assign.setActaAssignId(chosseableHandleField.trim());
            assignList.add(assign);
            break;
        case BY_TRIGGER:
        	 String chosseableHandleTrigger = dhActivityConf.getChooseableHandleTrigger();
        	 DhActivityAssign assign1 = new DhActivityAssign();
             assign1.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
             assign1.setActivityId(activityId);
             assign1.setActaAssignType(DhActivityAssignAssignType.TRIGGER.getCode());
             assign1.setActaType(DhActivityAssignType.CHOOSEABLE_HANDLER.getCode());
             assign1.setActaAssignId(chosseableHandleTrigger.trim());
             assignList.add(assign1);
        	break;
        default:
            break; 
        }
        if (assignList.size() > 0) {
            dhActivityAssignMapper.insertBatch(assignList);
        }
        return ServerResponse.createBySuccess();
	}

	/**
     * 为环节更新默认处理人信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse updateHandleOfActivity(DhActivityConf dhActivityConf) {
        String actcAssignType = dhActivityConf.getActcAssignType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
        if (assignTypeEnum == null) {
            return ServerResponse.createByErrorMessage("处理人类型不符合要求");
        }
        String activityAssignType = null;  // 分配的类型
        String activityId = dhActivityConf.getActivityId();
        // 删除老的默认处理人记录
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
        dhActivityAssignMapper.deleteBySelective(selective);
        List<DhActivityAssign> assignList = new ArrayList<>(); 
        
        switch (assignTypeEnum) {
        case NONE:
            break;
        case ROLE:
        case ROLE_AND_DEPARTMENT:
        case ROLE_AND_COMPANY:
            activityAssignType = DhActivityAssignAssignType.ROLE.getCode();
            String handleRole = dhActivityConf.getHandleRole();
            if (StringUtils.isBlank(handleRole)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> roleIdList = Arrays.asList(handleRole.split(";"));
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(roleIdList);
            if (roleIdList.size() != roleList.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysRole role : roleList) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(activityAssignType);
                assign.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
                assign.setActaAssignId(role.getRoleUid());
                assignList.add(assign);
            }
            break;
        case TEAM:
        case TEAM_AND_DEPARTMENT:
        case TEAM_AND_COMPANY:
            activityAssignType = DhActivityAssignAssignType.TEAM.getCode();
            String handleTeam2 = dhActivityConf.getHandleTeam();
            if (StringUtils.isBlank(handleTeam2)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> teamIdList2 = Arrays.asList(handleTeam2.split(";"));
            List<SysTeam> teamList2 = sysTeamMapper.listByPrimaryKeyList(teamIdList2);
            if (teamIdList2.size() != teamList2.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysTeam team : teamList2) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(activityAssignType);
                assign.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
                assign.setActaAssignId(team.getTeamUid());
                assignList.add(assign);
            }
            break;
        case LEADER_OF_PRE_ACTIVITY_USER:
            
            break;
        case USERS:
            String handleUser = dhActivityConf.getHandleUser();
            if (StringUtils.isBlank(handleUser)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            List<String> idList = Arrays.asList(handleUser.split(";"));
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            if (idList.size() != userList.size()) {
                return ServerResponse.createByErrorMessage("处理人信息错误");
            }
            for (SysUser user : userList) {
                DhActivityAssign assign = new DhActivityAssign();
                assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
                assign.setActivityId(activityId);
                assign.setActaAssignType(DhActivityAssignAssignType.USER.getCode());
                assign.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
                assign.setActaAssignId(user.getUserUid());
                assignList.add(assign);
            }
            break;
        case PROCESS_CREATOR:
            break;
        case BY_FIELD:
            String handleField = dhActivityConf.getHandleField();
            if (StringUtils.isBlank(handleField)) {
                return ServerResponse.createByErrorMessage("缺少处理人信息");
            }
            if (handleField.length() > 60) {
                return ServerResponse.createByErrorMessage("处理人字段过长");
            }
            DhActivityAssign assign = new DhActivityAssign();
            assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
            assign.setActivityId(activityId);
            assign.setActaAssignType(DhActivityAssignAssignType.FIELD.getCode());
            assign.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
            assign.setActaAssignId(handleField.trim());
            assignList.add(assign);
            break;
        default:
            break; 
        }
        if (assignList.size() > 0) {
            dhActivityAssignMapper.insertBatch(assignList);
        }
        return ServerResponse.createBySuccess();
    }
    
    public static void main(String[] args) {
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf("none");
        System.out.println(assignTypeEnum == DhActivityConfAssignType.NONE);
    }
    
    /**
     * 加载默认处理人的信息到字段  
     * handleUser;handleTeam;handleRole;handleField
     * handleUserView;handleTeamView;handleRoleView;handleFieldView
     * @param dhActivityConf
     * @return
     */
    private ServerResponse loadHandleOfActivity(DhActivityConf dhActivityConf) {
        String actcAssignType = dhActivityConf.getActcAssignType();
        DhActivityConfAssignType assignTypeEnum = DhActivityConfAssignType.codeOf(actcAssignType);
        String activityId = dhActivityConf.getActivityId();
        if (assignTypeEnum == null) {
            return ServerResponse.createByErrorMessage("处理人类型不符合要求");
        }
        if (assignTypeEnum == DhActivityConfAssignType.NONE) {
            return ServerResponse.createBySuccess();
        }
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.DEFAULT_HANDLER.getCode());
        List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        if (assignList.size() == 0) {
            return ServerResponse.createByErrorMessage("缺少处理人信息");
        }
        List<String> idList = getIdListFromDhActivityAssignList(assignList);
        String str = "";
        String strView = "";
        switch (assignTypeEnum) {
        case ROLE:
        case ROLE_AND_DEPARTMENT:
        case ROLE_AND_COMPANY:
            List<SysRole> roleList = sysRoleMapper.listByPrimaryKeyList(idList); 
            for (SysRole role : roleList) {
                str += role.getRoleUid() + ";";
                strView += role.getRoleName() + ";";
            }
            dhActivityConf.setHandleRole(str);
            dhActivityConf.setHandleRoleView(strView);
            break;
        case TEAM:
        case TEAM_AND_DEPARTMENT:
        case TEAM_AND_COMPANY:
            List<SysTeam> sysTeamList = sysTeamMapper.listByPrimaryKeyList(idList); 
            for (SysTeam sysTeam : sysTeamList) {
                str += sysTeam.getTeamUid() + ";";
                strView += sysTeam.getTeamName() + ";";
            }
            dhActivityConf.setHandleTeam(str);
            dhActivityConf.setHandleTeamView(strView);
            break;
        case LEADER_OF_PRE_ACTIVITY_USER:
            
            break;
        case USERS:
            List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(idList); 
            for (SysUser sysUser : userList) {
                str += sysUser.getUserUid() + ";";
                strView += sysUser.getUserName() + ";";
            }
            dhActivityConf.setHandleUser(str);
            dhActivityConf.setHandleUserView(strView);
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
        return ServerResponse.createBySuccess();
    }
    
    private List<String> getIdListFromDhActivityAssignList(List<DhActivityAssign> assignList) {
        List<String> idList = new ArrayList<>();
        for (DhActivityAssign assign : assignList) {
            idList.add(assign.getActaAssignId());
        }
        return idList;
    }
    
    /**
     * 根据传入的驳回配置更新 驳回环节信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse updateRejectActivities(DhActivityConf dhActivityConf) {
        // 删除老设置
        dhActivityRejectMapper.deleteByActivityId(dhActivityConf.getActivityId());
        
        String actcCanReject = dhActivityConf.getActcCanReject();
        if (!Const.Boolean.FALSE.equals(actcCanReject) && !Const.Boolean.TRUE.equals(actcCanReject)) {
            return ServerResponse.createByErrorMessage("驳回设置失败");
        }
        String actcRejectType = dhActivityConf.getActcRejectType();
        DhActivityConfRejectType rejectTypeEmun = DhActivityConfRejectType.codeOf(actcRejectType);
        
        if (Const.Boolean.TRUE.equals(actcCanReject) && rejectTypeEmun == null) {
            return ServerResponse.createByErrorMessage("驳回设置失败,驳回类型不正确");
        }
        if (rejectTypeEmun.equals(DhActivityConfRejectType.TO_ACTIVITIES)) {
            String rejectActivities = dhActivityConf.getRejectActivities();
            if (StringUtils.isBlank(rejectActivities)) {
            	if (Const.Boolean.TRUE.equals(actcCanReject)){
                return ServerResponse.createByErrorMessage("驳回设置失败,缺少驳回环节");
            
            	}
            }
            String[] actIdArr = rejectActivities.split(";");
            List<DhActivityReject> rejectList = new ArrayList<>();
            for (String actId : actIdArr) {
                String uid = EntityIdPrefix.DH_ACTIVITY_REJECT + UUID.randomUUID().toString();
                DhActivityReject reject = new DhActivityReject(uid, dhActivityConf.getActivityId(), actId);
                rejectList.add(reject);
            }
            dhActivityRejectMapper.insertBatch(rejectList);
        }
        return ServerResponse.createBySuccess();
    }
    
    /**
     * 更新超时通知人信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse updateOuttimeNotifyInfo(DhActivityConf dhActivityConf) {
        String activityId = dhActivityConf.getActivityId();
        List<DhActivityAssign> assignList = Lists.newArrayList();
        // 删除之前的记录
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.OUTTIME_NOTIFY.getCode());
        dhActivityAssignMapper.deleteBySelective(selective);
        // 超时通知人员
        DhActivityConfOuttimeNotifyType eumType = 
        		DhActivityConfOuttimeNotifyType.codeOf(dhActivityConf.getActcOuttimeNotifyType());
		if (eumType != null) {
			switch (eumType) {
			case USERS:
				String outtimeUser = dhActivityConf.getOuttimeUser();
				if (StringUtils.isNotBlank(outtimeUser)) {
					List<String> userIdList = Arrays.asList(outtimeUser.split(";"));
					List<SysUser> userList = sysUserMapper.listByPrimaryKeyList(userIdList);
					if (userIdList.size() != userList.size()) {
						return ServerResponse.createByErrorMessage("超时通知用户不存在");
					}
					for (SysUser user : userList) {
						DhActivityAssign assign = new DhActivityAssign();
						assign.setActaUid(EntityIdPrefix.DH_ACTIVITY_ASSIGN + UUID.randomUUID().toString());
						assign.setActivityId(activityId);
						assign.setActaType(DhActivityAssignType.OUTTIME_NOTIFY.getCode());
						assign.setActaAssignType(DhActivityAssignAssignType.USER.getCode());
						assign.setActaAssignId(user.getUserUid());
						assignList.add(assign);
					}
				}
				break;
			case HANDLER_USER:
				break;
			case HANDLER_USER_SUPERIOR:
				break;

			default:
				break;
			}
		}
        if (assignList.size() > 0) {
            dhActivityAssignMapper.insertBatch(assignList);
        }
        return ServerResponse.createBySuccess();
        
    }
    
    /**
     * 加载驳回环节的信息
     * @param dhActivityConf
     */
    private ServerResponse loadRejectActivities(DhActivityConf dhActivityConf) {
        String activityId = dhActivityConf.getActivityId();
        List<DhActivityReject> rejectList = dhActivityRejectMapper.listByActivityId(activityId);
        if (rejectList.size() == 0) {
            return ServerResponse.createBySuccess();
        }
        String str = "";
        String strView = "";
        for (DhActivityReject dhActivityReject : rejectList) {
            str += dhActivityReject.getActrRejectActivity() + ";";
            strView += dhActivityReject.getActivityName() + ";";
        }
        dhActivityConf.setRejectActivities(str);
        dhActivityConf.setRejectActivitiesView(strView);
        return ServerResponse.createBySuccess();
        
    }
    
    /**
     * 加载超时通知人的信息
     * @param dhActivityConf
     * @return
     */
    private ServerResponse loadOuttimeNotifyInfo(DhActivityConf dhActivityConf) {
        String activityId = dhActivityConf.getActivityId();
        DhActivityAssign selective = new DhActivityAssign();
        selective.setActivityId(activityId);
        selective.setActaType(DhActivityAssignType.OUTTIME_NOTIFY.getCode());
        List<DhActivityAssign> assignList = dhActivityAssignMapper.listByDhActivityAssignSelective(selective);
        String outtimeUser = "";
        String outtimeUserView = "";
        String outtimeRole = "";
        String outtimeRoleView = "";
        String outtimeTeam = "";
        String outtimeTeamView = "";
        for (DhActivityAssign assign : assignList) {
            if (DhActivityAssignAssignType.USER.getCode().equals(assign.getActaAssignType()) && assign.getUserName() != null) {
                outtimeUser += assign.getActaAssignId() + ";";
                outtimeUserView += assign.getUserName() + ";";
            }else if (DhActivityAssignAssignType.ROLE.getCode().equals(assign.getActaAssignType()) && assign.getRoleName() != null) {
                outtimeRole += assign.getActaAssignId() + ";";
                outtimeRoleView += assign.getRoleName() + ";";
            }else if (DhActivityAssignAssignType.TEAM.getCode().equals(assign.getActaAssignType()) && assign.getTeamName() != null) {
                outtimeTeam += assign.getActaAssignId() + ";";
                outtimeTeamView += assign.getTeamName() + ";";
            }
            
        }
        dhActivityConf.setOuttimeUser(outtimeUser);
        dhActivityConf.setOuttimeUserView(outtimeUserView);
        dhActivityConf.setOuttimeRole(outtimeRole);
        dhActivityConf.setOuttimeRoleView(outtimeRoleView);
        dhActivityConf.setOuttimeTeam(outtimeTeam);
        dhActivityConf.setOuttimeTeamView(outtimeTeamView);
        
        return ServerResponse.createBySuccess();
    }
}


