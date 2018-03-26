package com.desmart.desmartbpm.util.http;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责与IBM BPM引擎交互的工具类，内部维护了一个CloseableHttpClient
 */
public class BpmClientUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BpmClientUtils.class);
    private CloseableHttpClient httpClient;
    private CookieStore cookieStore;
    private BpmGlobalConfig bpmGlobalConfig;
    private boolean isAdmin;
    private int reLoginNum;

    public BpmClientUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
        this(false, bpmGlobalConfig, isAdmin, false, (ServletContext)null);
    }

    public BpmClientUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, ServletContext servletContext) {
        this(false, bpmGlobalConfig, isAdmin, true, servletContext);
    }

    public BpmClientUtils(boolean requireSSL, BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
        this(requireSSL, bpmGlobalConfig, isAdmin, false, (ServletContext)null);
    }

    /**
     *
     * @param requireSSL  是否需要SSL
     * @param bpmGlobalConfig  流程平台全局配置
     * @param isAdmin  是否是管理员, 管理员的token可以自行获得
     * @param needPoolMgr  是否需要用连接池中的http连接
     * @param servletContext  servlet上下文
     */
    public BpmClientUtils(boolean requireSSL, BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, boolean needPoolMgr,
                          ServletContext servletContext) {
        this.isAdmin = false;
        this.reLoginNum = 0;
        this.isAdmin = isAdmin;
        this.bpmGlobalConfig = bpmGlobalConfig;
        this.cookieStore = new BasicCookieStore();
        String host = bpmGlobalConfig.getBpmServerHost();
        host = StringUtils.isBlank(host) ? "" : host;
        if (StringUtils.isNotBlank(host) && host.startsWith("http")) {
            int port = host.startsWith("https") ? 443 : 80;

            try {
                URL url = new URL(host);
                port = url.getPort() != -1 ? url.getPort() : port;
            } catch (MalformedURLException var10) {
                LOG.error("BPM服务器地址解析失败！\r\n地址：" + host);
            }

            PoolingHttpClientConnectionManager poolmgr = HttpClientUtils.getConnectionPool(servletContext);
            SSLConnectionSocketFactory sslFact;
            if (requireSSL) {
                if (needPoolMgr && poolmgr != null) {
                    sslFact = this.enableSSL(this.httpClient, port);
                    this.httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).setSSLSocketFactory(sslFact).build();
                } else {
                    sslFact = this.enableSSL(this.httpClient, port);
                    this.httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).setSSLSocketFactory(sslFact).build();
                }
            } else if (StringUtils.isNotBlank(host) && host.toLowerCase().startsWith("https")) {
                if (needPoolMgr && poolmgr != null) {
                    sslFact = this.enableSSL(this.httpClient, port);
                    this.httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).setSSLSocketFactory(sslFact).build();
                } else {
                    sslFact = this.enableSSL(this.httpClient, port);
                    this.httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).setSSLSocketFactory(sslFact).build();
                }
            } else if (needPoolMgr && poolmgr != null) {
                this.httpClient = HttpClients.custom().setConnectionManager(poolmgr).setConnectionManagerShared(true).setDefaultCookieStore(this.cookieStore).build();
            } else {
                this.httpClient = HttpClients.custom().setDefaultCookieStore(this.cookieStore).build();
            }
        } else {
            LOG.error("构建HttpClient失败，请进入BPM全局配置中设置BPM服务器地址！");
        }

    }

    private SSLConnectionSocketFactory enableSSL(HttpClient httpclient, int port) {
        SSLConnectionSocketFactory sslFact = null;

        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslFact = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        } catch (Exception var5) {
            LOG.error("构建HTTPS失败！", var5);
        }

        return sslFact;
    }

    public HttpReturnStatus doGet(HttpServletRequest request, String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        Iterator iterator = params.keySet().iterator();

        String args;
        while(iterator.hasNext()) {
            args = (String)iterator.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }

        try {
            this.verifyToken(request);
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
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("get with " + withName + ": " + httpGet.getURI().toString());
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse httpResponse = this.httpClient.execute(httpGet, httpCtx);
            LOG.debug("return code: " + httpResponse.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == httpResponse.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, params, "get");
            }

            result.setCode(httpResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            httpResponse.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("get请求失败！", var13);
        }

        return result;
    }

    public HttpReturnStatus doGet(HttpServletRequest request, String url, String content) {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            this.verifyToken(request);
            String args = EntityUtils.toString(new StringEntity(content, Charset.forName("UTF-8")));
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
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("get with " + withName + ": " + httpGet.getURI().toString());
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse httpResponse = this.httpClient.execute(httpGet, httpCtx);
            LOG.debug("return code: " + httpResponse.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == httpResponse.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, content, "get");
            }

            result.setCode(httpResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            httpResponse.close();
            this.reLoginNum = 0;
        } catch (Exception var12) {
            LOG.error("get请求失败！", var12);
        }

        return result;
    }

    public HttpReturnStatus doGet(Cookie cookie, String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        Iterator var7 = params.keySet().iterator();

        String args;
        while(var7.hasNext()) {
            args = (String)var7.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }

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
            this.cookieStore.addCookie(cookie);
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("get with " + withName + ": " + httpGet.getURI().toString());
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse httpResponse = this.httpClient.execute(httpGet, httpCtx);
            LOG.debug("return code: " + httpResponse.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(httpResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            httpResponse.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("get请求失败！", var13);
        }

        return result;
    }

    public HttpReturnStatus doGet(String userName, String password, String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        Iterator var8 = params.keySet().iterator();

        String args;
        while(var8.hasNext()) {
            args = (String)var8.next();
            String val = (String)params.get(args);
            val = val == null ? "" : val;
            NameValuePair nkv = new BasicNameValuePair(args, val);
            nameValuePairs.add(nkv);
        }

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

            HttpClientContext httpctx = HttpClientContext.create();
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            Credentials credentials = new UsernamePasswordCredentials(userName, password);
            credsProvider.setCredentials(AuthScope.ANY, credentials);
            httpctx.setCredentialsProvider(credsProvider);
            HttpGet httpGet = new HttpGet(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpGet.setConfig(reqcfg);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Content-Language", "zh-CN");
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("get with " + withName + ": " + httpGet.getURI().toString());
            CloseableHttpResponse httpResponse = this.httpClient.execute(httpGet, httpctx);
            String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(httpResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            httpResponse.close();
        } catch (Exception var16) {
            LOG.error("get请求失败！", var16);
        }

        return result;
    }

    public HttpReturnStatus doLogin(HttpServletRequest request) {
        HttpReturnStatus result = new HttpReturnStatus();
        Session session = SecurityUtils.getSubject().getSession();
        String userName = (String)session.getAttribute("_currUserName");
        String userPsw = (String)session.getAttribute("_currUserPsw");
        List<NameValuePair> nameValuePairs = new ArrayList();
        String debugMsg = "j_username=" + userName + ", " + "j_password=******";
        nameValuePairs.add(new BasicNameValuePair("j_username", userName));
        nameValuePairs.add(new BasicNameValuePair("j_password", userPsw));

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "ProcessPortal/j_security_check";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            if (!nameValuePairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            }

            LOG.debug("login: " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            List<Cookie> cookies = this.cookieStore.getCookies();
            request.getSession().setAttribute("LtpaToken2", cookies);
            response.close();
        } catch (Exception var16) {
            result.setCode(-1);
            result.setMsg("login请求失败！异常信息：" + var16.toString());
            LOG.error("login请求失败！", var16);
        }

        return result;
    }

    public HttpReturnStatus doLoginSetCookie(HttpServletRequest request, HttpServletResponse response, String username, String password) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        String debugMsg = "j_username=" + username + ", " + "j_password=******";
        nameValuePairs.add(new BasicNameValuePair("j_username", username));
        nameValuePairs.add(new BasicNameValuePair("j_password", password));

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "ProcessPortal/j_security_check";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            if (!nameValuePairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            }

            LOG.debug("login: " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse retResponse = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + retResponse.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(retResponse.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(retResponse.getStatusLine().getStatusCode());
            result.setMsg(msg);
            List<Cookie> cookies = this.cookieStore.getCookies();
            Iterator var19 = cookies.iterator();

            while(var19.hasNext()) {
                Cookie ck = (Cookie)var19.next();
                LOG.debug("设置was Cookie:" + ck.getName() + "=" + ck.getValue());
                javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(ck.getName(), ck.getValue());
                cookie.setDomain(ck.getDomain());
                cookie.setPath(ck.getPath());
                response.addCookie(cookie);
            }

            retResponse.close();
        } catch (Exception var21) {
            result.setCode(-1);
            result.setMsg("login请求失败！异常信息：" + var21.toString());
            LOG.error("login请求失败！", var21);
        }

        return result;
    }

    public HttpReturnStatus doPost(HttpServletRequest request, String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        String debugMsg = "";
        HttpPost httpPost = new HttpPost(url);
        RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
        httpPost.setConfig(reqcfg);
        List<NameValuePair> nvParams = new ArrayList();
        Iterator var10 = params.keySet().iterator();

        String withName;
        while(var10.hasNext()) {
            withName = (String)var10.next();
            debugMsg = debugMsg + withName + "=" + params.get(withName).toString() + ", ";
            nvParams.add(new BasicNameValuePair(withName, params.get(withName).toString()));
        }

        try {
            this.verifyToken(request);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new UrlEncodedFormEntity(nvParams, Charsets.UTF_8));
            withName = this.isAdmin ? "admin" : "user";
            LOG.debug("post with " + withName + ": " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == response.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, params, "post");
            }

            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("post请求失败！", var13);
        }

        return result;
    }

    public HttpReturnStatus doPost(HttpServletRequest request, String url, String postContent) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
        httpPost.setConfig(reqcfg);

        try {
            this.verifyToken(request);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new StringEntity(postContent, "UTF-8"));
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("post with " + withName + ": " + url);
            LOG.debug("params: " + postContent);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == response.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, postContent, "post");
            }

            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var11) {
            LOG.error("post请求失败！", var11);
        }

        return result;
    }

    public HttpReturnStatus doPost(Cookie cookie, String url, Map<String, Object> params) {
        HttpReturnStatus result = new HttpReturnStatus();
        String debugMsg = "";
        HttpPost httpPost = new HttpPost(url);
        RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
        httpPost.setConfig(reqcfg);
        List<NameValuePair> nvParams = new ArrayList();
        Iterator var10 = params.keySet().iterator();

        String withName;
        while(var10.hasNext()) {
            withName = (String)var10.next();
            debugMsg = debugMsg + withName + "=" + params.get(withName).toString() + ", ";
            nvParams.add(new BasicNameValuePair(withName, params.get(withName).toString()));
        }

        try {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Language", "zh-CN");
            httpPost.setEntity(new UrlEncodedFormEntity(nvParams, Charsets.UTF_8));
            this.cookieStore.addCookie(cookie);
            withName = this.isAdmin ? "admin" : "user";
            LOG.debug("post with " + withName + ": " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("post请求失败！", var13);
        }

        return result;
    }

    public HttpReturnStatus doPut(HttpServletRequest request, String url, Map<String, Object> params) {
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
            this.verifyToken(request);
            httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            httpPut.setEntity(new UrlEncodedFormEntity(nvParams, Charsets.UTF_8));
            withName = this.isAdmin ? "admin" : "user";
            LOG.debug("put with " + withName + ": " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPut, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == response.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, params, "put");
            }

            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("put请求失败！", var13);
        }

        return result;
    }

    public HttpReturnStatus doPut(HttpServletRequest request, String url, String putContent) {
        HttpReturnStatus result = new HttpReturnStatus();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
        httpPut.setConfig(reqcfg);

        try {
            this.verifyToken(request);
            httpPut.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-Language", "zh-CN");
            httpPut.setEntity(new StringEntity(putContent, "UTF-8"));
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("put with " + withName + ": " + url);
            LOG.debug("params: " + putContent);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPut, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            if (401 == response.getStatusLine().getStatusCode() && StringUtils.isBlank(msg) && this.reLoginNum < 3) {
                ++this.reLoginNum;
                return this.reLogin(request, url, putContent, "put");
            }

            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var11) {
            LOG.error("put请求失败！", var11);
        }

        return result;
    }

    public HttpReturnStatus doPut(Cookie cookie, String url, Map<String, Object> params) {
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
            this.cookieStore.addCookie(cookie);
            withName = this.isAdmin ? "admin" : "user";
            LOG.debug("put with " + withName + ": " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPut, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
            this.reLoginNum = 0;
        } catch (Exception var13) {
            LOG.error("put请求失败！", var13);
        }

        return result;
    }

    /**
     * 验证token是否有效
     * @param request
     */
    public void verifyToken(HttpServletRequest request) {
        List<Cookie> cookies = null;
        if (this.isAdmin) {
            cookies = (List)request.getSession().getAttribute("adminLtpaToken2");
        } else {
            cookies = (List)request.getSession().getAttribute("LtpaToken2");
        }

        if (cookies == null) {
            if (this.isAdmin) {
                this.adminLogin(request);
            } else {
                this.doLogin(request);
            }
        } else {
            Iterator var4 = cookies.iterator();

            while(var4.hasNext()) {
                Cookie ck = (Cookie)var4.next();
                this.cookieStore.addCookie(ck);
            }
        }

    }

    public void adminLogin(HttpServletRequest request) {
        ArrayList nameValuePairs = new ArrayList();

        try {
            String debugMsg = "j_username=" + this.bpmGlobalConfig.getBpmAdminName() + ", " + "j_password=******";
            nameValuePairs.add(new BasicNameValuePair("j_username", this.bpmGlobalConfig.getBpmAdminName()));
            nameValuePairs.add(new BasicNameValuePair("j_password", this.bpmGlobalConfig.getBpmAdminPsw()));
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "ProcessPortal/j_security_check";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            if (!nameValuePairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            }

            LOG.debug("admin login: " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug(msg);
            List<Cookie> cookies = this.cookieStore.getCookies();
            request.getSession().setAttribute("adminLtpaToken2", cookies);
            response.close();
        } catch (Exception var12) {
            LOG.error("admin login 请求失败！", var12);
        }

    }

    public static Map<String, HttpReturnStatus> findErrorResult(Map<String, HttpReturnStatus> resultMap) {
        Map<String, HttpReturnStatus> errorHttpStatusMap = new HashMap();
        if (resultMap.containsKey("errorResult")) {
            errorHttpStatusMap.put("errorResult", (HttpReturnStatus)resultMap.get("errorResult"));
        } else {
            Iterator var3 = resultMap.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                HttpReturnStatus httpStatus = (HttpReturnStatus)resultMap.get(key);
                if (httpStatus.getCode() != 200 || StringUtils.isBlank(httpStatus.getMsg())) {
                    errorHttpStatusMap.put("errorResult", httpStatus);
                    break;
                }

                String msg = httpStatus.getMsg();
                JSONObject jsoMsg = new JSONObject(msg);
                String status = jsoMsg.optString("status", "");
                if ("error".equals(status)) {
                    errorHttpStatusMap.put("errorResult", httpStatus);
                    break;
                }
            }
        }

        return errorHttpStatusMap;
    }

    public static boolean isErrorResult(HttpReturnStatus httpResult) {
        boolean result = false;
        if (httpResult.getCode() == 200 && !StringUtils.isBlank(httpResult.getMsg())) {
            String msg = httpResult.getMsg();
            JSONObject jsoMsg = new JSONObject(msg);
            String status = jsoMsg.optString("status", "");
            if ("error".equals(status)) {
                result = true;
            }
        } else {
            result = true;
        }

        return result;
    }

    public static HttpReturnStatus findErrorResult(List<HttpReturnStatus> httpResults) {
        HttpReturnStatus resultStatus = null;
        Iterator var3 = httpResults.iterator();

        while(var3.hasNext()) {
            HttpReturnStatus hstatus = (HttpReturnStatus)var3.next();
            if (isErrorResult(hstatus)) {
                resultStatus = hstatus;
                break;
            }
        }

        return resultStatus;
    }

    public HttpReturnStatus reLogin(HttpServletRequest request, String url, Map<String, Object> params, String method) {
        LOG.debug("ldapToken 过期重新登录 ");
        if (this.isAdmin) {
            this.adminLogin(request);
        } else {
            this.doLogin(request);
        }

        if ("put".equals(method)) {
            return this.doPut(request, url, params);
        } else {
            return "post".equals(method) ? this.doPost(request, url, params) : this.doGet(request, url, params);
        }
    }

    public HttpReturnStatus reLogin(HttpServletRequest request, String url, String content, String method) {
        LOG.debug("ldapToken 过期重新登录 ");
        if (this.isAdmin) {
            this.adminLogin(request);
        } else {
            this.doLogin(request);
        }

        if ("put".equals(method)) {
            return this.doPut(request, url, content);
        } else {
            return "post".equals(method) ? this.doPost(request, url, content) : this.doGet(request, url, content);
        }
    }

    public HttpReturnStatus doLogin(String userName, String password, List<Cookie> cookies) {
        HttpReturnStatus result = new HttpReturnStatus();
        List<NameValuePair> nameValuePairs = new ArrayList();
        String debugMsg = "j_username=" + userName + ", " + "j_password=******";
        nameValuePairs.add(new BasicNameValuePair("j_username", userName));
        nameValuePairs.add(new BasicNameValuePair("j_password", password));

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "ProcessPortal/j_security_check";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            if (!nameValuePairs.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            }

            LOG.debug("login: " + url);
            LOG.debug("params: " + debugMsg);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            cookies.addAll(this.cookieStore.getCookies());
            response.close();
        } catch (Exception var14) {
            result.setCode(-1);
            result.setMsg("login请求失败！异常信息：" + var14.toString());
            LOG.error("login请求失败！", var14);
        }

        return result;
    }

    public HttpReturnStatus doLogout() {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "ProcessPortal/ibm_security_logout?logout=Logout";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpPost.setConfig(reqcfg);
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("logout with " + withName + ": " + url);
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse response = this.httpClient.execute(httpPost, httpCtx);
            LOG.debug("return code: " + response.getStatusLine().getStatusCode());
            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            LOG.debug("return content: " + msg);
            result.setCode(response.getStatusLine().getStatusCode());
            result.setMsg(msg);
            response.close();
        } catch (Exception var10) {
            result.setCode(-1);
            result.setMsg("logout请求失败！异常信息：" + var10.toString());
            LOG.error("logout请求失败！", var10);
        }

        return result;
    }

    public void closeClient() {
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException var2) {
                LOG.error("关闭HttpClient失败！", var2);
            }
        }

    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public String downloadFile(HttpServletRequest request, String downUrl, String saveFilePath) {
        return this.downloadFile(request, downUrl, saveFilePath, "");
    }

    public String downloadFile(HttpServletRequest request, String downUrl, String saveFilePath, String fileName) {
        FileOutputStream outstm = null;
        InputStream instm = null;
        String downFileName = "";

        try {
            this.verifyToken(request);
            HttpGet httpGet = new HttpGet(downUrl);
            RequestConfig reqcfg = HttpClientUtils.getRequestConfig(this.bpmGlobalConfig.getBpmClientTimeout());
            httpGet.setConfig(reqcfg);
            httpGet.setHeader("Content-Language", "utf-8");
            String withName = this.isAdmin ? "admin" : "user";
            LOG.debug("download with " + withName + ": " + httpGet.getURI().toString());
            HttpContext httpCtx = HttpClientContext.create();
            CloseableHttpResponse httpResponse = this.httpClient.execute(httpGet, httpCtx);
            downFileName = HttpResponseUtils.getFileName(httpResponse);
            if (StringUtils.isBlank(downFileName)) {
                downFileName = "export.zip";
            }

            String tmpPath = saveFilePath.replace("\\", "/");
            tmpPath = tmpPath.endsWith("/") ? tmpPath : tmpPath + "/";
            File tmpDir = new File(tmpPath);
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }

            tmpPath = tmpPath + (StringUtils.isBlank(fileName) ? downFileName : fileName);
            File tmpfile = new File(tmpPath);
            outstm = new FileOutputStream(tmpfile);
            instm = httpResponse.getEntity().getContent();
            byte[] buf = new byte[4096];
            boolean var17 = false;

            int i;
            while((i = instm.read(buf)) != -1) {
                outstm.write(buf, 0, i);
            }

            outstm.flush();
            httpResponse.close();
        } catch (Exception var26) {
            LOG.error("下载文件请求失败！", var26);
        } finally {
            try {
                if (outstm != null) {
                    outstm.close();
                }

                if (instm != null) {
                    instm.close();
                }
            } catch (IOException var25) {
                LOG.error("关闭文件下载数据流时发生异常！", var25);
            }

        }

        return downFileName;
    }
}