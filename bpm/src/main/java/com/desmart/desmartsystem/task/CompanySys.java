package com.desmart.desmartsystem.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartsystem.dao.SysCompanyMapper;
import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.util.UUIDTool;

public class CompanySys {
	public InputStream getSapCompanyStream(){
		HttpURLConnection conn = null;
		InputStream in=null;
		try{
			//测试String path = "http://10.1.0.217/cordys/com.eibus.web.soap.Gateway.wcp?organization=o=laiyifen,cn=cordys,cn=defaultInst1,o=laiyifen";
			String path="http://10.1.0.102:50300/XISOAPAdapter/MessageServlet?senderParty=&senderService=BS_HaiXin_Q&receiverParty=&receiverService=&interface=SIO_ZIFZC_BUKRS&interfaceNamespace=http://posdm.com/xi/XT";
			
			conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			StringBuilder sb=new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">");      
			sb.append("<soapenv:Header/>");      
			sb.append("<soapenv:Body>");  
			sb.append("<urn:ZIFZC_BUKRS>");
			sb.append("<OUT_BUKRS>");
			sb.append("<item>");
			sb.append("<BUKRS>1</BUKRS>");
			sb.append("<BUTXT>1</BUTXT>");
			sb.append("</item>");
			sb.append("</OUT_BUKRS>");
			sb.append("</urn:ZIFZC_BUKRS>");
			sb.append("</soapenv:Body>");
			sb.append("</soapenv:Envelope>");
			String soap =new String(sb);
			
			byte[] entity = soap.getBytes("utf-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "");
			conn.setRequestProperty("Authorization", "Basic cmZjX3VzZXI6cGFzc3dvcmQ=");
			conn.setRequestProperty("Content-Length",String.valueOf(entity.length));
			conn.getOutputStream().write(entity);
			if (conn.getResponseCode() == 200) {
				in = conn.getInputStream();				
			} else {
				System.out.println("连接出错！");
				System.out.println(conn.getResponseMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return in;
	}



	private List <SysCompany> parseSapCompanyStream(InputStream in){
		
		//容器中获取SysCompanyMapper实例
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SysCompanyMapper sysCompanyMapper = wac.getBean(SysCompanyMapper.class);
		
		
		
		Set<String> companySet=new HashSet<String>();
		//查询数据库中已经
		List<SysCompany>  companyList = sysCompanyMapper.selectAll(new SysCompany());
		for (SysCompany sysCompany : companyList) {
			companySet.add(sysCompany.getCompanyCode());
		}
		
		List<SysCompany> intCompanyList=new ArrayList<SysCompany>();
		
		List <SysCompany> companyInfoList = new ArrayList<SysCompany>();
		try{
			SAXReader reader = new SAXReader();
			Document document = reader.read(in);
			document.getRootElement();
			Element root=document.getRootElement();
			Element e1=root.element("Body");
			Element e2=e1.element("ZIFZC_BUKRS.Response");
			Element e3=e2.element("OUT_BUKRS");
			Element e4=e3.element("item");
			Iterator it1=e3.elementIterator();			
			while(it1.hasNext()) {
				SysCompany cib = new SysCompany();
				cib.setCompanyUid("sysCompany:"+UUIDTool.getUUID());
				Element element=(Element)it1.next();
				//读取一个item标签 
				element.getName();
				Element element2=element.element("BUKRS");
				cib.setCompanyCode(element2.getText());
				Element element3=element.element("BUTXT");
				cib.setCompanyName(element3.getText());
				cib.setCreateDate(new Date());
				cib.setEndDate(new Date());
				companyInfoList.add(cib);
				if(!companySet.contains(cib.getCompanyCode())) {
					intCompanyList.add(cib);
				}
			}
			sysCompanyMapper.insertBatch(intCompanyList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return companyInfoList;
	}
	
	//执行同步部门
	public void executeSysCompany() {
		parseSapCompanyStream(getSapCompanyStream());
	}
}
