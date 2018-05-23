package com.desmart.desmartbpm.service.impl;

import java.text.MessageFormat;
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
import com.desmart.desmartbpm.util.rest.RestUtil;
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
        String host = gcfg.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/processModel/{0}?{1}";
        String params = "processAppId=" + processAppId;
        if (StringUtils.isNotBlank(snapshotId)) {
            params = params + "&snapshotId=" + snapshotId;
        }
        url = MessageFormat.format(url, bpdId, params);
        Map<String, Object> pmap = new HashMap<>();
        //BpmClientUtils bpmClientUtil = new BpmClientUtils(gcfg, true, request.getServletContext());
        //HttpReturnStatus result = bpmClientUtil.doGet(request, url, pmap);
        RestUtil restUtil = new RestUtil(gcfg);
        HttpReturnStatus result = restUtil.doGet(url, pmap);
        restUtil.close();
        
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
        String host = gcfg.getBpmServerHost();
        host = host.endsWith("/") ? host : host + "/";
        String url = host + "rest/bpm/wle/v1/visual/processModel/{0}?{1}";
        String params = "projectId=" + processAppId;
        if (StringUtils.isNotBlank(snapshotId)) {
            params = params + "&snapshotId=" + snapshotId;
        }
        url = MessageFormat.format(url, bpdId, params);
        Map<String, Object> pmap = new HashMap<>();
        //BpmClientUtils bpmClientUtil = new BpmClientUtils(gcfg, true, request.getServletContext());
        //HttpReturnStatus result = bpmClientUtil.doGet(request, url, pmap);
        //bpmClientUtil.closeClient();
        RestUtil restUtil = new RestUtil(gcfg);
        HttpReturnStatus result = restUtil.doGet(url, pmap);
        restUtil.close();
        
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
     * 根据rest 返回的流程图信息解析为 环节表
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
        List<BpmActivityMeta> newActivityMetas = new ArrayList<BpmActivityMeta>();

        // 解析step中的每个元素
        if (diagram.containsKey("step")) {
            JSONArray step = diagram.getJSONArray("step");
            for (int i = 0; i < step.size(); ++i) {
                newActivityMetas.addAll(parseActivityMeta(request, step.getJSONObject(i), snapshotId, bpdId, visualModelData, processAppId, bpmProcessSnapshotId));
            }
        }// 至此newActivityMetas中已经是此流程全部的环节
        
        
        // 查询出表中已存在的此版本信息
        BpmActivityMeta metaSelective = new BpmActivityMeta();
        metaSelective.setBpdId(bpdId);
        metaSelective.setProAppId(processAppId);
        metaSelective.setSnapshotId(snapshotId);
        List<BpmActivityMeta> oldActivityMetas = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        List<BpmActivityMeta> delActivityMetas = new ArrayList<BpmActivityMeta>();
        // 需要删除的环节
        delActivityMetas.addAll(oldActivityMetas);
        int sortNum = 0;
        for (BpmActivityMeta newMeta : newActivityMetas) {
            LOG.error(newMeta.getActivityName());
        	String activityBpdId = newMeta.getActivityBpdId();
        	// 如果待删meta里有这个activityBpdId的元素，就移除
        	for(int i = 0; i < oldActivityMetas.size(); ++i) {
                BpmActivityMeta oldMeta = (BpmActivityMeta)oldActivityMetas.get(i);
                if (activityBpdId.equalsIgnoreCase(oldMeta.getActivityBpdId())) {
                    delActivityMetas.remove(oldMeta);
                }
            }
            // 查看这个节点是否存在
            metaSelective.setActivityBpdId(activityBpdId);
        	List<BpmActivityMeta> activityMetaExists = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        	
            if (activityMetaExists.size() > 0) {
            	BpmActivityMeta bpmActivityMeta = activityMetaExists.get(0);
                bpmActivityMeta.setSnapshotId(newMeta.getSnapshotId());
                bpmActivityMeta.setBpdId(newMeta.getBpdId());
                bpmActivityMeta.setActivityName(newMeta.getActivityName());
                bpmActivityMeta.setType(newMeta.getType());
                bpmActivityMeta.setActivityType(newMeta.getActivityType());
                bpmActivityMeta.setActivityTo(newMeta.getActivityTo());
                bpmActivityMeta.setParentActivityBpdId(newMeta.getParentActivityBpdId());
                bpmActivityMeta.setExternalId(newMeta.getExternalId());
                bpmActivityMeta.setBpmTaskType(newMeta.getBpmTaskType());
                bpmActivityMeta.setLoopType(newMeta.getLoopType());
                bpmActivityMeta.setMiOrder(newMeta.getMiOrder());
                bpmActivityMeta.setHandleSignType(newMeta.getHandleSignType());
                bpmActivityMeta.setPoId(newMeta.getPoId());
                bpmActivityMeta.setDeepLevel(newMeta.getDeepLevel());
                bpmActivityMeta.setSortNum(sortNum);
                bpmActivityMetaMapper.updateByPrimaryKeySelective(bpmActivityMeta);
                // 查看环节是否人工环节
                if (isHumanActivity(bpmActivityMeta)) {
                    // 查看此活动环节是否有相关配置
                    if (dhActivityConfMapper.getByActivityId(bpmActivityMeta.getActivityId()) == null) {
                        dhActivityConfMapper.insert(createDefaultActivityConf(bpmActivityMeta.getActivityId()));
                    }
                }
            } else {
                newMeta.setSortNum(sortNum);
            	bpmActivityMetaMapper.save(newMeta);
            	if (isHumanActivity(newMeta)) {
                    dhActivityConfMapper.insert(createDefaultActivityConf(newMeta.getActivityId()));
                }
            }
            sortNum++;
        }

        if (delActivityMetas.size() > 0) {
        	bpmActivityMetaMapper.batchRemoveByPrimaryKey(delActivityMetas);
        }


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
        conf.setActcCanTransfer(Const.Boolean.FALSE); // 是否允许
        conf.setActcResponsibility(null); // 职责描述
        return conf;
    }
    
}