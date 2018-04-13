package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 流程定义
 * @author yaoyunqing
 *
 */
public class DhProcessDefinition {
    private String proUid;// 流程id(引擎中流程图id)
    
    private String proAppId; // 流程应用库id

    private String proVerUid;// 流程版本id（引擎中流程快照id）
    
    private String proParent;// 父流程id

    private Double proTime;// 流程所需时间（数量）

    private String proTimeUnit;// 流程所需时间（单位）时，日，月，年

    private String proStatus;// 流程定义状态，草稿、发布

    private String proTypeDay;

    private String proType;

    private String proAssignment;// 流程分配(略过)

    private String proShowMap;// 是否可以查看流程图 true/false

    private String proShowMessage;// 是否显示消息

    private String proSubprocess;// 是否子流程

    private String proTriStart;// 发起流程触发器

    private String proTriDeleted;// 删除流程触发器

    private String proTriCanceled;// 取消触发器

    private String proTriPaused;// 暂停触发器

    private String proTriUnpaused;// 暂停取消触发器

    private String proTriReassigned;// 重新分配触发器

    private String proTypeProcess;// 流程类型（全填public）

    private String proShowDelegate;// 是否可以授权 true/false

    private String proShowDynaform;// 是否有表单

    private String proCategory;// 流程分类id

    private Date lastModifiedDate;// 最后更新时间

    private String lastModifiedUser;// 最后更新人

    private String createDate;// 创建时间

    private String createUser;// 创建人

    private Integer proHeight;// 流程图高度

    private Integer proWidth;// 流程图宽度

    private Integer proTitleX;// 流程标题x坐标

    private Integer proTitleY;// 流程标题y坐标

    private String proDebug;// 是否可以DEBUG

    private String proDynaforms;// 最终信息汇总表单名

    private String proDerivationScreenTpl;// 发送邮件通知的模板

    // 以下不属于表字段
    private String creatorFullName;

    private String updatorFullName;


    public String getProUid() {
        return proUid;
    }

    public void setProUid(String proUid) {
        this.proUid = proUid;
    }

    public String getProVerUid() {
        return proVerUid;
    }

    public void setProVerUid(String proVerUid) {
        this.proVerUid = proVerUid;
    }

    public String getProParent() {
        return proParent;
    }

    public void setProParent(String proParent) {
        this.proParent = proParent;
    }

    public Double getProTime() {
        return proTime;
    }

    public void setProTime(Double proTime) {
        this.proTime = proTime;
    }

    public String getProTimeUnit() {
        return proTimeUnit;
    }

