package com.desmart.desmartsystem.util;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpClientCallSoapUtil {
	static int socketTimeout = 60000;// 请求超时时间
	static int connectTimeout = 60000;// 传输超时时间
	static Logger logger = Logger.getLogger(HttpClientCallSoapUtil.class);

	/**
	 * 使用SOAP1.1发送消息
	 * 
	 * @param postUrl
	 * @param soapXml
	 * @param soapAction
	 * @return
	 */
	public static JSONObject doPostSoap1_1(String postUrl, String soapXml,
			String soapAction,String userName,String password) {
		String retStr = "";
		JSONObject object = new JSONObject();
		try {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(AuthScope.ANY,
	                new UsernamePasswordCredentials(userName, password));
			// HttpClient
	        CloseableHttpClient closeableHttpClient = HttpClients.custom()
	                .setDefaultCredentialsProvider(credsProvider)
	                .build();
			HttpPost httpPost = new HttpPost(postUrl);
	                //  设置请求和传输超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();
			httpPost.setConfig(requestConfig);
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			httpPost.setHeader("SOAPAction", soapAction);
			StringEntity data = new StringEntity(soapXml,
					Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient
					.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			object.put("statusCode",statusCode);
			
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				object.put("responseResult", retStr);
				logger.info("response:" + retStr);
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exception in doPostSoap1_1", e);
		}
		return object;
	}

	/**
	 * 使用SOAP1.2发送消息
	 * 
	 * @param postUrl
	 * @param soapXml
	 * @param soapAction
	 * @return
	 */
	public static String doPostSoap1_2(String postUrl, String soapXml,
			String soapAction) {
		String retStr = "";
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(postUrl);
                // 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
		httpPost.setConfig(requestConfig);
		try {
			httpPost.setHeader("Content-Type",
					"application/soap+xml;charset=UTF-8");
			httpPost.setHeader("SOAPAction", soapAction);
			StringEntity data = new StringEntity(soapXml,
					Charset.forName("UTF-8"));
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient
					.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println(retStr);
				logger.info("response:" + retStr);
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			logger.error("exception in doPostSoap1_2", e);
		}
		return retStr;
	}

	public static void main(String[] args) {
		
		String querySoapXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">\n" + 
				"   <soapenv:Header/>\n" + 
				"   <soapenv:Body>\n" + 
				"      <urn:ZIFSD_TBPM_LIFNR>\n" + 
				"         <!--You may enter the following 2 items in any order-->\n" + 
				"         <ITB_IN>\n" + 
				"            <!--Zero or more repetitions:-->\n" + 
				"            <item>\n" + 
				"               <PNO>20180810_000024</PNO>\n" + 
				"			<NAME1>南昌市东湖区迪卡乐娱乐休闲中心</NAME1>\n" + 
				"			<NAME2>AA01-江西省南昌市江大南路店</NAME2>\n" + 
				"			<SORT1>AA01-南昌市东湖区迪卡乐娱乐休闲中心</SORT1>\n" + 
				"			<SORT2>AA01-江西省南昌市江大南路店</SORT2>\n" + 
				"			<BUKRS>3500</BUKRS>\n" + 
				"			<VKORG>3500</VKORG>\n" + 
				"			<KTOKK>9002</KTOKK>\n" + 
				"			<EKORG>1000</EKORG>\n" + 
				"			<LOCLB>X001</LOCLB>\n" + 
				"			<NAMEV>杨晓霞</NAMEV>\n" + 
				"			<NAME1_GP>13907093150</NAME1_GP>\n" + 
				"			<STREET>江西省南昌市青山湖区湖坊镇江大南路193号</STREET>\n" + 
				"			<REGION>024</REGION>\n" + 
				"			<KVERM>网点拓展部-张训成</KVERM>\n" + 
				"			<TEL_NUMBER>13907093150</TEL_NUMBER>\n" + 
				"			<FAX_NUMBER></FAX_NUMBER>\n" + 
				"			<BANKS>CN</BANKS>\n" + 
				"			<BANKL></BANKL>\n" + 
				"			<BANKA>招商银行南昌北京西路支行</BANKA>\n" + 
				"			<BANKN>1</BANKN>\n" + 
				"			<KOINH>杨晓霞_4100627917666969</KOINH>\n" + 
				"			<AKONT>22020200</AKONT>\n" + 
				"			<ZTERM>0001</ZTERM>\n" + 
				"			<ZWELS>T</ZWELS>\n" + 
				"			<WAERS>CNY</WAERS>\n" + 
				"			<QSZDT>20180506</QSZDT>\n" + 
				"			<ZINDT>20180506</ZINDT>\n" + 
				"			<DATLZ>20210505</DATLZ>\n" + 
				"			<CERDT></CERDT>\n" + 
				"			<LTYPE>1</LTYPE>\n" + 
				"            </item>\n" + 
				"         </ITB_IN>\n" + 
				"      </urn:ZIFSD_TBPM_LIFNR>\n" + 
				"   </soapenv:Body>\n" + 
				"</soapenv:Envelope>";
		String postUrl = "http://10.1.0.102:50300/XISOAPAdapter/MessageServlet?senderParty=&senderService=BC_TBPM&receiverParty=&receiverService=&interface=SIO_TBPM_ZIFSD_TBPM_LIFNR&interfaceNamespace=http://laiyifen.com/xi/TBPM";
		String str = doPostSoap1_1(postUrl, querySoapXml,"","rfc_user","password").getString("responseResult");
		System.out.println(str);
	}
}
