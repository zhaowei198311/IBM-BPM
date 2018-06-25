package com.desmart.desmartsystem.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Dom4j {
	
	public static Object parse(Element root) {
		List<?> elements = root.elements();
		if (elements.size() == 0) {
			// 没有子元素
			return root.getTextTrim();
		} else {
			// 有子元素
			String prev = null;
			boolean guess = true; // 默认按照数组处理

			Iterator<?> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Element elem = (Element) iterator.next();
				String name = elem.getName();
				if (prev == null) {
					prev = name;
				} else {
					guess = name.equals(prev);
					break;
				}
			}
			iterator = elements.iterator();
			if (guess) {
				List<Object> data = new ArrayList<Object>();
				while (iterator.hasNext()) {
					Element elem = (Element) iterator.next();
					((List<Object>) data).add(parse(elem));
				}
				return data;
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				while (iterator.hasNext()) {
					Element elem = (Element) iterator.next();
					((Map<String, Object>) data).put(elem.getName(), parse(elem));
				}
				return data;
			}
		}
	}

	public static void main(String[] args) throws Throwable {
//		SAXReader reader = new SAXReader();
//		InputStream in = Dom4j.class.getResourceAsStream("toolbox.xml");
//		Document document = reader.read(in);
		
		
		String xml="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <soapenv:Body>\n" + 
				"      <sendOrganizationResponse xmlns=\"http://webservice.lyfwebservice.ibm.com\">\n" + 
				"         <sendOrganizationReturn>\n" + 
				"            <org>\n" + 
				"               <org>\n" + 
				"                  <changeType/>\n" + 
				"                  <deplead>00000000</deplead>\n" + 
				"                  <displayName>门店</displayName>\n" + 
				"                  <lyfSupervisoryDepartment>10003340</lyfSupervisoryDepartment>\n" + 
				"                  <name>上海市浦东新区乐购锦绣路店</name>\n" + 
				"                  <o>10014975</o>\n" + 
				"                  <supdepname>上海区域009</supdepname>\n" + 
				"               </org>\n" + 
				"               <org>\n" + 
				"                  <changeType/>\n" + 
				"                  <deplead>00000000</deplead>\n" + 
				"                  <displayName>门店</displayName>\n" + 
				"                  <lyfSupervisoryDepartment>10003050</lyfSupervisoryDepartment>\n" + 
				"                  <name>上海市普陀区汇融天地曹杨路店</name>\n" + 
				"                  <o>10015038</o>\n" + 
				"                  <supdepname>上海区域093</supdepname>\n" + 
				"               </org>\n" + 
				"               <org>\n" + 
				"                  <changeType/>\n" + 
				"                  <deplead>00000000</deplead>\n" + 
				"                  <displayName>门店</displayName>\n" + 
				"                  <lyfSupervisoryDepartment>10003370</lyfSupervisoryDepartment>\n" + 
				"                  <name>上海市嘉定区江桥万达广场金沙江西路店</name>\n" + 
				"                  <o>10015122</o>\n" + 
				"                  <supdepname>上海区域010</supdepname>\n" + 
				"               </org>\n" + 
				"            </org>\n" + 
				"            <result>sync success!</result>\n" + 
				"         </sendOrganizationReturn>\n" + 
				"      </sendOrganizationResponse>\n" + 
				"   </soapenv:Body>\n" + 
				"</soapenv:Envelope>";
		
		
		Document document = DocumentHelper.parseText(xml); 
		Element root = document.getRootElement();

		Object obj = parse(root); // 返回类型未知，已知DOM结构的时候可以强制转换
		
		
		JSONObject object = (JSONObject) JSON.toJSON(obj);
		
		System.out.println(object.get("result"));
		
		
		System.out.println(JSON.toJSON(obj)); // 打印JSON
	}
}
