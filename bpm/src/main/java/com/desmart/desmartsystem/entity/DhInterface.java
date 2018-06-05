package com.desmart.desmartsystem.entity;

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
	
	private String updateDate;//修改时间
	
	private String createDate; // 创建时间

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
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
			String intCallMethod, String intLoginUser, String intLoginPwd, String intStatus, String updateDate,
			String createDate) {
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

}
