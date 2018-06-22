/**
 * 
 */
package com.desmart.desmartbpm.entity;

import java.util.Date;

/**  
* <p>Title: 触发器接口中间表</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月8日  
*/
public class DhTriggerInterface {
	
	public static final String DH_TRIGGER_INTERFACE = "triggerInterface:";
	
	private String tinUid; // 主键
	
	private String triUid; // 触发器ID
	
	private String intUid; // 接口ID
	
	private String dynUid; // 表单ID
	
	private String creator; // 创建人
	
	private Date createTime; // 创建时间
	
	private String activityId; // 环节ID
	
	private String paraName; // 接口参数
	
	private String fldCodeName; // 字段编码
	
	private String parameterType; // 参数类型
	
	private BpmForm bpmForm; // 表单实体类

	/**
	 * @return the tinUid
	 */
	public String getTinUid() {
		return tinUid;
	}

	/**
	 * @param tinUid the tinUid to set
	 */
	public void setTinUid(String tinUid) {
		this.tinUid = tinUid;
	}

	/**
	 * @return the triUid
	 */
	public String getTriUid() {
		return triUid;
	}

	/**
	 * @param triUid the triUid to set
	 */
	public void setTriUid(String triUid) {
		this.triUid = triUid;
	}

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
	 * @return the dynUid
	 */
	public String getDynUid() {
		return dynUid;
	}

	/**
	 * @param dynUid the dynUid to set
	 */
	public void setDynUid(String dynUid) {
		this.dynUid = dynUid;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the actcUid
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * @param actcUid the actcUid to set
	 */
	public void setActivityId(String actcUid) {
		this.activityId = actcUid;
	}

	/**
	 * @return the paraName
	 */
	public String getParaName() {
		return paraName;
	}

	/**
	 * @param paraName the paraName to set
	 */
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	/**
	 * @return the fldCodeName
	 */
	public String getFldCodeName() {
		return fldCodeName;
	}

	/**
	 * @param fldCodeName the fldCodeName to set
	 */
	public void setFldCodeName(String fldCodeName) {
		this.fldCodeName = fldCodeName;
	}

	/**
	 * @return the bpmForm
	 */
	public BpmForm getBpmForm() {
		return bpmForm;
	}
	
	/**
	 * @return the parameterType
	 */
	public String getParameterType() {
		return parameterType;
	}

	/**
	 * @param tinUid the parameterType to set
	 */
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * @param bpmForm the bpmForm to set
	 */
	public void setBpmForm(BpmForm bpmForm) {
		this.bpmForm = bpmForm;
	}

	/**
	 * @return the dhTriggerInterface
	 */
	public static String getDhTriggerInterface() {
		return DH_TRIGGER_INTERFACE;
	}
	
	public DhTriggerInterface() {
		
	}

	/**
	 * @param tinUid
	 * @param triUid
	 * @param intUid
	 * @param dynUid
	 * @param creator
	 * @param createTime
	 * @param actcUid
	 * @param paraName
	 * @param fldCodeName
	 * @param bpmForm
	 */
	public DhTriggerInterface(String tinUid, String triUid, String intUid, String dynUid, String creator,
			Date createTime, String activityId, String paraName, String fldCodeName, String parameterType, BpmForm bpmForm) {
		super();
		this.tinUid = tinUid;
		this.triUid = triUid;
		this.intUid = intUid;
		this.dynUid = dynUid;
		this.creator = creator;
		this.createTime = createTime;
		this.activityId = activityId;
		this.paraName = paraName;
		this.fldCodeName = fldCodeName;
		this.parameterType = parameterType;
		this.bpmForm = bpmForm;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DhTriggerInterface [tinUid=" + tinUid + ", triUid=" + triUid + ", intUid=" + intUid + ", dynUid="
				+ dynUid + ", creator=" + creator + ", createTime=" + createTime + ", activityId=" + activityId
				+ ", paraName=" + paraName + ", fldCodeName=" + fldCodeName + ", parameterType=" + parameterType + ", bpmForm=" + bpmForm + "]";
	}

}