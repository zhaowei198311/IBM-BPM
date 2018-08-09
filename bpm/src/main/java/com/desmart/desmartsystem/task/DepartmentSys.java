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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartsystem.dao.SysDepartmentMapper;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.util.MyDateUtils;
import com.ibm.lyfwebservice.bean.CommonOrganizationField;
import com.ibm.lyfwebservice.bean.Organization;
import com.ibm.lyfwebservice.webservice.CommonOrganizationInterfaceService;
import com.ibm.lyfwebservice.webservice.CommonOrganizationInterfaceServiceLocator;

/**
 * <p>
  *  DepartmentSys 部门同步
 * </p>
 *
 * @author xsf
 * @since 2018-08-09
 */
public class DepartmentSys  implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SysDepartmentMapper sysDepartmentMapper = wac.getBean(SysDepartmentMapper.class);
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
			sysDepartment.setDepartUid(organization.getO());//部门UId
			sysDepartment.setDepartNo(organization.getO());//部门编号
			sysDepartment.setDepartParent(organization.getLyfSupervisoryDepartment());
			sysDepartment.setDisplayName(organization.getDisplayName());
			sysDepartment.setDepartName(organization.getName());
			sysDepartment.setDepartAdmins(organization.getDeplead());
			sysDepartment.setCreateDate(new Date());
			//判断当前部门是否存在
			SysDepartment department=sysDepartmentMap.get(sysDepartment.getDepartNo());
			if(department!=null) {//存在就代表更新和删除
				if(organization.getChangeType().equals("U")) {
					updateSysDepartment.add(sysDepartment);
				}else {
					deleteSysDepartment.add(sysDepartment);
				}
			}else {
				//不存在直接添加
				if(!organization.getChangeType().equals("D")) {
					insertSysDepartment.add(sysDepartment);
				}
			}
			sysDepartmentMap.put(sysDepartment.getDepartUid(), sysDepartment);
		}
		
		//添加部门
		sysDepartmentMapper.inserBatch(insertSysDepartment);
		
		//修改部门
		for (SysDepartment sysDepartment : updateSysDepartment) {
			sysDepartmentMapper.update(sysDepartment);
		}
		
		//删除部门
		for (SysDepartment sysDepartment : updateSysDepartment) {
			sysDepartmentMapper.delete(sysDepartment);
		}
	}

}
