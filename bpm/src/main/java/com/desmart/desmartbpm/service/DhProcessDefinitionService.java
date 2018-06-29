package com.desmart.desmartbpm.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.vo.DhProcessDefinitionVo;

public interface DhProcessDefinitionService {

    /**
     * 列出指定的流程元数据相关的所有流程定义, 包括未同步的公开的流程
     * @param metaUid  流程元数据主键
     * @return
     */
    ServerResponse listProcessDefinitionsIncludeUnSynchronizedByMetaUid(String metaUid, Integer pageNum, Integer pageSize);

    /**
     * 获得指定的流程定义
     */
    DhProcessDefinition getDhProcessDefinition(String proAppId, String proUid, String proVerUid);
    
    /**
     * 获得已经同步的指定的流程定义信息包含快照信息
     */
    ServerResponse<DhProcessDefinitionVo> getSynchronizedDhProcessDefinitionWithSnapshotInfo(String proAppId, String proUid, String proVerUid);
    
    /**
     * 创建流程定义
     * @param proAppId 流程应用库id
     * @param proUid  流程图id
     * @param proVerUid 流程版本id
     * @param request httpRequest
     * @return
     */
    ServerResponse createDhProcessDefinition(String proAppId, String proUid, String proVerUid, HttpServletRequest request);

    /**
     * 指定的流程定义是否存在，存在的话将满足条件的第一个元素返回在data中
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse isDhProcessDefinitionExist(String proAppId, String proUid, String proVerUid);

    /**
     * 更新流程定义
     * @param definition
     * @return
     */
    ServerResponse updateDhProcessDefinition(DhProcessDefinition definition);
    
    /**
     * 获得流程定义的第一个人工活动环节
     * 方法内调用了 主流程开始节点的 getActualNextActivities() 取第一个
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse<BpmActivityMeta> getFirstHumanBpmActivityMeta(String proAppId, String proUid, String proVerUid);
    
    /**
     * 根据快照id，从引擎中获得版本信息
     * @param snapshotId 如"2064.9303b288-e202-465f-9742-f042b5521fa5"
     * @return
     */
    LswSnapshot getLswSnapshotBySnapshotId(String snapshotId);
    
    /**
     * 
     * @Title: listProcessDefinitionById  
     * @Description: 查询拷贝所需的同类流程信息   
     * @param @param dhProcessDefinition
     * @param @return  
     * @return ServerResponse
     * @throws
     */
    ServerResponse listProcessDefinitionById(DhProcessDefinition dhProcessDefinition);
    
    /**
     * 
     * @Title: copySimilarProcess  
     * @Description: 拷贝同类流程  
     * @param @param dhProcessDefinition
     * @param @return  
     * @return ServerResponse
     * @throws
     */
    ServerResponse copySimilarProcess(Map<String, Object> mapId);
    
    /**
     * 找到指定元数据下可发起的流程定义
     * @param proAppId
     * @param proUid
     * @return
     */
    DhProcessDefinition getStartAbleProcessDefinition(String proAppId, String proUid);
    
    /**
     * 启用一个版本的流程定义
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    ServerResponse enableProcessDefinition(String proAppId, String proUid, String proVerUid);
    
    /**
     * 
     * @Title: checkWhetherLinkSynchronization  
     * @Description: 拷贝前验证是否进行环节同步  
     * @param @param bpmActivityMeta
     * @param @return  
     * @return ServerResponse  
     * @throws
     */
    ServerResponse checkWhetherLinkSynchronization(BpmActivityMeta bpmActivityMeta);
}