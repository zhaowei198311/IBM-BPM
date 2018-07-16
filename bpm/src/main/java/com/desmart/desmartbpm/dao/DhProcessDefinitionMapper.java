package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhProcessDefinitionMapper {

    int save(DhProcessDefinition dhProcessDefinition);

    List<DhProcessDefinition> listBySelective(DhProcessDefinition dhProcessDefinition);

    /**
     * 根据应用库id， 流程图id， 流程版本来更新全部字段
     * @param dhProcessDefinition
     * @return
     */
    int updateByProAppIdAndProUidAndProVerUid(DhProcessDefinition dhProcessDefinition);

    /**
     * 根据应用库id， 流程图id， 流程版本来更新不为空字段
     * @param dhProcessDefinition
     * @return
     */
    int updateByProAppIdAndProUidAndProVerUidSelective(DhProcessDefinition dhProcessDefinition);
    
    List<DhProcessDefinition> listAll();
    /**
     * 
     * @Title: deleteBySelective  
     * @Description: 根据PRO_UID,PRO_APP_ID删除  
     * @param @param dhProcessDefinition
     * @param @return  
     * @return int  
     * @throws
     */
    int deleteBySelective(DhProcessDefinition dhProcessDefinition);
    /**
     * 
     * @Title: listById  
     * @Description: 通过proUid,proAppId查找拷贝所需的同类流程信息  
     * @param @param dhProcessDefinition
     * @param @return  
     * @return List<DhProcessDefinition>
     * @throws
     */
    List<DhProcessDefinition> listById(DhProcessDefinition dhProcessDefinition);
    /**
     * 
     * @Title: getProcessById  
     * @Description: 通过proUid,proVerUid,proAppId查询一条流程  
     * @param @param proUid
     * @param @param proVeruid
     * @param @param proAppId
     * @param @return  
     * @return DhProcessDefinition
     * @throws
     */
    DhProcessDefinition getProcessById(@Param("proUid") String proUid, @Param("proVerUid") String proVeruid, @Param("proAppId") String proAppId);

    /**
     * 根据传入的流程定义的 proAppId proUid proVerUid 3项，查找数据库中对应的流程
     * @param processDefinitionList
     * @return
     */
    List<DhProcessDefinition> listByDhPocessDefinitionList(List<DhProcessDefinition> processDefinitionList);


}