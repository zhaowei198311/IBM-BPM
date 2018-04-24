package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.enginedao.LswSnapshotMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.BpmGlobalConfig;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionParticipateType;
import com.desmart.desmartbpm.enums.DhProcessDefinitionStatus;
import com.desmart.desmartbpm.exception.PermissionException;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.util.DateFmtUtils;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartbpm.util.rest.RestUtil;
import com.desmart.desmartbpm.vo.DhProcessDefinitionVo;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.github.pagehelper.PageInfo;

@Service
public class DhProcessDefinitionServiceImpl implements DhProcessDefinitionService {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessDefinitionServiceImpl.class);

    @Autowired
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    @Autowired
    private DhProcessMetaMapper dhProcessMetaMapper;
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private LswSnapshotMapper lswSnapshotMapper;
    @Autowired
    private BpmProcessSnapshotService bpmProcessSnapshotService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private DhObjectPermissionService dhObjectPermissionService;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;


    public ServerResponse listProcessDefinitionsIncludeUnSynchronized(String metaUid, Integer pageNum, Integer pageSize) {
        if (StringUtils.isBlank(metaUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }

        DhProcessMeta dhProcessMeta = dhProcessMetaMapper.queryByProMetaUid(metaUid);
        if (dhProcessMeta == null) {
            return ServerResponse.createByErrorMessage("流程元数据不存在");
        }

        DhProcessDefinition selective = new DhProcessDefinition();
        selective.setProUid(dhProcessMeta.getProUid());
        List<DhProcessDefinition> definitionInDb = dhProcessDefinitionMapper.listBySelective(selective);
        Set<String> versionInDb = new HashSet<>();
        for (DhProcessDefinition definition : definitionInDb) {
            versionInDb.add(definition.getProVerUid());
        }

        // 所有公开的流程中获得这个流程的公开版本
        List<Map<String, String>> exposedItemList = getAllExposedProcess();
        List<Map<String, String>> matchedItemList = getMatchedItem(exposedItemList, dhProcessMeta.getProUid(), dhProcessMeta.getProAppId());
        Iterator<Map<String, String>> iterator = matchedItemList.iterator();
        while (iterator.hasNext()) {
            Map<String, String> item = iterator.next();
            if (versionInDb.contains(item.get("snapshotId"))) {
                iterator.remove();
            }
        }

        List<DhProcessDefinitionVo> voList = new ArrayList<>();
        for (Map<String, String> item : matchedItemList) {
            DhProcessDefinitionVo vo = new DhProcessDefinitionVo();
            vo.setProName(dhProcessMeta.getProName());
            vo.setProAppId(dhProcessMeta.getProAppId());
            vo.setProUid(dhProcessMeta.getProUid());
            vo.setProVerUid(item.get("snapshotId"));
            vo.setProStatus("未同步");
            vo.setUpdator("");
            vo.setUpdateTime("");
            voList.add(vo);
        }

        for (DhProcessDefinition definition : definitionInDb) {
            DhProcessDefinitionVo vo = new DhProcessDefinitionVo();
            vo.setProName(dhProcessMeta.getProName());
            vo.setProAppId(dhProcessMeta.getProAppId());
            vo.setProUid(dhProcessMeta.getProUid());
            vo.setProVerUid(definition.getProVerUid());
            // 流程定义状态
            vo.setProStatus(DhProcessDefinitionStatus.codeOf(definition.getProStatus()).getValue());
            vo.setUpdator(definition.getUpdatorFullName());
            vo.setUpdateTime(DateFmtUtils.formatDate(definition.getLastModifiedDate(), "yyyy-MM-dd HH:mm:ss"));
            voList.add(vo);
        }

        List<DhProcessDefinitionVo> voToShow = new ArrayList<>();
        int total = voList.size();
        pageSize = pageSize == null ? 0 : Math.abs(pageSize);
        int startRow = (pageNum - 1) * pageSize;
        for (int i=0; i<voList.size(); i++) {
            if (i >= startRow) {
                voToShow.add(voList.get(i));
            }
            if (voToShow.size() >= pageSize) {
                break;
            }
        }

        // 装配流程版本信息
        assembleSnapshotInfo(voToShow);
        PageInfo pageInfo = new PageInfo(voToShow);
        pageInfo.setStartRow(startRow + 1);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);

        return ServerResponse.createBySuccess(pageInfo);

    }

    @Transactional
    public ServerResponse createDhProcessDefinition(String proAppId, String proUid, String proVerUid, HttpServletRequest request) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }

        bpmProcessSnapshotService.processModel(request, proUid, proVerUid, proAppId);
        
        DhProcessDefinition definitionSelective = new DhProcessDefinition();
        definitionSelective.setProVerUid(proVerUid);
        definitionSelective.setProAppId(proAppId);
        definitionSelective.setProUid(proUid);
        List<DhProcessDefinition> list = dhProcessDefinitionMapper.listBySelective(definitionSelective);
        if (list.size() == 0) { // 如果有环节信息，不再重复生成
            // 插入新流程定义记录
            DhProcessDefinition dhProcessDefinition = new DhProcessDefinition();
            dhProcessDefinition.setProUid(proUid);
            dhProcessDefinition.setProAppId(proAppId);
            dhProcessDefinition.setProVerUid(proVerUid);
            String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
            dhProcessDefinition.setCreateUser(creator);
            dhProcessDefinition.setProStatus(DhProcessDefinitionStatus.SETTING.getCode());
            int countRow = dhProcessDefinitionMapper.save(dhProcessDefinition);
            if (countRow > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("同步失败");
            }
        } else {
            return ServerResponse.createBySuccess();
        }
        
        
    }

    /**
     * 查看指定的流程定义是否存在
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @Override
    public ServerResponse isDhProcessDefinitionExist(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        DhProcessDefinition definitionSelective = new DhProcessDefinition(proAppId, proUid, proVerUid);
        List<DhProcessDefinition> list = dhProcessDefinitionMapper.listBySelective(definitionSelective);
        if (list.size() == 0) {
            return ServerResponse.createByErrorMessage("流程定义不存在");
        } else if (list.size() == 1) {
            return ServerResponse.createBySuccess(list.get(0));
        } else {
            return ServerResponse.createByErrorMessage("异常，存在一个以上此流程定义");
        }

    }

    /**
     * 获得所有公开的流程信息
     * @return Map中的属性 procAppName, procAppId, bpdName, bpdId, snapshotId, snapshotCreated, branchId
     */
    private List<Map<String, String>> getAllExposedProcess() {
        BpmGlobalConfig bpmcfg = bpmGlobalConfigService.getFirstActConfig();
        String url = bpmcfg.getBpmServerHost() + "rest/bpm/wle/v1/exposed/process";

        RestUtil restUtil = new RestUtil(bpmcfg);
        HttpReturnStatus procStatus = restUtil.doGet(url, new HashMap<String, Object>());
        restUtil.close();
        List<Map<String, String>> exposeItemList = new ArrayList<>();
        if (!BpmClientUtils.isErrorResult(procStatus)) {
            JSONObject jsoMsg = new JSONObject(procStatus.getMsg());
            JSONObject jsoData = jsoMsg.getJSONObject("data");
            JSONArray jayExpoItems = jsoData.optJSONArray("exposedItemsList");
            if (jayExpoItems != null) {
                for(int i = 0; i < jayExpoItems.length(); ++i) {
                    JSONObject jsoItem = jayExpoItems.getJSONObject(i);
                    Map<String, String> itemData = new HashMap<>();
                    String procAppId = jsoItem.optString("processAppID", "");
                    itemData.put("procAppName", jsoItem.optString("processAppName", ""));
                    itemData.put("procAppId", procAppId);
                    itemData.put("bpdName", jsoItem.optString("display", ""));
                    itemData.put("bpdId", jsoItem.optString("itemID", ""));
                    itemData.put("snapshotId", jsoItem.optString("snapshotID", ""));
                    itemData.put("snapshotCreated", jsoItem.optString("snapshotCreatedOn", ""));
                    itemData.put("branchId", jsoItem.optString("branchID", ""));
                    exposeItemList.add(itemData);
                }
            }
        }
        return exposeItemList;
    }

    /**
     * 从公开的流程中过滤出指定的流程
     * @param list
     * @return
     */
    private List<Map<String, String>> getMatchedItem(List<Map<String, String>> list, String bpdId, String appId) {
        Iterator<Map<String, String>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, String> item = iterator.next();
            if (!(bpdId.equals(item.get("bpdId")) && appId.equals(item.get("procAppId")))){
                iterator.remove();
            }
        }
        return list;
    }

    /**
     *
     */
    private void assembleSnapshotInfo(List<DhProcessDefinitionVo> volist) {
        List<LswSnapshot> snapshotList = lswSnapshotMapper.listAll();
        Map<String, LswSnapshot> map = new HashMap<>();
        for (LswSnapshot lswSnapshot : snapshotList) {
            map.put(lswSnapshot.getSnapshotId(), lswSnapshot);
        }

        for (DhProcessDefinitionVo vo : volist) {
            String temp = vo.getProVerUid();
            String snapshotUid = temp.substring(temp.indexOf(".") + 1, temp.length());
            LswSnapshot snapshot = map.get(snapshotUid);
            vo.setVerName(snapshot.getName());
            vo.setVerCreateTime(DateFmtUtils.formatDate(snapshot.getCreatedOn(), "yyyy-MM-dd HH:mm:ss"));
            vo.setIsActive("T".equals(snapshot.getIsActive()) ? "激活" : "未激活");
        }

    }

    @Transactional
    public ServerResponse updateDhProcessDefinition(DhProcessDefinition definition) {
        if (StringUtils.isBlank(definition.getProAppId()) || StringUtils.isBlank(definition.getProUid())
                || StringUtils.isBlank(definition.getProVerUid())) {
            return ServerResponse.createByErrorMessage("参数异常");
        }

        ServerResponse serverResponse = this.isDhProcessDefinitionExist(definition.getProAppId(), definition.getProUid(), definition.getProVerUid());
        if (!serverResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("找不到此流程定义");
        }
        String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        definition.setLastModifiedUser(user);
        
        // 人员发起权限
        String permissionStartUser = definition.getPermissionStartUser();
        ServerResponse response = dhObjectPermissionService.updatePermissionOfProcess(definition, permissionStartUser, 
                DhObjectPermissionParticipateType.USER.getCode(), DhObjectPermissionAction.START.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
       
        
        // 角色发起权限
        String permissionStartRole = definition.getPermissionStartRole();
        response = dhObjectPermissionService.updatePermissionOfProcess(definition, permissionStartRole, 
                DhObjectPermissionParticipateType.ROLE.getCode(), DhObjectPermissionAction.START.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
        
        // 角色组发起权限
        String permissionStartTeam = definition.getPermissionStartTeam();
        response = dhObjectPermissionService.updatePermissionOfProcess(definition, permissionStartTeam, 
                DhObjectPermissionParticipateType.TEAM.getCode(), DhObjectPermissionAction.START.getCode());
        if (!response.isSuccess()) {
            throw new PermissionException(response.getMsg());
        }
                
        int countRow = dhProcessDefinitionMapper.updateByProAppIdAndProUidAndProVerUidSelective(definition);
        if (countRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("更新流程定义失败");
        }

    }
    
    public ServerResponse<BpmActivityMeta> getFirstHumanBpmActivityMeta(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        BpmActivityMeta selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        selective.setActivityType("start");
        selective.setDeepLevel(0);
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (list.size() != 1) {
            return ServerResponse.createByErrorMessage("找不到开始环节(start event)");
        }
        BpmActivityMeta startMeta = list.get(0);
        selective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        selective.setActivityBpdId(startMeta.getActivityTo());
        list = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(selective);
        if (list.size() != 1) {
            return ServerResponse.createByErrorMessage("找不到开始环节后第一个人工环节");
        }
        return ServerResponse.createBySuccess(list.get(0));
    }

    @Override
    public LswSnapshot getLswSnapshotBySnapshotId(String snapshotId) {
        if (snapshotId.startsWith("2064")) {
            snapshotId = snapshotId.substring(snapshotId.indexOf(".") + 1);
        }
        return lswSnapshotMapper.queryBySnapshotId(snapshotId);
    }
    
    
}