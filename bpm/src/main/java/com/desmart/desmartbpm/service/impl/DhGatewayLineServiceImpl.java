package com.desmart.desmartbpm.service.impl;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;

import com.desmart.common.util.BpmProcessUtil;
import com.desmart.common.util.HttpReturnStatusUtil;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.mongo.ModelMongoDao;
import com.desmart.desmartbpm.util.http.HttpClientUtils;
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
import org.springframework.util.CollectionUtils;

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
    @Autowired
    private ModelMongoDao modelMongoDao;
    

    @Transactional(value = "transactionManager")
    public ServerResponse generateGatewayLine(String proAppId, String proUid, String proVerUid,
                                              Map<String, BpmActivityMeta> bpdIdMetaMap) {
        List<Map<String, Object>> prepareData = prepareData(proAppId,
                proUid, proVerUid);
        analysisData(prepareData, bpdIdMetaMap);
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
     * @param proAppId 应用库id
     * @param proUid 图id
     * @param proVerUid 版本id
     * @return 
     *  一个map集合，其中的key  
     *  1）visualModel（JSONObject）   
     *  2）bpdData（引擎中图数据，字符串类型的xml数据）
     */
    private List<Map<String, Object>> prepareData(String proAppId, String proUid, String proVerUid) {
        List<Map<String, Object>> dataList = new ArrayList<>();  // 用来返回的数据
        
        List<JSONObject> visualModelList = new ArrayList<>();
        getVisualModel(visualModelList, proAppId, proUid, proVerUid);
        // 调用结束，visualModelLsit中是主流程和其内所有内连子流程的VisualModel
        System.out.println(visualModelList.size());
        for (JSONObject visualModel : visualModelList) {
            String poId = visualModel.getString("poId");
            String versionId = visualModel.getString("poVersionId");
            Map<String, Object> map = new HashMap<>();
            poId = poId.substring(poId.indexOf(".") + 1);
            String lswBpdDataStr = modelMongoDao.getLswBpdData(poId, versionId);
            if (lswBpdDataStr == null) {
                LswBpd lswBpd = lswBpdMapper.queryByBpdIdAndVersionId(poId, versionId);
                if (lswBpd != null) {
                    try {
                        lswBpdDataStr = new String(lswBpd.getData(), "UTF-8");
                        modelMongoDao.saveLswBpdData(poId, versionId, lswBpdDataStr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            map.put("visualModel", visualModel);
            map.put("bpdData", lswBpdDataStr);
            dataList.add(map);
        }
        return dataList;
    }
    
    @Transactional(value = "transactionManager")
    public void analysisData(List<Map<String, Object>> mapList, Map<String, BpmActivityMeta> idMetaMap) {
        // 遍历处理Map中的visualModel和byteData数据————即主流程和每个子流程中的网关
        for (Map<String, Object> map : mapList) {
            JSONObject data = (JSONObject)map.get("visualModel");
            JSONArray itemArray = data.getJSONArray("items");
            List<DhGatewayLine> gatewayLineList = new ArrayList<>();
            // 1. 得到所有的排他网关

            Set<String> gatewayIds = new HashSet<>();
            for (Object item : itemArray) {
                // 如果这个环节是排他网关
                if (StringUtils.equals("gateway", ((JSONObject)item).getString("type"))) {
                    String activityBpdId = ((JSONObject)item).getString("id");
                    // 找到这个环节
                    gatewayIds.add(activityBpdId);
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
                    gatewayLine.setToActivityId(idMetaMap.get(endId).getActivityId());
                    gatewayLine.setActivityBpdId(activityBpdId);
                    gatewayLineList.add(gatewayLine);
                }
            }
            
            // 4. 装配条件
            if (gatewayLineList.size() > 0) {
                String bpdDataStr = (String)map.get("bpdData");
                ByteArrayInputStream inputStream = null;
                SAXReader reader = new SAXReader();
                Document document = null;
                // 记录线的路由条件
                Map<String, String> idConditionMap = new HashMap<>();
                try {
                    inputStream = new ByteArrayInputStream(bpdDataStr.getBytes("UTF-8"));
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
                } catch (Exception e) {
                    throw new PlatformException("解析网关XML错误", e);
                }
                
                
            }
            
        }
    }
    
    @Override
    public void getVisualModel(List<JSONObject> visualModelList, String proAppId, String proUid, String proVerUid) {
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
        String visualModelStr = modelMongoDao.getVisualModel(processAppId, bpdId, snapshotId);
        if (visualModelStr == null) {
            BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
            BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(gcfg);
            HttpReturnStatus returnStatus = bpmProcessUtil.getVisualModel(processAppId, bpdId, snapshotId);
            if (HttpReturnStatusUtil.isErrorResult(returnStatus)) {
                throw new PlatformException("获取VisualModel失败");
            }
            visualModelStr = returnStatus.getMsg();
        }
        JSONObject datas = JSON.parseObject(visualModelStr);
        return datas.getJSONObject("data");
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

    @Override
    public List<DhGatewayLine> listAllGateWayLineOfProcessDefinition(String proAppId, String proUid, String proVerUid) {
        return dhGatewayLineMapper.listAllGatewayLineOfProcessDefinition(proAppId, proUid, proVerUid);
    }

    @Override
    public List<DhGatewayLine> listByGatewayActivityIdList(List<String> activityIdList) {
        if (activityIdList == null || activityIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return dhGatewayLineMapper.listByGatewayActivityIdList(activityIdList);
    }

    @Override
    public int removeByGatewayLineUidList(List<String> gatewayLineUidList) {
        if (CollectionUtils.isEmpty(gatewayLineUidList)) {
            return 0;
        }
        return dhGatewayLineMapper.removeByGatewayLineUidList(gatewayLineUidList);
    }
}
