package com.desmart.common.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;

/**
 * 与流程引擎交互流程操作
 *
 */
public class BpmProcessUtil {
    private static final Logger log = LoggerFactory.getLogger(BpmProcessUtil.class);
    
    private BpmGlobalConfig bpmGlobalConfig;
    
    public BpmProcessUtil(BpmGlobalConfig bpmGlobalConfig) {
        this.bpmGlobalConfig = bpmGlobalConfig;
    }
    
    /**
     * 发起流程
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @param pubBo
     * @return
     */
    public HttpReturnStatus startProcess(String proAppId, String proUid, String proVerUid, CommonBusinessObject pubBo) {
        RestUtil restUtil = new RestUtil(bpmGlobalConfig);
        HttpReturnStatus httpReturnStatus = null;
        try {
            String host = this.bpmGlobalConfig.getBpmServerHost();
            host = host.endsWith("/") ? host : host + "/";
            String url = host + "rest/bpm/wle/v1/process";
            
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("pubBo", pubBo);
            JSONObject paramMapJson = new JSONObject(paramsMap);
            
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("action", "start");
            postMap.put("bpdId", proUid);
            postMap.put("processAppId", proAppId);
            postMap.put("snapshotId", proVerUid);
            postMap.put("params", paramMapJson.toString());
            
            httpReturnStatus = restUtil.sendPost(url, postMap);
            
            return httpReturnStatus;
        } catch (Exception e) {
            log.error("发起流程失败", e);
            httpReturnStatus = new HttpReturnStatus();
            httpReturnStatus.setCode(-1);
            httpReturnStatus.setMsg(e.toString());
        } finally {
            restUtil.close();
        }
        return httpReturnStatus;
    }
    
}
