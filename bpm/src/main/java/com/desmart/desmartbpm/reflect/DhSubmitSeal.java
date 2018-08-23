package com.desmart.desmartbpm.reflect;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
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
		
		//String str = "网点房租类合同";
		//人员信息
		insParaJson.put("applicantNo", sysUser.getUserUid());//提交人工号
		insParaJson.put("applicantName", sysUser.getUserName());//提交人姓名
		insParaJson.put("positionNo", sysUser.getStation());//提交人职位编码
		insParaJson.put("positionName", sysUser.getStationcode());//提交人职位名称
		insParaJson.put("departmentNo", dhProcessInstance.getDepartNo());//提交人部门编码
		insParaJson.put("departmentName", dhProcessInstance.getDepartName());//成本中心编码
		insParaJson.put("costCenterNo", sysUser.getCostCenter());//成本中心编码
		insParaJson.put("costCenterName", sysUser.getCostCenterName());//成本中心名称
		insParaJson.put("profitCenterNo", sysUser.getProfitCenterNo());//提交人利润中心编码
		insParaJson.put("profitCenterName", sysUser.getProfitCenterName());//提交人利润中心名称
		insParaJson.put("phoneNumber", sysUser.getOfficeFax());//提交人分机号
		insParaJson.put("paidAmount", 0);//已付金额
		insParaJson.put("contractSigner", sysUser.getUserName()); //合同签订人
		insParaJson.put("companyNo", dhProcessInstance.getCompanyNumber());//公司代码
		insParaJson.put("companyNamecontact", dhProcessInstance.getCompanyName());//公司名称
		//接口参数请求
		JSONObject json = new JSONObject();
		json.put("intUid", intUid);
		json.put("inputParameter",insParaJson);
		//接口调用
		DhInterfaceExecuteService dhInterfaceExecuteService = ac.getBean(DhInterfaceExecuteService.class);
		try {
			ServerResponse<Map<String, String>>  map = dhInterfaceExecuteService.interfaceSchedule(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
