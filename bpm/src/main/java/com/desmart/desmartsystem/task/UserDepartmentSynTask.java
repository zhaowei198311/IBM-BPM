package com.desmart.desmartsystem.task;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartbpm.service.SynchronizeTaskService;
import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.dao.SysUserDepartmentMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.entity.SysUserDepartment;
import com.desmart.desmartsystem.util.DateUtil;
import com.desmart.desmartsystem.util.MyDateUtils;
import com.desmart.desmartsystem.util.UUIDTool;
import com.ibm.lyfwebservice.bean.CommonEmployeeField;
import com.ibm.lyfwebservice.bean.CommonOrganizationField;
import com.ibm.lyfwebservice.bean.EmployeeInfo;
import com.ibm.lyfwebservice.bean.Organization;
import com.ibm.lyfwebservice.webservice.CommonInterfaceService;
import com.ibm.lyfwebservice.webservice.CommonInterfaceServiceLocator;
import com.ibm.lyfwebservice.webservice.CommonOrganizationInterfaceService;
import com.ibm.lyfwebservice.webservice.CommonOrganizationInterfaceServiceLocator;

/**
 * <p>
 *  用户和部门数据同步
 * </p>
 *
 * @author xsf
 * @since 2018-04-11
 **/

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath*:spring.xml"})
public class UserDepartmentSynTask implements Job {
	
	
//	@Autowired
//	private SysDepartmentMapper sysDepartmentMapper;
//	
//	@Autowired
//	private SysUserMapper sysUserMapper;
//	
//	@Autowired
//	private SysUserDepartmentMapper sysUserDepartmentMapper;
//	
//	
//	@Test
//	//同步全部部门信息
//	public void runDepartmentAll() throws Exception{
//		CommonOrganizationInterfaceService commonOrganizationInterfaceService=new CommonOrganizationInterfaceServiceLocator();
//		CommonOrganizationField commonOrganizationField = commonOrganizationInterfaceService.getCommonOrganizationInterface(new URL("http://10.1.0.90/LyfWebService/services/CommonOrganizationInterface")).sendOrganization("LYF_ALL_OFFICE", "", "TFlGX0FMTF9PRkZJQ0U=");
//		Organization[] org=commonOrganizationField.getOrg();
//		List<SysDepartment>  sysDepartments=new ArrayList<SysDepartment>();
//		for (Organization organization : org) {
//			SysDepartment sysDepartment=new SysDepartment();
//			sysDepartment.setDepartUid("sysDepartment:"+UUIDTool.getUUID());
//			sysDepartment.setDepartParent(organization.getLyfSupervisoryDepartment());
//			sysDepartment.setDisplayName(organization.getDisplayName());
//			sysDepartment.setDepartName(organization.getName());
//			sysDepartment.setDepartNo(organization.getO());
//			sysDepartment.setDepartAdmins(organization.getDeplead());
//			sysDepartment.setCreateDate(new Date());
//			sysDepartments.add(sysDepartment);
//		}
//		sysDepartmentMapper.inserBatch(sysDepartments);
//	}
//	
//	@Test
//	//同步全部用户信息
//	public void runUserAll(){
//		try {
//			//获取所有部门数据
//			Map<String, SysDepartment> sysDepartmentMap=new HashMap<String, SysDepartment>();
//			List<SysDepartment>  sysDepartments=sysDepartmentMapper.selectDesmarts();
//			for (SysDepartment sysDepartment : sysDepartments) {
//				sysDepartmentMap.put(sysDepartment.getDepartNo(), sysDepartment);
//			}
//			CommonInterfaceService servie = new CommonInterfaceServiceLocator();
//			CommonEmployeeField sendEmployee = servie.getCommonInterface(new URL("http://10.1.0.90/LyfWebService/services/CommonInterface")).sendEmployee("LYF_ALL_OFFICE","","TFlGX0FMTF9PRkZJQ0U=");
//			List<SysUser> sysUsers=new ArrayList<SysUser>();
//			EmployeeInfo[] emp = sendEmployee.getEmp();
//			int i=0;
//			for (EmployeeInfo employeeInfo : emp) {
//				SysUser sysUser=new SysUser();
//				sysUser.setUserUid(employeeInfo.getUid());//用户代码
//				sysUser.setUserName(employeeInfo.getCn());
//				sysUser.setUserNameUs(employeeInfo.getLyfchinesepinyin());
//				sysUser.setSn(employeeInfo.getSn());
//				sysUser.setGivenname(employeeInfo.getGivenname());
//				String sex=employeeInfo.getLyfsex();
//				if(StringUtils.isNotBlank(sex)) {
//					sysUser.setSex(Short.valueOf(sex));
//				}
//				sysUser.setHomeplace(employeeInfo.getLyfhomeplace());
//				sysUser.setPassport(employeeInfo.getLyfpassport());
//				sysUser.setBirthday(employeeInfo.getLyfbirthday());
//				//sysUser.setOrderIndex(1);
//				sysUser.setPassword("123456");
//				
//				sysUser.setDepartUid(employeeInfo.getDepartmentNumber());
//				
//				sysUser.setOfficeTel(employeeInfo.getTelephonenumber());
//				sysUser.setOfficeFax(employeeInfo.getTelexnumber());
//				sysUser.setMobile(employeeInfo.getMobile());
//				sysUser.setEmail(employeeInfo.getMail());
//				sysUser.setWorkStatus(employeeInfo.getErPersonStatus());
//				sysUser.setSessionTime(30);
//				sysUser.setStation(employeeInfo.getLyfstation());
//				sysUser.setStationcode(employeeInfo.getLyfstationcode());
//				sysUser.setManagernumber(employeeInfo.getLyfmanagernumber());
//				sysUser.setCostCenter(employeeInfo.getLyfcostcenter());
//				sysUser.setCompanynumber(employeeInfo.getLyfcompanynumber());
//				sysUser.setDepartmetNumber(employeeInfo.getDepartmentNumber());
//				String level=employeeInfo.getLyflevel();
//				if(StringUtils.isNotBlank(level)) {
//					sysUser.setLevels(employeeInfo.getLyflevel());
//				}
//				sysUser.setReportTo(employeeInfo.getLyfmanagernumber());
//				if(StringUtils.isNotBlank(employeeInfo.getLyfhireday())) {
//					DateUtil.strToDate(employeeInfo.getLyfhireday(), "yyyyMMdd");
//				}
//				if(StringUtils.isNotBlank(employeeInfo.getLyfbirthday())) {
//					DateUtil.strToDate(employeeInfo.getLyfbirthday(), "yyyyMMdd");
//				}
//				sysUser.setCreateDate(new Date());
//				sysUsers.add(sysUser);
//				i++;
//				if(i%1000==0) {
//					sysUserMapper.insertBatch(sysUsers);
//				}
//			}
//			if(sysUsers.size()>0) {
//				sysUserMapper.insertBatch(sysUsers);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	//每天执行同步
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		CompanySys companySys=new CompanySys();
		companySys.executeSysCompany();
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SysDepartmentMapper sysDepartmentMapper = wac.getBean(SysDepartmentMapper.class);
		SysUserMapper sysUserMapper = wac.getBean(SysUserMapper.class);
		SysUserDepartmentMapper sysUserDepartmentMapper = wac.getBean(SysUserDepartmentMapper.class);
		
