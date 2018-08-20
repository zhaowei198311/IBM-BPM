package com.desmart.desmartsystem.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
	
	public static String xmlToJson(String xml,List<String> responseConfig) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			JSONObject json = new JSONObject();
			dom4j2Json(doc.getRootElement(), json,responseConfig);
			return json.toJSONString();
		} catch (DocumentException e) {
			logger.warn("数据解析失败");
		}
		return null;

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