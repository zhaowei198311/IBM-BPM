package com.desmart.desmartbpm.entity;

/**
 * 
* <p>Title: DhInterface</p>  
* <p>Description: 接口管理实体类</p>  
* @author zhaowei 
* @date 2018年4月12日
 */
public class DhInterface {

	private int interfaceId;
	
	private String interfaceName;
	
	private String interfaceDescribe;
	
	private String interfaceType;
	
	private String interfaceCreateDate;
	
	private String interfaceCreateUser;

	/**
	 * @return the interfaceId
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the interfaceDescribe
	 */
	public String getInterfaceDescribe() {
		return interfaceDescribe;
	}

	/**
	 * @param interfaceDescribe the interfaceDescribe to set
	 */
	public void setInterfaceDescribe(String interfaceDescribe) {
		this.interfaceDescribe = interfaceDescribe;
	}

	/**
	 * @return the interfaceType
	 */
	public String getInterfaceType() {
		return interfaceType;
	}

	/**
	 * @param interfaceType the interfaceType to set
	 */
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	/**
	 * @return the interfaceCreateDate
	 */
	public String getInterfaceCreateDate() {
		return interfaceCreateDate;
	}

	/**
	 * @param interfaceCreateDate the interfaceCreateDate to set
	 */
	public void setInterfaceCreateDate(String interfaceCreateDate) {
		this.interfaceCreateDate = interfaceCreateDate;
	}

	/**
	 * @return the interfaceCreateUser
	 */
	public String getInterfaceCreateUser() {
		return interfaceCreateUser;
	}

	/**
	 * @param interfaceCreateUser the interfaceCreateUser to set
	 */
	public void setInterfaceCreateUser(String interfaceCreateUser) {
		this.interfaceCreateUser = interfaceCreateUser;
	}
	
	public DhInterface() {
		
	}

	/**
	 * @param interfaceId
	 * @param interfaceName
	 * @param interfaceDescribe
	 * @param interfaceType
	 * @param interfaceCreateDate
	 * @param interfaceCreateUser
	 */
	public DhInterface(int interfaceId, String interfaceName, String interfaceDescribe, String interfaceType,
			String interfaceCreateDate, String interfaceCreateUser) {
		super();
		this.interfaceId = interfaceId;
		this.interfaceName = interfaceName;
		this.interfaceDescribe = interfaceDescribe;
		this.interfaceType = interfaceType;
		this.interfaceCreateDate = interfaceCreateDate;
		this.interfaceCreateUser = interfaceCreateUser;
	}
}
