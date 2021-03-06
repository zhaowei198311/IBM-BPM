package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import org.springframework.util.CollectionUtils;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityAssignMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.dao.DhActivityRejectMapper;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.enginedao.LswSnapshotMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityAssign;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhActivityReject;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import com.desmart.desmartbpm.enums.DhObjectPermissionParticipateType;
import com.desmart.desmartbpm.enums.DhProcessDefinitionStatus;
import com.desmart.desmartbpm.exception.PermissionException;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.util.DateFmtUtils;
import com.desmart.desmartbpm.util.http.BpmClientUtils;
import com.desmart.desmartbpm.util.rest.RestUtil;
import com.desmart.desmartbpm.vo.DhProcessDefinitionVo;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
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
    @Autowired
    private DhActivityAssignMapper dhActivityAssignMapper;
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;
    @Autowired
    private DhActivityRejectMapper dhActivityRejectMapper;
    
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
        // 同步环节
        bpmProcessSnapshotService.processModel(request, proUid, proVerUid, proAppId);
        
        DhProcessDefinition definitionSelective = new DhProcessDefinition();
        definitionSelective.setProVerUid(proVerUid);
        definitionSelective.setProAppId(proAppId);
        definitionSelective.setProUid(proUid);
        List<DhProcessDefinition> list = dhProcessDefinitionMapper.listBySelective(definitionSelective);
        String currentUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        if (list.size() == 0) { 
            // 插入新流程定义记录
            DhProcessDefinition dhProcessDefinition = new DhProcessDefinition();
            dhProcessDefinition.setProUid(proUid);
            dhProcessDefinition.setProAppId(proAppId);
            dhProcessDefinition.setProVerUid(proVerUid);
            dhProcessDefinition.setCreateUser(currentUser);
            dhProcessDefinition.setProStatus(DhProcessDefinitionStatus.SETTING.getCode());
            int countRow = dhProcessDefinitionMapper.save(dhProcessDefinition);
            if (countRow > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("同步失败");
            }
        } else {
            // 更新流程定义状态
            DhProcessDefinition selective = new DhProcessDefinition(proAppId, proUid, proVerUid);
            selective.setProStatus(DhProcessDefinitionStatus.SETTING.getCode());
            selective.setLastModifiedUser(currentUser);
            dhProcessDefinitionMapper.updateByProAppIdAndProUidAndProVerUidSelective(selective);
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

	@Override
	public ServerResponse listProcessDefinitionById(DhProcessDefinition dhProcessDefinition) {
		List<DhProcessDefinition> dpd = dhProcessDefinitionMapper.listById(dhProcessDefinition);
		return ServerResponse.createBySuccess(dpd);
	}

	@Override
	public ServerResponse copySimilarProcess(Map<String, Object> mapId) {
		try {
			// 同类流程Id
			String proUid = mapId.get("proUid").toString();
			String proVerUid = mapId.get("proVerUid").toString();
			String proAppId = mapId.get("proAppId").toString();
			
			// 选择新流程Id
			String proUidNew = mapId.get("proUidNew").toString();
			String proVerUidNew = mapId.get("proVerUidNew").toString();
			String proAppIdNew = mapId.get("proAppIdNew").toString();
			// 同步流程
			synchronizationProcess(proUid, proVerUid, proAppId, proUidNew, proVerUidNew, proAppIdNew);
			
			Map<String, Object> idS = new HashMap<>();
			idS.put("proUid", proUid);
			idS.put("proVerUid", proVerUid);
			idS.put("proAppId", proAppId);
			idS.put("proUidNew", proUidNew);
			idS.put("proVerUidNew", proVerUidNew);
			idS.put("proAppIdNew", proAppIdNew);
			// 新旧流程相同的环节元素
			List<Map<String, Object>> similarBpmActivityMetaList = bpmActivityMetaMapper.listSimilarActivityMetaById(idS);
			// 同步config
			synchronizationConfig(similarBpmActivityMetaList);
			// 同步step
			synchronizationStep(similarBpmActivityMetaList);
			// 同步驳回环节
			synchronizationRule(similarBpmActivityMetaList, proUidNew, proVerUidNew, proAppIdNew);
			
			return ServerResponse.createBySuccess();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}

    @Override
    public ServerResponse<DhProcessDefinitionVo> getSynchronizedDhProcessDefinitionWithSnapshotInfo(
            String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        DhProcessDefinition dhProcessDefinition = getDhProcessDefinition(proAppId, proUid, proVerUid);
        if (dhProcessDefinition == null) {
            return ServerResponse.createByErrorMessage("找不到指定的流程定义");
        } 
        LswSnapshot lswSnapshot = getLswSnapshotBySnapshotId(dhProcessDefinition.getProVerUid());
        if (lswSnapshot == null) {
            return ServerResponse.createByErrorMessage("快照信息错误");
        }
        DhProcessDefinitionVo vo = new DhProcessDefinitionVo();
        vo.setProName(dhProcessDefinition.getProName());
        vo.setProVerUid(dhProcessDefinition.getProVerUid());
        vo.setProUid(dhProcessDefinition.getProUid());
        vo.setProAppId(dhProcessDefinition.getProAppId());
        vo.setVerName(lswSnapshot.getName());
        vo.setVerCreateTime(DateFmtUtils.formatDate(lswSnapshot.getCreatedOn(), "yyyy-MM-dd HH:mm:ss"));
        vo.setIsActive("T".equals(lswSnapshot.getIsActive()) ? "激活" : "未激活");
        vo.setProStatus(DhProcessDefinitionStatus.codeOf(dhProcessDefinition.getProStatus()).getValue());
        vo.setUpdator(dhProcessDefinition.getUpdatorFullName());
        vo.setUpdateTime(DateFmtUtils.formatDate(dhProcessDefinition.getLastModifiedDate(), "yyyy-MM-dd HH:mm:ss"));
        return ServerResponse.createBySuccess(vo);
    }
    
    @Override
    public DhProcessDefinition getDhProcessDefinition(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return null;
        }
        DhProcessDefinition selective = new DhProcessDefinition(proAppId, proUid, proVerUid);
        List<DhProcessDefinition> list = dhProcessDefinitionMapper.listBySelective(selective);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
    
    // 同步流程
    public void synchronizationProcess(String proUid, String proVerUid, String proAppId, 
    								String proUidNew, String proVerUidNew, String proAppIdNew){
    	// 同类流程信息
		DhProcessDefinition similarProcess = new DhProcessDefinition();
		similarProcess = dhProcessDefinitionMapper.getProcessById(proUid, proVerUid, proAppId);
		// 新流程
		DhProcessDefinition processNew = new DhProcessDefinition();
		processNew = dhProcessDefinitionMapper.getProcessById(proUidNew, proVerUidNew, proAppIdNew);
		similarProcess.setProUid(processNew.getProUid());
		similarProcess.setProVerUid(processNew.getProVerUid());
		similarProcess.setProAppId(processNew.getProAppId());
		similarProcess.setProStatus(processNew.getProStatus());
//    				similarProcess.setLastModifiedDate(processNew.getLastModifiedDate());
		similarProcess.setLastModifiedUser(processNew.getLastModifiedUser());
		similarProcess.setCreateDate(processNew.getCreateDate());
		similarProcess.setCreateUser(processNew.getCreateUser());
		// 更新DH_PROCESS_DEFINITION表
		dhProcessDefinitionMapper.updateByProAppIdAndProUidAndProVerUidSelective(similarProcess);

		List<DhObjectPermission> dhObjectPermissionList = dhProcessDefinitionMapper.listDhObjectPermissionById(proUid, proVerUid, proAppId);
		if (dhObjectPermissionList.size() > 0) {
			// 删除新流程旧的权限信息，重新添加
			dhProcessDefinitionMapper.deleteDhObjectPermissionById(proUidNew, proVerUidNew, proAppIdNew);
			
			for (DhObjectPermission dop : dhObjectPermissionList) {
				dop.setOpUid("obj_perm:"+UUID.randomUUID().toString());
				dop.setProUid(proUidNew);
				dop.setProVerUid(proVerUidNew);
				dop.setProAppId(proAppIdNew);
				// 新增 DH_OBJECT_PERMISSION表
				dhProcessDefinitionMapper.saveDhObjectPermissionById(dop);
			}
		}
    }
    
    // 同步config
    public void synchronizationConfig(List<Map<String, Object>> similarBpmActivityMetaList){
    	for (Map<String, Object> map : similarBpmActivityMetaList) {
			// 老流程配置
    		DhActivityConf OldDhActivityConf = dhActivityConfMapper.getByActivityId(map.get("ACTIVITY_ID").toString());
			// 新流程配置
			DhActivityConf NewDhActivityConf = dhActivityConfMapper.getByActivityId(map.get("ACTIVITY_ID_1").toString());
			if (NewDhActivityConf != null) {
				// 拷贝老流程配置信息（DH_ACTIVITY_CONF）
				OldDhActivityConf.setActcUid(NewDhActivityConf.getActcUid());
				OldDhActivityConf.setActivityId(NewDhActivityConf.getActivityId());
				dhActivityConfMapper.updateByPrimaryKey(OldDhActivityConf);
			}			
		}
    }
    // 默认处理人，可选处理人，超时通知环节
    public void synchronizationStep(List<Map<String, Object>> similarBpmActivityMetaList){
    	// 根据ACTIVITY_ID,清除新流程DH_ACTIVITY_ASSIGN表中信息
    	for (Map<String, Object> map : similarBpmActivityMetaList) {
			dhActivityAssignMapper.deleteByActivityId(map.get("ACTIVITY_ID_1").toString());
		}
    	// 拷贝旧流程DH_ACTIVITY_ASSIGN表中信息
    	for (Map<String, Object> map : similarBpmActivityMetaList) {
    		List<DhActivityAssign> oldDhActivityAssignList = dhActivityAssignMapper.listByActivityId(map.get("ACTIVITY_ID").toString());
    		for (DhActivityAssign dhActivityAssign : oldDhActivityAssignList) {
				dhActivityAssign.setActaUid("acta:"+UUID.randomUUID().toString());
    			dhActivityAssign.setActivityId(map.get("ACTIVITY_ID_1").toString());
				dhActivityAssignMapper.insert(dhActivityAssign);
			}
		}
    }
    // 同步驳回环节
    public void synchronizationRule(List<Map<String, Object>> similarBpmActivityMetaList, String proUidNew, String proVerUidNew, String proAppIdNew){
    	// 根据ACTIVITY_ID,清除新流程 DH_ACTIVITY_REJECT表中信息
    	for (Map<String, Object> map : similarBpmActivityMetaList) {
			dhActivityRejectMapper.deleteByActivityId(map.get("ACTIVITY_ID_1").toString());
		}
    	// 拷贝可驳回环节信息
    	for (Map<String, Object> map : similarBpmActivityMetaList) {
			List<DhActivityReject> OldDhActivityReject = dhActivityRejectMapper.listByActivityId(map.get("ACTIVITY_ID").toString());
			for (DhActivityReject dhActivityReject : OldDhActivityReject) {
				// 老流程 当前环节可驳回的流程名
				String activityName = dhActivityReject.getActivityName();
				Map<String, Object> idS = new HashMap<>();
				idS.put("proUid", proUidNew);
				idS.put("proVerUid", proVerUidNew);
				idS.put("proAppId", proAppIdNew);
				idS.put("activityName", activityName);
				// 新流程环节
				BpmActivityMeta newBpmActivityMeta = bpmActivityMetaMapper.getActivityIdByIdAndName(idS);
				DhActivityReject dar = new DhActivityReject();
				dar.setActrUid("act_rej:"+UUID.randomUUID());
				dar.setActivityId(map.get("ACTIVITY_ID_1").toString());
				dar.setActrRejectActivity(newBpmActivityMeta.getActivityId());
				dhActivityRejectMapper.insert(dar);
			}
		}
    }
}