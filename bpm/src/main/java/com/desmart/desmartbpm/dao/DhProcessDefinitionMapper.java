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
     * 
     * @Title: getDhObjectPermissionById  
     * @Description: 根据proUid,proVerUid,proAppId获取对象权限  
     * @param @param dhObjectPermission
     * @param @return  
     * @return DhObjectPermission
     * @throws
     */
    List<DhObjectPermission> listDhObjectPermissionById(@Param("proUid") String proUid, @Param("proVerUid") String proVeruid, @Param("proAppId") String proAppId);
    /**
     * 
     * @Title: updateDhObjectPermissionById  
     * @Description: TODO  
     * @param @param dhObjectPermission
     * @param @return  
     * @return int
     * @throws
     */
    int saveDhObjectPermissionById(DhObjectPermission dhObjectPermission);
    /**
     * 
     * @Title: deleteDhObjectPermissionById  
     * @Description: 删除对象权限表根据proUid，proVerUid，proAppId  
     * @param @param proUid
     * @param @param proVeruid
     * @param @param proAppId
     * @param @return  
     * @return int
     * @throws
     */
    int deleteDhObjectPermissionById(@Param("proUid") String proUid, @Param("proVerUid") String proVeruid, @Param("proAppId") String proAppId);
}