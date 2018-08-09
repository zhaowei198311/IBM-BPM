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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.util.DateUtil;
import com.desmart.desmartsystem.util.MyDateUtils;
import com.ibm.lyfwebservice.bean.CommonEmployeeField;
import com.ibm.lyfwebservice.bean.EmployeeInfo;
import com.ibm.lyfwebservice.webservice.CommonInterfaceService;
import com.ibm.lyfwebservice.webservice.CommonInterfaceServiceLocator;

/**
 * <p>
  *  UserSys 人员同步
 * </p>
 *
 * @author xsf
 * @since 2018-08-09
 */
public class UserSys implements Job{

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SysUserMapper sysUserMapper = wac.getBean(SysUserMapper.class);
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
