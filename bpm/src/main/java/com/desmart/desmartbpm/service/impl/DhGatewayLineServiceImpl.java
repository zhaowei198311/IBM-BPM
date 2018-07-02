package com.desmart.desmartbpm.service.impl;

import java.io.ByteArrayInputStream;
import java.text.MessageFormat;
import java.util.*;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.common.exception.PlatformException;
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
    private DhProcessDefinitionMapper dhProcessDefinitionMapper;
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private BpmActivityMetaMapper bpmActivityMetaMapper;
    @Autowired
    private DhGatewayLineMapper dhGatewayLineMapper;
    @Autowired
    private DatRuleMapper datRuleMapper;
    

    @Transactional(value = "transactionManager")
    public ServerResponse generateGatewayLine(DhProcessDefinition dhProcessDefinition) {
        List<Map<String, Object>> prepareData = prepareData(dhProcessDefinition.getProAppId(),
                dhProcessDefinition.getProUid(), dhProcessDefinition.getProVerUid());
        analysisData(prepareData, dhProcessDefinition);
        return ServerResponse.createBySuccess();
    }


    public boolean needGenerateGatewayLine(String proAppId, String proUid, String proVerUid) {
        BpmActivityMeta metaSelective = new BpmActivityMeta(proAppId, proUid, proVerUid);
        metaSelective.setActivityType("gateway");
        List<BpmActivityMeta> gatewayList = bpmActivityMetaMapper.queryByBpmActivityMetaSelective(metaSelective);
        Iterator<BpmActivityMeta> iterator = gatewayList.iterator();
        while (iterator.hasNext()) {
            BpmActivityMeta gatewayMeta = iterator.next();
            if (!gatewayMeta.getActivityId().equals(gatewayMeta.getSourceActivityId())) {
                // 如果源节点id与自身id不同，说明不是源节点，不需要为它生成网关连接线
                iterator.remove();
            }
        }
        if (gatewayList.size() == 0) {
            // 如果图中没有网关，返回不需要生成网关
            return false;
        }
        // 如果包含网关环节，返回true
        return true;
    }
    
    
    /**
     * 收集生成网关连接线需要的数据，Map中的值： 
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return 
     *  一个map集合，其中的key  
     *  1）visualModel（JSONObject）   
     *  2）byteData（字节数组）
     */
    private List<Map<String, Object>> prepareData(String proAppId, String proUid, String proVerUid) {
        List<Map<String, Object>> dataList = new ArrayList<>();  // 用来返回的数据
        
        List<JSONObject> visualModelList = new ArrayList<JSONObject>();
        getVisualModel(visualModelList, proAppId, proUid, proVerUid);
        // 调用结束，visualModelLsit中是主流程和其内所有内连子流程的VisualModel
        System.out.println(visualModelList.size());
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
    
    @Transactional(value = "transactionManager")
    public void analysisData(List<Map<String, Object>> mapList, DhProcessDefinition dhProcessDefinition) {
        // 遍历处理Map中的visualModel和byteData数据
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
            
            // 2. 找到所有的连接线，并装配成DhGatewayLine对象
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
                       // 如果不是默认的线路
                       if (StringUtils.equals("FALSE", gatewayLine.getIsDefault())) {
                           String condition = idConditionMap.get(gatewayLine.getActivityBpdId());
                           if (StringUtils.isNotBlank(condition)) {
                               // 如果condition中不含有 tw.decision.routeResult 返回异常
                               if (!condition.contains("tw.decision.routeResult")) {
                                   throw new PlatformException("流程图网关环节配置异常");
                               }
                               int firstIndex = condition.indexOf("\"");
                               int lastIndex = condition.lastIndexOf("\"");
                               if (firstIndex < lastIndex) {
                                   condition = condition.substring(firstIndex+1, lastIndex);
                                   gatewayLine.setRouteResult(condition);
                                   DatRule datRule = generateTemplateRule();
                                   datRule.setRuleName("result==\"" + condition + "\"");
                                   gatewayLine.setRuleId(datRule.getRuleId());
                                   ruleList.add(datRule);
                               } else {
                                   throw new PlatformException("流程图网关环节配置异常");
                               }
                           } else {
                               throw new PlatformException("流程图网关环节配置异常");
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
                    throw new PlatformException("解析网关XML错误", e);
                }
                
                
            }
            
        }
        
        
        
    }
    
    
    /**
     * 从主流程出发，得到主流程和它包含的所有子流程的VisualModel
     * 
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
		return dhGatewayLineMapper.getGateWayLinesByCondition(dhGatewayLine);
	}
}
