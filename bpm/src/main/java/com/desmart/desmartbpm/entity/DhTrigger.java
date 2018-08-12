package com.desmart.desmartbpm.entity;

import java.util.Date;

import com.desmart.desmartsystem.entity.SysUser;

/**
 * 触发器
 */
public class DhTrigger {
    private String triUid;
    private String triTitle;  // 触发器标题
    private String triDescription; // 触发器描述：Java class名；script 内容；SQL内容；接口名称
    private String triType; // 触发器类型：script；javaclass；sql；interface
    private String triWebbot; // 触发器执行命令：java class名；script 内容；sql内容；接口名称
    private String triParam; // 触发器参数
    private String creator;	// 创建人
    private Date createTime; // 创建时间
    private String updator; // 更新人
    private Date updateTime; // 更新时间
    private SysUser sysUser; // 用户实体类

	private String interfaceTitle; // 当触发器是接口类型时，前端展示接口名
    
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
	 * @return the triTitle
	 */
	public String getTriTitle() {
		return triTitle;
	}
	/**
	 * @param triTitle the triTitle to set
	 */
	public void setTriTitle(String triTitle) {
		this.triTitle = triTitle;
	}
	/**
	 * @return the triDescription
	 */
	public String getTriDescription() {
		return triDescription;
	}
	/**
	 * @param triDescription the triDescription to set
	 */
	public void setTriDescription(String triDescription) {
		this.triDescription = triDescription;
	}
	/**
	 * @return the triType
	 */
	public String getTriType() {
		return triType;
	}
	/**
	 * @param triType the triType to set
	 */
	public void setTriType(String triType) {
		this.triType = triType;
	}
	/**
	 * @return the triWebbot
	 */
	public String getTriWebbot() {
		return triWebbot;
	}
	/**
	 * @param triWebbot the triWebbot to set
	 */
	public void setTriWebbot(String triWebbot) {
		this.triWebbot = triWebbot;
	}
	/**
	 * @return the triParam
	 */
	public String getTriParam() {
		return triParam;
	}
	/**
	 * @param triParam the triParam to set
	 */
	public void setTriParam(String triParam) {
		this.triParam = triParam;
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
	 * @return the updator
	 */
	public String getUpdator() {
		return updator;
	}
	/**
	 * @param updator the updator to set
	 */
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the sysUser
	 */
	public SysUser getSysUser() {
		return sysUser;
	}
	/**
	 * @param sysUser the sysUser to set
	 */
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	public DhTrigger() {
		
	}
	
	/**
	 * @param triUid
	 * @param triTitle
	 * @param triDescription
	 * @param triType
	 * @param triWebbot
	 * @param triParam
	 * @param creator
	 * @param createTime
	 * @param updator
	 * @param updateTime
	 * @param sysUser
	 */
	public DhTrigger(String triUid, String triTitle, String triDescription, String triType, String triWebbot,
			String triParam, String creator, Date createTime, String updator, Date updateTime, SysUser sysUser) {
		super();
		this.triUid = triUid;
		this.triTitle = triTitle;
		this.triDescription = triDescription;
		this.triType = triType;
		this.triWebbot = triWebbot;
		this.triParam = triParam;
		this.creator = creator;
		this.createTime = createTime;
		this.updator = updator;
		this.updateTime = updateTime;
		this.sysUser = sysUser;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DhTrigger [triUid=" + triUid + ", triTitle=" + triTitle + ", triDescription=" + triDescription
				+ ", triType=" + triType + ", triWebbot=" + triWebbot + ", triParam=" + triParam + ", creator="
				+ creator + ", createTime=" + createTime + ", updator=" + updator + ", updateTime=" + updateTime
				+ ", sysUser=" + sysUser + "]";
	}

	public String getInterfaceTitle() {
		return interfaceTitle;
	}

	public void setInterfaceTitle(String interfaceTitle) {
		this.interfaceTitle = interfaceTitle;
	}
}