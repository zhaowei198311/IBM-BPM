package com.desmart.desmartsystem.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class XmlToJsonUtils {
	private static Logger logger = Logger.getLogger(XmlToJsonUtils.class);

	
	/**
	 * xml转json
	 *
	 * @param element
	 * @param json
	 */
	public static void dom4j2Json(Element element, JSONObject json,List<String> responseConfig) {
		List<Element> chdEl = element.elements();
		if (chdEl.isEmpty() && !isEmpty(element.getText())) {// 如果没有子元素,只有一个值\
			json.put(element.getName(), element.getText());
		}
		
		for (Element e : chdEl) {// 有子元素
			if (!e.elements().isEmpty()) {// 子元素也有子元素
				JSONObject chdjson = new JSONObject();
				
				if(responseConfig.contains(e.getName())==false) {
					dom4j2Json(e, json,responseConfig);
					continue;
				}else {
					dom4j2Json(e, chdjson,responseConfig);
				}
				
				Object o = json.get(e.getName());
				if (o != null) {
					JSONArray jsona = null;
					if (o instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
						JSONObject jsono = (JSONObject) o;
						json.remove(e.getName());
						jsona = new JSONArray();
						jsona.add(jsono);
						jsona.add(chdjson);
					}
					if (o instanceof JSONArray) {
						jsona = (JSONArray) o;
						jsona.add(chdjson);
					}
					json.put(e.getName(), jsona);
				} else {
					if (!chdjson.isEmpty()) {
						json.put(e.getName(), chdjson);
					}
				}
			} else {// 子元素没有子元素
				
				for (Object o : element.attributes()) {
					Attribute attr = (Attribute) o;
					if (!isEmpty(attr.getValue())) {
						json.put("@" + attr.getName(), attr.getValue());
					}
				}
				if (!e.getText().isEmpty()) {
					
					if(responseConfig.contains(e.getName())==false) {
						continue;
					}
					
					json.put(e.getName(), e.getText());
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		// String xmlStr= readFile("D:/ADA/et/Issue_20130506_back.xml");
		String xmlStr = "<auth_response status=\"OK\" xmlns=\"http://www.fnac.com/schemas/mp-dialog.xsd\">"
				+ "<token>00E9822E-E926-C274-53BF-A2784195A3CE</token>"
				+ "<validity>2018-03-23T10:45:21+01:00</validity>" + "<version>2.6.0</version>" + "</auth_response>";
		String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "   <soapenv:Body>\n"
				+ "      <sendOrganizationResponse xmlns=\"http://webservice.lyfwebservice.ibm.com\">\n"
				+ "         <sendOrganizationReturn>\n" + "            <org>\n" + "               <org>\n"
				+ "                  <changeType/>\n" + "                  <deplead>00000000</deplead>\n"
				+ "                  <displayName>门店</displayName>\n"
				+ "                  <lyfSupervisoryDepartment>10003340</lyfSupervisoryDepartment>\n"
				+ "                  <name>上海市浦东新区乐购锦绣路店</name>\n" + "                  <o>10014975</o>\n"
				+ "                  <supdepname>上海区域009</supdepname>\n" + "               </org>\n"
				+ "               <org>\n" + "                  <changeType/>\n"
				+ "                  <deplead>00000000</deplead>\n"
				+ "                  <displayName>门店</displayName>\n"
				+ "                  <lyfSupervisoryDepartment>10003050</lyfSupervisoryDepartment>\n"
				+ "                  <name>上海市普陀区汇融天地曹杨路店</name>\n" + "                  <o>10015038</o>\n"
				+ "                  <supdepname>上海区域093</supdepname>\n" + "               </org>\n"
				+ "               <org>\n" + "                  <changeType/>\n"
				+ "                  <deplead>00000000</deplead>\n"
				+ "                  <displayName>门店</displayName>\n"
				+ "                  <lyfSupervisoryDepartment>10003370</lyfSupervisoryDepartment>\n"
				+ "                  <name>上海市嘉定区江桥万达广场金沙江西路店</name>\n" + "                  <o>10015122</o>\n"
				+ "                  <supdepname>上海区域010</supdepname>\n" + "               </org>\n"
				+ "            </org>\n" + "            <result>sync success!</result>\n"
				+ "         </sendOrganizationReturn>\n" + "      </sendOrganizationResponse>\n"
				+ "   </soapenv:Body>\n" + "</soapenv:Envelope>";
		//JSONObject jsonObject=xml2Json(xml);
		//System.out.println(jsonObject);
	}

	public static String xmlToJson(String xml,List<String> responseConfig) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			JSONObject json = new JSONObject();
			dom4j2Json(doc.getRootElement(), json,responseConfig);
//			System.out.println("xml2Json:" + json.toJSONString());
//			logger.warn("xml2Json:" + json.toJSONString());
			return json.toJSONString();
		} catch (DocumentException e) {
			logger.warn("数据解析失败");
		}
		return null;

	}

	public static String readFile(String path) throws Exception {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		FileChannel fc = fis.getChannel();
		ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
		// fc向buffer中读入数据
		fc.read(bb);
		bb.flip();
		String str = new String(bb.array(), "UTF8");
		fc.close();
		fis.close();
		return str;

	}

	/**
	 * xml转json
	 *
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	public static JSONObject xml2Json(String xmlStr,List<String> responseConfig) throws DocumentException {
		Document doc = DocumentHelper.parseText(xmlStr);
		JSONObject json = new JSONObject();
		dom4j2Json(doc.getRootElement(), json,responseConfig);
		return json;
	}

	

	public static boolean isEmpty(String str) {

		if (str == null || str.trim().isEmpty() || "null".equals(str)) {
			return true;
		}
		return false;
	}
}