		//部门webService
		CommonOrganizationInterfaceService commonOrganizationInterfaceService=new CommonOrganizationInterfaceServiceLocator();
		CommonOrganizationField commonOrganizationField=null;
		try {
			commonOrganizationField = commonOrganizationInterfaceService.getCommonOrganizationInterface(new URL("http://10.1.0.90/LyfWebService/services/CommonOrganizationInterface")).sendOrganization("LYF_ALL_OFFICE",MyDateUtils.getCurrentDate(),"TFlGX0FMTF9PRkZJQ0U=");
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获取所有部门数据
		Map<String, SysDepartment> sysDepartmentMap=new HashMap<String, SysDepartment>();
		List<SysDepartment>  sysDepartments=sysDepartmentMapper.selectAll(new SysDepartment());
		for (SysDepartment sysDepartment : sysDepartments) {
			sysDepartmentMap.put(sysDepartment.getDepartNo(), sysDepartment);
		}
		//I增加 U修改 D删除
		List<SysDepartment>  insertSysDepartment=new ArrayList<SysDepartment>();
		List<SysDepartment>  updateSysDepartment=new ArrayList<SysDepartment>();
		List<SysDepartment>  deleteSysDepartment=new ArrayList<SysDepartment>();
		//获取接口返回的部门对象数字
		Organization[] org=commonOrganizationField.getOrg();
		for (Organization organization : org) {
			SysDepartment sysDepartment=new SysDepartment();
			sysDepartment.setDepartParent(organization.getLyfSupervisoryDepartment());
			sysDepartment.setDisplayName(organization.getDisplayName());
			sysDepartment.setDepartName(organization.getName());
			sysDepartment.setDepartNo(organization.getO());
			sysDepartment.setDepartAdmins(organization.getDeplead());
			sysDepartment.setCreateDate(new Date());
			//判断当前部门是否存在
			SysDepartment department=sysDepartmentMap.get(sysDepartment.getDepartNo());
			if(department!=null) {//存在就代表更新和删除
				//直接获取ID
				sysDepartment.setDepartUid(department.getDepartUid());
				if(organization.getChangeType().equals("U")) {
					updateSysDepartment.add(sysDepartment);
				}else {
					deleteSysDepartment.add(sysDepartment);
				}
			}else {
				//部门UID不存在直接生成
				sysDepartment.setDepartUid("sysDepartment"+UUIDTool.getUUID());
				//不存在直接添加
				if(!organization.getChangeType().equals("D")) {
					insertSysDepartment.add(sysDepartment);
				}
			}
			sysDepartmentMap.put(sysDepartment.getDepartNo(), sysDepartment);
		}
		//添加部门
		sysDepartmentMapper.inserBatch(insertSysDepartment);
		//修改部门
		for (SysDepartment sysDepartment : updateSysDepartment) {
			sysDepartmentMapper.update(sysDepartment);
		}
		//删除部门
		for (SysDepartment sysDepartment : updateSysDepartment) {
			//删除部门下的用户
			SysUserDepartment sysUserDepartment=new SysUserDepartment();
			sysUserDepartment.setDepartUid(sysDepartment.getDepartUid());
			sysUserDepartmentMapper.delete(sysUserDepartment);
			sysDepartmentMapper.delete(sysDepartment);
		}
		
		
		CommonInterfaceService servie = new CommonInterfaceServiceLocator();
		CommonEmployeeField sendEmployee=null;
		try {
			sendEmployee = servie.getCommonInterface(new URL("http://10.1.0.90/LyfWebService/services/CommonInterface")).sendEmployee("LYF_ALL_OFFICE",MyDateUtils.getCurrentDate(),"TFlGX0FMTF9PRkZJQ0U=");
		} catch (RemoteException | MalformedURLException | ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EmployeeInfo[] emp = sendEmployee.getEmp();
		//查询所有用户
		Map<String, SysUser> sysUserMap=new HashMap<String, SysUser>();
		List<SysUser> sysUsers=sysUserMapper.selectAll(null);
		for (SysUser sysUser : sysUsers) {
			sysUserMap.put(sysUser.getUserUid(), sysUser);
		}
		//I增加 U修改 D删除
		List<SysUser>  insertSysUser=new ArrayList<SysUser>();
		List<SysUser>  updateSysUser=new ArrayList<SysUser>();
		List<SysUser>  deleteSysUser=new ArrayList<SysUser>();
		for (EmployeeInfo employeeInfo : emp) {
			SysUser sysUser=new SysUser();
			sysUser.setUserUid(employeeInfo.getUid());//用户代码
			sysUser.setUserName(employeeInfo.getCn());
			sysUser.setUserNameUs(employeeInfo.getLyfchinesepinyin());
			sysUser.setSn(employeeInfo.getSn());
			sysUser.setGivenname(employeeInfo.getGivenname());
			String sex=employeeInfo.getLyfsex();
			if(StringUtils.isNotBlank(sex)) {
				sysUser.setSex(Short.valueOf(sex));
			}
			sysUser.setHomeplace(employeeInfo.getLyfhomeplace());
			sysUser.setPassport(employeeInfo.getLyfpassport());
			sysUser.setBirthday(employeeInfo.getLyfbirthday());
			//sysUser.setOrderIndex(1);
			sysUser.setPassword("123456");
			sysUser.setDepartUid(employeeInfo.getDepartmentNumber());
			sysUser.setOfficeTel(employeeInfo.getTelephonenumber());
			sysUser.setOfficeFax(employeeInfo.getTelexnumber());
			sysUser.setMobile(employeeInfo.getMobile());
			sysUser.setEmail(employeeInfo.getMail());
			sysUser.setWorkStatus(employeeInfo.getErPersonStatus());
			sysUser.setSessionTime(30);
			sysUser.setStation(employeeInfo.getLyfstation());
			sysUser.setStationcode(employeeInfo.getLyfstationcode());
			sysUser.setManagernumber(employeeInfo.getLyfmanagernumber());
			sysUser.setCostCenter(employeeInfo.getLyfcostcenter());
			sysUser.setCompanynumber(employeeInfo.getLyfcompanynumber());
			String level=employeeInfo.getLyflevel();
			if(StringUtils.isNotBlank(level)) {
				sysUser.setLevels(employeeInfo.getLyflevel());
			}
			sysUser.setReportTo(employeeInfo.getLyfmanagernumber());
			if(StringUtils.isNotBlank(employeeInfo.getLyfhireday())) {
				try {
					DateUtil.strToDate(employeeInfo.getLyfhireday(), "yyyyMMdd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(StringUtils.isNotBlank(employeeInfo.getLyfbirthday())) {
				try {
					DateUtil.strToDate(employeeInfo.getLyfbirthday(), "yyyyMMdd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sysUser.setCreateDate(new Date());
			String changeType=employeeInfo.getChangeType();
			//用户中是否包含待同步人员信息
			SysUser user=sysUserMap.get(sysUser.getUserUid());
			if(user!=null) {
				if(changeType.equals("U")) {
					//获取修改前的部门信息
					sysUser.setUpdateDate(new Date());
					updateSysUser.add(sysUser);
				}else if(changeType.equals("D")) {
					deleteSysUser.add(sysUser);
				}
			}else {
				//添加
				if(changeType.equals("I")||changeType.equals("U")) {
					insertSysUser.add(sysUser);
				}
			}
		}
		
		//添加
		sysUserMapper.insertBatch(insertSysUser);
		
		//修改用户
		for (SysUser sysUser : updateSysUser) {
			sysUserMapper.update(sysUser);
		}
		
		//删除
		for (SysUser sysUser : deleteSysUser) {
			sysUserMapper.delete(sysUser);
		}
	}
	
}
