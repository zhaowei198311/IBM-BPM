package com.desmart.desmartbpm.util.http;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.entity.BpmCommonBusObject;
import com.desmart.desmartbpm.entity.BpmRouteConditionResult;
import com.desmart.desmartbpm.util.JSONUtils;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

public class BpmTaskUtils extends BpmRestApiUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BpmTaskUtils.class);

    public  BpmTaskUtils(BpmGlobalConfig bpmGlobalConfig, ServletContext servletContext) {
    	super(bpmGlobalConfig, servletContext);
    }
    
    public BpmTaskUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
        super(bpmGlobalConfig, isAdmin);
    }

    public BpmTaskUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, ServletContext servletContext) {
        super(bpmGlobalConfig, isAdmin, servletContext);
    }

    public HttpReturnStatus getTaskDetails(HttpServletRequest request, String taskId) {
        return this._getTaskDetails(request, taskId, false);
    }

    public HttpReturnStatus getTaskDetails(Cookie cookie, String taskId) {
        return this._getTaskDetails(cookie, taskId, false);
    }

    public HttpReturnStatus getTaskDetails(HttpServletRequest request, String taskId, boolean readCache) {
        return this._getTaskDetails(request, taskId, readCache);
    }

    private HttpReturnStatus _getTaskDetails(Object webobj, String taskId, boolean readCache) {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "rest/bpm/wle/v1/task" + "/" + taskId;
            Map<String, Object> pmap = new HashMap<>();
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
            } else {
                result.setCode(-1);
                result.setMsg("无法使用缓存！");
            }
        } catch (Exception var11) {
            LOG.error("获取任务详细信息出错，任务ID：" + taskId, var11);
            result.setCode(-1);
            result.setMsg(var11.toString());
        }

        return result;
    }
    
    /**
     * 根据taskId获取 该任务中的pubBo
     * @param request
     * @param taskId
     * @return
     */
    public HttpReturnStatus getTaskData(HttpServletRequest request, String taskId) {
        return this._getTaskData(request, taskId);
    }

    public HttpReturnStatus getTaskData(Cookie cookie, String taskId) {
        return this._getTaskData(cookie, taskId);
    }

    private HttpReturnStatus _getTaskData(Object webobj, String taskId) {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "rest/bpm/wle/v1/task" + "/" + taskId;
            Map<String, Object> pmap = new HashMap();
            pmap.put("action", "getData");
            pmap.put("fields", "pubBo");
            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                result = this.clientUtils.doGet(request, url, pmap);
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                result = this.clientUtils.doGet(cookie, url, pmap);
            } else {
                result.setCode(-1);
                result.setMsg("Web对象类型不正确，无法执行请求！");
            }
        } catch (Exception var9) {
            LOG.error("获取任务数据出错，任务ID：" + taskId, var9);
            result.setCode(-1);
            result.setMsg(var9.toString());
        }

        return result;
    }

    public Map<String, HttpReturnStatus> commitTask(HttpServletRequest request, String taskId, String commitNote, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._commitTask(request, taskId, commitNote, pubBo, routeBo);
    }

    public Map<String, HttpReturnStatus> commitTask(Cookie cookie, String taskId, String commitNote, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._commitTask(cookie, taskId, commitNote, pubBo, routeBo);
    }

    private Map<String, HttpReturnStatus> _commitTask(Object webobj, String taskId, String commitNote, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        HashMap resultMap = new HashMap();

        try {
            HttpReturnStatus result = new HttpReturnStatus();
            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                result = this.applyTask(request, taskId, "toMe", "");
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                result = this.applyTask(cookie, taskId, "toMe", "");
            } else {
                result.setCode(-1);
                result.setMsg("Web对象类型不正确，无法执行请求！");
            }

            if (!BpmClientUtils.isErrorResult(result)) {
                resultMap.put("applyTaskMsg", result);
                JSONObject jsoResult = new JSONObject(result.getMsg());
                String status = jsoResult.optString("status", "");
                if (StringUtils.isNotBlank(status) && !"error".equals(status)) {
                    Map<String, HttpReturnStatus> setDataResult = new HashMap();
                    if (webobjType == 1) {
                        HttpServletRequest request = (HttpServletRequest)webobj;
                        setDataResult = this.setTaskData(request, taskId, pubBo, routeBo);
                    } else if (webobjType == 2) {
                        Cookie cookie = (Cookie)webobj;
                        setDataResult = this.setTaskData(cookie, taskId, pubBo, routeBo);
                    } else {
                        result.setCode(-1);
                        result.setMsg("Web对象类型不正确，无法执行请求！");
                    }

                    resultMap.putAll((Map)setDataResult);
                    Map<String, HttpReturnStatus> errorMap = BpmClientUtils.findErrorResult((Map)setDataResult);
                    if (errorMap.isEmpty() && !BpmClientUtils.isErrorResult(result)) {
                        if (webobjType == 1) {
                            HttpServletRequest request = (HttpServletRequest)webobj;
                            result = this.doTaskAction(request, taskId, "start", "");
                        } else if (webobjType == 2) {
                            Cookie cookie = (Cookie)webobj;
                            result = this.doTaskAction(cookie, taskId, "start", "");
                        } else {
                            result.setCode(-1);
                            result.setMsg("Web对象类型不正确，无法执行请求！");
                        }

                        resultMap.put("commitTaskMsg", result);
                        if (BpmClientUtils.isErrorResult(result)) {
                            resultMap.put("errorResult", result);
                        }
                    } else if (!errorMap.isEmpty()) {
                        resultMap.put("errorResult", (HttpReturnStatus)errorMap.get("errorResult"));
                    } else {
                        resultMap.put("errorResult", result);
                    }
                }
            } else {
                resultMap.put("errorResult", result);
            }
        } catch (Exception var14) {
            LOG.error("提交任务出错，任务ID：" + taskId, var14);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(var14.toString());
            resultMap.put("errorResult", errorStatus);
        }

        return resultMap;
    }

    public HttpReturnStatus completeTask(HttpServletRequest request, String taskId) {
        return this._completeTask(request, taskId);
    }

    public HttpReturnStatus completeTask(Cookie cookie, String taskId) {
        return this._completeTask(cookie, taskId);
    }

    private HttpReturnStatus _completeTask(Object webobj, String taskId) {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                result = this.doTaskAction(request, taskId, "finish", "");
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                result = this.doTaskAction(cookie, taskId, "finish", "");
            } else {
                result.setCode(-1);
                result.setMsg("Web对象类型不正确，无法执行请求！");
            }
        } catch (Exception var6) {
            LOG.error("完成任务出错，任务ID：" + taskId, var6);
            result.setCode(-1);
            result.setMsg(var6.toString());
        }

        return result;
    }

    public HttpReturnStatus applyTask(HttpServletRequest request, String taskId, String assignType, String targetObject) {
        return this._applyTask(request, taskId, assignType, targetObject);
    }

    public HttpReturnStatus applyTask(Cookie cookie, String taskId, String assignType, String targetObject) {
        return this._applyTask(cookie, taskId, assignType, targetObject);
    }

    private HttpReturnStatus _applyTask(Object webobj, String taskId, String assignType, String targetObject) {
        HttpReturnStatus result = new HttpReturnStatus();

        try {
            String params = "";
            String encTargetObj = URLEncoder.encode(targetObject, "utf-8");
            if ("toMe".equals(assignType)) {
                params = "&toMe=true";
            } else if ("back".equals(assignType)) {
                params = "&back=true";
            } else if ("toUser".equals(assignType)) {
                params = "&toUser=" + encTargetObj;
            } else if ("toGroup".equals(assignType)) {
                params = "&toGroup=" + encTargetObj;
            } else {
                params = "&toMe=true";
            }

            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                result = this.doTaskAction(request, taskId, "assign", params);
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                result = this.doTaskAction(cookie, taskId, "assign", params);
            } else {
                result.setCode(-1);
                result.setMsg("Web对象类型不正确，无法执行请求！");
            }
        } catch (Exception var10) {
            LOG.error("申请任务出错，任务ID：" + taskId, var10);
            result.setCode(-1);
            result.setMsg(var10.toString());
        }

        return result;
    }

    /**
     * 根据传入的pubBo，routeBo修改引擎中任务变量
     * @param request 
     * @param taskId  任务id
     * @param pubBo  pubBo中不为null的属性会覆盖原有属性，不是全量覆盖
     * @param routeBo  当routeBo的used属性为true时才会进行覆盖
     * @return
     */
    public Map<String, HttpReturnStatus> setTaskData(HttpServletRequest request, String taskId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._setTaskData(request, taskId, pubBo, routeBo);
    }

    public Map<String, HttpReturnStatus> setTaskData(Cookie cookie, String taskId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._setTaskData(cookie, taskId, pubBo, routeBo);
    }

    private Map<String, HttpReturnStatus> _setTaskData(Object webobj, String taskId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        HashMap resultMap = new HashMap();

        try {
            String params = "";
            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            HttpReturnStatus taskResult = new HttpReturnStatus();
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                taskResult = this.getTaskData(request, taskId);
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                taskResult = this.getTaskData(cookie, taskId);
            } else {
                taskResult.setCode(-1);
                taskResult.setMsg("Web对象类型不正确，无法执行请求！");
            }

            if (!BpmClientUtils.isErrorResult(taskResult)) {
                resultMap.put("getTaskDataMsg", taskResult);
                JSONObject jsoResult = new JSONObject(taskResult.getMsg());
                String status = jsoResult.optString("status");
                if ("200".equals(status)) {
                    JSONObject jsoBo = jsoResult.getJSONObject("data");
                    String resultBo = jsoBo.optString("result");
                    if (StringUtils.isNotBlank(resultBo)) {
                        jsoResult = new JSONObject(resultBo);
                        JSONObject oldPubBo = jsoResult.optJSONObject("pubBo");
                        if (!oldPubBo.isNull("@metadata")) {
                            oldPubBo.remove("@metadata");
                        }

                        JSONObject newPubBo = new JSONObject(pubBo);
                        newPubBo = JSONUtils.combine(newPubBo, oldPubBo);
                        Set<String> keyset = newPubBo.keySet();
                        Iterator var17 = keyset.iterator();

                        label84:
                        while(true) {
                            String key;
                            JSONObject jsoNextOwners;
                            do {
                                do {
                                    if (!var17.hasNext()) {
                                        pubBo = (BpmCommonBusObject)JSON.parseObject(newPubBo.toString(), BpmCommonBusObject.class);
                                        key = "";
                                        if (pubBo != null) {
                                            JSONObject jsoBO = new JSONObject();
                                            jsoBO.put("pubBo", JSON.toJSON(pubBo));
                                            if (routeBo.isUsed()) {
                                                jsoBO.put("routeBo", JSON.toJSON(routeBo));
                                            }

                                            try {
                                                key = URLEncoder.encode(jsoBO.toString(), "UTF-8");
                                            } catch (UnsupportedEncodingException var22) {
                                                LOG.error("TaskAction的参数编码出错！", var22);
                                            }
                                        }

                                        if (StringUtils.isNotBlank(key)) {
                                            params = "&params=" + key;
                                        }
                                        break label84;
                                    }

                                    key = (String)var17.next();
                                    jsoNextOwners = newPubBo.optJSONObject(key);
                                } while(!key.startsWith("nextOwners_"));
                            } while(jsoNextOwners == null);

                            JSONArray jayOwnerItems = jsoNextOwners.optJSONArray("items");
                            List<String> tmpOwners = new ArrayList();
                            if (jayOwnerItems != null) {
                                for(int i = 0; i < jayOwnerItems.length(); ++i) {
                                    tmpOwners.add(jayOwnerItems.optString(i, ""));
                                }
                            }

                            newPubBo.put(key, tmpOwners);
                        }
                    }
                }

                HttpReturnStatus result = new HttpReturnStatus();
                if (webobjType == 1) {
                    HttpServletRequest request = (HttpServletRequest)webobj;
                    result = this.doTaskAction(request, taskId, "setData", params);
                } else if (webobjType == 2) {
                    Cookie cookie = (Cookie)webobj;
                    result = this.doTaskAction(cookie, taskId, "setData", params);
                } else {
                    result.setCode(-1);
                    result.setMsg("Web对象类型不正确，无法执行请求！");
                }

                if (!BpmClientUtils.isErrorResult(result)) {
                    resultMap.put("setTaskDataMsg", result);
                } else {
                    resultMap.put("errorResult", result);
                }
            } else {
                resultMap.put("errorResult", taskResult);
            }
        } catch (Exception var23) {
            LOG.error("设置任务数据出错，任务ID：" + taskId, var23);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(var23.toString());
            resultMap.put("errorResult", errorStatus);
        }

        return resultMap;
    }

    public Map<String, HttpReturnStatus> getPreviousTask(HttpServletRequest request, String instanceId, String currTaskId) {
        Map<String, HttpReturnStatus> httpStatusMap = new HashMap();
        BpmInstanceUtils instUtils = new BpmInstanceUtils(this.bpmGlobalConfig, true);
        HttpReturnStatus instHttpStatus = instUtils.getInstance(request, instanceId, true);
        if (!BpmClientUtils.isErrorResult(instHttpStatus)) {
            httpStatusMap.put("getInstanceResult", instHttpStatus);
            JSONObject jsoMsg = new JSONObject(instHttpStatus.getMsg());
            JSONArray jayTasks = jsoMsg.getJSONObject("data").getJSONArray("tasks");
            int currTaskPos = 0;

            int i;
            JSONObject jsoTask;
            String status;
            for(i = jayTasks.length() - 1; i >= 0; --i) {
                jsoTask = jayTasks.getJSONObject(i);
                status = jsoTask.optString("tkiid", "");
                if (currTaskId.equals(status)) {
                    currTaskPos = i;
                    break;
                }
            }

            if (currTaskPos > 0) {
                for(i = currTaskPos - 1; i >= 0; --i) {
                    jsoTask = jayTasks.getJSONObject(i);
                    status = jsoTask.optString("status", "");
                    if ("Closed".equalsIgnoreCase(status)) {
                        HttpReturnStatus taskHttpStatus = new HttpReturnStatus();
                        taskHttpStatus.setCode(200);
                        taskHttpStatus.setMsg(jsoTask.toString());
                        httpStatusMap.put("getPrevTask", taskHttpStatus);
                        break;
                    }
                }
            }
        } else {
            httpStatusMap.put("errorResult", instHttpStatus);
        }

        return httpStatusMap;
    }

    public Map<String, HttpReturnStatus> getPreviousTask(HttpServletRequest request, String instanceId) {
        Map<String, HttpReturnStatus> httpStatusMap = new HashMap<>();
        BpmInstanceUtils instUtils = new BpmInstanceUtils(this.bpmGlobalConfig, true);
        HttpReturnStatus instHttpStatus = instUtils.getInstance(request, instanceId, true);
        if (!BpmClientUtils.isErrorResult(instHttpStatus)) {
            httpStatusMap.put("getInstanceResult", instHttpStatus);
            JSONObject jsoMsg = new JSONObject(instHttpStatus.getMsg());
            JSONArray jayTasks = jsoMsg.getJSONObject("data").getJSONArray("tasks");

            for(int i = jayTasks.length() - 1; i >= 0; --i) {
                JSONObject jsoTask = jayTasks.getJSONObject(i);
                String status = jsoTask.optString("status", "");
                if ("Closed".equalsIgnoreCase(status)) {
                    HttpReturnStatus taskHttpStatus = new HttpReturnStatus();
                    taskHttpStatus.setCode(200);
                    taskHttpStatus.setMsg(jsoTask.toString());
                    httpStatusMap.put("getPrevTask", taskHttpStatus);
                    break;
                }
            }
        } else {
            httpStatusMap.put("errorResult", instHttpStatus);
        }

        return httpStatusMap;
    }

    /** @deprecated */
    @Deprecated
    public Map<String, HttpReturnStatus> getFirstTask(HttpServletRequest request, String instanceId) {
        Map<String, HttpReturnStatus> httpStatusMap = new HashMap();
        BpmInstanceUtils instUtils = new BpmInstanceUtils(this.bpmGlobalConfig, true);
        HttpReturnStatus instHttpStatus = instUtils.getInstance(request, instanceId, true);
        if (!BpmClientUtils.isErrorResult(instHttpStatus)) {
            httpStatusMap.put("getInstanceResult", instHttpStatus);
            JSONObject jsoMsg = new JSONObject(instHttpStatus.getMsg());
            JSONArray jayTasks = jsoMsg.getJSONObject("data").getJSONArray("tasks");
            if (jayTasks.length() > 0) {
                JSONObject jsoTask = jayTasks.getJSONObject(0);
                HttpReturnStatus taskHttpStatus = new HttpReturnStatus();
                taskHttpStatus.setCode(200);
                taskHttpStatus.setMsg(jsoTask.toString());
                httpStatusMap.put("getFirstTask", taskHttpStatus);
            }
        } else {
            httpStatusMap.put("errorResult", instHttpStatus);
        }

        return httpStatusMap;
    }

    private HttpReturnStatus doTaskAction(HttpServletRequest request, String taskId, String actionName, String params) {
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        StringBuilder url = new StringBuilder(host + "rest/bpm/wle/v1/task" + "/" + taskId);
        url.append("?action=" + actionName);
        if (StringUtils.isNotBlank(params)) {
            url.append(params);
        }

        return this.clientUtils.doPut(request, url.toString(), new HashMap());
    }

    private HttpReturnStatus doTaskAction(Cookie cookie, String taskId, String actionName, String params) {
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        StringBuilder url = new StringBuilder(host + "rest/bpm/wle/v1/task" + "/" + taskId);
        url.append("?action=" + actionName);
        if (StringUtils.isNotBlank(params)) {
            url.append(params);
        }

        return this.clientUtils.doPut(cookie, url.toString(), new HashMap());
    }

    /** @deprecated */
    @Deprecated
    public HttpReturnStatus getCurrentTasks(HttpServletRequest request) throws Exception {
        List<NameValuePair> newParams = new ArrayList();
        newParams.add(new BasicNameValuePair("condition", "taskStatus|Received"));
        newParams.add(new BasicNameValuePair("organization", "byTask"));
        newParams.add(new BasicNameValuePair("run", "true"));
        newParams.add(new BasicNameValuePair("filterByCurrentUser", "true"));
        newParams.add(new BasicNameValuePair("columns", "taskActivityName,docTitle,instanceCreateDate,creatorName,documentId,appId"));
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/search/query" + "?" + EntityUtils.toString(new UrlEncodedFormEntity(newParams, Charset.forName("UTF-8")));
        HttpReturnStatus result = this.clientUtils.doPut(request, url, new HashMap());
        return result;
    }
}
