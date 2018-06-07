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
     * Map{
            "children": [
                {
                    "activityId": "act_meta:3064efa6-8094-4d0e-9e99-0f8ebf9aae17",
                    "activityBpdId": "5e4610e4-584e-4b4c-82e8-a6a410c40257",
                    "actcUid": "act_conf:1e9f581f-4445-4a39-b686-cfe2e7f8312c",
                    "activityName": "合同用印(子公司财务备案)"
                }
            ],
            "name": "主流程环节",
            "id": "main"
        }
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
    Map<String, List<BpmActivityMeta>> getNextToActivity(BpmActivityMeta sourceActivityMeta, String insUid);
    
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
    
    /**
     * 从指定列表中过滤出activityId 与 sourceActivityId相同的集合
     * @param bpmActivityMetaList
     * @return 
     */
    List<BpmActivityMeta> filterBasicActivity(List<BpmActivityMeta> bpmActivityMetaList);

    /**
     * 根据环节的 元素id和父元素的主键来查找唯一的一个环节
     * @param parentActivityId
     * @param activityBpdId
     * @return
     */
    BpmActivityMeta queryMetaByActivityBpdIdAndParentActivityId(String activityBpdId, String parentActivityId);

    /**
     * 根据代表子流程的节点找到它代表的子流程的开始节点
     * @param subProcessNode  代表子流程的节点
     * @return
     */
    BpmActivityMeta getStartMetaOfSubProcess(BpmActivityMeta subProcessNode);
}