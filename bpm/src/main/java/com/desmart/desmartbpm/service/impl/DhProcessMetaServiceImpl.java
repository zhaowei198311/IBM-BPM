package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.DhProcessCategoryMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartbpm.util.rest.RestUtil;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class DhProcessMetaServiceImpl implements DhProcessMetaService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessMetaServiceImpl.class);
    
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private DhProcessMetaMapper dhProcessMetaMapper;
    @Autowired
    private DhProcessCategoryMapper dhProcessCategoryMapper;
    
    
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
        PageInfo pageInfo = new PageInfo(itemToShow);
        pageInfo.setStartRow(startRow+1);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(++pageNum);
        pageInfo.setPageSize(pageSize);
        
        return ServerResponse.createBySuccess(pageInfo);
    }
    
    /**
     * 获得已经绑定的流程元数据的标识（库名+流程图名）集合
     * @return
     */
    private Set<String> getBindedProcessMetaIdentify() {
        List<DhProcessMeta> metaListInDb = dhProcessMetaMapper.listAll();
        Set<String> identifyList = new HashSet<>();
        for (DhProcessMeta meta : metaListInDb) {
            identifyList.add(meta.getProAppId() + meta.getProUid());
        }
        return identifyList;
    }
    

    public ServerResponse<PageInfo<List<DhProcessMeta>>> listDhProcessMetaByCategoryList(List<DhProcessCategory> categoryList, String proName, 
            Integer pageNum, Integer pageSize) {
        if (categoryList == null || categoryList.isEmpty()) {
            return ServerResponse.createByErrorMessage("列表参数错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        
        // 获取所有的子分类
        List<DhProcessMeta> metalist = dhProcessMetaMapper.listByCategoryListAndProName(categoryList, proName);
        PageInfo<List<DhProcessMeta>> pageInfo = new PageInfo(metalist);
        return ServerResponse.createBySuccess(pageInfo);
    }
    
    
    public ServerResponse createDhProcessMeta(DhProcessMeta dhProcessMeta) {
        if (StringUtils.isBlank(dhProcessMeta.getCategoryUid()) || StringUtils.isBlank(dhProcessMeta.getProAppId())
                || StringUtils.isBlank(dhProcessMeta.getProUid()) || StringUtils.isBlank(dhProcessMeta.getProName())) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        
        DhProcessCategory category = dhProcessCategoryMapper.queryByCategoryUid(dhProcessMeta.getCategoryUid());
        if (category == null) {
            return ServerResponse.createByErrorMessage("此分类不存在");
        }
        
        int countRow = dhProcessMetaMapper.countByProAppIdAndProUid(dhProcessMeta.getProAppId(), dhProcessMeta.getProUid());
        if (countRow > 0) {
            return ServerResponse.createByErrorMessage("流程元数据已经设置了分类，不能重复配置");
        }
        if (dhProcessMeta.getProName().length() > 50) {
            return ServerResponse.createByErrorMessage("流程名过长");
        }
        
        // 查找分类下有没有重名的元数据
        DhProcessMeta metaSelective = new DhProcessMeta();
        metaSelective.setProName(dhProcessMeta.getProName());
        metaSelective.setCategoryUid(dhProcessMeta.getCategoryUid());
        if (dhProcessMetaMapper.listByDhProcessMetaSelective(metaSelective).size() > 0) {
            return ServerResponse.createByErrorMessage("分类下存在同名的元数据，请重新命名");
        }
        
        dhProcessMeta.setProMetaUid(EntityIdPrefix.DH_PROCESS_META + UUID.randomUUID().toString());
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhProcessMeta.setCreator(currUser);
        countRow = dhProcessMetaMapper.save(dhProcessMeta);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("添加失败");
        }
        
    }
    
    public ServerResponse renameDhProcessMeta(String metaUid, String newName) {
        if (StringUtils.isBlank(metaUid) || StringUtils.isBlank(newName)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        newName = newName.trim();
        
        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(metaUid);
        if (dhProcessMeta == null) {
            return ServerResponse.createByErrorMessage("此流程元数据不存在");
        } 
        
        if (newName.equals(dhProcessMeta.getProName())) {
            return ServerResponse.createBySuccess();
        }
        
        DhProcessMeta metaSelective = new DhProcessMeta();
        metaSelective.setProName(newName);
        metaSelective.setCategoryUid(dhProcessMeta.getCategoryUid());
        if (dhProcessMetaMapper.listByDhProcessMetaSelective(metaSelective).size() > 0) {
            return ServerResponse.createByErrorMessage("分类下存在同名的元数据，请重新命名");
        }
        
        String updator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        dhProcessMeta.setUpdateUser(updator);
        dhProcessMeta.setProName(newName);
        int countRow = dhProcessMetaMapper.updateByProMetaUidSelective(dhProcessMeta);
        
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("修改失败");
        }
    }
    
    @Transactional
    public ServerResponse removeProcessMeta(String uids) {
        String[] uidArr = uids.split(";");
        // todo 判断能否删除元数据
        for (String uid : uidArr) {
            DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(uid);
            if (dhProcessMeta == null) {
                throw new PlatformException("找不到指定的元数据");
            }
            int countRow = dhProcessMetaMapper.removeByProMetaUid(uid);
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public List<DhProcessMeta> listAll() {
        
        return dhProcessMetaMapper.listAll();
    }
    
}
