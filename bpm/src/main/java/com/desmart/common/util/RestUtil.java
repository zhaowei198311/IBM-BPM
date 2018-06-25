package com.desmart.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.util.http.HttpClientUtils;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

public class RestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RestUtil.class);
    
    private BpmGlobalConfig bpmGlobalConfig;
    private CloseableHttpClient httpClient;
    private HttpClientContext context;
    
    /**
     * 构造方法，使用配置中的用户名密码
     * @param bpmGlobalConfig
     */
    public RestUtil(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
        httpClient = HttpClients.custom().build();
        context = HttpClientContext.create();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials(bpmGlobalConfig.getBpmAdminName(), bpmGlobalConfig.getBpmAdminPsw());
        credsProvider.setCredentials(AuthScope.ANY, credentials);
        context.setCredentialsProvider(credsProvider);
        
    }
    
    /** 
     * 关闭连接释放资源
     */
    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.error("连接关闭出错");
            }
        }
    }
    
    /**
     * 调用Get方法
     * @param url  访问的路径
     * @param params  如果有参数采用Map传递，没有参数传递new HashMap<String, Object>() 或 null
     * @return
     */
    public HttpReturnStatus doGet(String url, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Iterator iterator = params.keySet().iterator();
        String args;
        while(iterator.hasNext()) {
            args = (String)iterator.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }
        
        CloseableHttpResponse response = null;
        try {
            args = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
            if (StringUtils.isNotBlank(args)) {
                if (url.indexOf("?") == -1) {
                    url = url + "?" + args;
                } else {
                    url = url.endsWith("&") ? url : url + "&";
                    url = url + args;
                }
            }
            HttpGet httpGet = new HttpGet(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpGet.setConfig(reqcfg);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Content-Language", "zh-CN");
            response = httpClient.execute(httpGet, context);
            
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            LOG.info(msg);
        } catch (ParseException | IOException e) {
            LOG.error("GET请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 调用Post请求，请求正文是json类型
     * @param url
     * @param postContent
     * @return
     */
    public HttpReturnStatus doPost(String url, String postContent) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new StringEntity(postContent, "UTF-8"));
            response = httpClient.execute(httpPost, context);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
        } catch (Exception e) {
            LOG.error("POST请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    
    

    /**
     * 调用PUT请求，请求正文是json类型
     * @param url
     * @param postContent
     * @return
     */
    public HttpReturnStatus doPut(String url, String postContent) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPut httpPut = new HttpPut(url);
        CloseableHttpResponse response = null;

        try {
            httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            httpPut.setEntity(new StringEntity(postContent, "UTF-8"));
            response = httpClient.execute(httpPut, context);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
        } catch (Exception e) {
            LOG.error("PUT请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
    
    /**
     * 调用DELETE方法
     * @param url
     * @return
     */
    public HttpReturnStatus doDel(String url) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpDelete httpDelete = new HttpDelete(url);
        CloseableHttpResponse response = null;

        try {
            httpDelete.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-Language", "zh-CN");
            response = httpClient.execute(httpDelete, context);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
        } catch (Exception e) {
            LOG.error("DELETE请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
    
    
    
    /**
     * sendPost 方法
     * @param url  访问的路径
     * @param params  如果有参数采用Map传递，没有参数传递new HashMap<String, Object>() 或 null
     * @return
     */
    public HttpReturnStatus sendPost(String url, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Iterator iterator = params.keySet().iterator();
        String args;
        while(iterator.hasNext()) {
            args = (String)iterator.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }
        
        CloseableHttpResponse response = null;
        try {
            args = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
            if (StringUtils.isNotBlank(args)) {
                if (url.indexOf("?") == -1) {
                    url = url + "?" + args;
                } else {
                    url = url.endsWith("&") ? url : url + "&";
                    url = url + args;
                }
            }
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            response = httpClient.execute(httpPost, context);
            
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            LOG.info(msg);
        } catch (ParseException | IOException e) {
            LOG.error("Post请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    
    /**
     * sendPut 方法
     * @param url  访问的路径
     * @param params  如果有参数采用Map传递，没有参数传递new HashMap<String, Object>() 或 null
     * @return
     */
    public HttpReturnStatus sendPut(String url, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Iterator iterator = params.keySet().iterator();
        String args;
        while(iterator.hasNext()) {
            args = (String)iterator.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }
        
        CloseableHttpResponse response = null;
        try {
            args = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
            if (StringUtils.isNotBlank(args)) {
                if (url.indexOf("?") == -1) {
                    url = url + "?" + args;
                } else {
                    url = url.endsWith("&") ? url : url + "&";
                    url = url + args;
                }
            }
            HttpPut httpPut = new HttpPut(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPut.setConfig(reqcfg);
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            response = httpClient.execute(httpPut, context);
            
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            LOG.info(msg);
        } catch (ParseException | IOException e) {
            LOG.error("Put请求发生错误！", e);
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 调用PUT请求
     * @param request
     * @param url
     * @param params
     * @return
     */
    public HttpReturnStatus doPut(String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        String debugMsg = "";
        HttpPut httpPut = new HttpPut(url);
        RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
        httpPut.setConfig(reqcfg);
        List<NameValuePair> nvParams = new ArrayList();
        Iterator var10 = params.keySet().iterator();

        String withName;
        while(var10.hasNext()) {
            withName = (String)var10.next();
            debugMsg = debugMsg + withName + "=" + params.get(withName).toString() + ", ";
            nvParams.add(new BasicNameValuePair(withName, params.get(withName).toString()));
        }

        try {
            httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            httpPut.setEntity(new UrlEncodedFormEntity(nvParams, Charsets.UTF_8));
            CloseableHttpResponse response = this.httpClient.execute(httpPut, context);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
        } catch (Exception var13) {
            LOG.error("put请求失败！", var13);
        }

        return result;
    }
    
}
