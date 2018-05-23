package com.desmart.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     *              applyTaskMsg
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
                resultMap.put("applyTaskMsg", result);
                // 查看是否分配任务成功
                JSONObject jsoResult = new JSONObject(result.getMsg());
                String status = jsoResult.optString("status", "");
                if (StringUtils.isNotBlank(status) && !"error".equals(status)) {
                    
                    
                }
                
                
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
     * 使某人申领任务
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
            log.error("发起流程失败", e);
            result = new HttpReturnStatus();
            result.setCode(-1);
            result.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        
        return result;
    }
    
}



















