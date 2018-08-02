package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessDefinitionBo;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 应用库升级服务
 */
public interface DhProcessAppUpdateService {

    /**
     * 获得所有的应用库信息
     * @return map中数据 的键是 "proAppId", "proAppName"
     */
    ServerResponse<List<Map<String, String>>> getAllProcessApp();

    /**
     * 根据应用库id获得已经同步过的版本
     * @param proAppId 应用库id
     * @return map中的数据 键： "snapshotId", "snapshotName", "createTime"
     */
    ServerResponse<List<Map<String, String>>> findSynchronizedSnapshotByProAppId(String proAppId);

    /**
     * 根据应用库id获得为同步过的版本
     * @param proAppId 应用库id
     * @return
     */
    ServerResponse<List<Map<String, String>>> findUnsynchronizedSnapshotByProAppId(String proAppId);

    /**
     * 升级应用库前准备数据
     * @param proAppId  应用库id
     * @param newProVerUid  新版本快照id
     * @return
     */
    ServerResponse<Queue<DhProcessDefinitionBo>> prepareData(String proAppId, String newProVerUid);

    /**
     * 将某个应用库的流程，从老版本提升到新版本
     * @param proAppId 应用国库名
     * @param oldProVerUid 老版本快照id
     * @param newProVerUid 新版本快照id
     * @param definitionBoQueue  准备拉取的流程定义队列
     * @return
     */
    ServerResponse updateProcessApp(String proAppId, String oldProVerUid, String newProVerUid, Queue<DhProcessDefinitionBo> definitionBoQueue);

    /**
     * 将队列中的所有流程按顺序拉取到平台
     * @param boToPullQueue  应用库id
     * @return  返回新版本包含的所有流程定义
     */
    ServerResponse<List<DhProcessDefinition>> pullAllProcessActivityMeta(Queue<DhProcessDefinitionBo> boToPullQueue);



}