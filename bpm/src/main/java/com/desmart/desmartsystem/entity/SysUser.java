package com.desmart.desmartsystem.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;

import java.util.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-04-08
 */
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userUid;

	private String userNo;

	private String userId;

	private String userName;

	private String userNameUs;

	private String sn;

	private String givenname;

	private Short sex;

	private String homeplace;

	private String passport;

	private String birthday;

	private Integer orderIndex;

	private String password;

	private String departUid;

	private Short isManager;

	private String workCalendar;

	private String officeTel;

	private String officeFax;

	private String mobile;

	private String email;

	private String userIp;

	private Short isSingleLogin;

	private String workStatus;

	private Integer sessionTime;

	private String wechat;

	private String station;

	private String stationcode;

	private String managernumber;

	private String levels;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;

	private String ext5;

	private String reportTo;

	private Integer isClosed;

	private Date closeDate;

	private Date beginDate;

	private Date endDate;

	private Date createDate;

	private Date updateDate;

	private String companynumber; 

	private String departmetNumber;

	private String accountType;
	private SysDepartment sysDepartment;
	
	private List<SysUserDepartment> sysUserDepartmentList;

	private String costCenter;

	private String departName;
	private String employeeType;
	
	private String managerName;
	
	private String costCenterName;//成本中心名称
	private String profitCenterNo;//利润中心
	private String profitCenterName;//利润名称

	private List<String> companyNumberList;   // 员工所属的公司，可能属于多个公司
	private List<String> departmetNumberList; // 员工所属的部门，可能属于多个部门




	public void addToDepartNumberList(String departmetNumber) {
		if (StringUtils.isBlank(departmetNumber)) {
			return;
		}
		if (CollectionUtils.isEmpty(this.departmetNumberList)) {
			this.departmetNumberList = new ArrayList<>();
		}
		this.departmetNumberList.add(departmetNumber);
	}

	public void addToCompanyNumberList(String companyNumber) {
		if (StringUtils.isBlank(companyNumber)) {
			return;
		}
		if (CollectionUtils.isEmpty(this.companyNumberList)) {
			this.companyNumberList = new ArrayList<>();
		}
		this.companyNumberList.add(companyNumber);
	}

	/**
	 * 用户是否是指定公司编码的成员<br/>
	 * 使用此方法前，需要装配companyNumberList属性
	 * @param companyNumber
	 * @return
	 */
	public boolean isMemberOfCompany(String companyNumber) {
		if (StringUtils.isBlank(companyNumber)) {
			return false;
		}
		if (companyNumber.equals(this.companynumber)) {
			return true;
		}
		if (this.companyNumberList == null) {
			return false;
		}
		return this.companyNumberList.contains(companyNumber);
	}

	/**
	 * 判断用户是否是关联部门中的一员
	 * @param relationDepartmentNumbers 相关部门uid集合
	 * @return
	 */
	public boolean isMemberOfRelationDepartments(List<String> relationDepartmentNumbers) {
		if (CollectionUtils.isEmpty(relationDepartmentNumbers)) {
			return false;
		}
		if (relationDepartmentNumbers.contains(this.departUid)) {
			return true;
		}
		if (CollectionUtils.isEmpty(this.departmetNumberList)) {
			return false;
		}
		for (String departmetNumber : this.departmetNumberList) {
			if (relationDepartmentNumbers.contains(departmetNumber)) {
				return true;
			}
		}
		return false;
	}




	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid == null ? null : userUid.trim();
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo == null ? null : userNo.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getUserNameUs() {
		return userNameUs;
	}

	public List<String> getCompanyNumberList() {
		return companyNumberList;
	}

	public void setCompanyNumberList(List<String> companyNumberList) {
		this.companyNumberList = companyNumberList;
	}

	public void setUserNameUs(String userNameUs) {
		this.userNameUs = userNameUs == null ? null : userNameUs.trim();
	}

	public String getSn() {
		return sn;
	}

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public void setSn(String sn) {
		this.sn = sn == null ? null : sn.trim();
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname == null ? null : givenname.trim();
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getHomeplace() {
		return homeplace;
	}

	public void setHomeplace(String homeplace) {
		this.homeplace = homeplace == null ? null : homeplace.trim();
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport == null ? null : passport.trim();
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday == null ? null : birthday.trim();
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getDepartUid() {
		return departUid;
	}

	public void setDepartUid(String departUid) {
		this.departUid = departUid == null ? null : departUid.trim();
	}

	public Short getIsManager() {
		return isManager;
	}

	public void setIsManager(Short isManager) {
		this.isManager = isManager;
	}

	public String getWorkCalendar() {
		return workCalendar;
	}

	public void setWorkCalendar(String workCalendar) {
		this.workCalendar = workCalendar == null ? null : workCalendar.trim();
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel == null ? null : officeTel.trim();
	}

	public String getOfficeFax() {
		return officeFax;
	}

	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax == null ? null : officeFax.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp == null ? null : userIp.trim();
	}

	public Short getIsSingleLogin() {
		return isSingleLogin;
	}

	public void setIsSingleLogin(Short isSingleLogin) {
		this.isSingleLogin = isSingleLogin;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus == null ? null : workStatus.trim();
	}

	public Integer getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(Integer sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat == null ? null : wechat.trim();
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station == null ? null : station.trim();
	}

	public String getStationcode() {
		return stationcode;
	}

	public void setStationcode(String stationcode) {
		this.stationcode = stationcode == null ? null : stationcode.trim();
	}

	public String getManagernumber() {
		return managernumber;
	}

	public void setManagernumber(String managernumber) {
		this.managernumber = managernumber == null ? null : managernumber.trim();
	}


	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1 == null ? null : ext1.trim();
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2 == null ? null : ext2.trim();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3 == null ? null : ext3.trim();
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4 == null ? null : ext4.trim();
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5 == null ? null : ext5.trim();
	}

	public String getReportTo() {
		return reportTo;
	}

	public void setReportTo(String reportTo) {
		this.reportTo = reportTo == null ? null : reportTo.trim();
	}

	public Integer getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Integer isClosed) {
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

	public String getCompanynumber() { 
		return companynumber;
	}

	public void setCompanynumber(String companynumber) {
		this.companynumber = companynumber == null ? null : companynumber.trim();
	}

	public String getDepartmetNumber() { 
		return departmetNumber;
	}

	public void setDepartmetNumber(String departmetNumber) {
		this.departmetNumber = departmetNumber == null ? null : departmetNumber.trim();
	}

	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType == null ? null : accountType.trim();
	}

	public SysDepartment getSysDepartment() {
		return sysDepartment;
	}

	public void setSysDepartment(SysDepartment sysDepartment) {
		this.sysDepartment = sysDepartment;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public List<SysUserDepartment> getSysUserDepartmentList() {
		return sysUserDepartmentList;
	}

	public void setSysUserDepartmentList(List<SysUserDepartment> sysUserDepartmentList) {
		this.sysUserDepartmentList = sysUserDepartmentList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SysUser sysUser = (SysUser) o;
		return Objects.equals(userUid, sysUser.userUid);
	}

	@Override
	public int hashCode() {

		return Objects.hash(userUid);
	}

	public List<String> getDepartmetNumberList() {
		return departmetNumberList;
	}

	public void setDepartmetNumberList(List<String> departmetNumberList) {
		this.departmetNumberList = departmetNumberList;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getCostCenterName() {
		return costCenterName;
	}

	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}

	public String getProfitCenterNo() {
		return profitCenterNo;
	}

	public void setProfitCenterNo(String profitCenterNo) {
		this.profitCenterNo = profitCenterNo;
	}

	public String getProfitCenterName() {
		return profitCenterName;
	}

	public void setProfitCenterName(String profitCenterName) {
		this.profitCenterName = profitCenterName;
	}
	
	
}
