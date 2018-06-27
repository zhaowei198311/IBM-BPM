package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.DataListUtils;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.enums.DhActivityConfAssignType;
import com.desmart.desmartbpm.enums.DhActivityConfRejectType;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.desmart.desmartportal.entity.CommonBusinessObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Service
public class BpmProcessSnapshotServiceImpl implements BpmProcessSnapshotService {
    private static final Logger LOG = LoggerFactory.getLogger(BpmProcessSnapshotService.class);

    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhActivityConfMapper dhActivityConfMapper;


    
    public void processModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId) {
        String bpmProcessSnapshotId = "";
        // 获得所有活动节点（事件节点、网关、人工活动...）和泳道对象
        JSONArray visualModelData = processVisualModel(request, bpdId, snapshotId, processAppId);
        BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
        
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(gcfg);
        HttpReturnStatus result = bpmProcessUtil.getProcessModel(processAppId, bpdId, snapshotId);
        
        if (StringUtils.isNotBlank(result.getMsg())) {
            JSONObject datas = (JSONObject)JSON.parse(result.getMsg());
            if (datas.containsKey("data")) {
                JSONObject data = datas.getJSONObject("data");
                if (data.containsKey("Diagram")) {
                    // 元素的信息（包含连接线信息）
                    JSONObject diagram = data.getJSONObject("Diagram");
                    parseDiagram(request, diagram, snapshotId, bpdId, visualModelData, processAppId, bpmProcessSnapshotId);
                }
            }
        }
        
    }

    public JSONArray processVisualModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId) {
        JSONArray results = new JSONArray();
        BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
        
        BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(gcfg);
        HttpReturnStatus result = bpmProcessUtil.getVisualModel(processAppId, bpdId, snapshotId);
        
        if (StringUtils.isNotBlank(result.getMsg())) {
            JSONObject datas = (JSONObject)JSON.parse(result.getMsg());
            if ("200".equalsIgnoreCase(datas.getString("status"))) {
                JSONObject data = datas.getJSONObject("data");
                if (data != null) {
                    results = data.getJSONArray("items");
                }
            }
        }

        if (results == null) {
            results = new JSONArray();
        }

        return results;
    }

    @Transactional
    /**
     * 根据RESTful API 返回的流程图信息解析为 环节表
     * @param request
     * @param diagram  图信息
     * @param snapshotId 快照版本
     * @param bpdId  流程图ID
     * @param visualModelData  元素信息
     * @param processAppId  流程应用id
     * @param bpmProcessSnapshotId   流程版本主键
     */
    public void parseDiagram(HttpServletRequest request, JSONObject diagram, String snapshotId, String bpdId,
                             JSONArray visualModelData, String processAppId, String bpmProcessSnapshotId) {
        List<BpmActivityMeta> basicActivityMetaList = new ArrayList<>();

        // 解析step中的每个元素
        if (diagram.containsKey("step")) {
            JSONArray step = diagram.getJSONArray("step");
            for (int i = 0; i < step.size(); ++i) {
                basicActivityMetaList.addAll(parseActivityMeta(request, step.getJSONObject(i), snapshotId, bpdId, visualModelData, processAppId, bpmProcessSnapshotId));
            }
        }// 至此newActivityMetas中已经是此流程全部的环节
        
        // 根据原先的parentActivityBpdId 生成出 parentActivityId 
        Map<String, String> actBpdIdAndActIdMap = new HashMap<>();
        for (BpmActivityMeta tempItem : basicActivityMetaList) {
            actBpdIdAndActIdMap.put(tempItem.getActivityBpdId(), tempItem.getActivityId());
        }
        
        List<BpmActivityMeta> externalNodeList = new ArrayList<>(); // bascicNode中的外链节点
        
        int sortNum = 0;
        for (BpmActivityMeta basticActivityMeta : basicActivityMetaList) {
            LOG.error(basticActivityMeta.getActivityName());
        	String activityBpdId = basticActivityMeta.getActivityBpdId();
        	// 设置父activityId 没有则为"0"
        	if ("0".equals(basticActivityMeta.getParentActivityBpdId())) {
        	    basticActivityMeta.setParentActivityId("0");
        	} else {
        	    basticActivityMeta.setParentActivityId(actBpdIdAndActIdMap.get(basticActivityMeta.getParentActivityBpdId()));
        	}
        	
        	basticActivityMeta.setSortNum(sortNum);
            bpmActivityMetaMapper.save(basticActivityMeta);
            if (isHumanActivity(basticActivityMeta)) {
                dhActivityConfMapper.insert(createDefaultActivityConf(basticActivityMeta.getActivityId()));
            }
            if ("CalledProcess".equals(basticActivityMeta.getBpmTaskType())) {
                // 如果这个环节是外链子流程，加入待处理列表
                externalNodeList.add(basticActivityMeta);
            }
            sortNum++;
        }
        
        // 引入外链流程的节点，找出所有外链节点
        if (externalNodeList.size() == 0) {
            return;
        }
        
        System.out.println("主流程中存在外接节点个数：" + externalNodeList.size());
        List<BpmActivityMeta> allNodeToInclude = new ArrayList<>();
        // 记录刚拉进来的时候
        
        List<BpmActivityMeta> newList = null;
        for (int i=0; i<externalNodeList.size(); i++) {
            allNodeToInclude.addAll(includeCalledProcess(externalNodeList.get(i)));
        }
        System.out.println("需要被纳入流程的总环节数：" + allNodeToInclude.size());
        
        for (BpmActivityMeta item : allNodeToInclude) {
            item.setSortNum(sortNum);
            bpmActivityMetaMapper.save(item);
            sortNum++;
        }
        
        
    }
    
    
    /**
     * 找出流程图中的外链环节的所有元素
     * @param externalNode  作为子流程标识的节点
     * @return 经过修改的外链流程的所有环节
     */
    private List<BpmActivityMeta> includeCalledProcess(BpmActivityMeta externalNode) {
        // 找到外链流程的所有节点
        BpmActivityMeta metaSelective = new BpmActivityMeta(externalNode.getProAppId(), externalNode.getExternalId(), externalNode.getSnapshotId());
        List<BpmActivityMeta> nodesSelected = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        
        List<BpmActivityMeta> nodeToIncludeList = DataListUtils.cloneList(nodesSelected, BpmActivityMeta.class);
        
        if (nodeToIncludeList.size() == 0) {
            throw new PlatformException("外链的流程不存在，或未被同步, 外链流程bpd_id: " + externalNode.getExternalId() + ", snapshot_id: " + externalNode.getSnapshotId());
        }
        
        // 为同步的节点设置新的主键并记录老的主键
        String temp = null;
        Map<String, String> oldActIdAndNewActIdMap = new HashMap<>(); // 记录新老主键对应关系
        for (BpmActivityMeta nodeToInclude : nodeToIncludeList) {
            temp = nodeToInclude.getActivityId();
            nodeToInclude.setActivityId(EntityIdPrefix.BPM_ACTIVITY_META + UUID.randomUUID().toString());
            oldActIdAndNewActIdMap.put(temp, nodeToInclude.getActivityId());
        }
        
        for (BpmActivityMeta nodeToInclude : nodeToIncludeList) {
            // 将所有的节点拷贝进来， 更新bpdId，更新层级,更新父节点
            nodeToInclude.setBpdId(externalNode.getBpdId());
            nodeToInclude.setDeepLevel(nodeToInclude.getDeepLevel() + externalNode.getDeepLevel() + 1);
            // 设置新的父节点
            if ("0".equals(nodeToInclude.getParentActivityId())) {
                nodeToInclude.setParentActivityId(externalNode.getActivityId());
            } else {
                nodeToInclude.setParentActivityId(oldActIdAndNewActIdMap.get(nodeToInclude.getParentActivityId()));
            }
            
        }
        
        return nodeToIncludeList;
    }

    /**
     * 将流程图(Diagram)中一个step解析为BpmActivityMeta，可能是一个或多个(子流程)
     * @param request
     * @param stepElement
     * @param snapshotId
     * @param bpdId
     * @param visualModelData
     * @param processAppId
     * @param bpmProcessSnapshotId
     * @return
     */
    public List<BpmActivityMeta> parseActivityMeta(HttpServletRequest request, JSONObject stepElement, String snapshotId, String bpdId,
                                                   JSONArray visualModelData, String processAppId, String bpmProcessSnapshotId) {
        List<BpmActivityMeta> bpmActivityMetas = new ArrayList<BpmActivityMeta>();
        String type = stepElement.getString("type");
        String activityType = stepElement.getString("activityType");
        String id = stepElement.getString("ID"); // 元素id
        String name = stepElement.getString("name");  // 活动命名
        String activityTo = "";
        String externalID = null;
        String loopType = null;
        String bpmTaskType = null;
        String miOrder = null;
        JSONArray lines = stepElement.getJSONArray("lines");  // 连接线信息
        Object obj;
        Iterator var20;
        JSONObject tmp;
        
        // 设置 activityTo
        if (lines != null) {
            for(var20 = lines.iterator(); var20.hasNext(); activityTo = activityTo + tmp.getString("to") + ",") {
                obj = var20.next();
                tmp = (JSONObject)obj;
            }
            if (activityTo.indexOf(",") > 0) { // 去除多余的","
                activityTo = activityTo.substring(0, activityTo.length() - 1);
            }
        }

        if ("activity".equals(type)) { // 如果是活动节点
            if ("subProcess".equals(activityType)) {  // 如果是子流程
                String poId = "";
                Iterator var29 = visualModelData.iterator();

                while (var29.hasNext()) {
                    JSONObject tmpObj = (JSONObject)var29.next();
                    if (id.equals(tmpObj.getString("id"))) {
                        poId = tmpObj.getString("poId");
                    }
                }

                if (stepElement.containsKey("diagram")) { // 子流程自己的图信息
                    JSONObject diagram = stepElement.getJSONObject("diagram");
                    List<BpmActivityMeta> subBpmActivityMetas = parseSubProcess(request, processAppId, snapshotId,
                            bpdId, poId, id, diagram, bpmProcessSnapshotId, 1);
                    Iterator var23 = subBpmActivityMetas.iterator();
                    // 为子流程中的环节设置poId
                    while(var23.hasNext()) {
                        BpmActivityMeta actyMeta = (BpmActivityMeta)var23.next();
                        actyMeta.setPoId(poId);
                    }

                    bpmActivityMetas.addAll(subBpmActivityMetas);
                }
            } else if ("subBpd".equals(activityType)) { // 如果是外链流程，设置外联流程的bpdId
                externalID = stepElement.getString("externalID");
            }
        }

        var20 = visualModelData.iterator();

        while (var20.hasNext()) {
            obj = var20.next();
            tmp = (JSONObject)obj;
            if (id.equals(tmp.getString("id"))) {
                activityType = tmp.getString("type");
                loopType = tmp.getString("loopType");
                bpmTaskType = tmp.getString("bpmn2TaskType");
                miOrder = tmp.getString("MIOrdering");
                break;
            }
        }

        try {
            BpmActivityMeta temp = bpmActivityMetaService.getBpmActivityMeta(id, name, snapshotId, bpdId, type, activityType, "0", activityTo, externalID, loopType, bpmTaskType, bpmProcessSnapshotId, miOrder, 0, processAppId);
            bpmActivityMetas.add(temp);
        } catch (Exception var24) {
            LOG.error(var24.getMessage(), var24);
        }

        return bpmActivityMetas;
    }


    /**
     * 解析子流程 返回环节配置，可能是一个可能是多个
     * @param request
     * @param processAppId
     * @param snapshotId
     * @param bpdId
     * @param poId
     * @param parentActivityBpdId
     * @param diagram
     * @param bpmProcessSnapshotId
     * @param deepLevel
     * @return
     */
    public List<BpmActivityMeta> parseSubProcess(HttpServletRequest request, String processAppId, String snapshotId,
                                                 String bpdId, String poId, String parentActivityBpdId, JSONObject diagram,
                                                 String bpmProcessSnapshotId, Integer deepLevel) {
        List<BpmActivityMeta> subBpmActivityMetas = new ArrayList();
        JSONArray visualModelData = processVisualModel(request, poId, snapshotId, processAppId);
        if (diagram.containsKey("step")) {
            JSONArray stepArray = diagram.getJSONArray("step");

            for (int i = 0; i < stepArray.size(); ++i) {
                JSONObject step = stepArray.getJSONObject(i);
                String type = step.getString("type");
                String activityType = step.getString("activityType");
                String id = step.getString("ID");
                String name = step.getString("name");
                String activityTo = "";
                String externalID = null;
                String loopType = null;
                String bpmTaskType = null;
                String miOrder = null;
                JSONArray lines = step.getJSONArray("lines");
                Object obj;
                Iterator var26;
                JSONObject tmp;
                if (lines != null) {
                    for (var26 = lines.iterator(); var26.hasNext(); activityTo = activityTo + tmp.getString("to") + ",") {
                        obj = var26.next();
                        tmp = (JSONObject) obj;
                    }

                    if (activityTo.indexOf(",") > 0) {
                        activityTo = activityTo.substring(0, activityTo.length() - 1);
                    }
                }

                if ("activity".equals(type)) {
                    if (!"subProcess".equals(activityType)) {
                        if ("subBpd".equals(activityType)) {
                            externalID = step.getString("externalID");
                        }
                    } else {
                        String poId_tmp = "";
                        Iterator var34 = visualModelData.iterator();

                        while (var34.hasNext()) {
                            Object obj1 = var34.next();
                            JSONObject tmp1 = (JSONObject) obj1;
                            if (id.equals(tmp1.getString("id"))) {
                                poId_tmp = tmp1.getString("poId");
                            }
                        }

                        if (step.containsKey("diagram")) {
                            JSONObject diagram_tmp = step.getJSONObject("diagram");
                            List<BpmActivityMeta> subActivityMetas = parseSubProcess(request, processAppId, snapshotId, bpdId, poId_tmp, id, diagram_tmp, bpmProcessSnapshotId, deepLevel + 1);
                            subBpmActivityMetas.addAll(subActivityMetas);
                        }
                    }
                }

                var26 = visualModelData.iterator();

                while (var26.hasNext()) {
                    obj = var26.next();
                    tmp = (JSONObject) obj;
                    if (id.equals(tmp.getString("id"))) {
                        activityType = tmp.getString("type");
                        loopType = tmp.getString("loopType");
                        bpmTaskType = tmp.getString("bpmn2TaskType");
                        miOrder = tmp.getString("MIOrdering");
                        break;
                    }
                }

                try {
                    BpmActivityMeta tmp2 = bpmActivityMetaService.getBpmActivityMeta(id, name, snapshotId, bpdId, type,
                            activityType, parentActivityBpdId, activityTo, externalID, loopType, bpmTaskType, bpmProcessSnapshotId,
                            miOrder, deepLevel, processAppId);
                    subBpmActivityMetas.add(tmp2);
                } catch (Exception var29) {
                    LOG.error(var29.getMessage(), var29);
                }
            }

        }
        return subBpmActivityMetas;
    }

    /**
     * 根据活动的bpdId,和版本的主键查看数据库中是否已有此环节
     * @param bpdId
     * @param snapshotUid
     * @return
     */
    private boolean isActivityMetaExists(String bpdId, String snapshotUid) {
        List<BpmActivityMeta> list = bpmActivityMetaMapper.queryByActivityBpdIdAndSnapshotUid(bpdId, snapshotUid);
        return list.size() > 0;
    }
    
    /**
     * 检查这个环节是不是人工环节
     * @return
     */
    private boolean isHumanActivity(BpmActivityMeta bpmActivityMeta) {
        return "UserTask".equalsIgnoreCase(bpmActivityMeta.getBpmTaskType());
    }
    
    /**
     * 根据环节id生成一个默认配置
     * @param activityId 活动id
     * @return
     */
    private DhActivityConf createDefaultActivityConf(String activityId) {
        DhActivityConf conf = new DhActivityConf();
        conf.setActcUid(EntityIdPrefix.DH_ACTIVITY_CONF + UUID.randomUUID().toString());
        conf.setActivityId(activityId);
        conf.setActcSort(1);
        conf.setActcTime(1.0); // 处理时间 1天
        conf.setActcTimeunit(Const.TimeUnit.DAY);  
        conf.setActcAssignType(DhActivityConfAssignType.NONE.getCode());  // 默认没有处理人
        conf.setActcAssignVariable(CommonBusinessObject.NEXT_OWNER_VARNAME[0]);  
        conf.setSignCountVarname(CommonBusinessObject.OWNER_SIGN_COUNT[0]);
        conf.setActcCanEditAttach(Const.Boolean.TRUE); // 是否可以编辑附件
        conf.setActcCanUploadAttach(Const.Boolean.TRUE); // 是否可以上传附件
        conf.setActcCanDelegate(Const.Boolean.TRUE); // 是否允许代理
        conf.setActcCanDeleteAttach(Const.Boolean.TRUE); // 是否可以删除附件
        conf.setActcCanMessageNotify(Const.Boolean.TRUE); // 是否短信通知
        conf.setActcCanMailNotify(Const.Boolean.TRUE); // 是否邮件通知
        conf.setActcMailNotifyTemplate(null); // 邮件通知模版
        conf.setActcCanReject(Const.Boolean.FALSE); // 是否允许驳回
        conf.setActcRejectType(DhActivityConfRejectType.TO_PRE_ACTIVITY.getCode()); // 驳回类型
        conf.setActcCanRevoke(Const.Boolean.TRUE); // 是否可以取回
        conf.setActcCanAutocommit(Const.Boolean.FALSE); // 是否允许自动提交
        conf.setActcCanAdd(Const.Boolean.TRUE); // 是否允许加签
        conf.setActcCanUserToField(Const.Boolean.FALSE); // 略
        conf.setActcUserToField(null);
        conf.setActcCanAuditToField(Const.Boolean.FALSE);
        conf.setActcAuditToField(null);
        conf.setActcCanApprove(Const.Boolean.TRUE);  // 是否允许审批
        conf.setActcOuttimeTrigger(null); // 超时触发器
        conf.setActcOuttimeTemplate(null); // 超时通知模版
        conf.setActcDescription(null); 
        conf.setActcDefTitle(null);
        conf.setActcDefSubjectMessage(null);
        conf.setActcDefMessage(null);
        conf.setActcDefDescription(null);
        conf.setActcAlert(null);
        conf.setActcPriorityVariable(null);
        conf.setActcCanCancel(null);
        conf.setActcCanPause(null);
        conf.setActcCanSkip(null);
        conf.setActcCanChooseUser(Const.Boolean.FALSE);
        conf.setActcChooseableHandlerType(DhActivityConfAssignType.ALL_USER.getCode());
        conf.setActcCanTransfer(Const.Boolean.FALSE); // 是否允许抄送
        conf.setActcResponsibility(null); // 职责描述
        conf.setActcCanSkipFromReject(Const.Boolean.FALSE); // 是否允许驳回后直接提交回来
        return conf;
    }
    

    

    
    
}