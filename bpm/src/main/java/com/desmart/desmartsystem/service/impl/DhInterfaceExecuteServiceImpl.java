package com.desmart.desmartsystem.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.util.UUIDTool;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartsystem.dao.DhInterfaceLogMapper;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.entity.DhInterfaceLog;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.enums.InterfaceParameterType;
import com.desmart.desmartsystem.enums.InterfaceType;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
import com.desmart.desmartsystem.service.DhInterfaceService;
import com.desmart.desmartsystem.task.SAPConn;
import com.desmart.desmartsystem.util.DateFmtUtils;
import com.desmart.desmartsystem.util.HttpClientCallSoapUtil;
import com.desmart.desmartsystem.util.HttpRequestUtils;
import com.desmart.desmartsystem.util.Json;
import com.desmart.desmartsystem.util.TestXML;
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

	@Autowired
	private DhInterfaceLogMapper dhInterfaceLogMapper;

	@Override
	public Json interfaceSchedule(JSONObject jsonObject) throws Exception {
		Json json = new Json();

		String intUid = jsonObject.getString("intUid");
		if (StringUtils.isBlank(intUid)) {
			json.setSuccess(false);
			json.setMsg("接口未找到");
			return json;
		}

		DhInterface dhInterface = dhInterfaceService.getByIntUid(intUid);
		jsonObject.put("dhinterface", dhInterface);
		String intType = dhInterface.getIntType();

		String intCallMethod = dhInterface.getIntCallMethod(); // 执行方法
		String intLoginPwd = dhInterface.getIntLoginPwd(); // 密码
		String intLoginUser = dhInterface.getIntLoginUser();// 用户名
		String intUrl = dhInterface.getIntUrl();// 接口Url

		String requestXml = dhInterface.getIntRequestXml(); // 请求xml
		String responseXml = dhInterface.getIntResponseXml();// 响应xml

		String status = dhInterface.getIntStatus();
		// 判断接口是否为停用状态
		if (status.equals(Const.INATERFACE_DISABLED_STATUS)) {
			json.setSuccess(false);
			json.setMsg("接口为停用状态！");
			return json;
		}

		JSONObject inputParameter = (JSONObject) jsonObject.getJSONObject("inputParameter");

		// 1.获取参数的配置信息
		DhInterfaceParameter iptParameter = new DhInterfaceParameter();
		iptParameter.setIntUid(intUid);
		iptParameter.setParaInOut(InterfaceParameterType.INPUT.getCode());
		List<DhInterfaceParameter> interfaceParameterItem = dhInterfaceParameterService.byQueryParameter(iptParameter);

		// 2.获取传入的参数 根据配置进行验证
		Set<String> parameterSet = inputParameter.keySet();
		Iterator<String> parameterIter = parameterSet.iterator();
		while (parameterIter.hasNext()) {
			String key = (String) parameterIter.next();
			String value = inputParameter.getString(key);
			for (DhInterfaceParameter dhInterfaceParameter : interfaceParameterItem) {
				String paraName = dhInterfaceParameter.getParaName();
				String para_intUid = dhInterfaceParameter.getIntUid();
				String paraDescription = dhInterfaceParameter.getParaDescription();
				String isMust = dhInterfaceParameter.getIsMust();

				String paraType = dhInterfaceParameter.getParaType();
				String dateFmt = dhInterfaceParameter.getDateFormat();

				if (key.equals(paraName) && para_intUid.equals(intUid)) {// 根据传入参数名和接口Uid获取参数对应的配置信息
					if (isMust.equals("true")) {
						if (StringUtils.isBlank(value)) {
							json.setSuccess(false);
							json.setMsg(paraDescription + "不能为空");
							return json;
						} else {
							Integer paraSize = dhInterfaceParameter.getParaSize();
							if (paraSize < value.getBytes().length
									&& paraType.equals(InterfaceParameterType.STRING.getCode())) {
								json.setSuccess(false);
								json.setMsg(paraDescription + "参数长度过长!");
								return json;
							}
						}
					}
					if (paraType.equals(InterfaceParameterType.DATE.getCode())) {
						if (StringUtils.isNoneBlank(value)) {
							if (!DateFmtUtils.isValidDate(value, dateFmt)) {
								json.setSuccess(false);
								json.setMsg(value + "不是正确的日期格式！");
								return json;
							}
						}
					}

					// 判断如果是integer double
					if (paraType.equals(InterfaceParameterType.INTEGER.getCode())
							|| paraType.equals(InterfaceParameterType.DOUBLE.getCode())) {
						if (StringUtils.isNoneBlank(value)) {
							if (!StringUtils.isNumeric(value)) {
								json.setSuccess(false);
								json.setMsg(value + "不是正确的数字类型!");
								return json;
							}
						}
					}
				}
			}
		}

		// 接口类型
		if (intType.equals(InterfaceType.RPC.getCode())) {
			SAPConn.sapConfiguration(intUrl, intLoginUser, intLoginPwd);
			JCoFunction function = null;
			JCoDestination destination = SAPConn.connect();
			function = destination.getRepository().getFunction(intCallMethod);
			JCoParameterList input = function.getImportParameterList();
			Set<String> parameters = inputParameter.keySet();
			Iterator<String> iterator = parameters.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String paraType = "";
				for (DhInterfaceParameter dhInterfaceParameter : interfaceParameterItem) {
					String paraName = dhInterfaceParameter.getParaName();
					String intUidNew = dhInterfaceParameter.getIntUid();
					if (paraName.equals(key) && intUidNew.equals(intUid)) {
						paraType = dhInterfaceParameter.getParaType();
					}
				}
				if (!paraType.equals(InterfaceParameterType.ARRAY.getCode())) {
					String value = inputParameter.getString(key);
					input.setValue(key, value);
				} else {// array参数拼装
					JCoTable fields = function.getTableParameterList().getTable(key);
					String value = inputParameter.getString(key);
					JSONArray jsonArray = JSON.parseArray(value);
					fields.appendRows(jsonArray.size());
					for (int i = 0; i < jsonArray.size(); i++) {
						fields.setRow(i);
						JSONObject jsonObjectA = JSONObject.parseObject(jsonArray.getString(i));
						for (Map.Entry<String, Object> entry : jsonObjectA.entrySet()) {
							fields.setValue(entry.getKey(), entry.getValue());
						}
					}
				}
			}
			function.execute(destination);

			JCoParameterList tblexport = function.getTableParameterList();
			// 查询指定接口下输出类型的参数
			DhInterfaceParameter dfParameter = new DhInterfaceParameter();
			dfParameter.setIntUid(intUid);
			dfParameter.setParaInOut(InterfaceParameterType.OUTPUT.getCode());
			List<DhInterfaceParameter> dhInterfaceParameterList = dhInterfaceParameterService
					.byQueryParameter(dfParameter);
			JSONArray jsonArray = new JSONArray();
			// 参数拼接
			for (DhInterfaceParameter dhInterfaceParameter : dhInterfaceParameterList) {
				// 参数类型
				String paraType = dhInterfaceParameter.getParaType();
				// 参数名称
				String paraName = dhInterfaceParameter.getParaName();
				// 参数UId
				String paraUid = dhInterfaceParameter.getParaUid();
				if (paraType.equals(InterfaceParameterType.ARRAY.getCode())) {
					JCoTable getTable1 = tblexport.getTable(paraName);
					boolean loopFlag1 = !getTable1.isEmpty(); // 判断 这张表中有木有数据
					while (loopFlag1) {
						JSONObject jSONObject = new JSONObject();
						// 得到返回参数填入
						for (DhInterfaceParameter ifl : dhInterfaceParameterList) {
							String iflParaParent = ifl.getParaParent();
							if (StringUtils.isNoneBlank(iflParaParent)) {
								if (iflParaParent.equals(paraUid)) {// 获取Array子参数
									String iflParaName = ifl.getParaName();
									String value = getTable1.getString(iflParaName);
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
		} else if (intType.equals(InterfaceType.WEBSERVICE.getCode())) {
			String soapRequestData = XmlParsing.getSaopParameter(requestXml, inputParameter); // soap协议的格式，定义了方法和参数
			String xml = HttpClientCallSoapUtil.doPostSoap1_1(intUrl, soapRequestData, "", intLoginUser, intLoginPwd);
			// 返回参数格式拼接
			List<String> responseConfig = new ArrayList<String>();
			TestXML.testGetRoot(responseXml, responseConfig);
			json.setMsg(XmlToJsonUtils.xmlToJson(xml, responseConfig));

		} else if (intType.equals(InterfaceType.RESTAPI.getCode())) {
			json.setSuccess(true);
			json.setMsg(HttpRequestUtils.httpPost(intUrl + intCallMethod, inputParameter));
		}

		DhInterfaceLog dhInterfaceLog = new DhInterfaceLog();
		dhInterfaceLog.setDilUid("dhinterfacelog:" + UUIDTool.getUUID());
		dhInterfaceLog.setCreatedate(new Date());
		dhInterfaceLog.setCreateuser("interfaceAdmin");
		dhInterfaceLog.setDilRequest(inputParameter.toJSONString());
		dhInterfaceLog.setDilResponse(json.getMsg());
		dhInterfaceLog.setIntUid(intUid);
		dhInterfaceLogMapper.insert(dhInterfaceLog);
		return json;
	}

	@Override
	public Json executeRpcInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j = new Json();
		return j;
	}

	@Override
	public Json executeWebServiceInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j = new Json();

		return j;
	}

	@Override
	public Json executeRestInterface(JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		Json j = new Json();
		return j;
	}

}
