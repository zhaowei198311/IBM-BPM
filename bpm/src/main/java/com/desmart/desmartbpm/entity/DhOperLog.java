package com.desmart.desmartbpm.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志实体类
 * @author zbw
 */
public class DhOperLog implements Serializable{

	private static final long serialVersionUID = 970629292344295182L;
	
	//日志类别
	public static final String systemLog = "systemLog";
	public static final String errorLog = "errorLog";
	
	//id主键
	private String id;
	
	//员工工号
	private String userId;
	
	//员工姓名
	private String userName;
	
	//登陆ip
	private String host;
	
	//日志级别        系统/异常
	private String logType;
	
	//请求参数
	private String requestParam;
	
	//输出信息
	private String responseParam;
	
	//方法描述信息
	private String methodDescription;
	
	//请求路径
	private String path;
	
	//请求时间
	private Date createTime;
	
	//附加备注
	private String attach;
	
	//非表中字段
	private String startTime;
	private String endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getResponseParam() {
		return responseParam;
	}

	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public String getMethodDescription() {
		return methodDescription;
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreatTime() {
		return createTime;
	}

	public void setCreatTime(Date creatTime) {
		this.createTime = creatTime;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "DhOperLog [id=" + id + ", userId=" + userId + ", userName=" + userName + ", host=" + host + ", logType="
				+ logType + ", requestParam=" + requestParam + ", responseParam=" + responseParam
				+ ", methodDescription=" + methodDescription + ", path=" + path + ", createTime=" + createTime
				+ ", attach=" + attach + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	

}
