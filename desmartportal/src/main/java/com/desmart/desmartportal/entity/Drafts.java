/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.util.Date;

/**  
* <p>Title: 草稿箱实体类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月28日  
*/
public class Drafts {

	private String dfsId; // 草稿id
	
	private String dfsTitle; // 草稿标题
	
	private String insUid; // 流程实例id
	
	private String dfsStatus; // 草稿状态
	
	private String dfsData; // 草稿数据
	
	private Date dfsCreatedate; // 草稿创建时间
	
	private String dfsCreator; // 草稿创建人

	/**
	 * @return the dfsId
	 */
	public String getDfsId() {
		return dfsId;
	}

	/**
	 * @param dfsId the dfsId to set
	 */
	public void setDfsId(String dfsId) {
		this.dfsId = dfsId;
	}

	/**
	 * @return the dfsTitle
	 */
	public String getDfsTitle() {
		return dfsTitle;
	}

	/**
	 * @param dfsTitle the dfsTitle to set
	 */
	public void setDfsTitle(String dfsTitle) {
		this.dfsTitle = dfsTitle;
	}

	/**
	 * @return the insUid
	 */
	public String getInsUid() {
		return insUid;
	}

	/**
	 * @param insUid the insUid to set
	 */
	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}

	/**
	 * @return the dfsStatus
	 */
	public String getDfsStatus() {
		return dfsStatus;
	}

	/**
	 * @param dfsStatus the dfsStatus to set
	 */
	public void setDfsStatus(String dfsStatus) {
		this.dfsStatus = dfsStatus;
	}

	/**
	 * @return the dfsData
	 */
	public String getDfsData() {
		return dfsData;
	}

	/**
	 * @param dfsData the dfsData to set
	 */
	public void setDfsData(String dfsData) {
		this.dfsData = dfsData;
	}

	/**
	 * @return the dfsCreatedate
	 */
	public Date getDfsCreatedate() {
		return dfsCreatedate;
	}

	/**
	 * @param dfsCreatedate the dfsCreatedate to set
	 */
	public void setDfsCreatedate(Date dfsCreatedate) {
		this.dfsCreatedate = dfsCreatedate;
	}

	/**
	 * @return the dfsCreator
	 */
	public String getDfsCreator() {
		return dfsCreator;
	}

	/**
	 * @param dfsCreator the dfsCreator to set
	 */
	public void setDfsCreator(String dfsCreator) {
		this.dfsCreator = dfsCreator;
	}
	
	public Drafts(){
		
	}
	
	/**
	 * @param dfsId 
	 * @param dfsTitle
	 * @param insUid
	 * @param dfsStatus
	 * @param dfsData
	 * @param dfsCreatedate
	 * @param dfsCreator
	 */
	public Drafts(String dfsId, String dfsTitle, String insUid, String dfsStatus, String dfsData, Date dfsCreatedate,
			String dfsCreator) {
		super();
		this.dfsId = dfsId;
		this.dfsTitle = dfsTitle;
		this.insUid = insUid;
		this.dfsStatus = dfsStatus;
		this.dfsData = dfsData;
		this.dfsCreatedate = dfsCreatedate;
		this.dfsCreator = dfsCreator;
	}
}
