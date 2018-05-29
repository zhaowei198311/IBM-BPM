package com.desmart.desmartsystem.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartsystem.dao.SysCompanyMapper;
import com.desmart.desmartsystem.entity.SysCompany;

  
//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试  
//@ContextConfiguration(locations={"classpath:applicationContext.xml"}) //加载配置文件   
//------------如果加入以下代码，所有继承该类的测试类都会遵循该配置，也可以不加，在测试类的方法上///控制事务，参见下一个实例    
//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！    
//@Transactional    
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！    
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)    
//------------    
public class BaseJunit4Test{  
      @Autowired 
      SysCompanyMapper sysCompanyMapper;
      
      @Test
      public void batchInsertUser() {
    	  List<SysCompany> lists=new ArrayList<SysCompany>();
    	  SysCompany cy=new SysCompany();
    	  cy.setCompanyUid("sysCompany:"+UUIDTool.getUUID());
    	  cy.setCompanyCode("qwefqw");
    	  cy.setCompanyName("132");
    	  cy.setCreateDate(new Date());
    	  cy.setEndDate(new Date());
    	  lists.add(cy);
    	  sysCompanyMapper.insertBatch(lists);
      }
      
      
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
  				companyInfoList.add(cib);			
  			}
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  		return companyInfoList;
  	}
        
  	
  	@Test
  	public void  show01() {
  		DhActivityConfAssignType type=DhActivityConfAssignType.codeOf("roleAndDepartment");
  		
  		if(type.equals(DhActivityConfAssignType.ROLE_AND_COMPANY)) {
  			System.out.println(type);
  		}
  		
  		if(type.equals(DhActivityConfAssignType.ROLE_AND_DEPARTMENT)) {
  			System.out.println(type);
  		}
  		
  		
  	}
  	
  	
  	@Test
  	public void companySys() {
  		sysCompanyMapper.insertBatch(parseSapCompanyStream(getSapCompanyStream()));
  	}
      
      
}  