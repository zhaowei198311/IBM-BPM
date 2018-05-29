package com.desmart.desmartbpm.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;

public interface BpmActivityMetaService {
    BpmActivityMeta getBpmActivityMeta(String activityBpdId, String activityName, String snapshotId, String bpdId, String type,
                                       String activityType, String parentActivityBpdId, String activityTo, String externalID,
                                       String loopType, String bpmTaskType, String bpmProcessSnapshotId, String miOrder,
                                       Integer deepLevel, String proAppId) throws Exception;

    /**
     * 获得用来生产环节配置层级菜单的信息
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse<List<Map<String, Object>>> getActivitiyMetasForConfig(String proAppId, String proUid, String proVerUid);
    
    /**
     * 获取流程定义的所有人工环节
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse<List<BpmActivityMeta>> getHumanActivitiesOfDhProcessDefinition(String proAppId, String proUid, String proVerUid);
    
    /**
     * 获得流程定义的所有排他网关
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse<List<BpmActivityMeta>> getGatewaysOfDhProcessDefinition(String proAppId, String proUid, String proVerUid);
    
    /**
     * 获得指定环节的后续人工环节, 有局限性！！不能进入子流程，只能在同层
     * @param bpmActivityMeta
     * @return
     */
    List<BpmActivityMeta> getNextToActivity(BpmActivityMeta bpmActivityMeta);
    
    /**
     * 根据元素id，流程图id和版本id获得BpmActivityMeta，应该是只有一个
     * @param bpmActivityId
     * @param snapshotId
     * @param bpdId
     * @return
     */
    List<BpmActivityMeta> getBpmActivityMeta(String activityBpdId, String snapshotId, String bpdId);
    
    /**
     * 查找指定节点的下个节点
     * @param sourceActivityMeta
     * @param insUid
     * @return  Map的key： 
     * normal  gateAnd  end 
     */
    Map<String, Object> getNextToActivity(BpmActivityMeta sourceActivityMeta, String insUid);
    
    /**
     * 根据主键查询
     * @param activityId
     * @return
     */
    BpmActivityMeta queryByPrimaryKey(String activityId);
    /**
     * 根据流程应用库id，元素id，流程图id和版本id获得BpmActivityMeta 只有一个
     * @param proAppId
     * @param bpmActivityId
     * @param snapshotId
     * @param bpdId
     * @return
     */
    BpmActivityMeta getBpmActivityMeta(String proAppId,String activityBpdId, String snapshotId, String bpdId);
    /**
     * 根据元素id，流程图id和版本id以及activityType获得BpmActivityMeta
     * @param bpmActivityId
     * @param snapshotId
     * @param bpdId
     * @return
     */
    List<BpmActivityMeta> getBpmActivityMetaByActivityType(String activityBpdId, String snapshotId, String bpdId,String activityType);
}