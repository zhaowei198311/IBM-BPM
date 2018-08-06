package com.desmart.desmartsystem.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 流程平台全局配置
 */
public class BpmGlobalConfig implements Serializable {

    private String configId;  // 主键
    private String configName;  // 配置名
    private String bpmServerHost;  // 流程引擎ip端口
    private String bpmAdminName;   // 管理员用户名
    private String bpmAdminPsw;    // 管理员密码
    private String configStatus;   // 配置状态  on 启用  off 停用
    private Date createTime;     // 创建时间
    private Date updateTime;     // 更新时间
    private String gmtTimeZone;   // 时区
    private String bpmformsHost;  // 平台host
    private Integer bpmClientTimeout;  // 超时时间
    private String bpmformsWebContext; // 平台上下文
    private Integer httpMaxConnection; // 最大http连接
    private Integer preRouteMaxConnection;
    private String schedulerHost;  // 定时任务管理host
    private String bpmAdminEmail;  // 管理员邮件地址
    private String bpmApiHost;   // 平台api地址
    private String sftpPath;     //附件上传根目录
    private String sftpUserName;  //附件上传用户名
    private String sftpPassword;  //附件上传根密码
    private String sftpIp;       //附件上传IP地址ַ
    private Integer sftpPort;	//附件上传端口号
    private String fileFormat; //附件上传文件后缀
    private Integer maxFileSize;//附件上次文件最大大小
    private Integer maxFileCount;//附件上次文件最大数量
    private String incrementIdBydate;//表单或者接口中自增长id
	public String getIncrementIdBydate() {
		return incrementIdBydate;
	}

	public void setIncrementIdBydate(String incrementIdBydate) {
		this.incrementIdBydate = incrementIdBydate;
	}

	public BpmGlobalConfig(String configId, String configName, String bpmServerHost, String bpmAdminName,
			String bpmAdminPsw, String configStatus, Date createTime, Date updateTime, String gmtTimeZone,
			String bpmformsHost, Integer bpmClientTimeout, String bpmformsWebContext, Integer httpMaxConnection,
			Integer preRouteMaxConnection, String schedulerHost, String bpmAdminEmail, String bpmApiHost,
			String sftpPath, String sftpUserName, String sftpPassword, String sftpIp, Integer sftpPort,
			String fileFormat, Integer maxFileSize, Integer maxFileCount) {
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
		this.sftpPort = sftpPort;
		this.fileFormat = fileFormat;
		this.maxFileSize = maxFileSize;
		this.maxFileCount = maxFileCount;
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

    public Integer getSftpPort() {
		return sftpPort;
	}

	public void setSftpPort(Integer sftpPort) {
		this.sftpPort = sftpPort;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public Integer getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Integer maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public Integer getMaxFileCount() {
		return maxFileCount;
	}

	public void setMaxFileCount(Integer maxFileCount) {
		this.maxFileCount = maxFileCount;
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
                ", sftpPort='" + sftpPort + '\'' +
                ", maxFileSize='" + maxFileSize + '\'' +
                ", maxFileCount='" + maxFileCount + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                '}';
    }
}