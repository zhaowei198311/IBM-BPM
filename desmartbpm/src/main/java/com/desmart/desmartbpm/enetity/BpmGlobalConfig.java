package com.desmart.desmartbpm.enetity;

import java.sql.Timestamp;

/**
 * 流程平台全局配置
 */
public class BpmGlobalConfig {

    private String configId;  // 主键
    private String configName;  // 配置名
    private String bpmServerHost;  // bpm引擎host
    private String bpmAdminName;   // 管理员账号
    private String bpmAdminPsw;    // 管理员密码
    private String configStatus;   // 配置状态 on 启用  off 停用
    private Timestamp createTime;
    private Timestamp updateTime;
    private String gmtTimeZone;   // 时区
    private String bpmformsHost;  // 平台host
    private Integer bpmClientTimeout;  // 超时时间
    private String bpmformsWebContext; // 平台应用上下文
    private Integer httpMaxConnection; // 最大http连接
    private Integer preRouteMaxConnection;
    private String schedulerHost;  // 定时任务管理host
    private String bpmAdminEmail;  // 管理员邮件地址
    private String bpmApiHost;   // 平台api地址

    public BpmGlobalConfig() {

    }


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getBpmServerHost() {
        return bpmServerHost;
    }

    public void setBpmServerHost(String bpmServerHost) {
        this.bpmServerHost = bpmServerHost;
    }

    public String getBpmAdminName() {
        return bpmAdminName;
    }

    public void setBpmAdminName(String bpmAdminName) {
        this.bpmAdminName = bpmAdminName;
    }

    public String getBpmAdminPsw() {
        return bpmAdminPsw;
    }

    public void setBpmAdminPsw(String bpmAdminPsw) {
        this.bpmAdminPsw = bpmAdminPsw;
    }

    public String getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(String configStatus) {
        this.configStatus = configStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getGmtTimeZone() {
        return gmtTimeZone;
    }

    public void setGmtTimeZone(String gmtTimeZone) {
        this.gmtTimeZone = gmtTimeZone;
    }

    public String getBpmformsHost() {
        return bpmformsHost;
    }

    public void setBpmformsHost(String bpmformsHost) {
        this.bpmformsHost = bpmformsHost;
    }

    public Integer getBpmClientTimeout() {
        return bpmClientTimeout;
    }

    public void setBpmClientTimeout(Integer bpmClientTimeout) {
        this.bpmClientTimeout = bpmClientTimeout;
    }

    public String getBpmformsWebContext() {
        return bpmformsWebContext;
    }

    public void setBpmformsWebContext(String bpmformsWebContext) {
        this.bpmformsWebContext = bpmformsWebContext;
    }

    public Integer getHttpMaxConnection() {
        return httpMaxConnection;
    }

    public void setHttpMaxConnection(Integer httpMaxConnection) {
        this.httpMaxConnection = httpMaxConnection;
    }

    public Integer getPreRouteMaxConnection() {
        return preRouteMaxConnection;
    }

    public void setPreRouteMaxConnection(Integer preRouteMaxConnection) {
        this.preRouteMaxConnection = preRouteMaxConnection;
    }

    public String getSchedulerHost() {
        return schedulerHost;
    }

    public void setSchedulerHost(String schedulerHost) {
        this.schedulerHost = schedulerHost;
    }

    public String getBpmAdminEmail() {
        return bpmAdminEmail;
    }

    public void setBpmAdminEmail(String bpmAdminEmail) {
        this.bpmAdminEmail = bpmAdminEmail;
    }

    public String getBpmApiHost() {
        return bpmApiHost;
    }

    public void setBpmApiHost(String bpmApiHost) {
        this.bpmApiHost = bpmApiHost;
    }

    @Override
    public String toString() {
        return "BpmGlobalConfig{" +
                "configId='" + configId + '\'' +
                ", configName='" + configName + '\'' +
                ", bpmServerHost='" + bpmServerHost + '\'' +
                ", bpmAdminName='" + bpmAdminName + '\'' +
                ", bpmAdminPsw='" + bpmAdminPsw + '\'' +
                ", configStatus='" + configStatus + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", gmtTimeZone='" + gmtTimeZone + '\'' +
                ", bpmformsHost='" + bpmformsHost + '\'' +
                ", bpmClientTimeout=" + bpmClientTimeout +
                ", bpmformsWebContext='" + bpmformsWebContext + '\'' +
                ", httpMaxConnection=" + httpMaxConnection +
                ", preRouteMaxConnection=" + preRouteMaxConnection +
                ", schedulerHost='" + schedulerHost + '\'' +
                ", bpmAdminEmail='" + bpmAdminEmail + '\'' +
                ", bpmApiHost='" + bpmApiHost + '\'' +
                '}';
    }
}