package com.desmart.desmartbpm.reflect;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartsystem.controller.DhInterfaceExecuteController;
import com.desmart.desmartsystem.dao.DhInterfaceParameterMapper;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.util.Json;

/**
 * 合同用印触发器
 * @author zbw
 *
 */
public class DhSubmitSeal extends DhOneTimeJavaClassTrigger{

	
	
	
	@Override
	public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
		// TODO Auto-generated method stub
		String intUid = "int_meta:d1398f55-fa5a-481b-baa9-ec6df734a987";
		//当前实例
		DhProcessInstanceService dhProcessInstanceService = ac.getBean(DhProcessInstanceService.class);
		DhProcessInstance dhProcessInstance = dhProcessInstanceService.getByInsUid(insUid);
		String uesrUid = dhProcessInstance.getInsInitUser();
		//表单数据
		String insData = dhProcessInstance.getInsData();
		JSONObject  insDataJson = JSONObject.parseObject(insData);
		JSONObject  formData =insDataJson.getJSONObject("formData");
		//获取用户信息
		SysUserMapper  sysUserMapper = ac.getBean(SysUserMapper.class);
		SysUser sysUser = new SysUser();
		sysUser.setUserUid("00014495");
		sysUserMapper.findById(sysUser);
		//接口数据
		JSONObject insParaJson  = new  JSONObject();
		DhInterfaceParameterMapper dhInterfaceParameterMapper	= ac.getBean(DhInterfaceParameterMapper.class);
		List<DhInterfaceParameter> dhInterfaceParameterList	 = dhInterfaceParameterMapper.listAll(intUid);
		for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
			//接口参数字段名
			String paraName = dhInterfaceParameter.getParaName();
			//获取触器起参数
			String mapValue = jsonObject.getString(paraName);
			if(StringUtils.isNotBlank(mapValue)) {
				//获取表单字段值
				String formFieldValue = formData.getString(mapValue);
				//判断表单
				if(formFieldValue!=null) {
					String value  = JSONObject.parseObject(formFieldValue).getString("value");
					insParaJson.put(paraName,value);
				};
			};
		}
		//人员信息
		insParaJson.put("applicantNo", sysUser.getUserUid());
		insParaJson.put("applicantName", sysUser.getUserName());
		insParaJson.put("positionNo", sysUser.getStation());
		insParaJson.put("positionName", sysUser.getStationcode());
		insParaJson.put("departmentNo", sysUser.getDepartName());
		insParaJson.put("departmentName", sysUser.getDepartmetNumber());
		insParaJson.put("costCenterNo", sysUser.getCostCenter());
		insParaJson.put("costCenterName", sysUser.getCostCenterName());
		insParaJson.put("profitCenterNo", sysUser.getProfitCenterNo());
		insParaJson.put("profitCenterName", sysUser.getProfitCenterName());
		insParaJson.put("phoneNumber", sysUser.getMobile());
		insParaJson.put("paidAmount", 0);
		
		//接口参数请求
		JSONObject json = new JSONObject();
		json.put("intUid", intUid);
		json.put("inputParameter",insParaJson);
		//接口调用
		DhInterfaceExecuteService dhInterfaceExecuteService = ac.getBean(DhInterfaceExecuteService.class);
		try {
			Json responseJson = dhInterfaceExecuteService.interfaceSchedule(json);
			String msg =  responseJson.getMsg();
			System.out.println(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
