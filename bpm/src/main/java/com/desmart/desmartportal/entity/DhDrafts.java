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
public class DhDrafts {

	private String dfsId; // 草稿id
	
	private String dfsTitle; // 草稿标题
	
	private String dfsData; // 草稿数据
	
	private Date dfsCreatedate; // 草稿创建时间
	
	private String dfsCreator; // 草稿创建人
	
	private String proUid; // 流程id
	
	private String proVerUid; // 流程版本id
	
	private String proAppId; // 流程应用库id

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

	/**
	 * @return the proUid
	 */
	public String getProUid() {
		return proUid;
	}

	/**
	 * @param proUid the proUid to set
	 */
	public void setProUid(String proUid) {
		this.proUid = proUid;
	}

	/**
	 * @return the proVerUid
	 */
	public String getProVerUid() {
		return proVerUid;
	}

	/**
	 * @param proVerUid the proVerUid to set
	 */
	public void setProVerUid(String proVerUid) {
		this.proVerUid = proVerUid;
	}

	/**
	 * @return the proAppId
	 */
	public String getProAppId() {
		return proAppId;
	}

	/**
	 * @param proAppId the proAppId to set
	 */
	public void setProAppId(String proAppId) {
		this.proAppId = proAppId;
	}
	
	public DhDrafts() {
		
	}

	/**
	 * @param dfsId
	 * @param dfsTitle
	 * @param dfsData
	 * @param dfsCreatedate
	 * @param dfsCreator
	 * @param proUid
	 * @param proVerUid
	 * @param proAppId
	 */
	public DhDrafts(String dfsId, String dfsTitle, String dfsData, Date dfsCreatedate, String dfsCreator, String proUid,
			String proVerUid, String proAppId) {
		super();
		this.dfsId = dfsId;
		this.dfsTitle = dfsTitle;
		this.dfsData = dfsData;
		this.dfsCreatedate = dfsCreatedate;
		this.dfsCreator = dfsCreator;
		this.proUid = proUid;
		this.proVerUid = proVerUid;
		this.proAppId = proAppId;
	}

	
}
