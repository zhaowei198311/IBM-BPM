package com.desmart.desmartbpm.entity;

import java.util.Date;

public class DhActivityConf {
    public static final String TIME_UNIT_HOUR = "hour";
    public static final String TIME_UNIT_DAY = "day";
    public static final String TIME_UNIT_MONTH = "month";
    public static final String DELAY_TYPE_NONE = "none";
    public static final String DELAY_TYPE_TIME = "time";
    public static final String DELAY_TYPE_FIELD = "field";

    private String actcUid;

    private String activityId;

    private Integer actcSort;

    private Double actcTime;  // 运行时间（数量）

    private String actcTimeunit; // 运行时间（单位）

    private String actcAssignType;  // 默认处理人类型 （无，角色+部门等）
    
    private String actcChooseableHandlerType; // 可选处理人类型 （全员，不可选，角色）
    
    private String actcOuttimeNotifyType; //超时通知人类型（处理人，处理人的上级，指定人员）

    private String actcAssignVariable;

    private String signCountVarname;

    private String actcCanEditAttach;

    private String actcCanUploadAttach;

    private String actcCanDelegate;

    private String actcCanDeleteAttach;

    private String actcCanMessageNotify;

    private String actcCanMailNotify;

    private String actcMailNotifyTemplate;
    
    private String actcMessageNotifyTemplate;

    private String actcCanReject;  // 是否可以驳回

    private String actcRejectType;  // 驳回类型

    private String actcCanRevoke; // 是否可以取回

    private String actcCanAutocommit;

    private String actcCanAdd; // 是否允许加签

    private String actcCanUserToField;

    private String actcCanAuditToField;

    private String actcCanApprove; // 是否允许审批

    private String actcUserToField;

    private String actcAuditToField;

    private String actcOuttimeTrigger;

    private String actcOuttimeTemplate;

    private String actcDescription;

    private String actcDefTitle;

    private String actcDefSubjectMessage;

    private String actcDefMessage;

    private String actcDefDescription;

    private String actcAlert;

    private String actcPriorityVariable;

    private String actcCanCancel;

    private String actcCanPause;

    private String actcCanSkip;

    private String updator;

    private Date updateTime;
    
    private String actcCanChooseUser;
    
    private String actcCanTransfer;  // 传阅
    
    private String actcResponsibility;

    private String actcCanSkipFromReject;  // 驳回后能否直接回到此环节

    private String actcIsSystemTask;  // 是否系统任务

    private String actcDelayType; // 延迟类型  none  time   field

    private Integer actcDelayTime;  // 延时时间数量

    private String actcDelayTimeunit;  // 延时时间单位

    private String actcDelayField;  // 指定延时提交任务的时间点的表单字段
    
    // 非表中字段
    private String actcOuttimeTriggerTitle; // 超时触发器名称
    private String handleUser;  // 默认处理人
    private String handleUserView;
    private String handleRole;  // 默认处理角色
    private String handleRoleView;
    private String handleTeam;  // 默认处理角色组
    private String handleTeamView;
    private String handleField; // 默认处理字段
    private String chooseableHandleUser;  // 可选处理人
    private String chooseableHandleUserView;
    private String chooseableHandleRole;  // 可选处理角色
    private String chooseableHandleRoleView;
    private String chooseableHandleTeam;  // 可选处理角色组
    private String chooseableHandleTeamView;
    private String chooseableHandleField; // 可选处理字段
    private String chooseableHandleTriggerTitle;
    private String chooseableHandleTrigger;//可选处理触发器
    private String outtimeUser;
    private String outtimeUserView;
    private String outtimeRole;
    private String outtimeRoleView;
    private String outtimeTeam;
    private String outtimeTeamView;
    private String rejectActivities;
    private String rejectActivitiesView;
    private String actcMailNotifyTemplateView;
    private String actcOuttimeTemplateView;//超时通知模板名


    public DhActivityConf() {

    }

    public String getActcCanSkipFromReject() {
        return actcCanSkipFromReject;
    }

    public void setActcCanSkipFromReject(String actcCanSkipFromReject) {
        this.actcCanSkipFromReject = actcCanSkipFromReject;
    }

    public String getActcUid() {
        return actcUid;
    }

