package com.desmart.desmartbpm.service.impl;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.dao.BpmActivityMetaMapper;
import com.desmart.desmartbpm.dao.DatRuleMapper;
import com.desmart.desmartbpm.dao.DhGatewayLineMapper;
import com.desmart.desmartbpm.enginedao.LswBpdMapper;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DatRule;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.engine.LswBpd;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhGatewayLineService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.util.DateUtil;
import com.desmart.desmartbpm.util.UUIDTool;
import com.desmart.desmartbpm.util.rest.RestUtil;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@Service
public class DhGatewayLineServiceImpl implements DhGatewayLineService {
    private static final Logger LOG = LoggerFactory.getLogger(DhGatewayLineServiceImpl.class);
    @Autowired
    private BpmGlobalConfigService bpmGlobalConfigService;
    @Autowired
    private LswBpdMapper lswBpdMapper;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhGatewayLineMapper dhGatewayLineMapper;
    @Autowired
    private DatRuleMapper datRuleMapper;
    
    
    public ServerResponse generateGatewayLine(String proAppId, String proUid, String proVerUid) {
        if (StringUtils.isBlank(proAppId) || StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
            return ServerResponse.createByErrorMessage("参数异常");
        }
        DhProcessDefinition dhProcessDefinition = dhProcessDefinitionService.getDhProcessDefinition(proAppId, proUid, proVerUid);
        if (dhProcessDefinition == null) {
            return ServerResponse.createByErrorMessage("找不到此流程定义");
        }
        
        List<Map<String, Object>> prepareData = prepareData(proAppId, proUid, proVerUid);
        analysisData(prepareData, dhProcessDefinition);
        return ServerResponse.createBySuccess();
    }
    
    public boolean needGenerateGatewayLine(String proAppId, String proUid, String proVerUid) {
        BpmActivityMeta metaSelective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        metaSelective.setActivityType("gateway");
        List<BpmActivityMeta> gatewayList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        if (gatewayList.size() > 0) {
            BpmActivityMeta firstGateway = gatewayList.get(0);
            int count = dhGatewayLineMapper.countByActivityId(firstGateway.getActivityId());
            return count == 0;
        } else {
            return false;
        }
    }
    
    
    /**
     * 收集生成网关连接线需要的数据，Map中的值： visualModel（JSONObject）   byteData（字节数组）
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    private List<Map<String, Object>> prepareData(String proAppId, String proUid, String proVerUid) {
        List<JSONObject> visualModelList = new ArrayList<JSONObject>();
        getVisualModel(visualModelList, proAppId, proUid, proVerUid);
        System.out.println(visualModelList.size());
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (JSONObject visualModel : visualModelList) {
            String poId = visualModel.getString("poId");
            String versionId = visualModel.getString("poVersionId");
            Map<String, Object> map = new HashMap<>();
            poId = poId.substring(poId.indexOf(".") + 1);
            LswBpd lswBpd = lswBpdMapper.queryByBpdIdAndVersionId(poId, versionId);
            if (lswBpd != null) {
                map.put("visualModel", visualModel);
                map.put("byteData", lswBpd.getData());
            }
            dataList.add(map);
        }
        return dataList;
    }
    
    @Transactional
    public void analysisData(List<Map<String, Object>> mapList, DhProcessDefinition dhProcessDefinition) {
        for (Map<String, Object> map : mapList) {
            JSONObject data = (JSONObject)map.get("visualModel");
            JSONArray itemArray = data.getJSONArray("items");
            List<DhGatewayLine> gatewayLineList = new ArrayList<>();
            // 1. 得到所有的排他网关
            Map<String, BpmActivityMeta> idMetaMap = new HashMap<>();
            Set<String> gatewayIds = new HashSet<>();
            for (Object item : itemArray) {
                // 如果这个环节是排他网关
                if (StringUtils.equals("gateway", ((JSONObject)item).getString("type"))) {
                    String activityBpdId = ((JSONObject)item).getString("id");
                    List<BpmActivityMeta> list = bpmActivityMetaService.getBpmActivityMeta(activityBpdId, dhProcessDefinition.getProVerUid(), 
                            dhProcessDefinition.getProUid());
                    if (list.size() > 0) {
                        idMetaMap.put(activityBpdId, list.get(0));
                        gatewayIds.add(activityBpdId);
                    }
                }
            }
            
            // 2. 找到所有的连接线
            JSONArray linkArray = data.getJSONArray("links");
            for (Object link : linkArray) {
                String stratId = ((JSONObject)link).getString("start");
                
                // 如果这个连接线连接的起始点是排他网关
                if (gatewayIds.contains(stratId)) {
                    String activityBpdId = ((JSONObject)link).getString("id");
                    String endId = ((JSONObject)link).getString("end");
                    boolean isDefault = ((JSONObject)link).getBoolean("needDefaultMarker");
                    
                    
                    DhGatewayLine gatewayLine = new DhGatewayLine();
                    gatewayLine.setGatewayLineUid(EntityIdPrefix.DH_GATEWAY_LINE + UUID.randomUUID().toString());
                    // 设置起点的activityId
                    gatewayLine.setActivityId(idMetaMap.get(stratId).getActivityId());
                    // 设置是否默认路径
                    gatewayLine.setIsDefault(isDefault ? "TRUE" : "FALSE");
                    // 设置结束点activityId
                    List<BpmActivityMeta> list = bpmActivityMetaService.getBpmActivityMeta(endId, dhProcessDefinition.getProVerUid(), 
                            dhProcessDefinition.getProUid());
                    if (list.size() > 0) {
                        gatewayLine.setToActivityId(list.get(0).getActivityId());
                    }
                    gatewayLine.setActivityBpdId(activityBpdId);
                    gatewayLineList.add(gatewayLine);
                }
            }
            
            // 4. 装配条件
            if (gatewayLineList.size() > 0) {
                byte[] byteData = (byte[])map.get("byteData");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteData);
                SAXReader reader = new SAXReader();
                Document document = null;
                // 记录线的路由条件
                Map<String, String> idConditionMap = new HashMap<>();
                try {
                    document = reader.read(inputStream);
                    Element rootEle = document.getRootElement();
                    // 获得根节点下所有的 flow 节点
                    List<Element> flowList = rootEle.elements("flow");
                    for (Element flowEle : flowList) {
                        Attribute attribute = flowEle.attribute("id");
                        String idValue = attribute.getValue();
                        Element expressionEle = flowEle.element("connection").element("SequenceFlow")
                                .element("condition").element("expression");
                        if (expressionEle != null) {
                            idConditionMap.put(idValue, expressionEle.getTextTrim());
                        }
                    }
                    
                    List<DatRule> ruleList = new ArrayList<>();
                    for (DhGatewayLine gatewayLine : gatewayLineList) {
                       if (StringUtils.equals("FALSE", gatewayLine.getIsDefault())) {
                           String condition = idConditionMap.get(gatewayLine.getActivityBpdId());
                           if (condition != null) {
                               int firstIndex = condition.indexOf("\"");
                               int lastIndex = condition.lastIndexOf("\"");
                               if (firstIndex < lastIndex) {
                                   condition = condition.substring(firstIndex+1, lastIndex);
                                   gatewayLine.setRouteResult(condition);
                                   DatRule datRule = generateTemplateRule();
                                   datRule.setRuleName("result==\"" + condition + "\"");
                                   gatewayLine.setRuleId(datRule.getRuleId());
                                   ruleList.add(datRule);
                               }
                           }
                       } 
                    }
                    if (gatewayLineList.size() > 0) {
                        dhGatewayLineMapper.insertBatch(gatewayLineList);
                    }
                    if (ruleList.size() > 0) {
                        datRuleMapper.batchInsertDatRule(ruleList);
                    }
                } catch (DocumentException e) {
                    LOG.error("解析网关XML错误", e);
                }
                
                
            }
            
        }
        
        
        
    }
    
    
    /**
     * 获得visualModel "data" 节点下的对象
     * @param visualModelList
     * @param proAppId
     * @param proUid
     * @param proVerUid
     */
    private void getVisualModel(List<JSONObject> visualModelList, String proAppId, String proUid, String proVerUid) {
        JSONObject visualModel = getVisualModelData(proUid, proVerUid, proAppId);
        visualModelList.add(visualModel);
        JSONArray itemArr = visualModel.getJSONArray("items");
        for (Object item : itemArr) {
            // 如果这个item是子流程就去
            if (StringUtils.equals("SubProcess", ((JSONObject)item).getString("bpmn2TaskType"))) {
                String poId = ((JSONObject)item).getString("poId");
                String snapshotId = ((JSONObject)item).getString("snapshotId");
                getVisualModel(visualModelList, proAppId, poId, snapshotId);
            }
        }
        
    }
    
    
    
