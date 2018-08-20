package com.desmart.desmartsystem.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.json.XML;

import com.alibaba.fastjson.JSONObject;

public class XmlParsing {
	

	/**
	 * 格式化XML文档
	 * 
	 * @param document
	 *            xml文档
	 * @param charset
	 *            字符串的编码
	 * @param istrans
	 *            是否对属性和元素值进行转移
	 * @return 格式化后XML字符串
	 */
	public static String formatXml(Document document, String charset, boolean istrans) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(charset);
		format.setIndentSize(2);
		format.setNewlines(true);
		format.setTrimText(false);
		format.setPadText(true);
		// 以上4行用于处理base64图片编码以后放入xml时的回车变空格问题
		StringWriter sw = new StringWriter();
		XMLWriter xw = new XMLWriter(sw, format);
		xw.setEscapeText(istrans);
		try {
			xw.write(document);
			xw.flush();
			xw.close();
		} catch (IOException e) {
			System.out.println("格式化XML文档发生异常，请检查！");
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public static String getSaopParameter(String xml,JSONObject jsonObject) throws UnsupportedEncodingException, DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		getNodes(root,jsonObject);
		System.out.println(document.asXML());
		String resultXMl=formatXml(document, "utf-8", false);
		return resultXMl;
	}

	public static String json2xml(String json) {
		org.json.JSONObject jsonObj = new org.json.JSONObject(json);
        return XML.toString(jsonObj);
    }

	/**
	 * @param node
	 * @param jsonObject
	 * @throws DocumentException
	 */
	public static void getNodes(Element node, JSONObject jsonObject) throws DocumentException {
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			e.setText("");
			String nodeName = e.getQualifiedName();
			String nodeValue = jsonObject.getString(nodeName);
			int elementsSize = e.elements().size();
			if (StringUtils.isNotBlank(nodeValue)) {
				jsonObject.remove(nodeName);
				if (elementsSize > 0) {// 表示有子元素
					Element parentElement = e.getParent();
					Element itemElement = e.element("item");
					String paraJson =nodeValue;
					parentElement.remove(e);
					if(itemElement!=null) {
						paraJson = "{\"" + nodeName + "\":{\"item\":" + nodeValue + "}}";
					}else {
						paraJson = "{\"" + nodeName + "\":" + nodeValue + "}";
					}
					parentElement.setText(json2xml(paraJson));
				} else {
					e.setText(nodeValue);
				}
			}
			if (elementsSize > 0) {
				getNodes(e, jsonObject); // 递归
			}

		}
	}
	
	

}
