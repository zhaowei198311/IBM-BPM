package com.desmart.desmartbpm.service.impl;

import com.desmart.desmartbpm.enetity.BpmActivityMeta;
import com.desmart.desmartbpm.enetity.BpmGlobalConfig;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.BpmGlobalConfigService;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.util.BpmClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;

@Service
public class BpmProcessSnapshotServiceImpl implements BpmProcessSnapshotService{
    private static final Logger LOG = LoggerFactory.getLogger(BpmProcessSnapshotService.class);

    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;


    public void startSysncActivityMeta(HttpServletRequest request, List<String> snapshotIds) {

    }


    public void processModel(HttpServletRequest request, String bpdId, String snapshotId, String processAppId, String bpmProcessSnapshotId) {
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
        Map<String, Object> pmap = new HashMap();
        HttpReturnStatus result = (new BpmClientUtils(gcfg, true)).doGet(request, url, pmap);
        if (StringUtils.isNotBlank(result.getMsg())) {
            JSONObject datas = (JSONObject)JSON.parse(result.getMsg());
            if (datas.containsKey("data")) {
                JSONObject data = datas.getJSONObject("data");
                if (data.containsKey("Diagram")) {
                    // 元素的二分类细化
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
        Map<String, Object> pmap = new HashMap();
        HttpReturnStatus result = (new BpmClientUtils(gcfg, true)).doGet(request, url, pmap);
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

    public void parseDiagram(HttpServletRequest request, JSONObject diagram, String snapshotId, String bpdId,
                             JSONArray visualModelData, String processAppId, String bpmProcessSnapshotId) {
        List<BpmActivityMeta> newActivityMetas = new ArrayList();

        // 解析step中的元素
        if (diagram.containsKey("step")) {
            JSONArray step = diagram.getJSONArray("step");

            for(int i = 0; i < step.size(); ++i) {
                newActivityMetas.addAll(parseActivityMeta(request, step.getJSONObject(i), snapshotId, bpdId, visualModelData, processAppId, bpmProcessSnapshotId));
            }
        }



    }

    /**
     * 解析活动节点
     * @param request
     * @param step
     * @param snapshotId
     * @param bpdId
     * @param visualModelData
     * @param processAppId
     * @param bpmProcessSnapshotId
     * @return
     */
    public List<BpmActivityMeta> parseActivityMeta(HttpServletRequest request, JSONObject step, String snapshotId, String bpdId,
                                                   JSONArray visualModelData, String processAppId, String bpmProcessSnapshotId) {
        List<BpmActivityMeta> bpmActivityMetas = new ArrayList();
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
        Iterator var20;
        JSONObject tmp;
        if (lines != null) {
            for(var20 = lines.iterator(); var20.hasNext(); activityTo = activityTo + tmp.getString("to") + ",") {
                obj = var20.next();
                tmp = (JSONObject)obj;
            }

            if (activityTo.indexOf(",") > 0) {
                activityTo = activityTo.substring(0, activityTo.length() - 1);
            }
        }


        if ("activity".equals(type)) { // 如果是活动节点
            if ("subProcess".equals(activityType)) {  // 如果是子流程
                String poId = "";
                Iterator var29 = visualModelData.iterator();

                while(var29.hasNext()) {
                    JSONObject tmpObj = (JSONObject)var29.next();
                    if (id.equals(tmpObj.getString("id"))) {
                        poId = tmpObj.getString("poId");
                    }
                }

                if (step.containsKey("diagram")) { // 解析子流程的环节
                    JSONObject diagram = step.getJSONObject("diagram");
                    List<BpmActivityMeta> subBpmActivityMetas = parseSubProcess(request, processAppId, snapshotId,
                            bpdId, poId, id, diagram, bpmProcessSnapshotId, 1);
                    Iterator var23 = subBpmActivityMetas.iterator();

                    while(var23.hasNext()) {
                        BpmActivityMeta actyMeta = (BpmActivityMeta)var23.next();
                        actyMeta.setPoId(poId);
                    }

                    bpmActivityMetas.addAll(subBpmActivityMetas);
                }
            } else if ("subBpd".equals(activityType)) {
                externalID = step.getString("externalID");
            }
        }

        var20 = visualModelData.iterator();

        while(var20.hasNext()) {
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
            BpmActivityMeta temp = bpmActivityMetaService.getBpmActivityMeta(id, name, snapshotId, bpdId, type, activityType, "0", activityTo, externalID, loopType, bpmTaskType, bpmProcessSnapshotId, miOrder, 0);
            bpmActivityMetas.add(temp);
        } catch (Exception var24) {
            LOG.error(var24.getMessage(), var24);
        }

        return bpmActivityMetas;
    }


    /**
     * 解析子流程 返回环节配置
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
                            miOrder, deepLevel);
                    subBpmActivityMetas.add(tmp2);
                } catch (Exception var29) {
                    LOG.error(var29.getMessage(), var29);
                }
            }

        }
        return subBpmActivityMetas;
    }

}