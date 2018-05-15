/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.util.Date;

/**  
* <p>Title: 流程实例实体类</p>  
* <p>Description:  </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public class DhProcessInstance {
	
	private String insUid; // 流程实例ID
	
	private String insTitle; // 流程实例标题

	private String insDescription; // 流程实例描述
	
	private int insId; // 流程实例编号(引擎)

	private String insParent; // 父流程实例ID

	private String insStatus; // 流程实例状态

	private int insStatusId; // 流程实例状态ID

	private String proAppId; // 流程应用库ID
	
	private String proUid; // 流程定义ID

	private String proVerUid; // 流程版本ID

	private String insInitUser; // 流程实例启动用户ID

	private Date insCreateDate; // 流程实例创建日期

	private Date insInitDate; // 流程实例初始日期

	private Date insFinishDate; // 流程实例完成日期

	private Date insUpdateDate; // 流程实例更新日期
	
	private String insData; // 流程实例数据

	private double insDuration; // 流程实例持续时间

	private double insDelayDuration; // 流程实例延迟持续时间

	private String insDriveFolderUid; // 流程实例文件目录ID

	private String insRoutingData; // 流程实例路由数据

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
	 * @return the insTitle
	 */
	public String getInsTitle() {
		return insTitle;
	}

	/**
	 * @param insTitle the insTitle to set
	 */
	public void setInsTitle(String insTitle) {
		this.insTitle = insTitle;
	}

	/**
	 * @return the insDescription
	 */
	public String getInsDescription() {
		return insDescription;
	}

	/**
	 * @param insDescription the insDescription to set
	 */
	public void setInsDescription(String insDescription) {
		this.insDescription = insDescription;
	}

	/**
	 * @return the insId
	 */
	public int getInsId() {
		return insId;
	}

	/**
	 * @param insId the insId to set
	 */
	public void setInsId(int insId) {
		this.insId = insId;
	}

	/**
	 * @return the insParent
	 */
	public String getInsParent() {
		return insParent;
	}

	/**
	 * @param insParent the insParent to set
	 */
	public void setInsParent(String insParent) {
		this.insParent = insParent;
	}

	/**
	 * @return the insStatus
	 */
	public String getInsStatus() {
		return insStatus;
	}

	/**
	 * @param insStatus the insStatus to set
	 */
	public void setInsStatus(String insStatus) {
		this.insStatus = insStatus;
	}

	/**
	 * @return the insStatusId
	 */
	public int getInsStatusId() {
		return insStatusId;
	}

	/**
	 * @param insStatusId the insStatusId to set
	 */
	public void setInsStatusId(int insStatusId) {
		this.insStatusId = insStatusId;
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
	 * @return the insInitUser
	 */
	public String getInsInitUser() {
		return insInitUser;
	}

	/**
	 * @param insInitUser the insInitUser to set
	 */
	public void setInsInitUser(String insInitUser) {
		this.insInitUser = insInitUser;
	}

	/**
	 * @return the insCreateDate
	 */
	public Date getInsCreateDate() {
		return insCreateDate;
	}

	/**
	 * @param insCreateDate the insCreateDate to set
	 */
	public void setInsCreateDate(Date insCreateDate) {
		this.insCreateDate = insCreateDate;
	}

	/**
	 * @return the insInitDate
	 */
	public Date getInsInitDate() {
		return insInitDate;
	}

	/**
	 * @param insInitDate the insInitDate to set
	 */
	public void setInsInitDate(Date insInitDate) {
		this.insInitDate = insInitDate;
	}

	/**
	 * @return the insFinishDate
	 */
	public Date getInsFinishDate() {
		return insFinishDate;
	}

	/**
	 * @param insFinishDate the insFinishDate to set
	 */
	public void setInsFinishDate(Date insFinishDate) {
		this.insFinishDate = insFinishDate;
	}

	/**
	 * @return the insUpdateDate
	 */
	public Date getInsUpdateDate() {
		return insUpdateDate;
	}

	/**
	 * @param insUpdateDate the insUpdateDate to set
	 */
	public void setInsUpdateDate(Date insUpdateDate) {
		this.insUpdateDate = insUpdateDate;
	}

	/**
	 * @return the insData
	 */
	public String getInsData() {
		return insData;
	}

	/**
	 * @param insData the insData to set
	 */
	public void setInsData(String insData) {
		this.insData = insData;
	}

	/**
	 * @return the insDuration
	 */
	public double getInsDuration() {
		return insDuration;
	}

	/**
	 * @param insDuration the insDuration to set
	 */
	public void setInsDuration(double insDuration) {
		this.insDuration = insDuration;
	}

	/**
	 * @return the insDelayDuration
	 */
	public double getInsDelayDuration() {
		return insDelayDuration;
	}

	/**
	 * @param insDelayDuration the insDelayDuration to set
	 */
	public void setInsDelayDuration(double insDelayDuration) {
		this.insDelayDuration = insDelayDuration;
	}

	/**
	 * @return the insDriveFolderUid
	 */
	public String getInsDriveFolderUid() {
		return insDriveFolderUid;
	}

	/**
	 * @param insDriveFolderUid the insDriveFolderUid to set
	 */
	public void setInsDriveFolderUid(String insDriveFolderUid) {
		this.insDriveFolderUid = insDriveFolderUid;
	}

	/**
	 * @return the insRoutingData
	 */
	public String getInsRoutingData() {
		return insRoutingData;
	}

	/**
	 * @param insRoutingData the insRoutingData to set
	 */
	public void setInsRoutingData(String insRoutingData) {
		this.insRoutingData = insRoutingData;
	}
	
	public DhProcessInstance() {
		
	}

	/**
	 * @param insUid
	 * @param insTitle
	 * @param insDescription
	 * @param insId
	 * @param insParent
	 * @param insStatus
	 * @param insStatusId
	 * @param proAppId
	 * @param proUid
	 * @param proVerUid
	 * @param insInitUser
	 * @param insCreateDate
	 * @param insInitDate
	 * @param insFinishDate
	 * @param insUpdateDate
	 * @param insData
	 * @param insDuration
	 * @param insDelayDuration
	 * @param insDriveFolderUid
	 * @param insRoutingData
	 */
	public DhProcessInstance(String insUid, String insTitle, String insDescription, int insId, String insParent,
			String insStatus, int insStatusId, String proAppId, String proUid, String proVerUid, String insInitUser,
			Date insCreateDate, Date insInitDate, Date insFinishDate, Date insUpdateDate, String insData,
			double insDuration, double insDelayDuration, String insDriveFolderUid, String insRoutingData) {
		super();
		this.insUid = insUid;
		this.insTitle = insTitle;
		this.insDescription = insDescription;
		this.insId = insId;
		this.insParent = insParent;
		this.insStatus = insStatus;
		this.insStatusId = insStatusId;
		this.proAppId = proAppId;
		this.proUid = proUid;
		this.proVerUid = proVerUid;
		this.insInitUser = insInitUser;
		this.insCreateDate = insCreateDate;
		this.insInitDate = insInitDate;
		this.insFinishDate = insFinishDate;
		this.insUpdateDate = insUpdateDate;
		this.insData = insData;
		this.insDuration = insDuration;
		this.insDelayDuration = insDelayDuration;
		this.insDriveFolderUid = insDriveFolderUid;
		this.insRoutingData = insRoutingData;
	}
}
