package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhObjectPermission;

@Repository
public interface DhObjectPermissionMapper {
    int save(DhObjectPermission dhObjectPermission);
    
    int saveBatch(List<DhObjectPermission> list);
    
    /**
     * 根据传入的条件删除记录
     * @param selective
     * @return
     */
    int delectByDhObjectPermissionSelective(DhObjectPermission selective);
    
    /**
     * 查询符合条件的权限记录
     * @param selective
     * @return
     */
    List<DhObjectPermission> listByDhObjectPermissionSelective(DhObjectPermission selective);
    
    /**
     * 
     * @Title: getDhObjectPermissionByFldUid  
     * @Description: 根据fldUid查找单个DhObjectPermission对象  
     * @param: @param fldUid
     * @param: @return  
     * @return: DhObjectPermission
     * @throws
     */
    List<DhObjectPermission> getDhObjectPermissionByFldUid(String fldUid);

    /**
     * 根据步骤ID查表单字段权限信息(不包打印)
     */
	List<DhObjectPermission> getFieldPermissionByStepUidNotPrint(String stepUid);

	/**
	 * 根据步骤id和字段id找到字段权限信息(不包打印)
	 */
	DhObjectPermission getFieldPermissionByStepUidAndFldUidNotPrint(@Param("stepUid")String stepUid, @Param("fldUid")String fldUid);

	/**
	 * 根据步骤id和字段id找到字段打印权限信息
	 */
	DhObjectPermission getFieldPrintPermissionByStepUidAndFldUid(@Param("stepUid")String stepUid, @Param("fldUid")String fldUid);

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
     * 将权限表中表单字段id更新为新的表单字段id
     * @param oldFieldUid 老的表单字段主键
     * @param newFieldUid 新的表单字段主键
     * @return
     */
    int updateformFieldUidToNewFieldUid(@Param("oldFieldUid")String oldFieldUid, @Param("newFieldUid")String newFieldUid);

    /**
     * 根据opObjUid集合删除 对应的记录
     * @param opObjUidList
     * @return
     */
    int removeByOpObjUidList(List<String> opObjUidList);

	/**
	 * 通过字段ID和活动ID获得所属权限信息
	 */
	List<String> queryFieldByFieldIdAndStepId(@Param("stepUid")String stepUid, 
			@Param("fieldUid")String fieldUid);

	/**
	 * 根据环节ID和表单字段ID删除字段权限信息
	 */
	int deleteFormFieldPermission(DhObjectPermission dhObjectPermission);
	
	/**
	 * 根据表单字段Id删除该字段权限
	 */
	int deleteFieldPermissById(String fldUid);

	/**
	 * 查询流程定义范围的权限， 比如发起权限
	 * @param proAppId 应用库id
	 * @param proUid   图id
	 * @param proVerUid 版本id
	 * @return
	 */
	List<DhObjectPermission> listProcessDefinitionScopePermission(@Param("proAppId") String proAppId,
																	@Param("proUid") String proUid, @Param("proVerUid") String proVerUid);
}
