package com.desmart.desmartbpm.entity;

import java.util.Date;

public class DhProcessRetrieve {
	public static final String SOURCE_BY_DICTIONARIES = "数据字典拉取";
	public static final String SOURCE_BY_HAND = "手动填写";
	private String retrieveUid;
	private String proAppId;
	private String proUid;
	private String fieldLabel;
	private String fieldName;
	private String elementType;
	private String isScope;
	private String dataSource;
	private Date createTime;
	private String createUserUid;
	private String dataSet;
	
	/**不在表内**/
	private String userName;
	
	public String getRetrieveUid() {
		return retrieveUid;
	}
	public void setRetrieveUid(String retrieveUid) {
		this.retrieveUid = retrieveUid;
	}
	public String getProAppId() {
		return proAppId;
	}
	public void setProAppId(String proAppId) {
		this.proAppId = proAppId;
	}
	public String getProUid() {
		return proUid;
	}
	public void setProUid(String proUid) {
		this.proUid = proUid;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getIsScope() {
		return isScope;
	}
	public void setIsScope(String isScope) {
		this.isScope = isScope;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserUid() {
		return createUserUid;
	}
	public void setCreateUserUid(String createUserUid) {
		this.createUserUid = createUserUid;
	}
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "DhProcessRetrieve [retrieveUid=" + retrieveUid + ", proAppId=" + proAppId + ", proUid=" + proUid
				+ ", fieldLabel=" + fieldLabel + ", fieldName=" + fieldName + ", elementType=" + elementType
				+ ", isScope=" + isScope + ", dataSource=" + dataSource + ", createTime=" + createTime
				+ ", createUserUid=" + createUserUid + ", dataSet=" + dataSet + ", userName=" + userName + "]";
	}
	
}
