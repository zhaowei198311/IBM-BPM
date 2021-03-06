package com.desmart.common.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;

import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 与流程引擎交互任务操作
 *
 */
public class BpmTaskUtil {
    private static final Logger log = LoggerFactory.getLogger(BpmTaskUtil.class);
    public static final String TASK_STATUS_RECEIVED = "Received";
    public static final String TASK_STATUS_CLOSED = "Closed";
    private BpmGlobalConfig bpmGlobalConfig;
    
    public BpmTaskUtil(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
    }
    
    /**
     * 尝试将任务分配给指定用户，设置任务中的pubBo变量并完成任务
     * @param taskId 任务编号
     * @param pubBo  引擎中变量
     * @param applyUser
     * @return  Map中的信息： 
     *              assignTaskResult
     */
    public Map<String, HttpReturnStatus> commitTask(Integer taskId, CommonBusinessObject pubBo, String applyUser) {
        HashMap<String, HttpReturnStatus> resultMap = new HashMap<>();
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        HttpReturnStatus result = null;

        try {
            // 如果分配人不为空，尝试将任务分配给指定人员
            if (StringUtils.isNotBlank(applyUser)) {
                this.applyTask(taskId, applyUser);
            }
            Map<String, HttpReturnStatus> setDataResult = setTaskData(taskId, pubBo);
            resultMap.putAll(setDataResult);
            Map<String, HttpReturnStatus> errorMap = HttpReturnStatusUtil.findErrorResult(setDataResult);
            if (errorMap.isEmpty()) {
                result = this.completeTask(taskId);
                resultMap.put("commitTaskResult", result);
                if (HttpReturnStatusUtil.isErrorResult(result)) {
                    resultMap.put("errorResult", result);
                }
            } else{
                resultMap.put("errorResult", errorMap.get("errorResult"));
            }

        } catch (Exception e) {
            log.error("完成任务失败，任务id：" + taskId, e);
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
     * @param taskId  任务id
     * @param userUid  用户工号/主键
     * @return
     */
    public HttpReturnStatus applyTask(Integer taskId, String userUid) {
        String params = "&toUser=" + userUid;
        return doTaskAction(taskId, "assign", params);
    }

    /**
     * 对任务进行各种操作的底层方法
     * @param taskId
     * @param actionName
     * @param params
     * @return
     */
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
    public HttpReturnStatus getTaskData(int taskId) {
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
     * 获得指定任务的状态，失败时返回null
     * @param taskId
     * @return  预期的值： "Closed" / "Received"
     */
    public String getTaskStatus(int taskId) {
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            String url = host + "rest/bpm/wle/v1/task" + "/" + taskId;
            Map<String, Object> params = new HashMap();
            params.put("parts", "viewTask");
            HttpReturnStatus httpReturnStatus = restUtil.doGet(url, params);
            if (HttpReturnStatusUtil.isErrorResult(httpReturnStatus)) {
                return null;
            } else {
                return JSON.parseObject(httpReturnStatus.getMsg()).getJSONObject("data").getString("status");
            }
        } catch (Exception e) {
            log.error("获得指定任务状态失败,任务ID：" + taskId, e);
            return null;
        } finally {
            restUtil.close();
        }
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
            if (!HttpReturnStatusUtil.isErrorResult(getTaskResult)) {
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
                        // 重新拼装JSON对象
                        newPubBo = JSONUtils.combine(newPubBo, oldPubBo);
                        
                        String key;
                        JSONObject jsoNextOwners;
                        Set<String> keyset = newPubBo.keySet();
                        Iterator<String> iterator = keyset.iterator();
                        while (iterator.hasNext()) {
                            key = iterator.next();
                            if (key.startsWith("nextOwners_")) {
                                jsoNextOwners = newPubBo.optJSONObject(key);
                                /* 
                                 * 如果是oldPubBo中带过来的， 取JSONObject可以取到，
                                 * 如果是newPubBo中设置的，是一个JSONArray数组，无法用取JSONObject方法获得
                                 */
                                if (jsoNextOwners != null) {
                                    JSONArray jayOwnerItems = jsoNextOwners.optJSONArray("items");
                                    List<String> tmpOwners = new ArrayList<>();
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
                        if (!HttpReturnStatusUtil.isErrorResult(setTaskResult)) {
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
     * 直接完成任务，不分配处理人，不修改pubBo值
     * @param taskId
     * @return
     */
    public HttpReturnStatus completeTask(Integer taskId) {
        return this.doTaskAction(taskId, "finish", "");
    }

    /**
     * 重新分配引擎中的任务处理人，操作的是引擎数据库
     * @param taskId 任务id
     * @param userUid 用户主键，只能传一个人
     * @return
     */
    public ServerResponse changeOwnerOfLaswTask(int taskId, String userUid) {
        ServerResponse serverResponse = null;
        HttpReturnStatus result = null;
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);

        String host = this.bpmGlobalConfig.getBpmformsHost();
        String webContext = this.bpmGlobalConfig.getBpmformsWebContext();
        StringBuilder url = new StringBuilder(host + webContext + "/lswTask/changeOwnerOfLswTask");
        JSONObject json = new JSONObject();
        json.put("taskId", taskId);
        json.put("userUid", userUid);
        System.out.println(url.toString());
        System.out.println(json.toString());
        try {
            result = restUtil.doPost(url.toString(), json.toString());
            if (result.getCode() == 200) {
                JSONObject resJson = new JSONObject(result.getMsg());
                int status = resJson.getInt("status");
                if (status == 0) {
                    serverResponse = ServerResponse.createBySuccess();
                } else {
                    serverResponse = ServerResponse.createByErrorMessage(resJson.optString("msg", ""));
                }
            } else {
                serverResponse = ServerResponse.createByErrorMessage("分配任务失败");
            }
        } catch (Exception e) {
            log.error("分配任务失败, 任务id: " + taskId, e);
            serverResponse = ServerResponse.createByErrorMessage("分配任务失败");
        } finally {
            restUtil.close();
        }
        return serverResponse;
    }
    
}



















