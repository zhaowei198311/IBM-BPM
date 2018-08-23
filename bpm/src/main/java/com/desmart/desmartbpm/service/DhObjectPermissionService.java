package com.desmart.desmartbpm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.github.pagehelper.PageInfo;

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
     * 查询指定流程定义的发起权限
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
     * 删除指定流程定义的发起权限
     * @param proAppId 应用库id
     * @param proUid  流程图id
     * @param proVerUid  版本id
     * @return
     */
    int deletePermissionListOfStartProcess(String proAppId, String proUid, String proVerUid);
    
    /**
     * 根据对象属性实体查询 信息
     * @param dhObjectPermission
     * @return
     */
    List<DhObjectPermission> getDhObjectPermissionInfo(DhObjectPermission dhObjectPermission);

    /**
     * 列出流程定义发起人权限列表
     * @param proAppId 应用库id
     * @param proUid  流程图id
     * @param proVerUid  版本id
     * @return
     */
    List<DhObjectPermission> getPermissionListOfStartProcess(String proAppId, String proUid, String proVerUid);



    /**
     * 根据步骤ID查表单字段权限信息(不包括打印)
     */
    List<DhObjectPermission> getFieldPermissionByStepUidNotPrint(String stepUid);

    /**
     * 根据步骤id和表单字段id查询字段权限信息(不包括打印)
     */
	DhObjectPermission getFieldPermissionByStepUidAndFldUidNotPrint(String stepUid, String fldUid);

	/**
	 * 根据步骤id和表单字段id查询字段打印权限信息
	 */
	DhObjectPermission getFieldPrintPermissionByStepUidAndFldUid(String stepUid, String fldUid);

    /**
     * 根据步骤id集合获得所有相关的权限
     * @param stepUidList
     * @return
     */
	List<DhObjectPermission> listByStepUidList(List<String> stepUidList);

    /**
     * 根据步骤id集合删除所有相关的权限
     * @param stepUidList
     * @return
     */
	int removeByStepUidList(List<String> stepUidList);

    /**
     * 根据3个要素删除相关权限
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    int removeByProAppIdAndProUidAndProVerUid(String proAppId, String proUid, String proVerUid);

    /**
     * 更新流程元数据权限
     * @param dhProcessMeta
     * @param ids
     * @param participateType
     * @param action
     * @return
     */
    public ServerResponse updatePermissionOfMeta(DhProcessMeta dhProcessMeta, String ids, String participateType,
			String action);
    /**
     * 查询指定流程元数据的查看权限
     * @param opUid 应用库id
     * @return ServerResponse中的data(map类型)描述
     *    key: permissionUser 人员  uid1;uid2;
     *    key: permissionUserView  人员显示名   张三;李四;
     *    key: permissionRole 角色 
     *    key: permissionRoleView  角色显示
     *    key: permissionTeam  角色组
     *    key: permissionTeamView  角色组显示
     */
    public ServerResponse getPermissionReadOfMeta(String opUid);

    /**
     * 
     * @param proAppId
     * @param proUid
     * @param pageNum
     * @param pageSize
     *    key: permissionUser 人员  uid1;uid2;
     *    key: permissionUserView  人员显示名   张三;李四;
     *    key: permissionRole 角色 
     *    key: permissionRoleView  角色显示
     *    key: permissionTeam  角色组
     *    key: permissionTeamView  角色组显示
     * @return
     */
	ServerResponse<PageInfo<List<DhObjectPermission>>> getPermissionReadOfMetaByPage(Integer pageNum,Integer pageSize
			,String proAppId, String proUid);
	/**
	 * 根据传入的对象权限集合批量删除
	 * @param primaryKeys
	 * @return
	 */
	ServerResponse deleteBatchByPrimaryKeys(List<String> primaryKeys);
}