    public void setProTimeUnit(String proTimeUnit) {
        this.proTimeUnit = proTimeUnit;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public String getProTypeDay() {
        return proTypeDay;
    }

    public void setProTypeDay(String proTypeDay) {
        this.proTypeDay = proTypeDay;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProAssignment() {
        return proAssignment;
    }

    public void setProAssignment(String proAssignment) {
        this.proAssignment = proAssignment;
    }

    public String getProShowMap() {
        return proShowMap;
    }

    public void setProShowMap(String proShowMap) {
        this.proShowMap = proShowMap;
    }

    public String getProShowMessage() {
        return proShowMessage;
    }

    public void setProShowMessage(String proShowMessage) {
        this.proShowMessage = proShowMessage;
    }

    public String getProSubprocess() {
        return proSubprocess;
    }

    public void setProSubprocess(String proSubprocess) {
        this.proSubprocess = proSubprocess;
    }

    public String getProTriStart() {
        return proTriStart;
    }

    public void setProTriStart(String proTriStart) {
        this.proTriStart = proTriStart;
    }

    public String getProTriDeleted() {
        return proTriDeleted;
    }

    public void setProTriDeleted(String proTriDeleted) {
        this.proTriDeleted = proTriDeleted;
    }

    public String getProTriCanceled() {
        return proTriCanceled;
    }

    public void setProTriCanceled(String proTriCanceled) {
        this.proTriCanceled = proTriCanceled;
    }

    public String getProTriPaused() {
        return proTriPaused;
    }

    public void setProTriPaused(String proTriPaused) {
        this.proTriPaused = proTriPaused;
    }

    public String getProTriUnpaused() {
        return proTriUnpaused;
    }

    public void setProTriUnpaused(String proTriUnpaused) {
        this.proTriUnpaused = proTriUnpaused;
    }

    public String getProTriReassigned() {
        return proTriReassigned;
    }

    public void setProTriReassigned(String proTriReassigned) {
        this.proTriReassigned = proTriReassigned;
    }

    public String getProTypeProcess() {
        return proTypeProcess;
    }

    public void setProTypeProcess(String proTypeProcess) {
        this.proTypeProcess = proTypeProcess;
    }

    public String getProShowDelegate() {
        return proShowDelegate;
    }

    public void setProShowDelegate(String proShowDelegate) {
        this.proShowDelegate = proShowDelegate;
    }

    public String getProShowDynaform() {
        return proShowDynaform;
    }

    public void setProShowDynaform(String proShowDynaform) {
        this.proShowDynaform = proShowDynaform;
    }

    public String getProCategory() {
        return proCategory;
    }

    public void setProCategory(String proCategory) {
        this.proCategory = proCategory;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getProHeight() {
        return proHeight;
    }

    public void setProHeight(Integer proHeight) {
        this.proHeight = proHeight;
    }

    public Integer getProWidth() {
        return proWidth;
    }

    public void setProWidth(Integer proWidth) {
        this.proWidth = proWidth;
    }

    public Integer getProTitleX() {
        return proTitleX;
    }

    public void setProTitleX(Integer proTitleX) {
        this.proTitleX = proTitleX;
    }

    public Integer getProTitleY() {
        return proTitleY;
    }

    public void setProTitleY(Integer proTitleY) {
        this.proTitleY = proTitleY;
    }

    public String getProDebug() {
        return proDebug;
    }

    public void setProDebug(String proDebug) {
        this.proDebug = proDebug;
    }

    public String getProDynaforms() {
        return proDynaforms;
    }

    public void setProDynaforms(String proDynaforms) {
        this.proDynaforms = proDynaforms;
    }

    public String getProDerivationScreenTpl() {
        return proDerivationScreenTpl;
    }

    public void setProDerivationScreenTpl(String proDerivationScreenTpl) {
        this.proDerivationScreenTpl = proDerivationScreenTpl;
    }

    public String getProAppId() {
        return proAppId;
    }

    public void setProAppId(String proAppId) {
        this.proAppId = proAppId;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getUpdatorFullName() {
        return updatorFullName;
    }

    public void setUpdatorFullName(String updatorFullName) {
        this.updatorFullName = updatorFullName;
    }

    @Override
    public String toString() {
        return "DhProcessDefinition{" +
                "proUid='" + proUid + '\'' +
                ", proAppId='" + proAppId + '\'' +
                ", proVerUid='" + proVerUid + '\'' +
                ", proParent='" + proParent + '\'' +
                ", proTime=" + proTime +
                ", proTimeUnit='" + proTimeUnit + '\'' +
                ", proStatus='" + proStatus + '\'' +
                ", proTypeDay='" + proTypeDay + '\'' +
                ", proType='" + proType + '\'' +
                ", proAssignment='" + proAssignment + '\'' +
                ", proShowMap='" + proShowMap + '\'' +
                ", proShowMessage='" + proShowMessage + '\'' +
                ", proSubprocess='" + proSubprocess + '\'' +
                ", proTriStart='" + proTriStart + '\'' +
                ", proTriDeleted='" + proTriDeleted + '\'' +
                ", proTriCanceled='" + proTriCanceled + '\'' +
                ", proTriPaused='" + proTriPaused + '\'' +
                ", proTriUnpaused='" + proTriUnpaused + '\'' +
                ", proTriReassigned='" + proTriReassigned + '\'' +
                ", proTypeProcess='" + proTypeProcess + '\'' +
                ", proShowDelegate='" + proShowDelegate + '\'' +
                ", proShowDynaform='" + proShowDynaform + '\'' +
                ", proCategory='" + proCategory + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", lastModifiedUser='" + lastModifiedUser + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", proHeight=" + proHeight +
                ", proWidth=" + proWidth +
                ", proTitleX=" + proTitleX +
                ", proTitleY=" + proTitleY +
                ", proDebug='" + proDebug + '\'' +
                ", proDynaforms='" + proDynaforms + '\'' +
                ", proDerivationScreenTpl='" + proDerivationScreenTpl + '\'' +
                ", creatorFullName='" + creatorFullName + '\'' +
                ", updatorFullName='" + updatorFullName + '\'' +
                '}';
    }
}