    public JSONObject getVisualModelData(String bpdId, String snapshotId, String processAppId) {
        JSONObject results = new JSONObject();
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
        RestUtil restUtil = new RestUtil(gcfg);
        HttpReturnStatus result = restUtil.doGet(url, pmap);
        restUtil.close();
        
        if (StringUtils.isNotBlank(result.getMsg())) {
            JSONObject datas = (JSONObject)JSON.parse(result.getMsg());
            if ("200".equalsIgnoreCase(datas.getString("status"))) {
                JSONObject data = datas.getJSONObject("data");
                results = data;
            }
        }

        if (results == null) {
            results = new JSONObject();
        }

        return results;
    }
    
    /**
     * 创建默认的规则，不包含具体条件
     * @return
     */
    private DatRule generateTemplateRule() {
        DatRule datRule = new DatRule();
        datRule.setRuleId(EntityIdPrefix.DAT_RULE + UUID.randomUUID().toString());
        datRule.setBizType("wfCondCtrl");
        datRule.setEditMode("STD");// 不明确
        datRule.setIsActivate("on");
        datRule.setReturnType("text");
        datRule.setRuleStatus("on");
        datRule.setCreateTime(DateUtil.datetoString(new Date()));
        datRule.setStartTime(DateUtil.datetoString(new Date()));
        datRule.setEndTime(null);
        datRule.setCreator((String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
        datRule.setRuleType("PARAMS");
        datRule.setRuleVersion(1);// 规则版本
        return datRule;
    }

	@Override
	public List<DhGatewayLine> getGateWayLinesByCondition(DhGatewayLine dhGatewayLine) {
		// TODO Auto-generated method stub
		return dhGatewayLineMapper.getGateWayLinesByCondition(dhGatewayLine);
	}
}
