package com.desmart.desmartsystem.entity;

import java.util.Date;

/**
 * 
* <p>Title: DhInterface</p>  
* <p>Description: 接口管理实体类</p>  
* @author zhaowei 
* @date 2018年4月12日
 */
public class DhInterface {

	private String intUid; // 接口id
	
	private String intTitle; // 接口名
	
	private String intDescription; // 接口描述 
	
	private String intType; // 接口类型
	
	private String intUrl; // 接口访问地址
	
	private String intCallMethod; // 接口访问方法名
	
	private String intLoginUser; // 登陆用户操作
	
	private String intLoginPwd; // 登陆用户密码
	
	private String intStatus; // 接口状态
	
	private Date updateDate;//修改时间
	
	private Date createDate; // 创建时间
	
	
	private String intXml;//webservice参数
	private String intRequestXml;
	private String intResponseXml;
	private String intLabel;
	
	/**
	 * @return the intUid
	 */
	public String getIntUid() {
		return intUid;
	}

	/**
	 * @param intUid the intUid to set
	 */
	public void setIntUid(String intUid) {
		this.intUid = intUid;
	}

	/**
	 * @return the intTitle
	 */
	public String getIntTitle() {
		return intTitle;
	}

	/**
	 * @param intTitle the intTitle to set
	 */
	public void setIntTitle(String intTitle) {
		this.intTitle = intTitle;
	}

	/**
	 * @return the intDescription
	 */
	public String getIntDescription() {
		return intDescription;
	}

	/**
	 * @param intDescription the intDescription to set
	 */
	public void setIntDescription(String intDescription) {
		this.intDescription = intDescription;
	}

	/**
	 * @return the intType
	 */
	public String getIntType() {
		return intType;
	}

	/**
	 * @param intType the intType to set
	 */
	public void setIntType(String intType) {
		this.intType = intType;
	}

	/**
	 * @return the intUrl
	 */
	public String getIntUrl() {
		return intUrl;
	}

	/**
	 * @param intUrl the intUrl to set
	 */
	public void setIntUrl(String intUrl) {
		this.intUrl = intUrl;
	}

	/**
	 * @return the intCallMethod
	 */
	public String getIntCallMethod() {
		return intCallMethod;
	}

	/**
	 * @param intCallMethod the intCallMethod to set
	 */
	public void setIntCallMethod(String intCallMethod) {
		this.intCallMethod = intCallMethod;
	}

	/**
	 * @return the intLoginUser
	 */
	public String getIntLoginUser() {
		return intLoginUser;
	}

	/**
	 * @param intLoginUser the intLoginUser to set
	 */
	public void setIntLoginUser(String intLoginUser) {
		this.intLoginUser = intLoginUser;
	}

	/**
	 * @return the intLoginPwd
	 */
	public String getIntLoginPwd() {
		return intLoginPwd;
	}

	/**
	 * @param intLoginPwd the intLoginPwd to set
	 */
	public void setIntLoginPwd(String intLoginPwd) {
		this.intLoginPwd = intLoginPwd;
	}

	/**
	 * @return the intStatus
	 */
	public String getIntStatus() {
		return intStatus;
	}

	/**
	 * @param intStatus the intStatus to set
	 */
	public void setIntStatus(String intStatus) {
		this.intStatus = intStatus;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public DhInterface() {
		
	}
	
	
	/**
	 * @param intUid
	 * @param intTitle
	 * @param intDescription
	 * @param intType
	 * @param intUrl
	 * @param intCallMethod
	 * @param intLoginUser
	 * @param intLoginPwd
	 * @param intStatus
	 * @param updateDate
	 * @param createDate
	 */
	public DhInterface(String intUid, String intTitle, String intDescription, String intType, String intUrl,
			String intCallMethod, String intLoginUser, String intLoginPwd, String intStatus, Date updateDate,
			Date createDate) {
		super();
		this.intUid = intUid;
		this.intTitle = intTitle;
		this.intDescription = intDescription;
		this.intType = intType;
		this.intUrl = intUrl;
		this.intCallMethod = intCallMethod;
		this.intLoginUser = intLoginUser;
		this.intLoginPwd = intLoginPwd;
		this.intStatus = intStatus;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	public String getIntXml() {
		return intXml;
	}

	public void setIntXml(String intXml) {
		this.intXml = intXml;
	}

	public String getIntRequestXml() {
		return intRequestXml;
	}

	public void setIntRequestXml(String intRequestXml) {
		this.intRequestXml = intRequestXml;
	}

	public String getIntResponseXml() {
		return intResponseXml;
	}

	public void setIntResponseXml(String intResponseXml) {
		this.intResponseXml = intResponseXml;
	}

	public String getIntLabel() {
		return intLabel;
	}

	public void setIntLabel(String intLabel) {
		this.intLabel = intLabel;
	}

}