    public void setActcUid(String actcUid) {
        this.actcUid = actcUid;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Integer getActcSort() {
        return actcSort;
    }

    public void setActcSort(Integer actcSort) {
        this.actcSort = actcSort;
    }

    public Double getActcTime() {
        return actcTime;
    }

    public void setActcTime(Double actcTime) {
        this.actcTime = actcTime;
    }

    public String getActcTimeunit() {
        return actcTimeunit;
    }

    public void setActcTimeunit(String actcTimeunit) {
        this.actcTimeunit = actcTimeunit;
    }

    public String getActcAssignType() {
        return actcAssignType;
    }

    public void setActcAssignType(String actcAssignType) {
        this.actcAssignType = actcAssignType;
    }

    public String getActcAssignVariable() {
        return actcAssignVariable;
    }

    public void setActcAssignVariable(String actcAssignVariable) {
        this.actcAssignVariable = actcAssignVariable;
    }

    public String getSignCountVarname() {
        return signCountVarname;
    }

    public void setSignCountVarname(String signCountVarname) {
        this.signCountVarname = signCountVarname;
    }

    public String getActcCanEditAttach() {
        return actcCanEditAttach;
    }

    public void setActcCanEditAttach(String actcCanEditAttach) {
        this.actcCanEditAttach = actcCanEditAttach;
    }

    public String getActcCanUploadAttach() {
        return actcCanUploadAttach;
    }

    public void setActcCanUploadAttach(String actcCanUploadAttach) {
        this.actcCanUploadAttach = actcCanUploadAttach;
    }

    public String getActcCanDelegate() {
        return actcCanDelegate;
    }

    public void setActcCanDelegate(String actcCanDelegate) {
        this.actcCanDelegate = actcCanDelegate;
    }

    public String getActcCanDeleteAttach() {
        return actcCanDeleteAttach;
    }

    public void setActcCanDeleteAttach(String actcCanDeleteAttach) {
        this.actcCanDeleteAttach = actcCanDeleteAttach;
    }

    public String getActcCanMessageNotify() {
        return actcCanMessageNotify;
    }

    public void setActcCanMessageNotify(String actcCanMessageNotify) {
        this.actcCanMessageNotify = actcCanMessageNotify;
    }

    public String getActcCanMailNotify() {
        return actcCanMailNotify;
    }

    public void setActcCanMailNotify(String actcCanMailNotify) {
        this.actcCanMailNotify = actcCanMailNotify;
    }

    public String getActcMailNotifyTemplate() {
        return actcMailNotifyTemplate;
    }

    public void setActcMailNotifyTemplate(String actcMailNotifyTemplate) {
        this.actcMailNotifyTemplate = actcMailNotifyTemplate;
    }

    public String getActcCanReject() {
        return actcCanReject;
    }

    public void setActcCanReject(String actcCanReject) {
        this.actcCanReject = actcCanReject;
    }

    public String getActcRejectType() {
        return actcRejectType;
    }

    public void setActcRejectType(String actcRejectType) {
        this.actcRejectType = actcRejectType;
    }

    public String getActcCanRevoke() {
        return actcCanRevoke;
    }

    public void setActcCanRevoke(String actcCanRevoke) {
        this.actcCanRevoke = actcCanRevoke;
    }

    public String getActcCanAutocommit() {
        return actcCanAutocommit;
    }

    public void setActcCanAutocommit(String actcCanAutocommit) {
        this.actcCanAutocommit = actcCanAutocommit;
    }

    public String getActcCanAdd() {
        return actcCanAdd;
    }

    public void setActcCanAdd(String actcCanAdd) {
        this.actcCanAdd = actcCanAdd;
    }

    public String getActcCanUserToField() {
        return actcCanUserToField;
    }

    public void setActcCanUserToField(String actcCanUserToField) {
        this.actcCanUserToField = actcCanUserToField;
    }

    public String getActcCanAuditToField() {
        return actcCanAuditToField;
    }

    public void setActcCanAuditToField(String actcCanAuditToField) {
        this.actcCanAuditToField = actcCanAuditToField;
    }

    public String getActcCanApprove() {
        return actcCanApprove;
    }

    public void setActcCanApprove(String actcCanApprove) {
        this.actcCanApprove = actcCanApprove;
    }

    public String getActcUserToField() {
        return actcUserToField;
    }

    public void setActcUserToField(String actcUserToField) {
        this.actcUserToField = actcUserToField;
    }

    public String getActcAuditToField() {
        return actcAuditToField;
    }

    public void setActcAuditToField(String actcAuditToField) {
        this.actcAuditToField = actcAuditToField;
    }

    public String getActcOuttimeTrigger() {
        return actcOuttimeTrigger;
    }

    public void setActcOuttimeTrigger(String actcOuttimeTrigger) {
        this.actcOuttimeTrigger = actcOuttimeTrigger;
    }

    public String getActcOuttimeTemplate() {
        return actcOuttimeTemplate;
    }

    public void setActcOuttimeTemplate(String actcOuttimeTemplate) {
        this.actcOuttimeTemplate = actcOuttimeTemplate;
    }

    public String getActcDescription() {
        return actcDescription;
    }

    public void setActcDescription(String actcDescription) {
        this.actcDescription = actcDescription;
    }

    public String getActcDefTitle() {
        return actcDefTitle;
    }

    public void setActcDefTitle(String actcDefTitle) {
        this.actcDefTitle = actcDefTitle;
    }

    public String getActcDefSubjectMessage() {
        return actcDefSubjectMessage;
    }

    public void setActcDefSubjectMessage(String actcDefSubjectMessage) {
        this.actcDefSubjectMessage = actcDefSubjectMessage;
    }

    public String getActcDefMessage() {
        return actcDefMessage;
    }

    public void setActcDefMessage(String actcDefMessage) {
        this.actcDefMessage = actcDefMessage;
    }

    public String getActcDefDescription() {
        return actcDefDescription;
    }

    public void setActcDefDescription(String actcDefDescription) {
        this.actcDefDescription = actcDefDescription;
    }

    public String getActcAlert() {
        return actcAlert;
    }

    public void setActcAlert(String actcAlert) {
        this.actcAlert = actcAlert;
    }

    public String getActcPriorityVariable() {
        return actcPriorityVariable;
    }

    public void setActcPriorityVariable(String actcPriorityVariable) {
        this.actcPriorityVariable = actcPriorityVariable;
    }

    public String getActcCanCancel() {
        return actcCanCancel;
    }

    public void setActcCanCancel(String actcCanCancel) {
        this.actcCanCancel = actcCanCancel;
    }

    public String getActcCanPause() {
        return actcCanPause;
    }

    public void setActcCanPause(String actcCanPause) {
        this.actcCanPause = actcCanPause;
    }

    public String getActcCanSkip() {
        return actcCanSkip;
    }

    public void setActcCanSkip(String actcCanSkip) {
        this.actcCanSkip = actcCanSkip;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getActcOuttimeTriggerTitle() {
        return actcOuttimeTriggerTitle;
    }

    public void setActcOuttimeTriggerTitle(String actcOuttimeTriggerTitle) {
        this.actcOuttimeTriggerTitle = actcOuttimeTriggerTitle;
    }

    public String getActcCanChooseUser() {
        return actcCanChooseUser;
    }

    public void setActcCanChooseUser(String actcCanChooseUser) {
        this.actcCanChooseUser = actcCanChooseUser;
    }

    public String getActcCanTransfer() {
        return actcCanTransfer;
    }

    public void setActcCanTransfer(String actcCanTransfer) {
        this.actcCanTransfer = actcCanTransfer;
    }

    public String getActcResponsibility() {
        return actcResponsibility;
    }

    public void setActcResponsibility(String actcResponsibility) {
        this.actcResponsibility = actcResponsibility;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    public String getHandleUserView() {
        return handleUserView;
    }

    public void setHandleUserView(String handleUserView) {
        this.handleUserView = handleUserView;
    }

    public String getHandleRole() {
        return handleRole;
    }

    public void setHandleRole(String handleRole) {
        this.handleRole = handleRole;
    }

    public String getHandleRoleView() {
        return handleRoleView;
    }

    public void setHandleRoleView(String handleRoleView) {
        this.handleRoleView = handleRoleView;
    }

    public String getHandleTeam() {
        return handleTeam;
    }

    public void setHandleTeam(String handleTeam) {
        this.handleTeam = handleTeam;
    }

    public String getHandleTeamView() {
        return handleTeamView;
    }

    public void setHandleTeamView(String handleTeamView) {
        this.handleTeamView = handleTeamView;
    }

    public String getHandleField() {
        return handleField;
    }

    public void setHandleField(String handleField) {
        this.handleField = handleField;
    }

    public String getOuttimeUser() {
        return outtimeUser;
    }

    public void setOuttimeUser(String outtimeUser) {
        this.outtimeUser = outtimeUser;
    }

    public String getOuttimeUserView() {
        return outtimeUserView;
    }

    public void setOuttimeUserView(String outtimeUserView) {
        this.outtimeUserView = outtimeUserView;
    }

    public String getOuttimeRole() {
        return outtimeRole;
    }

    public void setOuttimeRole(String outtimeRole) {
        this.outtimeRole = outtimeRole;
    }

    public String getOuttimeRoleView() {
        return outtimeRoleView;
    }

    public void setOuttimeRoleView(String outtimeRoleView) {
        this.outtimeRoleView = outtimeRoleView;
    }

    public String getOuttimeTeam() {
        return outtimeTeam;
    }

    public void setOuttimeTeam(String outtimeTeam) {
        this.outtimeTeam = outtimeTeam;
    }

    public String getOuttimeTeamView() {
        return outtimeTeamView;
    }

    public void setOuttimeTeamView(String outtimeTeamView) {
        this.outtimeTeamView = outtimeTeamView;
    }

    public String getRejectActivities() {
        return rejectActivities;
    }

    public void setRejectActivities(String rejectActivities) {
        this.rejectActivities = rejectActivities;
    }

    public String getRejectActivitiesView() {
        return rejectActivitiesView;
    }

    public void setRejectActivitiesView(String rejectActivitiesView) {
        this.rejectActivitiesView = rejectActivitiesView;
    }

    public String getActcChooseableHandlerType() {
        return actcChooseableHandlerType;
    }


    public void setActcChooseableHandlerType(String actcChooseableHandlerType) {
        this.actcChooseableHandlerType = actcChooseableHandlerType;
    }


    public String getChooseableHandleUser() {
        return chooseableHandleUser;
    }


    public void setChooseableHandleUser(String chooseableHandleUser) {
        this.chooseableHandleUser = chooseableHandleUser;
    }


    public String getChooseableHandleUserView() {
        return chooseableHandleUserView;
    }


    public void setChooseableHandleUserView(String chooseableHandleUserView) {
        this.chooseableHandleUserView = chooseableHandleUserView;
    }


    public String getChooseableHandleRole() {
        return chooseableHandleRole;
    }


    public void setChooseableHandleRole(String chooseableHandleRole) {
        this.chooseableHandleRole = chooseableHandleRole;
    }


    public String getChooseableHandleRoleView() {
        return chooseableHandleRoleView;
    }


    public void setChooseableHandleRoleView(String chooseableHandleRoleView) {
        this.chooseableHandleRoleView = chooseableHandleRoleView;
    }


    public String getChooseableHandleTeam() {
        return chooseableHandleTeam;
    }


    public void setChooseableHandleTeam(String chooseableHandleTeam) {
        this.chooseableHandleTeam = chooseableHandleTeam;
    }


    public String getChooseableHandleTeamView() {
        return chooseableHandleTeamView;
    }


    public void setChooseableHandleTeamView(String chooseableHandleTeamView) {
        this.chooseableHandleTeamView = chooseableHandleTeamView;
    }


    public String getChooseableHandleField() {
        return chooseableHandleField;
    }


    public void setChooseableHandleField(String chooseableHandleField) {
        this.chooseableHandleField = chooseableHandleField;
    }


	public String getChooseableHandleTriggerTitle() {
		return chooseableHandleTriggerTitle;
	}


	public void setChooseableHandleTriggerTitle(String chooseableHandleTriggerTitle) {
		this.chooseableHandleTriggerTitle = chooseableHandleTriggerTitle;
	}


	public String getChooseableHandleTrigger() {
		return chooseableHandleTrigger;
	}


	public void setChooseableHandleTrigger(String chooseableHandleTrigger) {
		this.chooseableHandleTrigger = chooseableHandleTrigger;
	}


	public String getActcMessageNotifyTemplate() {
		return actcMessageNotifyTemplate;
	}


	public void setActcMessageNotifyTemplate(String actcMessageNotifyTemplate) {
		this.actcMessageNotifyTemplate = actcMessageNotifyTemplate;
	}
	

	public String getActcOuttimeNotifyType() {
		return actcOuttimeNotifyType;
	}

	public void setActcOuttimeNotifyType(String actcOuttimeNotifyType) {
		this.actcOuttimeNotifyType = actcOuttimeNotifyType;
	}

    public String getActcIsSystemTask() {
        return actcIsSystemTask;
    }

    public void setActcIsSystemTask(String actcIsSystemTask) {
        this.actcIsSystemTask = actcIsSystemTask;
    }


    public String getActcDelayType() {
        return actcDelayType;
    }

    public void setActcDelayType(String actcDelayType) {
        this.actcDelayType = actcDelayType;
    }

    public Integer getActcDelayTime() {
        return actcDelayTime;
    }

    public void setActcDelayTime(Integer actcDelayTime) {
        this.actcDelayTime = actcDelayTime;
    }



    public String getActcDelayField() {
        return actcDelayField;
    }

    public void setActcDelayField(String actcDelayField) {
        this.actcDelayField = actcDelayField;
    }

    public String getActcDelayTimeunit() {
        return actcDelayTimeunit;
    }

    public void setActcDelayTimeunit(String actcDelayTimeunit) {
        this.actcDelayTimeunit = actcDelayTimeunit;
    }

    public String getActcMailNotifyTemplateView() {
		return this.actcMailNotifyTemplateView;
	}

	public void setActcMailNotifyTemplateView(String actcMailNotifyTemplateView) {
		this.actcMailNotifyTemplateView = actcMailNotifyTemplateView;
	}

	public String getActcOuttimeTemplateView() {
		return actcOuttimeTemplateView;
	}

	public void setActcOuttimeTemplateView(String actcOuttimeTemplateView) {
		this.actcOuttimeTemplateView = actcOuttimeTemplateView;
	}
	
	


	
}