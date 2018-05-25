package com.desmart.desmartsystem.entity;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-05-22
 */
public class SysCompany{


    /**
     * 公司ID     
     */
	private String companyUid;
    /**
     * 公司代码     
     */
	private String companyCode;
    /**
     * 公司名称     
     */
	private String companyName;
    /**
     * 公司Logo   
     */
	private String companyLogo;
    /**
     * 公司类型     
     */
	private String companyType;
    /**
     * 公司描述     
     */
	private String companyDesc;
    /**
     * 备用字段     
     */
	private String ext1;
    /**
     * 备用字段     
     */
	private String ext2;
    /**
     * 备用字段     
     */
	private String ext3;
    /**
     * 备用字段     
     */
	private String ext4;
    /**
     * 备用字段     
     */
	private String ext5;
    /**
     * 是否禁用     
     */
	private String isClosed;
    /**
     * 禁用日期     
     */
	private Date closeDate;
    /**
     * 开始日期     
     */
	private Date beginDate;
    /**
     * 结束日期     
     */
	private Date endDate;
    /**
     * 数据生成日期   
     */
	private Date createDate;
    /**
     * 数据更新日期   
     */
	private Date updateDate;


	public String getCompanyUid() {
		return companyUid;
	}

	public void setCompanyUid(String companyUid) {
		this.companyUid = companyUid;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
