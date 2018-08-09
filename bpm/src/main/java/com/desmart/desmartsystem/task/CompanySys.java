package com.desmart.desmartsystem.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.desmart.desmartsystem.controller.BaseController;
import com.desmart.desmartsystem.dao.SysCompanyMapper;
import com.desmart.desmartsystem.entity.SysCompany;
import com.desmart.desmartsystem.entity.SysDepartment;
import com.desmart.desmartsystem.util.UUIDTool;

public class CompanySys  implements Job{
	
	private Logger logger = Logger.getLogger(CompanySys.class);
	
	public static InputStream getSapCompanyStream(){
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



	private static List <SysCompany> parseSapCompanyStream(InputStream in){
		
		
		List <SysCompany> companyInfoList = new ArrayList<SysCompany>();
		try{
			SAXReader reader = new SAXReader();
			Document document = reader.read(in);
			System.out.println(document.asXML());
			document.getRootElement();
			Element root=document.getRootElement();
			Element e1=root.element("Body");
			Element e2=e1.element("ZIFZC_BUKRS.Response");
			Element e3=e2.element("OUT_BUKRS");
			Element e4=e3.element("item");
			Iterator it1=e3.elementIterator();			
			while(it1.hasNext()) {
				SysCompany cib = new SysCompany();
				
				Element element=(Element)it1.next();
				//读取一个item标签 
				element.getName();
				Element element2=element.element("BUKRS");
				cib.setCompanyCode(element2.getText());
				Element element3=element.element("BUTXT");
				cib.setCompanyName(element3.getText());
				cib.setCreateDate(new Date());
				cib.setEndDate(new Date());
				System.out.println(element3.getText());
				companyInfoList.add(cib);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return companyInfoList;
	}
	
	//执行同步部门
	public static List<SysCompany> executeSysCompany() {
		return parseSapCompanyStream(getSapCompanyStream());
	}
	
	
	public static void main(String[] args) {
		executeSysCompany();
	}



	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		SysCompanyMapper sysCompanyMapper = wac.getBean(SysCompanyMapper.class);
		
		//查询返回所有公司信息
		logger.error("查询返回所有公司信息");
		Map<String, SysCompany> sysCompanyMap=new HashMap<String, SysCompany>();
		List<SysCompany> sysCompanys  = sysCompanyMapper.selectAll(new SysCompany());
		for (SysCompany sysCompany : sysCompanys) {
			sysCompanyMap.put(sysCompany.getCompanyUid(), sysCompany);
		}
		
		
		//接口返回所有公司信息
		logger.error("接口返回所有公司信息");
		List<SysCompany>  sysCompanyList = executeSysCompany();
		for (SysCompany sysCompany : sysCompanyList) {
			SysCompany company = sysCompanyMap.get(sysCompany.getCompanyUid());
			if(company!=null) {
				sysCompanyMapper.update(company);
			}else {
				sysCompany.setCompanyUid("sysCompany:"+UUIDTool.getUUID());
				sysCompanyMapper.insert(sysCompany);
			}
		}
		
	}
}
