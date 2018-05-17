package com.desmart.desmartbpm.util.http;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartportal.entity.BpmCommonBusObject;
import com.desmart.desmartportal.entity.BpmRouteConditionResult;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

public class BpmProcessUtils extends BpmRestApiUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BpmProcessUtils.class);

    public BpmProcessUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin) {
        super(bpmGlobalConfig, isAdmin);
    }

    public BpmProcessUtils(BpmGlobalConfig bpmGlobalConfig, boolean isAdmin, ServletContext servletContext) {
        super(bpmGlobalConfig, isAdmin, servletContext);
    }

    /**
     * 获得所有公开的流程
     * @param request
     * @return
     */
    public HttpReturnStatus getAllExposedProcess(HttpServletRequest request) {
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/exposed/process";
        Map<String, Object> params = new HashMap();
        HttpReturnStatus result = this.clientUtils.doGet(request, url, params);
        return result;
    }

    public Map<String, HttpReturnStatus> startProcess(HttpServletRequest request, String procAppId, String bpdId, String snapShotId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._startProcess(request, procAppId, bpdId, snapShotId, pubBo, routeBo);
    }

    public Map<String, HttpReturnStatus> startProcess(Cookie cookie, String procAppId, String bpdId, String snapShotId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        return this._startProcess(cookie, procAppId, bpdId, snapShotId, pubBo, routeBo);
    }

    private Map<String, HttpReturnStatus> _startProcess(Object webobj, String procAppId, String bpdId, String snapshotId, BpmCommonBusObject pubBo, BpmRouteConditionResult routeBo) {
        HashMap resultMap = new HashMap();

        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "rest/bpm/wle/v1/process";
            Map<String, Object> pubBoMap = new HashMap();
            pubBoMap.put("creatorId", pubBo.getCreatorId());
            String creatorName = this.getSession().getAttribute("_currUserNickName").toString();
            pubBoMap.put("creatorName", creatorName);
            pubBoMap.put("docTitle", pubBo.getDocTitle());
            pubBoMap.put("documentId", pubBo.getDocumentId());
            pubBoMap.put("appId", pubBo.getAppId());
            pubBoMap.put("ttlAmount", pubBo.getTtlAmount());
            pubBoMap.put("tag", pubBo.getTag());
            if (StringUtils.isNotBlank(pubBo.getParams())) {
                JSONObject jsoParams = new JSONObject(pubBo.getParams());
                pubBoMap.put("params", jsoParams.toString());
            } else {
                pubBoMap.put("params", "");
            }

            Map<String, Object> paramsMap = new HashMap();
            paramsMap.put("pubBo", pubBoMap);
            JSONObject jsoBo = new JSONObject(paramsMap);
            Map<String, Object> postMap = new HashMap();
            postMap.put("params", jsoBo.toString());
            postMap.put("action", "start");
            postMap.put("bpdId", bpdId);
            postMap.put("processAppId", procAppId);
            if (StringUtils.isNotBlank(snapshotId)) {
                postMap.put("snapshotId", snapshotId);
            }

            int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
            HttpReturnStatus result = new HttpReturnStatus();
            if (webobjType == 1) {
                HttpServletRequest request = (HttpServletRequest)webobj;
                result = this.clientUtils.doPost(request, url, postMap);
            } else if (webobjType == 2) {
                Cookie cookie = (Cookie)webobj;
                result = this.clientUtils.doPost(cookie, url, postMap);
            } else {
                result.setCode(-1);
                result.setMsg("Web对象类型不正确，无法执行请求！");
            }

            if (!BpmClientUtils.isErrorResult(result)) {
                resultMap.put("startProcessMsg", result);
                JSONObject jsoResult = new JSONObject(result.getMsg());
                String status = jsoResult.optString("status", "");
                if ("200".equals(status)) {
                    String taskId = jsoResult.getJSONObject("data").getJSONArray("tasks").getJSONObject(0).getString("tkiid");
                    BpmTaskUtils taskUtils = null;
                    if (webobjType == 1) {
                        HttpServletRequest request = (HttpServletRequest)webobj;
                        taskUtils = new BpmTaskUtils(this.bpmGlobalConfig, false, request.getServletContext());
                        result = taskUtils.applyTask(request, taskId, "toMe", "");
                    } else if (webobjType == 2) {
                        Cookie cookie = (Cookie)webobj;
                        taskUtils = new BpmTaskUtils(this.bpmGlobalConfig, false);
                        result = taskUtils.applyTask(cookie, taskId, "toMe", "");
                    } else {
                        result.setCode(-1);
                        result.setMsg("Web对象类型不正确，无法执行请求！");
                    }

                    taskUtils.closeClient();
                    resultMap.put("applyTaskMsg", result);
                }
            } else {
                resultMap.put("errorResult", result);
            }
        } catch (Exception var22) {
            LOG.error("启动流程出错！", var22);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(var22.toString());
            resultMap.put("errorResult", errorStatus);
        }

        return resultMap;
    }

    public HttpReturnStatus getProcessModel(HttpServletRequest request, String procAppId, String bpdId, String snapshotId) {
        return this._getProcessModel(request, procAppId, bpdId, snapshotId);
    }

    public HttpReturnStatus getProcessModel(Cookie cookie, String procAppId, String bpdId, String snapshotId) {
        return this._getProcessModel(cookie, procAppId, bpdId, snapshotId);
    }

    private HttpReturnStatus _getProcessModel(Object webobj, String procAppId, String bpdId, String snapshotId) {
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/processModel/{0}?{1}";
        Map<String, Object> params = new HashMap();
        params.put("processAppId", procAppId);
        if (StringUtils.isNotBlank(snapshotId)) {
            params.put("snapshotId", snapshotId);
        }

        url = MessageFormat.format(url, bpdId, "");
        HttpReturnStatus result = new HttpReturnStatus();
        int webobjType = HttpRequestUtils.isRequestOrCookieType(webobj);
        if (webobjType == 1) {
            HttpServletRequest request = (HttpServletRequest)webobj;
            result = this.clientUtils.doGet(request, url, params);
        } else if (webobjType == 2) {
            Cookie cookie = (Cookie)webobj;
            result = this.clientUtils.doGet(cookie, url, params);
        } else {
            result.setCode(-1);
            result.setMsg("Web对象类型不正确，无法执行请求！");
        }

        return result;
    }

    public HttpReturnStatus getVisualProcessModel(HttpServletRequest request, String procAppId, String bpdId, String snapshotId) {
        String host = this.bpmGlobalConfig.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/visual/processModel/{0}?{1}";
        Map<String, Object> params = new HashMap<>();
        params.put("projectId", procAppId);
        if (StringUtils.isNotBlank(snapshotId)) {
            params.put("snapshotId", snapshotId);
        }

        url = MessageFormat.format(url, bpdId, "");
        HttpReturnStatus result = this.clientUtils.doGet(request, url, params);
        return result;
    }

    public HttpReturnStatus getExportUrl(HttpServletRequest request, String procAppId, String snapshotId) {
        HttpReturnStatus resultStatus = new HttpReturnStatus();
        if (StringUtils.isNoneBlank(new CharSequence[]{procAppId, snapshotId})) {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "bpm/repo/projects/{0}/export?snapshot={1}";
            url = MessageFormat.format(url, procAppId, snapshotId);
            resultStatus.setCode(200);
            resultStatus.setMsg(url);
        } else {
            resultStatus.setCode(-1);
            resultStatus.setMsg("ProcessAppId和SnapshotId这两个参数值不能为空！");
        }

        return resultStatus;
    }
}
