package com.desmart.desmartsystem.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 流程平台全局配置
 */
public class BpmGlobalConfig implements Serializable {

    private String configId;  // 主键
    private String configName;  // 配置�?
    private String bpmServerHost;  // bpm引擎host
    private String bpmAdminName;   // 管理员账�?
    private String bpmAdminPsw;    // 管理员密�?
    private String configStatus;   // 配置状�?? on 启用  off 停用
    private Date createTime;     // 创建时间
    private Date updateTime;     // 更新时间
    private String gmtTimeZone;   // 时区
    private String bpmformsHost;  // 平台host
    private Integer bpmClientTimeout;  // 超时时间
    private String bpmformsWebContext; // 平台应用上下�?
    private Integer httpMaxConnection; // �?大http连接
    private Integer preRouteMaxConnection;
    private String schedulerHost;  // 定时任务管理host
    private String bpmAdminEmail;  // 管理员邮件地�?
    private String bpmApiHost;   // 平台api地址
    private String sftpPath;     //�����ϴ���Ŀ¼
    private String sftpUserName;  //�����ϴ��û���
    private String sftpPassword;  //�����ϴ�������
    private String sftpIp;       //�����ϴ�ip��ַ
    public BpmGlobalConfig(String configId, String configName, String bpmServerHost, String bpmAdminName,
			String bpmAdminPsw, String configStatus, Date createTime, Date updateTime, String gmtTimeZone,
			String bpmformsHost, Integer bpmClientTimeout, String bpmformsWebContext, Integer httpMaxConnection,
			Integer preRouteMaxConnection, String schedulerHost, String bpmAdminEmail, String bpmApiHost,
			String sftpPath, String sftpUserName, String sftpPassword, String sftpIp) {
		super();
		this.configId = configId;
		this.configName = configName;
		this.bpmServerHost = bpmServerHost;
		this.bpmAdminName = bpmAdminName;
		this.bpmAdminPsw = bpmAdminPsw;
		this.configStatus = configStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.gmtTimeZone = gmtTimeZone;
		this.bpmformsHost = bpmformsHost;
		this.bpmClientTimeout = bpmClientTimeout;
		this.bpmformsWebContext = bpmformsWebContext;
		this.httpMaxConnection = httpMaxConnection;
		this.preRouteMaxConnection = preRouteMaxConnection;
		this.schedulerHost = schedulerHost;
		this.bpmAdminEmail = bpmAdminEmail;
		this.bpmApiHost = bpmApiHost;
		this.sftpPath = sftpPath;
		this.sftpUserName = sftpUserName;
		this.sftpPassword = sftpPassword;
		this.sftpIp = sftpIp;
	}

	public String getSftpPath() {
		return sftpPath;
	}

	public void setSftpPath(String sftpPath) {
		this.sftpPath = sftpPath;
	}

	public String getSftpUserName() {
		return sftpUserName;
	}

	public void setSftpUserName(String sftpUserName) {
		this.sftpUserName = sftpUserName;
	}

	public String getSftpPassword() {
		return sftpPassword;
	}

	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}

	public String getSftpIp() {
		return sftpIp;
	}

	public void setSftpIp(String sftpIp) {
		this.sftpIp = sftpIp;
	}

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
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