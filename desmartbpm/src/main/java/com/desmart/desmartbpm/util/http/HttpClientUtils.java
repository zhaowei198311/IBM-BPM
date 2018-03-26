package com.desmart.desmartbpm.util.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
    //public static final String CONNECTION_POOL_NAME = Const.HTTP_CLIENT_CONNECTION_POOL;
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

    public HttpClientUtils() {
    }


    public static RequestConfig getRequestConfig(Integer timeout) {
        int itimeout = timeout == null ? '\uea60' : Math.abs(timeout);
        itimeout = itimeout < 3000 ? 3000 : itimeout;
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(itimeout).setConnectionRequestTimeout(itimeout).setSocketTimeout(itimeout).build();
        return requestConfig;
    }

    /**
     * 从httpclient连接池中获取连接
     * @param servletContext servlet上下文
     * @return
     */
    public static CloseableHttpClient getConnectionInPool(ServletContext servletContext) {
        PoolingHttpClientConnectionManager poolmgr = null;
        if (servletContext != null) {
            poolmgr = (PoolingHttpClientConnectionManager)servletContext.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
        }

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolmgr).setConnectionManagerShared(true).build();
        return httpClient;
    }

    public static CloseableHttpClient getConnectionInPool(ServletContext servletContext, CookieStore cookieStore) {
        PoolingHttpClientConnectionManager poolmgr = null;
        if (servletContext != null) {
            poolmgr = (PoolingHttpClientConnectionManager)servletContext.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
        }

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolmgr).setConnectionManagerShared(true)
                .setDefaultCookieStore(cookieStore).build();
        return httpClient;
    }

    public static PoolingHttpClientConnectionManager getConnectionPool(ServletContext servletContext) {
        PoolingHttpClientConnectionManager poolmgr = null;
        if (servletContext != null) {
            poolmgr = (PoolingHttpClientConnectionManager)servletContext.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
        }

        return poolmgr;
    }

    public static HttpReturnStatus doPost(String url, String postContent, List<Exception> exceptions) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig reqcfg = getRequestConfig(10000);
        httpPost.setConfig(reqcfg);
        CloseableHttpClient httpClient = HttpClients.custom().build();

        try {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new StringEntity(postContent, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
        } catch (Exception var9) {
            LOG.error("POST请求发生错误！", var9);
            result.setCode(-1);
            result.setMsg(var9.toString());
            if (exceptions != null) {
                exceptions.add(var9);
            }
        }

        return result;
    }

    public static HttpReturnStatus doPost(String url, String postContent, Map<String, String> headers, List<Exception> exceptions) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig reqcfg = getRequestConfig(10000);
        httpPost.setConfig(reqcfg);
        CloseableHttpClient httpClient = HttpClients.custom().build();

        try {
            Iterator var9 = headers.keySet().iterator();

            while(var9.hasNext()) {
                String head = (String)var9.next();
                httpPost.setHeader(head, (String)headers.get(head));
            }

            httpPost.setEntity(new StringEntity(postContent, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
        } catch (Exception var10) {
            LOG.error("POST请求发生错误！", var10);
            result.setCode(-1);
            result.setMsg(var10.getMessage());
            if (exceptions != null) {
                exceptions.add(var10);
            }
        }

        return result;
    }

    public static HttpReturnStatus doPut(String url, String postContent) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig reqcfg = getRequestConfig(10000);
        httpPut.setConfig(reqcfg);
        CloseableHttpClient httpClient = HttpClients.custom().build();

        try {
            httpPut.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            httpPut.setEntity(new StringEntity(postContent, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPut);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
        } catch (Exception var8) {
            result.setCode(-1);
            result.setMsg(var8.getMessage());
        }

        return result;
    }

    public static void main(String[] args) {
        HttpReturnStatus httpReturnStatus = doDel("http://192.168.2.127:9200/code");
        System.out.println(httpReturnStatus.getMsg());
    }

    public static HttpReturnStatus doDel(String url) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig reqcfg = getRequestConfig(10000);
        httpDelete.setConfig(reqcfg);
        CloseableHttpClient httpClient = HttpClients.custom().build();

        try {
            httpDelete.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-Language", "zh-CN");
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            httpClient.close();
        } catch (Exception var7) {
            ;
        }

        return result;
    }

    public static HttpReturnStatus doGet(String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        Iterator var5 = params.keySet().iterator();

        while(var5.hasNext()) {
            String key = (String)var5.next();
            String val = (String)params.get(key);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(key, val);
            nameValuePairs.add(nkv);
        }

        CloseableHttpClient httpClient = null;

        try {
            String args = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
            if (StringUtils.isNotBlank(args)) {
                if (url.indexOf("?") == -1) {
                    url = url + "?" + args;
                } else {
                    url = url.endsWith("&") ? url : url + "&";
                    url = url + args;
                }
            }

            httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig reqcfg = getRequestConfig(10000);
            httpGet.setConfig(reqcfg);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Content-Language", "zh-CN");
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet, httpCtx);
            LOG.debug("return code: " + httpResponse.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(httpResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            httpResponse.close();
        } catch (Exception var19) {
            LOG.error("get请求失败！", var19);
        } finally {
            try {
                httpClient.close();
            } catch (IOException var18) {
                LOG.error("关闭HttpClient时发生异常！", var18);
            }

        }

        return result;
    }

    public static Map<String, String> getCommonHttpHeaders(String headerType) {
        Map<String, String> headers = new HashMap();
        String htype = StringUtils.isBlank(headerType) ? "json" : headerType.toLowerCase();
        switch(htype.hashCode()) {
            case 118807:
                if (!htype.equals("xml")) {
                    return headers;
                }
                break;
            case 3271912:
                if (htype.equals("json")) {
                    headers.put("Content-Type", "application/json;charset=UTF-8");
                    headers.put("Accept", "application/json");
                    headers.put("Content-Language", "zh-CN");
                    break;
                }

                return headers;
            default:
                return headers;
        }

        headers.put("Content-Type", "text/xml;charset=UTF-8");
        headers.put("Accept", "text/xml");
        headers.put("Content-Language", "zh-CN");
        return headers;
    }
}