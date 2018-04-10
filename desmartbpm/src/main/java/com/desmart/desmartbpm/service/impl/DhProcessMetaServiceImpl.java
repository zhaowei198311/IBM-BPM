package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhProcessMetaDao;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartbpm.util.rest.RestUtil;

@Service
public class DhProcessMetaServiceImpl implements DhProcessMetaService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessMetaServiceImpl.class);
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhProcessMetaDao dhProcessMetaDao;
    
    
    public ServerResponse getAllExposedProcess(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 0 : (pageNum < 0 ? 0 : pageNum - 1);
        pageSize = pageSize == null ? 0 : Math.abs(pageSize);
        
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        String url = bpmcfg.getBpmServerHost() + "rest/bpm/wle/v1/exposed/process";
        
        RestUtil restUtil = new RestUtil(bpmcfg);
        HttpReturnStatus procStatus = restUtil.doGet(url, new HashMap<String, Object>());
        restUtil.close();
        
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> exposeItemList = new ArrayList<>();
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

    
    public ServerResponse getExposedProcess(Integer pageNum, Integer pageSize, String processAppName, 
            String processAppAcronym, String display) {
        pageNum = pageNum == null ? 0 : (pageNum < 0 ? 0 : pageNum - 1);
        pageSize = pageSize == null ? 0 : Math.abs(pageSize);
        
        // 获取所有公开的流程 
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        String url = bpmcfg.getBpmServerHost() + "rest/bpm/wle/v1/exposed/process";
        RestUtil restUtil = new RestUtil(bpmcfg);
        HttpReturnStatus procStatus = restUtil.doGet(url, new HashMap<String, Object>());
        restUtil.close();
        Map<String, Object> results = new HashMap<>();
        
        // 满足条件的结果
        List<Map<String, Object>> exposeItemList = new ArrayList<>();
        Set<String> checkedItems = new HashSet<>(); // 已经检查的数据 snapshotId+bpdId
        int total = 0;
        
        
        if (!BpmClientUtils.isErrorResult(procStatus)) {
            JSONObject jsoMsg = new JSONObject(procStatus.getMsg());
            JSONObject jsoData = jsoMsg.getJSONObject("data");
            JSONArray jayExpoItems = jsoData.optJSONArray("exposedItemsList");
            if (jayExpoItems != null) {
                // step1: 过滤掉重复的和不符合检索条件的流程
                for(int i = 0; i < jayExpoItems.length(); ++i) {
                    JSONObject jsoItem = jayExpoItems.getJSONObject(i);
                    String procAppID = jsoItem.optString("processAppID", "");
                    String itemID = jsoItem.optString("itemID", "");
                    String identify = procAppID + itemID;
                    if (checkedItems.contains(identify)) {// 检查过此种流程跳过
                        continue;
                    }
                    String procAppName = jsoItem.optString("processAppName", "");
                    String procAppAcronym = jsoItem.optString("processAppAcronym", "");
                    String procDisplay = jsoItem.optString("display", "");
                    
                    if (StringUtils.isNotBlank(processAppName)) {
                        // 检查应用名是否符合检索条件,不符合要求continue
                        if (!StringUtils.containsIgnoreCase(procAppName, processAppName)) {
                            checkedItems.add(identify);
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotBlank(processAppAcronym)) {
                        // 检查应用缩略名是否符合检索条件
                        if (!StringUtils.containsIgnoreCase(procAppAcronym, processAppAcronym)) {
                            checkedItems.add(identify);
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotBlank(display)) {
                        // 检查流程名是否符合检索条件
                        if (!StringUtils.containsIgnoreCase(procDisplay, display)) {
                            checkedItems.add(identify);
                            continue;
                        }
                    }
                    
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("identify", identify);
                    itemData.put("processAppId", procAppID);
                    itemData.put("bpdId", itemID);
                    itemData.put("processAppName", procAppName);
                    itemData.put("processAppAcronym", procAppAcronym);
                    itemData.put("display", procDisplay);
                    exposeItemList.add(itemData);
                    checkedItems.add(identify);
                }
                
            } else {
                return ServerResponse.createByErrorMessage("获取公开的流程失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("获取公开的流程失败");
        }
        // step2: 检查其中没有绑定的元数据，并分页返回
        
        Set<String> identifyListInDB = getBindedProcessMetaIdentify();
        Iterator<Map<String, Object>> iterator = exposeItemList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> item = iterator.next();
            if (identifyListInDB.contains((String)item.get("identify"))) {
                iterator.remove();
            }
        }
        
        total = exposeItemList.size();
        List<Map<String, Object>> itemToShow = new ArrayList<>();
        int startRow = pageNum * pageSize;
        for (int i=0; i<exposeItemList.size(); i++) {
            if (i >= startRow) {
                itemToShow.add(exposeItemList.get(i));
            }
            
            if (itemToShow.size() >= pageSize) {
                break;
            }
        }
        
        results.put("total", total);
        results.put("list", itemToShow);
        
        return ServerResponse.createBySuccess(results);
    }
    
    /**
     * 获得已经绑定的流程元数据的标识（库名+流程图名）集合
     * @return
     */
    private Set<String> getBindedProcessMetaIdentify() {
        List<DhProcessMeta> metaListInDb = dhProcessMetaDao.listAll();
        Set<String> identifyList = new HashSet<>();
        for (DhProcessMeta meta : metaListInDb) {
            identifyList.add(meta.getProAppId() + meta.getProUid());
        }
        return identifyList;
    }
    
    public static void main(String[] args) {
        System.out.println(StringUtils.contains("defg", "ef"));
        System.out.println(StringUtils.contains("defg", "fg"));
        System.out.println(StringUtils.contains("defg", "dg"));
        System.out.println(StringUtils.containsIgnoreCase("defg", "ef"));
    }
    
}
