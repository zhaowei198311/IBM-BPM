package com.desmart.desmartbpm.util.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.util.rest.RestUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
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
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

	private static final String USERNAME = "deadmin";

	private static final String PASSWORD = "passw0rd";

	private static final String URL = "http://10.0.4.201:9080/rest/bpm/wle/v1/";
	
	// 全局任务标识
	private int tkiid = 0;
	
	public HttpClientUtils() {
	}

	public static RequestConfig getRequestConfig(Integer timeout) {
		int itimeout = timeout == null ? '\uea60' : Math.abs(timeout);
		itimeout = itimeout < 3000 ? 3000 : itimeout;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(itimeout)
				.setConnectionRequestTimeout(itimeout).setSocketTimeout(itimeout).build();
		return requestConfig;
	}

	/**
	 * 从httpclient连接池中获取连接
	 * 
	 * @param servletContext
	 *            servlet上下文
	 * @return
	 */
	public static CloseableHttpClient getConnectionInPool(ServletContext servletContext) {
		PoolingHttpClientConnectionManager poolmgr = null;
		if (servletContext != null) {
			poolmgr = (PoolingHttpClientConnectionManager) servletContext
					.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolmgr)
				.setConnectionManagerShared(true).build();
		return httpClient;
	}

	public static CloseableHttpClient getConnectionInPool(ServletContext servletContext, CookieStore cookieStore) {
		PoolingHttpClientConnectionManager poolmgr = null;
		if (servletContext != null) {
			poolmgr = (PoolingHttpClientConnectionManager) servletContext
					.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolmgr)
				.setConnectionManagerShared(true).setDefaultCookieStore(cookieStore).build();
		return httpClient;
	}

	public static PoolingHttpClientConnectionManager getConnectionPool(ServletContext servletContext) {
		PoolingHttpClientConnectionManager poolmgr = null;
		if (servletContext != null) {
			poolmgr = (PoolingHttpClientConnectionManager) servletContext
					.getAttribute(Const.HTTP_CLIENT_CONNECTION_POOL);
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

	public static HttpReturnStatus doPost(String url, String postContent, Map<String, String> headers,
			List<Exception> exceptions) {
		HttpReturnStatus result = new HttpReturnStatus();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig reqcfg = getRequestConfig(10000);
		httpPost.setConfig(reqcfg);
		CloseableHttpClient httpClient = HttpClients.custom().build();

		try {
			Iterator var9 = headers.keySet().iterator();

			while (var9.hasNext()) {
				String head = (String) var9.next();
				httpPost.setHeader(head, (String) headers.get(head));
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

		while (var5.hasNext()) {
			String key = (String) var5.next();
			String val = (String) params.get(key);
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
		switch (htype.hashCode()) {
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

	/**
	 * 
	 * @param method
	 *            请求方法(get,post,delete,put)
	 * @param action
	 *            调用api执行的动作 (删除,暂挂,恢复 等)
	 * @param type
	 *            调用类型 (process,task)
	 * @param id
	 *            流程id/任务标识 (在IBM 引擎中可查到当前流程实例id 或者说当前任务tas标识)
	 * @param taskIDs
	 *            任务标识 (选填参数)
	 * @return
	 */
	public String IbmApi(Map<String, Object> params) {
		// 解析用户名与密码
		LOG.info("请求IBM API 开始...");
		CloseableHttpClient httpClient = HttpClients.custom().build();
		HttpClientContext context = HttpClientContext.create();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
		// 追加拼接 url 链接路径
		StringBuffer buffer = new StringBuffer(URL);
		buffer.append(params.get("type"));
		buffer.append("/" + params.get("id"));
		buffer.append("?action=" + params.get("action"));
		buffer.append("&parts=all");
		// 转换为string类型 方便后面请求调用
		String metod = String.valueOf(params.get("method"));
		String url = buffer.toString();
		//
		CloseableHttpResponse httpResponse = null;
		String msg = null;
		try {
			credsProvider.setCredentials(AuthScope.ANY, credentials);
			context.setCredentialsProvider(credsProvider);
			// method方法 判断 请求方法 然后 使用拼接参数
			switch (metod) {
			case "post":
				HttpPost httpPost = new HttpPost(url);
				httpResponse = httpClient.execute(httpPost, context);
				break;
			case "get":
				HttpGet httpget = new HttpGet(url);
				httpResponse = httpClient.execute(httpget, context);
				break;
			case "put":
				HttpPut httpPut = new HttpPut(url);
				httpResponse = httpClient.execute(httpPut, context);
				break;
			case "delete":
				HttpDelete httpdelete = new HttpDelete(url);
				httpResponse = httpClient.execute(httpdelete, context);
			default:
				break;
			}
			msg = org.apache.http.util.EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		LOG.info("status-code: " + httpResponse.getStatusLine().getStatusCode());
		LOG.info("response-info: " + msg);
		LOG.info("请求IBM API 结束...");
		return msg;
	}

	/**
	 *  驳回通用服务
	 * @param instansId  当前流程实例标识id
	 * @param flowobjectId 需要驳回到哪一个节点的标识id
	 * @param user 将任务分配给某个用户标识id
	 */
	public void Reject(String instansId,String flowobjectId, String user) {
		LOG.info("驳回服务  开始...");
		String msg = "";
		try {
			// 这一步操作 是调用API 获取当前流程实例信息数据
			Map<String, Object> params = new HashMap<>();
			params.put("parts", "data|header|executionTree");
			HttpReturnStatus result =checkApiLogin("get","http://10.0.4.201:9080/rest/bpm/wle/v1/process/"+instansId, params);
			msg = result.getMsg();
			// 判断当前状态如果服务器返回200 才让执行回退任务
			if(result.getCode()==200) {
				// JSON 转换当前信息 获取 flowobjectid 和 分配给 某个用户
				JSONObject jsonobject = JSONObject.parseObject(msg);		
				// 获取data内容·
				JSONObject data = (JSONObject) jsonobject.get("data");
				JSONObject executionTree = (JSONObject) data.get("executionTree");
				JSONObject root = (JSONObject) executionTree.get("root");
				// 拿取tokenid 当前token
				JSONArray jsonArray = root.getJSONArray("children");
				// 拿取当前 tkiid 任务标识  操作 是 分配任务
				JSONArray jsonArray2 = data.getJSONArray("tasks");
				String tokenId = "" ;
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					// 获取到当前tokenId 节点id 回退起始位置
					tokenId = object.getString("tokenId");
				}
				for (int j = jsonArray2.size()-1; j < jsonArray2.size(); j++) {
					JSONObject tasks = jsonArray2.getJSONObject(j);
					tkiid = tasks.getInteger("tkiid");
					System.err.println(tkiid);
					System.err.println("任务标识:" + tasks.getInteger("tkiid"));
				}
				// 第二次请求 带入参数 注意 第二次请求 是以post 请求方式 去执行一个动作 moveToken  移动token
				Map<String, Object> params2 = new HashMap<>();
				params2.put("action", "moveToken");
				params2.put("resume", "true");
				params2.put("parts", "data|header|executionTree");
				params2.put("target", flowobjectId);
				params2.put("tokenId", tokenId);
				HttpReturnStatus result2 =checkApiLogin("post","http://10.0.4.201:9080/rest/bpm/wle/v1/process/"+instansId, params2);				
				// 将当前用户分配给user 
				int taskid = tkiid+1;
				Map<String, Object> params3 = new HashMap<>();
				params3.put("action", "assign");
				params3.put("toUser", user);
				params3.put("parts", "all");
				HttpReturnStatus result3 =checkApiLogin("put","http://10.0.4.201:9080/rest/bpm/wle/v1/task/"+taskid, params3);				
				if(result3.getCode()==200) {
					LOG.info("驳回成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("驳回服务 结束...");
	}
	
	
	/**
	 * 通用加签服务
	 * @param user 
	 * @param message
	 */
	public void addSign(String user,String message,String tkiid) {
		LOG.info("加签服务 开始...");
		try {
			// 首先邀请某一个用户参与任务协作 （组织架构树 选择）
			// 调用 api
		Map<String, Object> params = new HashMap<>();	
		params.put("action", "invite");
		params.put("user", user);
		params.put("message", message);
		HttpReturnStatus result = checkApiLogin("post", "http://10.0.4.201:9080/rest/bpm/wle/v1/task/"+tkiid, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("加签服务 结束...");
	}
	
	/**
	 * 加签通用服务完成之后 将任务分配给原始所有者
	 */
	public void minusSign(String tkiid) {
		LOG.info("减签服务 开始...");
		try {
			// 首先邀请某一个用户参与任务协作 （组织架构树 选择）
			Map<String, Object> paramsMap = new HashMap<>();
			paramsMap.put("action", "assign");
			paramsMap.put("back", "true");
			paramsMap.put("parts", "all");
			HttpReturnStatus result = checkApiLogin("put", "http://10.0.4.201:9080/rest/bpm/wle/v1/task/"+tkiid, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("减签服务 结束...");
	}
	
	/**
	 * 每次调用api 验证的方法
	 */
	public HttpReturnStatus checkApiLogin(String method,String url, Map<String, Object> params) {
		BpmGlobalConfig bpmGlobalConfig = new BpmGlobalConfig();
		bpmGlobalConfig.setBpmAdminName("deadmin");
		bpmGlobalConfig.setBpmAdminPsw("passw0rd");
		RestUtil restUtil = new RestUtil(bpmGlobalConfig);
		switch (method) {
		case "get":
			return restUtil.doGet(url, params);
		case "post":
			return restUtil.sendPost(url, params);
		case "put":
			return restUtil.sendPut(url, params);
		default:
			break;
		}
		return null;
	}
	
	/**
	 * 验证登陆 IBM 引擎方法
	 */
	public String checkLoginIbm(String url) {
		LOG.info("验证IBM登陆开始...");
		String msg = null;
		try {
			CloseableHttpClient httpClient = HttpClients.custom().build();
			HttpClientContext context = HttpClientContext.create();
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			Credentials credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
			credsProvider.setCredentials(AuthScope.ANY, credentials);
			context.setCredentialsProvider(credsProvider);
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse httpResponse = httpClient.execute(null, context);
		//	msg = org.apache.http.util.EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
}