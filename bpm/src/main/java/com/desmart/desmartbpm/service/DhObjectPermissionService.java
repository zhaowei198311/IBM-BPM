package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface DhObjectPermissionService {
    
    /**
     * 更新流程定义的权限
     * @param definition
     * @param ids  被赋予权限的对象uid字符串，uid1;uid2;uid3; 如果传空串或null，将清除流程定义的指定类型的人员权限
     * @param participateType 赋予权限的对象类型 USER ROLE TEAM
     * @param action 被赋予何种权限  如：START
     * @return
     */
    ServerResponse updatePermissionOfProcess(DhProcessDefinition definition, String ids, String participateType, String action);
    

    /**
     * 查询制定流程定义的发起权限
     * @param proAppId 应用库id
     * @param proUid 流程id
     * @param proVerUid 流程版本id
     * @return ServerResponse中的data(map类型)描述
     *    key: permissionStartUser 人员  uid1;uid2;
     *    key: permissionStartUserView  人员显示名   张三;李四;
     *    key: permissionStartRole 角色 
     *    key: permissionStartRoleView  角色显示
     *    key: permissionStartTeam  角色组
     *    key: permissionStartTeamView  角色组显示
     */
    ServerResponse getPermissionStartOfProcess(String proAppId, String proUid, String proVerUid);
    
    
    /**
     * 根据对象属性实体查询 信息
     * @param dhObjectPermission
     * @return
     */
    List<DhObjectPermission> getDhObjectPermissionInfo(DhObjectPermission dhObjectPermission);
}
