package com.desmart.desmartsystem.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.desmart.desmartsystem.dao.SysCostCenterMapper;
import com.desmart.desmartsystem.dao.SysProfitCenterMapper;
import com.desmart.desmartsystem.entity.SysCostCenter;
import com.desmart.desmartsystem.entity.SysProfitCenter;

public class CompanySynchronousTask {

	@Autowired
	private SysCostCenterMapper sysCostCenterMapper;

	@Autowired
	private SysProfitCenterMapper sysProfitCenterMapper;

	// 同步成本中心
	@Test
	public void costCenter() {
		try {
			// 添加成本中心
			List<SysCostCenter> sysCostCenterList = parsecostCenterInfoStream(getCostCenterStream());
			for (SysCostCenter sysCostCenter : sysCostCenterList) {
				sysCostCenterMapper.insert(sysCostCenter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 同步利润中心
	@Test
	public void profitCenter() {
		try {
			List<SysProfitCenter> sysProfitCenterList = parseSapProfitStream(getCostCenterStream());
			for (SysProfitCenter sysProfitCenter : sysProfitCenterList) {
				sysProfitCenterMapper.insert(sysProfitCenter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<SysCostCenter> parsecostCenterInfoStream(InputStream in5) {
		List<SysCostCenter> costCenterList = new ArrayList<SysCostCenter>();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(in5);
			// System.out.println(document.asXML());
			Element root = document.getRootElement();
			Element e1 = root.element("Body");
			Element e2 = e1.element("ZIFZC_KOSTL.Response");
			Element e3 = e2.element("OUT_KOSTL");
			Element e4 = e3.element("item");
			Iterator it1 = e3.elementIterator();
			while (it1.hasNext()) {
				SysCostCenter ccib = new SysCostCenter();
				Element element = (Element) it1.next();
				// 读取一个item标签
				element.getName();
				Element element2 = element.element("KOSTL");
				if (element2 != null && !element2.getText().equals("") && element2.getText().length() == 10) {
					ccib.setCostCenterNo(element2.getText().substring(5));
				} else {
					ccib.setCostCenterNo(element2.getText());
				}
				Element element3 = element.element("LTEXT");
				ccib.setCostCenterName(element3.getText());
				Element element4 = element.element("PRCTR");
				if (element4 != null && !element4.getText().equals("") && element2.getText().length() == 10) {
					ccib.setProfitCenterNo(element4.getText().substring(5));
				} else {
					ccib.setProfitCenterNo(element4.getText());
				}

				costCenterList.add(ccib);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return costCenterList;
	}

	// 成本中心
	private InputStream getCostCenterStream() {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			String path = "http://10.1.0.102:50300/XISOAPAdapter/MessageServlet?senderParty=&senderService=BS_HaiXin_Q&receiverParty=&receiverService=&interface=SIO_ZIFZC_KOSTL&interfaceNamespace=http://posdm.com/xi/XT";
			conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append(
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">");
			sb.append("<soapenv:Header/>");
			sb.append("<soapenv:Body>");
			sb.append("<urn:ZIFZC_KOSTL>");
			sb.append("<OUT_KOSTL>");
			sb.append("<item>");
			sb.append("<KOSTL>1</KOSTL>");
			sb.append("<LTEXT>1</LTEXT>");
			sb.append("<BUKRS>1</BUKRS>");
			sb.append("<PRCTR>1</PRCTR>");
			sb.append("<PTEXT>1</PTEXT>");
			sb.append("</item>");
			sb.append("</OUT_KOSTL>");
			sb.append(" </urn:ZIFZC_KOSTL>");
			sb.append("</soapenv:Body>");
			sb.append("</soapenv:Envelope>");
			String soap = new String(sb);

			byte[] entity = soap.getBytes("utf-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "");
			conn.setRequestProperty("Authorization", "Basic cmZjX3VzZXI6cGFzc3dvcmQ=");
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.getOutputStream().write(entity);
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				in = conn.getInputStream();
			} else {
				System.out.println("连接出错！");
				System.out.println(conn.getResponseMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	// 成本中心接口返回
	private InputStream getShopStream() {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			String path = "http://10.1.0.102:50300/XISOAPAdapter/MessageServlet?senderParty=&senderService=BS_HaiXin_Q&receiverParty=&receiverService=&interface=SIO_ZIFSD_GET_FIELD&interfaceNamespace=http://posdm.com/xi/XT";

			conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append(
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">");
			sb.append("<soapenv:Header/>");
			sb.append("<soapenv:Body>");
			sb.append("<urn:ZIFSD_GET_FIELD>");
			sb.append("<FIELDNAME_IN>WERKS_MD</FIELDNAME_IN>");
			sb.append("<FIELD_TAB_OUT>");
			sb.append("<item>");
			sb.append("<FIELDNAME></FIELDNAME>");
			sb.append("<FIELDVALUE></FIELDVALUE>");
			sb.append("<FIELDDESCRIBE></FIELDDESCRIBE>");
			sb.append("</item>");
			sb.append("</FIELD_TAB_OUT>");
			sb.append("</urn:ZIFSD_GET_FIELD>");
			sb.append("</soapenv:Body>");
			sb.append("</soapenv:Envelope>");
			String soap = new String(sb);

			byte[] entity = soap.getBytes("utf-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "");
			conn.setRequestProperty("Authorization", "Basic cmZjX3VzZXI6cGFzc3dvcmQ=");
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.getOutputStream().write(entity);
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				in = conn.getInputStream();
			} else {
				System.out.println("连接出错！");
				System.out.println(conn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	// 利润中心返回
	private List<SysProfitCenter> parseSapProfitStream(InputStream in2) {
		List<SysProfitCenter> profitInfoList = new ArrayList<SysProfitCenter>();
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(in2);
			Element root = document.getRootElement();
			// System.out.println(root.asXML());
			Element e1 = root.element("Body");
			Element e2 = e1.element("ZIFZC_PRCTR.Response");
			Element e3 = e2.element("OUT_PRCTR");
			Element e4 = e3.element("item");
			Iterator it1 = e3.elementIterator();
			while (it1.hasNext()) {
				SysProfitCenter pci = new SysProfitCenter();
				Element element = (Element) it1.next();
				// 读取一个item标签
				element.getName();
				Element element3 = element.element("LTEXT");
				pci.setProfitCenterName(element3.getText());
				Element element4 = element.element("PRCTR");
				pci.setProfitCenterNo(element4.getText().replaceFirst("^0+", ""));
				profitInfoList.add(pci);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profitInfoList;
	}

	// 利润中心
	private InputStream getSapProfitStream() {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			String path = "http://10.1.0.102:50300/XISOAPAdapter/MessageServlet?senderParty=&senderService=BS_HaiXin_Q&receiverParty=&receiverService=&interface=SIO_ZIFZC_PRCTR&interfaceNamespace=http://posdm.com/xi/XT";
			conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append(
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">");
			sb.append("<soapenv:Header/>");
			sb.append("<soapenv:Body>");
			sb.append(" <urn:ZIFZC_PRCTR>");
			sb.append("<OUT_PRCTR>");
			sb.append("<item>");
			sb.append("<BUKRS>1</BUKRS>");
			sb.append("<BUTXT>1</BUTXT>");
			sb.append("<PRCTR>1</PRCTR>");
			sb.append("<LTEXT>1</LTEXT>");
			sb.append("</item>");
			sb.append("</OUT_PRCTR>");
			sb.append("</urn:ZIFZC_PRCTR>");
			sb.append("</soapenv:Body>");
			sb.append("</soapenv:Envelope>");
			String soap = new String(sb);
			byte[] entity = soap.getBytes("utf-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "");
			conn.setRequestProperty("Authorization", "Basic cmZjX3VzZXI6cGFzc3dvcmQ=");
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.getOutputStream().write(entity);
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				in = conn.getInputStream();
			} else {
				System.out.println("连接出错！");
				System.out.println(conn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}
