package com.desmart.desmartsystem.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.desmart.desmartsystem.task.SAPConn;
import com.desmart.desmartsystem.util.HttpClientCallSoapUtil;
import com.desmart.desmartsystem.util.HttpRequestUtils;
import com.desmart.desmartsystem.util.Json;
import com.desmart.desmartsystem.util.XmlFormat;
import com.desmart.desmartsystem.util.XmlParsing;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

@Service
public class DhInterfaceExecuteServiceImpl implements DhInterfaceExecuteService {

	@Autowired
	private DhInterfaceService dhInterfaceService;


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
		String intUrl = dhInterface.getIntUrl();
		String intXml =	dhInterface.getIntXml();
		
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
				String value = jsonObject.getString(key);
				input.setValue(key, value);
			}
			function.execute(destination);
			function.getTableParameterList().getFieldIterator();
			JCoParameterList tblexport = function.getTableParameterList();
			JCoTable tab = tblexport.getTable("ITB_OUT");
			json.setSuccess(true);
			json.setMsg(tab.toString());
			json.setObj(tab.toString());
		}else if(intType.equals("webservice")) {
			//SAPConn.sapConfiguration(intUrl, intLoginUser, intLoginPwd);
			String soapRequestData =XmlParsing.getSaopParameter(intXml,inputParameter); //soap协议的格式，定义了方法和参数
			String retStr =HttpClientCallSoapUtil.doPostSoap1_2(intUrl, soapRequestData, "");
			json.setSuccess(true);
			json.setMsg(XmlFormat.format(retStr));
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
