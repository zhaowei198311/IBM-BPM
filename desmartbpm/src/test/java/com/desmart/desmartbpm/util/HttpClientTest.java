package com.desmart.desmartbpm.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.util.http.BpmClientUtils;

public class HttpClientTest {

	private BpmGlobalConfig config;
	private CloseableHttpClient httpClient;
	private CookieStore cookieStore;
	
	
	@Before
	public void init() throws Exception {
		config = new BpmGlobalConfig();
		config.setBpmServerHost("http://10.2.7.73:9080/");
		config.setBpmAdminName("deadmin");
		config.setBpmAdminPsw("password");
		config.setGmtTimeZone("GMT");
		config.setBpmformsWebContext("/smartforms");
		config.setBpmClientTimeout(60000);
		config.setConfigStatus("on");
		config.setPreRouteMaxConnection(300);
		
		cookieStore = new BasicCookieStore();
		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		
		ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("j_username", config.getBpmAdminName()));
        nameValuePairs.add(new BasicNameValuePair("j_password", config.getBpmAdminPsw()));
        
        String url = config.getBpmServerHost() + "ProcessPortal/j_security_check";
        HttpPost httpPost = new HttpPost(url);
        //RequestConfig reqcfg = HttpClientUtils.getRequestConfig(config.getBpmClientTimeout());
        RequestConfig reqcfg = RequestConfig.custom().setConnectTimeout(config.getBpmClientTimeout()).setConnectionRequestTimeout(config.getBpmClientTimeout()).setSocketTimeout(config.getBpmClientTimeout()).build();
        httpPost.setConfig(reqcfg);
        
        if (!nameValuePairs.isEmpty()) {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
        }
        HttpContext httpCtx = HttpClientContext.create();
        // 装在 cookies
        CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
        response.close();
        
	}
	
	/**  doGet  */
	@Test
	public void test() {
		BpmClientUtils bpmClientUtils = new BpmClientUtils(config, true);
		String url = "http://10.2.7.73:9080/rest/bpm/wle/v1/process/500";
		Map<String, Object> params = new HashMap<>();
		
		HttpReturnStatus httpReturnStatus = bpmClientUtils.doGet(cookieStore.getCookies().get(0), url, params);
		
	}
	
	
	@Test
	public void simpleTest() throws ClientProtocolException, IOException {
	    CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpClientContext context = HttpClientContext.create();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        

        Credentials credentials = new UsernamePasswordCredentials("deadmin", "passw0rd");
        
        credsProvider.setCredentials(AuthScope.ANY, credentials);
        context.setCredentialsProvider(credsProvider);
        
        HttpGet httpGet = new HttpGet("http://10.0.4.201:9080/rest/bpm/wle/v1/exposed/process");
        CloseableHttpResponse response = httpClient.execute(httpGet, context);
        
        String msg = org.apache.http.util.EntityUtils.toString(response.getEntity(), "UTF-8"); 
        System.out.println(msg);
	}
	
	

}
