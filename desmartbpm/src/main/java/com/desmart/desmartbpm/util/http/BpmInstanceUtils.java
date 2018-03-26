package com.desmart.desmartbpm.util.http;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.util.JSONUtils;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmInstanceUtils extends BpmRestApiUtils {
 private static final Logger LOG = LoggerFactory.getLogger(BpmInstanceUtils.class);

 public BpmInstanceUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
     super(bpmGlobalConfig, isAdmin);
 }

 public BpmInstanceUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, ServletContext servletContext) {
     super(bpmGlobalConfig, isAdmin, servletContext);
 }

/* public HttpReturnStatus getInstance(HttpServletRequest request, String instanceId) {
     return this._getInstance(request, instanceId, false);
 }*/

/* public HttpReturnStatus getInstance(Cookie cookie, String instanceId) {
     return this._getInstance(cookie, instanceId, false);
 }*/

 /*public HttpReturnStatus getInstance(HttpServletRequest request, String instanceId, boolean readCache) {
     return this._getInstance(request, instanceId, readCache);
 }*/

 public HttpReturnStatus getInstance(String userName, String password, String instanceId) {
     Map<String, Object> params = new HashMap();
     String host = this.bpmGlobalConfig.getBpmServerHost();
     host = host.endsWith("/") ? host : host + "/";
     String url = host + "rest/bpm/wle/v1/process/{0}";
     url = MessageFormat.format(url, instanceId);
     HttpReturnStatus result = this.clientUtils.doGet(userName, password, url, params);
     return result;
 }

 /*private HttpReturnStatus _getInstance(Object webobj, String instanceId, boolean readCache) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
         HttpServletRequest request;
         if (!readCache) {
             if (webobjType == 1) {
                 request = (HttpServletRequest)webobj;
                 result = this.clientUtils.doGet(request, url, pmap);
             } else if (webobjType == 2) {
                 Cookie cookie = (Cookie)webobj;
                 result = this.clientUtils.doGet(cookie, url, pmap);
             } else {
                 result.setCode(-1);
                 result.setMsg("Web对象类型不正确，无法执行请求！");
             }
         } else if (webobjType == 1) {
             request = (HttpServletRequest)webobj;
             String key = (this.clientUtils.isAdmin() ? "admin" : "user") + "_instance_" + instanceId;
             if (!BpmCacheDataUtils.existInstance(request, key)) {
                 result = this.clientUtils.doGet(request, url, pmap);
                 if (!BpmClientUtils.isErrorResult(result)) {
                     BpmCacheDataUtils.addInstance(request, key, result);
                 }
             } else {
                 result = BpmCacheDataUtils.getInstance(request, key);
             }
         } else {
             result.setCode(-1);
             result.setMsg("webobj参数非HttpRequest类型，无法使用缓存！");
         }
     } catch (Exception var11) {
         LOG.error("获取实例详细信息出错，instanceId：" + instanceId, var11);
         result.setCode(-1);
         result.setMsg(var11.toString());
     }

     return result;
 }*/

 public HttpReturnStatus getInstanceData(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         pmap.put("parts", "data");
         url = MessageFormat.format(url, instanceId);
         result = this.clientUtils.doGet(request, url, pmap);
     } catch (Exception var7) {
         LOG.error("获取实例BO数据信息出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }

 public HttpReturnStatus getInstanceTasks(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         pmap.put("parts", "header");
         url = MessageFormat.format(url, instanceId);
         result = this.clientUtils.doGet(request, url, pmap);
     } catch (Exception var7) {
         LOG.error("获取实例任务列表信息出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }

 /*public Map<String, BpmInstanceToken> getTokenIdWithInstance(String instanceDetails, String taskId) {
     Map<String, BpmInstanceToken> bpmTokens = new HashMap();
     String instanceId = "";

     try {
         if (StringUtils.isNotBlank(instanceDetails)) {
             JSONObject jsoMsg = JSONObject.parseObject(instanceDetails);
             JSONObject jsoData = jsoMsg.getJSONObject("data");
             if (jsoData != null) {
                 instanceId = jsoData.getString("piid");
                 JSONObject jsoTreeRoot = jsoData.getJSONObject("executionTree").getJSONObject("root");
                 if (jsoTreeRoot != null) {
                     JSONArray jayChildren = jsoTreeRoot.getJSONArray("children");
                     if (jayChildren != null && !jayChildren.isEmpty()) {
                         for(int i = 0; i < jayChildren.size(); ++i) {
                             JSONObject jsoChild = jayChildren.getJSONObject(i);
                             if (jsoChild != null) {
                                 String rootTokenId = jsoChild.getString("tokenId");
                                 String rootFlowObjectId = jsoChild.getString("flowObjectId");
                                 BpmInstanceToken rootToken = new BpmInstanceToken();
                                 rootToken.setTokenId(rootTokenId);
                                 rootToken.setRootTokenId(rootTokenId);
                                 rootToken.setActivityBpdId(rootFlowObjectId);
                                 bpmTokens.put("root", rootToken);
                                 BpmInstanceToken taskToken = this.getTokenIdWithToken(jsoChild, taskId);
                                 if (taskToken != null) {
                                     taskToken.setRootTokenId(rootTokenId);
                                     bpmTokens.put("task", taskToken);
                                     break;
                                 }

                                 bpmTokens = new HashMap();
                             }
                         }
                     }
                 }
             }
         }
     } catch (Exception var15) {
         LOG.error("获取实例Token ID出错，instanceId：" + instanceId + ", taskId：" + taskId, var15);
     }

     return bpmTokens;
 }*/

 /*public List<BpmInstanceToken> getAllRootTokens(String instanceDetails) {
     List<BpmInstanceToken> tokens = new ArrayList();
     String instanceId = "";

     try {
         if (StringUtils.isNotBlank(instanceDetails)) {
             JSONObject jsoMsg = JSONObject.parseObject(instanceDetails);
             JSONObject jsoData = jsoMsg.getJSONObject("data");
             if (jsoData != null) {
                 instanceId = jsoData.getString("piid");
                 JSONObject jsoTreeRoot = jsoData.getJSONObject("executionTree").getJSONObject("root");
                 if (jsoTreeRoot != null) {
                     JSONArray jayChildren = jsoTreeRoot.getJSONArray("children");
                     if (jayChildren != null && !jayChildren.isEmpty()) {
                         for(int i = 0; i < jayChildren.size(); ++i) {
                             JSONObject jsoChild = jayChildren.getJSONObject(i);
                             BpmInstanceToken instToken = new BpmInstanceToken();
                             if (jsoChild != null) {
                                 instToken.setActivityBpdId(jsoChild.getString("flowObjectId"));
                                 instToken.setActivityName(jsoChild.getString("name"));
                                 instToken.setInstanceId(instanceId);
                                 String tokenId = jsoChild.getString("tokenId");
                                 instToken.setTokenId(tokenId);
                                 instToken.setRootTokenId(tokenId);
                                 JSONArray taskIds = jsoChild.getJSONArray("createdTaskIDs");
                                 Map<String, String> tokenTaskIds = new HashMap();
                                 if (taskIds != null && !taskIds.isEmpty()) {
                                     tokenTaskIds.put(tokenId, taskIds.getString(0));
                                 } else {
                                     JSONArray jaySubChildren = jsoChild.getJSONArray("children");
                                     if (jaySubChildren != null) {
                                         for(int j = 0; j < jaySubChildren.size(); ++j) {
                                             JSONObject jsoSubChild = jaySubChildren.getJSONObject(j);
                                             if (jsoSubChild != null) {
                                                 tokenTaskIds.putAll(this.findTokenTasks(jsoSubChild));
                                             }
                                         }
                                     }
                                 }

                                 instToken.setTaskIds(tokenTaskIds);
                                 tokens.add(instToken);
                             }
                         }
                     }
                 }
             }
         }
     } catch (Exception var17) {
         LOG.error("获取实例Token列表时发生异常，instanceId：" + instanceId + ", " + "\r\n实例详细信息：" + instanceDetails, var17);
     }

     return tokens;
 }*/

 public HttpReturnStatus getAllInstanceByBpdName(HttpServletRequest request, String bpdName) {
     HttpReturnStatus instHttpStatus = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/processes/search";
         Map<String, Object> pmap = new HashMap();
         if (StringUtils.isNotBlank(bpdName)) {
             pmap.put("searchFilter", bpdName);
         }

         instHttpStatus = this.clientUtils.doGet(request, url, pmap);
     } catch (Exception var7) {
         LOG.error("获取流程实例列表发生异常！bpdName：" + bpdName, var7);
     }

     return instHttpStatus;
 }

 public HttpReturnStatus getAllInstanceByName(HttpServletRequest request, String instanceName, int offset, int rows) {
     HttpReturnStatus instHttpStatus = new HttpReturnStatus();
     offset = offset < 0 ? 0 : offset;
     rows = Math.abs(rows);

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/processes/search";
         Map<String, Object> pmap = new HashMap();
         if (StringUtils.isNotBlank(instanceName)) {
             pmap.put("searchFilter", instanceName);
         }

         pmap.put("offset", "" + offset);
         pmap.put("limit", "" + rows);
         instHttpStatus = this.clientUtils.doGet(request, url, pmap);
     } catch (Exception var9) {
         LOG.error("获取流程实例列表发生异常！instanceName=" + instanceName, var9);
     }

     return instHttpStatus;
 }

 public HttpReturnStatus moveToken(HttpServletRequest request, String tokenId, String instanceId, String targetNodeId) {
     HttpReturnStatus httpStatus = new HttpReturnStatus();

     String msg;
     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         msg = host + "rest/bpm/wle/v1/process/{0}";
         msg = msg + "?action=moveToken&tokenId={1}&target={2}&resume=true";
         msg = MessageFormat.format(msg, instanceId, tokenId, targetNodeId);
         Map<String, Object> pmap = new HashMap();
         httpStatus = this.clientUtils.doPost(request, msg, pmap);
     } catch (Exception var9) {
         msg = "实例移动标记出错，instanceId：{0}, tokenId：{1}, targetNodeId：{2}";
         msg = MessageFormat.format(msg, instanceId, tokenId, targetNodeId);
         LOG.error(msg, var9);
         httpStatus.setCode(-1);
         httpStatus.setMsg(var9.toString());
     }

     return httpStatus;
 }

 public HttpReturnStatus deleteToken(HttpServletRequest request, String tokenId, String instanceId) {
     HttpReturnStatus httpStatus = new HttpReturnStatus();

     String msg;
     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         msg = host + "rest/bpm/wle/v1/process/{0}";
         msg = msg + "?action=deleteToken&tokenId={1}";
         msg = MessageFormat.format(msg, instanceId, tokenId);
         Map<String, Object> pmap = new HashMap();
         httpStatus = this.clientUtils.doPost(request, msg, pmap);
     } catch (Exception var8) {
         msg = "实例删除标记出错，instanceId：{0}, tokenId：{1}";
         msg = MessageFormat.format(msg, instanceId, tokenId);
         LOG.error(msg, var8);
         httpStatus.setCode(-1);
         httpStatus.setMsg(var8.toString());
     }

     return httpStatus;
 }

 /*public Map<String, HttpReturnStatus> setInstancePubBo(HttpServletRequest request, String instanceId, BpmCommonBusObject pubBo) {
     Map<String, HttpReturnStatus> httpStatusMap = new HashMap();
     HttpReturnStatus instHttpStatus = this.getInstanceData(request, instanceId);
     httpStatusMap.put("getInstanceDataResult", instHttpStatus);
     if (!BpmClientUtils.isErrorResult(instHttpStatus)) {
         org.json.JSONObject jsoMsg = new org.json.JSONObject(instHttpStatus.getMsg());
         org.json.JSONObject jsoVar = jsoMsg.getJSONObject("data").getJSONObject("variables");
         org.json.JSONObject jsoBo = jsoVar.optJSONObject("pubBo");
         if (jsoBo != null) {
             if (jsoBo.get("@metadata") != null) {
                 jsoBo.remove("@metadata");
             }

             org.json.JSONObject jsoNew = new org.json.JSONObject(pubBo);
             jsoNew = JSONUtils.combine(jsoNew, jsoBo);
             String host = this.bpmGlobalConfig.getBpmServerHost();
             host = host.endsWith("/") ? host : host + "/";
             String url = host + "rest/bpm/wle/v1/process/{0}" + "/variable/pubBo";
             url = MessageFormat.format(url, instanceId);
             HttpReturnStatus httpStatus = this.clientUtils.doPut(request, url, jsoNew.toString());
             httpStatusMap.put("setInstanceBOMsg", httpStatus);
             if (BpmClientUtils.isErrorResult(httpStatus)) {
                 LOG.error("设置流程实例BO对象失败！");
                 httpStatusMap.put("errorResult", httpStatus);
             }
         } else {
             LOG.error("pubBo对象为空！");
             HttpReturnStatus errorStatus = new HttpReturnStatus();
             errorStatus.setCode(-1);
             JSONObject msg = new JSONObject();
             msg.put("msg", "pubBo对象为空！");
             errorStatus.setMsg(msg.toJSONString());
             httpStatusMap.put("errorResult", errorStatus);
         }
     } else {
         LOG.error("获取流程实例数据信息失败！");
         httpStatusMap.put("errorResult", instHttpStatus);
     }

     return httpStatusMap;
 }*/

 public HttpReturnStatus pauseInstance(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         url = url + "?action=suspend";
         result = this.clientUtils.doPut(request, url, pmap);
         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("暂停流程实例失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var7) {
         LOG.error("暂停流程实例出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }

 public HttpReturnStatus resumeInstance(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         url = url + "?action=resume";
         result = this.clientUtils.doPut(request, url, pmap);
         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("恢复流程实例失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var7) {
         LOG.error("恢复流程实例出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }

 public HttpReturnStatus terminateInstance(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         url = url + "?action=terminate";
         result = this.clientUtils.doPut(request, url, pmap);
         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("停止流程实例失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var7) {
         LOG.error("停止流程实例出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }


 /*public BpmInstanceToken getTokenIdWithToken(JSONObject tokenDetail, String taskId) {
     BpmInstanceToken bpmToken = null;
     JSONArray jayTaskIds = tokenDetail.getJSONArray("createdTaskIDs");
     JSONArray jayChildren = tokenDetail.getJSONArray("children");
     if (jayTaskIds != null && !jayTaskIds.isEmpty()) {
         String tmpTaskId = jayTaskIds.getString(0);
         if (taskId.equals(tmpTaskId)) {
             bpmToken = new BpmInstanceToken();
             String tokenId = tokenDetail.getString("tokenId");
             bpmToken.setTokenId(tokenId);
             bpmToken.setActivityBpdId(tokenDetail.getString("flowObjectId"));
             Map<String, String> taskIds = new HashMap();
             taskIds.put(tokenId, taskId);
             bpmToken.setTaskIds(taskIds);
         }
     } else if (jayChildren != null && !jayChildren.isEmpty()) {
         for(int i = 0; i < jayChildren.size(); ++i) {
             JSONObject jsoChild = jayChildren.getJSONObject(i);
             bpmToken = this.getTokenIdWithToken(jsoChild, taskId);
             if (bpmToken != null) {
                 break;
             }
         }
     }

     return bpmToken;
 }*/

 public String getTaskParentTokenId(String instanceDetails, String taskId) {
     String parentTokenId = "";
     String instanceId = "";

     try {
         if (StringUtils.isNotBlank(instanceDetails)) {
             JSONObject jsoMsg = JSONObject.parseObject(instanceDetails);
             JSONObject jsoData = jsoMsg.getJSONObject("data");
             if (jsoData != null) {
                 instanceId = jsoData.getString("piid");
                 JSONObject jsoTreeRoot = jsoData.getJSONObject("executionTree").getJSONObject("root");
                 if (jsoTreeRoot != null) {
                     JSONArray jayChildren = jsoTreeRoot.getJSONArray("children");
                     if (jayChildren != null) {
                         for(int i = 0; i < jayChildren.size(); ++i) {
                             JSONObject jsoChild = jayChildren.getJSONObject(i);
                             parentTokenId = this.getParentTokenId(jsoChild, taskId);
                             if (StringUtils.isNotBlank(parentTokenId)) {
                                 break;
                             }
                         }
                     }
                 }
             }
         }
     } catch (Exception var11) {
         LOG.error("获取任务的上级Token ID出错，instanceId：" + instanceId + ", taskId：" + taskId, var11);
     }

     return parentTokenId;
 }

 public String getParentTokenId(JSONObject jsoToken, String taskId) {
     String parentTokenId = "";
     JSONArray jayChildren = jsoToken.getJSONArray("children");
     if (jayChildren != null) {
         for(int i = 0; i < jayChildren.size(); ++i) {
             JSONObject jsoChild = jayChildren.getJSONObject(i);
             JSONArray jayTaskIds = jsoChild.getJSONArray("createdTaskIDs");
             boolean foundTask = false;
             if (jayTaskIds != null) {
                 for(int j = 0; j < jayTaskIds.size(); ++j) {
                     if (taskId.equals(jayTaskIds.getString(j))) {
                         foundTask = true;
                         break;
                     }
                 }
             }

             if (foundTask) {
                 parentTokenId = jsoToken.getString("tokenId");
                 if (parentTokenId == null) {
                     parentTokenId = "nullTokenGetParent";
                 }
                 break;
             }

             parentTokenId = this.getParentTokenId(jsoChild, taskId);
         }
     }

     if ("nullTokenGetParent".equals(parentTokenId)) {
         parentTokenId = jsoToken.getString("tokenId");
         if (parentTokenId == null) {
             parentTokenId = "nullTokenGetParent";
         }
     }

     return parentTokenId;
 }

 public Map<String, String> findTokenTasks(JSONObject jsoToken) {
     Map<String, String> tokenTaskIds = new HashMap();
     JSONArray taskIds = jsoToken.getJSONArray("createdTaskIDs");
     if (taskIds != null && !taskIds.isEmpty()) {
         String tokenId = jsoToken.getString("tokenId");
         tokenTaskIds.put(tokenId, taskIds.getString(0));
     } else {
         JSONArray jsoChildren = jsoToken.getJSONArray("children");
         if (jsoChildren != null && !jsoChildren.isEmpty()) {
             for(int i = 0; i < jsoChildren.size(); ++i) {
                 JSONObject jsoChild = jsoChildren.getJSONObject(i);
                 if (jsoChild != null) {
                     tokenTaskIds.putAll(this.findTokenTasks(jsoChild));
                 }
             }
         }
     }

     return tokenTaskIds;
 }

 public HttpReturnStatus retryInstance(HttpServletRequest request, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         url = url + "?action=retry";
         result = this.clientUtils.doPut(request, url, pmap);
         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("重试流程实例失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var7) {
         LOG.error("重试流程实例出错，instanceId：" + instanceId, var7);
         result.setCode(-1);
         result.setMsg(var7.toString());
     }

     return result;
 }

 public HttpReturnStatus getInstanceErrorMsg(HttpServletRequest request, String[] instanceIds) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, "errors");
         String strInstIds = "";

         for(int i = 0; i < instanceIds.length; ++i) {
             String id = instanceIds[i];
             if (StringUtils.isNotBlank(id)) {
                 id = id.trim();
                 strInstIds = strInstIds + id;
                 if (i < instanceIds.length - 1) {
                     strInstIds = strInstIds + ",";
                 }
             }
         }

         url = url + "?instanceIds=" + URLEncoder.encode(strInstIds, "utf-8");
         result = this.clientUtils.doPut(request, url, pmap);
         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("获取流程实例错误信息失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var10) {
         LOG.error("获取流程实例错误信息失败，instanceIds：" + Arrays.toString(instanceIds), var10);
         result.setCode(-1);
         result.setMsg(var10.toString());
     }

     return result;
 }

 public String extractErrorMsg(String errMsg) {
     String result = "";
     if (StringUtils.isNotBlank(errMsg)) {
         try {
             String errorMsg = errMsg.substring(errMsg.indexOf("[<") + 2, errMsg.indexOf(">]"));
             String[] tmpError = errorMsg.split("><");
             result = tmpError[1].substring(tmpError[1].indexOf("//") + 2, tmpError[1].indexOf("/", tmpError[1].indexOf("//") + 2));
         } catch (Exception var5) {
             LOG.error("分解流程实例错误信息发生错误！", var5);
             result = errMsg;
         }
     }

     return result;
 }

 public HttpReturnStatus deleteInstance(HttpServletRequest request, String instanceId) {
     return this._deleteInstance(request, instanceId);
 }

 public HttpReturnStatus deleteInstance(Cookie cookie, String instanceId) {
     return this._deleteInstance(cookie, instanceId);
 }

 private HttpReturnStatus _deleteInstance(Object webobj, String instanceId) {
     HttpReturnStatus result = new HttpReturnStatus();

     try {
         String host = this.bpmGlobalConfig.getBpmServerHost();
         host = host.endsWith("/") ? host : host + "/";
         String url = host + "rest/bpm/wle/v1/process/{0}";
         Map<String, Object> pmap = new HashMap();
         url = MessageFormat.format(url, instanceId);
         url = url + "?action=delete";
         int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
         if (webobjType == 1) {
             HttpServletRequest request = (HttpServletRequest)webobj;
             result = this.clientUtils.doPost(request, url, pmap);
         } else if (webobjType == 2) {
             Cookie cookie = (Cookie)webobj;
             result = this.clientUtils.doPost(cookie, url, pmap);
         } else {
             result.setCode(-1);
             result.setMsg("Web对象类型不正确，无法执行请求！");
         }

         if (BpmClientUtils.isErrorResult(result)) {
             LOG.error("删除流程实例失败！\r\n" + result.getMsg());
             result.setCode(-1);
         }
     } catch (Exception var9) {
         LOG.error("删除流程实例出错，instanceId：" + instanceId, var9);
         result.setCode(-1);
         result.setMsg(var9.toString());
     }

     return result;
 }
}
