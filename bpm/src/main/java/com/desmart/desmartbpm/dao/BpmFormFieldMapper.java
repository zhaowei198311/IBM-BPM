package com.desmart.desmartbpm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;

@Repository
public interface BpmFormFieldMapper {
	/**
	 * 保存表单字段信息
	 */
	int insertBatch(@Param("fields")List<BpmFormField> fields);

	/**
	 * 删除指定表单的所有字段信息
	 * @param  formUid 表单主键
	 */
	int deleteFormField(String formUid);

	/**
	 * 根据表单Id获得表单字段集合
	 */
	List<BpmFormField> queryFormFieldByFormUid(@Param("dynUid")String dynUid);

	/**
	 * 通过字段ID和活动ID获得所属权限信息
	 */
	List<String> queryFieldByFieldIdAndStepId(@Param("stepUid")String stepUid, 
			@Param("fieldUid")String fieldUid);

	/**
	 * 保存某环节上表单字段的权限信息
	 */
	int saveFormFieldPermission(DhObjectPermission dhObjectPermission);

	/**
	 * 根据环节ID和表单字段ID删除字段权限信息
	 */
	int deleteFormFieldPermission(DhObjectPermission dhObjectPermission);
	/**
	 * 
	 * @Title: listFldUidByFormUid  
	 * @Description: 根据新老formUid查询相同FLD_CODE_NAME的FLD_UID  
	 * @param: @param oldFormUid
	 * @param: @param newFormUid
	 * @param: @return  
	 * @return: List<Map<String,Object>>
	 * @throws
	 */
	List<Map<String, Object>> listFldUidByFormUid(@Param("oldFormUid")String oldFormUid, @Param("newFormUid")String newFormUid);

	/**
	 * 根据表单字段Id删除该字段权限
	 */
	int deleteFieldPermissById(String fldUid);

	/**
	 * 根据字段id查询字段对象
	 */
	BpmFormField queryFieldByFldUid(String fldUid);

	/**
	 * 根据表单查找具体类型的字段集合(类型为空-->普通字段集合,类型不为空-->区块字段集合)
	 */
	List<BpmFormField> queryFormFieldByFormUidAndType(@Param("formUid")String formUid, @Param("fieldType")String fieldType);

	/**
	 * 根据表单id找到所有的关联子表单字段集合
	 */
	List<BpmFormField> queryPublicFormFieldByFormUid(@Param("formUid")String formUid,@Param("fieldType")String fieldType);

	/**
	 * 根据表单id和字段在表单中的name查询到唯一的字段
	 */
	BpmFormField queryFieldByFldUidAndCodeName(@Param("formUid")String dynUid, @Param("fieldCodeName")String fieldCodeName);

	/**
	 * 根据表单id查询非title的表单字段
	 */
	List<BpmFormField> queryNotTitleFormFieldByFormUid(String formUid);

	/**
	 * 根据表单id查询表单中的表格集合
	 */
	List<BpmFormField> queryFormTabByFormUid(String formUid);

	/**
	 * 根据表格id和表格名找到表格中字段集合
	 */
	List<BpmFormField> queryFormTabFieldByFormUidAndTabName(@Param("formUid")String formUid,
			@Param("tableName")String tableName);

	/**
	 * 根据表单id获得所有关联的字段
	 * @param formUidList
	 * @return
	 */
	List<BpmFormField> listByFormUidList(List<String> formUidList);


	/**
	 * 根据表单主键列表批量删除
	 * @param formUidList
	 * @return
	 */
	int removeByFormUidList(List<String> formUidList);
}
