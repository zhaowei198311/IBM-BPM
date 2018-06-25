package com.desmart.desmartsystem.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.desmart.desmartsystem.task.SAPConn;
import com.desmart.desmartsystem.util.HttpClientCallSoapUtil;
import com.desmart.desmartsystem.util.HttpRequestUtils;
import com.desmart.desmartsystem.util.Json;
import com.desmart.desmartsystem.util.TestXML;
import com.desmart.desmartsystem.util.XmlFormat;
import com.desmart.desmartsystem.util.XmlParsing;
import com.desmart.desmartsystem.util.XmlToJsonUtils;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

@Service
public class DhInterfaceExecuteServiceImpl implements DhInterfaceExecuteService {

	@Autowired
	private DhInterfaceService dhInterfaceService;
	
	@Autowired
	private DhInterfaceParameterService dhInterfaceParameterService;

	@Override
	public Json interfaceSchedule(JSONObject jsonObject) throws Exception {
		Json json = new Json();
		
		String intUid = jsonObject.getString("intUid");
		if (StringUtils.isBlank(intUid)) {
			json.setSuccess(false);
			json.setMsg("接口未找到");
			return json;
		}

		DhInterface dhInterface=dhInterfaceService.selectDhInterfaceByid(intUid);
		jsonObject.put("dhinterface", dhInterface);
		String intType = dhInterface.getIntType();
		
		String intCallMethod = dhInterface.getIntCallMethod(); // 执行方法
		String intLoginPwd = dhInterface.getIntLoginPwd(); // 用户名
		String intLoginUser = dhInterface.getIntLoginUser();// 密码
		String intUrl = dhInterface.getIntUrl();//接口Url
		String intXml =	dhInterface.getIntXml();//接口xml
		
		String requestXml = dhInterface.getIntRequestXml(); //请求xml
		String responseXml = dhInterface.getIntResponseXml();//响应xml
		
		JSONObject 	inputParameter = (JSONObject) jsonObject.getJSONObject("inputParameter");
		if (intType.equals("rpc")) {
			SAPConn.sapConfiguration(intUrl, intLoginUser, intLoginPwd);
			JCoFunction function = null;
			JCoDestination destination = SAPConn.connect();
			function = destination.getRepository().getFunction(intCallMethod);
			JCoParameterList input = function.getImportParameterList();
			Set<String> parameters = inputParameter.keySet();
			Iterator<String> iterator = parameters.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = inputParameter.getString(key);
				System.out.println(key);
				System.out.println(value);
				input.setValue(key, value);
			}
			function.execute(destination);
			JCoParameterList tblexport = function.getTableParameterList();
			
			
			DhInterfaceParameter dfParameter = new DhInterfaceParameter();
			dfParameter.setIntUid(intUid);
			dfParameter.setParaInOut("output");
			
			List<DhInterfaceParameter> dhInterfaceParameterList =dhInterfaceParameterService.byQueryParameter(dfParameter);
			
			JSONArray jsonArray=new JSONArray();
			
			//参数拼接
			for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
				//参数类型
				String paraType = dhInterfaceParameter.getParaType();
				//参数名称
				String paraName = dhInterfaceParameter.getParaName();
				//参数UId
				String paraUid = dhInterfaceParameter.getParaUid();
				
				if(paraType.equals("Array")) {
					JCoTable getTable1 = tblexport.getTable(paraName);
					boolean loopFlag1 = !getTable1.isEmpty(); //判断 这张表中有木有数据
		            while(loopFlag1){
		            	JSONObject jSONObject=new JSONObject();
		            	//得到返回参数懂填入
						for (DhInterfaceParameter ifl : dhInterfaceParameterList) {
							String iflParaParent = ifl.getParaParent();
							if(StringUtils.isNoneBlank(iflParaParent)) {
								if(iflParaParent.equals(paraUid)) {//获取Array子参数 
									String iflParaName=ifl.getParaName();
									String value=getTable1.getString(iflParaName);
									jSONObject.put(iflParaName, value);
								}
							}
						}
						jsonArray.add(jSONObject);
						loopFlag1 = getTable1.nextRow();// 移动到下一行
		            }
				}
			}
			
			json.setSuccess(true);
			json.setMsg(JSON.toJSONString(jsonArray.toArray()));
			json.setObj(jsonArray.toArray());
			
		}else if(intType.equals("webservice")) {
			//SAPConn.sapConfiguration(intUrl, intLoginUser, intLoginPwd);
			String soapRequestData =XmlParsing.getSaopParameter(requestXml,inputParameter); //soap协议的格式，定义了方法和参数
			String xml =HttpClientCallSoapUtil.doPostSoap1_2(intUrl, soapRequestData, "");
			json.setSuccess(true);
			json.setMsg("查询成功");
			
			
			//返回参数格式拼接
			List<String> responseConfig=new ArrayList<String>();
			TestXML.testGetRoot(responseXml,responseConfig);
			json.setMsg(XmlToJsonUtils.xmlToJson(xml,responseConfig));
			
		}else if(intType.equals("restapi")) {
			json.setSuccess(true);
			json.setMsg(HttpRequestUtils.httpPost(intUrl+intCallMethod, inputParameter));
		}
		return json;
	}

	@Override
	public Json executeRpcInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j=new Json();
		return j;
	}

	@Override
	public Json executeWebServiceInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j=new Json();
		
		
		
		return j;
	}

	@Override
	public Json executeRestInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j=new Json();
		return j;
	}

}
