package com.desmart.common.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 与流程引擎交互任务操作
 *
 */
public class BpmTaskUtil {
    private static final Logger log = LoggerFactory.getLogger(BpmTaskUtil.class);
    
    private BpmGlobalConfig bpmGlobalConfig;
    
    public BpmTaskUtil(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
    }
    
    /**
     * 设置任务中的pubBo变量，同时运行人工任务中的脚本自然完成任务
     * @param taskId
     * @param pubBo
     * @return  Map中的信息： 
     *              assignTaskResult
     *              
     */
    public Map<String, HttpReturnStatus> commitTask(Integer taskId, CommonBusinessObject pubBo) {
        HashMap<String, HttpReturnStatus> resultMap = new HashMap<>();
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        HttpReturnStatus result = new HttpReturnStatus();
        
        String currentUserUid = (String)SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        
        try {
            // 将任务分配给当前用户
            result = this.applyTask(taskId, currentUserUid);
            if (!BpmClientUtils.isErrorResult(result)) {
                // 将分配任务返回的信息放入返回值
                resultMap.put("assignTaskResult", result);
                // 查看是否分配任务成功
                JSONObject jsoResult = new JSONObject(result.getMsg());
                String status = jsoResult.optString("status", "");
                if (StringUtils.isNotBlank(status) && !"error".equals(status)) {
                    Map<String, HttpReturnStatus> setDataResult = setTaskData(taskId, pubBo);
                    resultMap.putAll((Map)setDataResult);
                    Map<String, HttpReturnStatus> errorMap = BpmClientUtils.findErrorResult(setDataResult);
                    if (errorMap.isEmpty() && !BpmClientUtils.isErrorResult(result)) {
                        result = this.completeTask(taskId);
                        resultMap.put("commitTaskResult", result);
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
        } catch (Exception e) {
            log.error("发起流程失败", e);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(e.toString());
            resultMap.put("errorResult", errorStatus);
        } finally {
            restUtil.close();
        }
        return resultMap;
    }
    
    /**
     * 将任务分配给某人
     * @return
     */
    public HttpReturnStatus applyTask(Integer taskId, String userUid) {
        String params = "&toUser=" + userUid;
        return doTaskAction(taskId, "assign", params);
    }
    
    private HttpReturnStatus doTaskAction(Integer taskId, String actionName, String params) {
        HttpReturnStatus result = null;
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        
        String host = this.bpmGlobalConfig.getBpmServerHost();
        StringBuilder url = new StringBuilder(host + "rest/bpm/wle/v1/task" + "/" + taskId);
        url.append("?action=" + actionName);
        if (StringUtils.isNotBlank(params)) {
            url.append(params);
        }
        try {
            result = restUtil.sendPut(url.toString(), new HashMap());
        } catch (Exception e) {
            log.error("分配任务失败, 任务id: " + taskId, e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        
        return result;
    }
    
    /**
     * 获得task中的变量pubBo
     * @param taskId
     * @return
     */
    public HttpReturnStatus getTaskData(Integer taskId) {
        HttpReturnStatus result = null;
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        
        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            String url = host + "rest/bpm/wle/v1/task" + "/" + taskId;
            Map<String, Object> params = new HashMap();
            params.put("action", "getData");
            params.put("fields", "pubBo");
            
            result = restUtil.doGet(url, params);
        } catch (Exception e) {
            log.error("获得任务信息失败,任务ID：" + taskId, e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        return result;
    }
    
    /**
     * 将引擎中pubBo的数据更新
     * @param taskId
     * @param pubBo
     * @return
     */
    public Map<String, HttpReturnStatus> setTaskData(Integer taskId, CommonBusinessObject pubBo) {
        Map<String, HttpReturnStatus> resultMap = new HashMap<>();
        
        try {
            HttpReturnStatus getTaskResult = this.getTaskData(taskId);
            if (!BpmClientUtils.isErrorResult(getTaskResult)) {
                resultMap.put("getTaskResult", getTaskResult);
                JSONObject jsoResult = new JSONObject(getTaskResult.getMsg());
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
                        
                        String key;
                        JSONObject jsoNextOwners;
                        Set<String> keyset = newPubBo.keySet();
                        Iterator<String> iterator = keyset.iterator();
                        while(iterator.hasNext()) {
                            key = iterator.next();
                            if (key.startsWith("nextOwners_")) {
                                jsoNextOwners = newPubBo.optJSONObject(key);
                                if (jsoNextOwners != null) {
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
                        // 新的pubBo准备完成
                        System.out.println(newPubBo);
                        JSONObject jsoBO = new JSONObject();
                        jsoBO.put("pubBo", newPubBo);
                        String params = URLEncoder.encode(jsoBO.toString(), "UTF-8");
                        if (StringUtils.isNotBlank(params)) {
                            params = "&params=" + params;
                        }
                        HttpReturnStatus setTaskResult = this.doTaskAction(taskId, "setData", params);
                        if (!BpmClientUtils.isErrorResult(setTaskResult)) {
                            resultMap.put("setTaskResult", setTaskResult);
                        } else {
                            resultMap.put("errorResult", setTaskResult);
                        }
                    }
                }
                
            } else {
                resultMap.put("errorResult", getTaskResult);
            }
            
        } catch (Exception e) {
            log.error("设置任务数据出错，任务ID：" + taskId, e);
            HttpReturnStatus errorStatus = new HttpReturnStatus();
            errorStatus.setCode(-1);
            errorStatus.setMsg(e.toString());
            resultMap.put("errorResult", errorStatus);
        }
        return resultMap;
    }
    
    public HttpReturnStatus startTask(Integer taskId) {
        return this.doTaskAction(taskId, "start", "");
    }
    
    /**
     * 完成任务
     * @param taskId
     * @return
     */
    public HttpReturnStatus completeTask(Integer taskId) {
        return this.doTaskAction(taskId, "finish", "");
    }
    
    
}



















