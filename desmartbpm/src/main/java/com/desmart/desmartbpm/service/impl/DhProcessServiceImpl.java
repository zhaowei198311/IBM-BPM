package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.desmart.desmartbpm.service.DhProcessService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartbpm.util.http.BpmProcessUtils;

@Service
public class DhProcessServiceImpl implements DhProcessService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessServiceImpl.class);
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    
    
    public ServerResponse getExposedProcess(HttpServletRequest request, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 0 : (pageNum < 0 ? 0 : pageNum - 1);
        pageSize = pageSize == null ? 0 : Math.abs(pageSize);
        
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        BpmProcessUtils procUtils = new BpmProcessUtils(bpmcfg, true);
        HttpReturnStatus procStatus = procUtils.getAllExposedProcess(request);
        procUtils.closeClient();
        
        Map<String, Object> results = new HashMap();
        
        List<Map<String, Object>> exposeItemList = new ArrayList();
        int total = 0;
        if (!BpmClientUtils.isErrorResult(procStatus)) {
            JSONObject jsoMsg = new JSONObject(procStatus.getMsg());
            JSONObject jsoData = jsoMsg.getJSONObject("data");
            JSONArray jayExpoItems = jsoData.optJSONArray("exposedItemsList");
            if (jayExpoItems != null) {
                total = jayExpoItems.length();
                int startRow = pageNum * pageSize;
                Set<String> keyset = new HashSet<>();

                for(int i = 0; i < jayExpoItems.length(); ++i) {
                    if (i >= startRow) {
                        JSONObject jsoItem = jayExpoItems.getJSONObject(i);
                        Map<String, Object> itemData = new HashMap<>();
                        String procAppId = jsoItem.optString("processAppID", "");
                        if (StringUtils.isNotBlank(procAppId) && !keyset.contains(procAppId)) {
                            itemData.put("procAppName", jsoItem.optString("processAppName", ""));
                            itemData.put("procAppId", procAppId);
                            itemData.put("bpdName", jsoItem.optString("display", ""));
                            itemData.put("bpdId", jsoItem.optString("itemID", ""));
                            itemData.put("snapshotId", jsoItem.optString("snapshotID", ""));
                            itemData.put("snapshotCreated", jsoItem.optString("snapshotCreatedOn", ""));
                            itemData.put("branchId", jsoItem.optString("branchID", ""));
                            exposeItemList.add(itemData);
                            keyset.add(procAppId);
                        }
                    }

                    if (exposeItemList.size() >= pageSize) {
                        break;
                    }
                }
            }
        }
        results.put("total", total);
        results.put("processList", exposeItemList);
        return ServerResponse.createBySuccess(results);
        
    }


    
    
}
