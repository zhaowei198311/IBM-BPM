package com.desmart.desmartbpm.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhGatewayLine;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface DhGatewayLineService {
    
    /**
     * 生成网关连接线
     * @param dhProcessDefinition
     * @return
     */
    ServerResponse generateGatewayLine(DhProcessDefinition dhProcessDefinition);
    
    /**
     * 判断是否需要为流程定义生成网关连接线记录
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    boolean needGenerateGatewayLine(String proAppId, String proUid, String proVerUid);

    /**
     * 根据条件查询网关连接线集合,按照is_delfault、rule_id排序，默认线路放在最后
     * @param dhGatewayLine
     * @return
     */
    List<DhGatewayLine> getGateWayLinesByCondition(DhGatewayLine dhGatewayLine);


    /**
     * 获得指定流程定义下所有的网关输出连接线
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    List<DhGatewayLine> listAllGateWayLineOfProcessDefinition(String proAppId, String proUid, String proVerUid);

    /**
     * 获得多个网关的所有连接线
     * @param activityIdList 网关的activityId集合
     * @return
     */
    List<DhGatewayLine> listByGatewayActivityIdList(List<String> activityIdList);

    /**
     * 根据主键批量删除
     * @param gatewayLineUidList
     * @return
     */
    int removeByGatewayLineUidList(List<String> gatewayLineUidList);

    /**
     * 从主流程出发，得到主流程和它包含的所有子流程的VisualModel
     * 获得visualModel "data" 节点下的对象
     * @param visualModelList
     * @param proAppId
     * @param proUid
     * @param proVerUid
     */
    void getVisualModel(List<JSONObject> visualModelList, String proAppId, String proUid, String proVerUid);
}
