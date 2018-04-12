package com.desmart.desmartbpm.entity;

import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 */
public class SysResource implements Serializable {

    private static final long serialVersionUID = 1L;

	private String resouceUid;
	private String resouceName;
	private Integer orderIndex;
	private String parentId;
	private String resouceNo;
	private String resouceDesc;
	private Integer resouceType;
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String isClosed;
	private Date closeDate;
	private Date beginDate;
	private Date endDate;
	private Date createDate;
	private Date updateDate;


	public String getResouceUid() {
		return resouceUid;
	}

	public void setResouceUid(String resouceUid) {
		this.resouceUid = resouceUid;
	}

	public String getResouceName() {
		return resouceName;
	}

	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getResouceNo() {
		return resouceNo;
	}

	public void setResouceNo(String resouceNo) {
		this.resouceNo = resouceNo;
	}

	public String getResouceDesc() {
		return resouceDesc;
	}

	public void setResouceDesc(String resouceDesc) {
		this.resouceDesc = resouceDesc;
	}

	public Integer getResouceType() {
		return resouceType;
	}

	public void setResouceType(Integer resouceType) {
		this.resouceType = resouceType;
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
