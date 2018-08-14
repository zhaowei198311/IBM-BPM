package com.desmart.desmartsystem.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

class Model{
	private String name;
	private String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

public class XmlParsing {
	
	
	public static void main(String[] args) throws Exception {
		JSONArray josnArray=new JSONArray();
		
		JSONObject jsonObject1=new JSONObject();
		jsonObject1.put("PNO", "PNO1");
		jsonObject1.put("MATNR", "MATNR1");
		jsonObject1.put("KBETR", "KBETR1");
		
		JSONObject jsonObject2=new JSONObject();
		jsonObject2.put("PNO", "PNO2");
		jsonObject2.put("MATNR", "MATNR2");
		jsonObject2.put("KBETR", "KBETR2");
		
		josnArray.add(jsonObject1);
		josnArray.add(jsonObject2);
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("PNO", "222222");
		jsonObject.put("Z_WERKS", "111111");
		jsonObject.put("Z_BUKRS", "333333");
		jsonObject.put("ITB_IN",josnArray);
		String xml="<ZIFSD_TBPM_MATNR\n" + 
				"	xmlns=\"http://schemas.laiyifen.com/saprfcs\">\n" + 
				"	<PNO>20180503_195401</PNO>\n" + 
				"	<Z_FLAG>U</Z_FLAG>\n" + 
				"	<Z_WERKS>AA01</Z_WERKS>\n" + 
				"	<Z_BUKRS>3500</Z_BUKRS>\n" + 
				"	<ITB_IN>\n" + 
				"		<item>\n" + 
				"			<PNO/>\n" + 
				"			<MATNR>17070</MATNR>\n" + 
				"			<WERKS>AA01</WERKS>\n" + 
				"			<KBETR></KBETR>\n" + 
				"			<SDATE>99991231</SDATE>\n" + 
				"			<ZZMMSTA>00</ZZMMSTA>\n" + 
				"			<ZZITARK>X</ZZITARK>\n" + 
				"			<CDATE>20180503</CDATE>\n" + 
				"			<VDATE>20180503</VDATE>\n" + 
				"		</item>\n" + 
				"		<item>\n" + 
				"			<PNO/>\n" + 
				"			<MATNR>17070</MATNR>\n" + 
				"			<WERKS>AA01</WERKS>\n" + 
				"			<KBETR></KBETR>\n" + 
				"			<SDATE>99991231</SDATE>\n" + 
				"			<ZZMMSTA>00</ZZMMSTA>\n" + 
				"			<ZZITARK>X</ZZITARK>\n" + 
				"			<CDATE>20180503</CDATE>\n" + 
				"			<VDATE>20180503</VDATE>\n" + 
				"		</item>\n" + 
				"	</ITB_IN>\n" + 
				"</ZIFSD_TBPM_MATNR>";
		SAXReader sax = new SAXReader();
		Document document = sax.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		Element root = document.getRootElement();
		//getNodes(root,jsonObject);
		
		root.remove(root.element("ITB_IN"));
		System.out.println(document.getDocument().asXML());
	}
	
	
	

	public static String getSaopParameter(String xml,JSONObject jsonObject) throws UnsupportedEncodingException, DocumentException {
		SAXReader sax = new SAXReader();
		Document document = sax.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		Element root = document.getRootElement();
		getNodes(root,jsonObject);
		String resultXMl=document.getDocument().asXML();
		return resultXMl;
	}
	
	public static boolean isJson(String content){
	    try {
	        JSONObject jsonStr= JSONObject.parseObject(content);
	        return  true;
	   } catch (Exception e) {
	        return false;
	  }
	}
	
	
	public static void getNodes(Element node,JSONObject jsonObject){  
	    //当前节点的名称、文本内容和属性  
		String nodeName=node.getName();
		String nodeValue=jsonObject.getString(nodeName);
		node.setText("");
		if(StringUtils.isNotBlank(nodeValue)) {
			if(isJson(nodeValue)) {
				JSONArray josnArray1= (JSONArray) jsonObject.get(nodeName);
				node.remove(node.element(nodeName));
				for (int i = 0; i < josnArray1.size(); i++) {
					JSONObject jsonObj = (JSONObject) josnArray1.get(i);
					Element rootParent=node.addElement(nodeName);
					Element rootItem=rootParent.addElement("item");
					for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
						rootItem.addElement(entry.getKey()).addText(String.valueOf(entry.getValue()));
			        }
				}
			}else {
				node.setText(nodeValue);
			}
		}
	    List<Element> listElement=node.elements();//所有一级子节点的list  
	    for(Element e:listElement){//遍历所有一级子节点  
	        getNodes(e,jsonObject);//递归  
	    }  
	} 
	
	
	
	
}
