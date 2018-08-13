package com.desmart.desmartbpm.entity;

import java.util.Date;

/**
 * 完成新开店流程后需要记录的数据
 * @author zbw
 *
 */
public class DhNewStore {
	
	private String id;//id
	
	private String createUserName;//发布人
	
	private Date publishDate; //发布时间
	
	private String storeCode;//门店编码
	
	private String sapCode;//sap编码
	
	private String storeAddress;//门店地址
	
	private Date deliverTime;//交付时间
	
	private Date createStoreDate;//开店时间
	
	private String attachData;//附加的图片

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public Date getCreateStoreDate() {
		return createStoreDate;
	}

	public void setCreateStoreDate(Date createStoreDate) {
		this.createStoreDate = createStoreDate;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	@Override
	public String toString() {
		return "DhNewStore [id=" + id + ", createUserName=" + createUserName + ", publishDate=" + publishDate
				+ ", storeCode=" + storeCode + ", sapCode=" + sapCode + ", storeAddress=" + storeAddress
				+ ", deliverTime=" + deliverTime + ", createStoreDate=" + createStoreDate + ", attachData=" + attachData
				+ "]";
	}
	

}
