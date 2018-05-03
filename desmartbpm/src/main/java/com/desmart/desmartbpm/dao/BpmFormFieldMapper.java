package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;

@Repository
public interface BpmFormFieldMapper {
	/**
	 * 保存表单字段信息
	 */
	int saveFormField(@Param("fields")List<BpmFormField> fields);

	/**
	 * 删除表单字段信息
	 */
	int deleteFormField(String formUid);

	/**
	 * 根据表单Id获得表单字段集合
	 */
	List<BpmFormField> queryFormFieldByFormUid(@Param("dynUid")String dynUid);

	/**
	 * 通过字段ID和活动ID获得所属权限信息
	 */
	String queryFieldByFieldIdAndStepId(@Param("stepUid")String stepUid, 
			@Param("fieldUid")String fieldUid);

	/**
	 * 保存某环节上表单字段的权限信息
	 */
	int saveFormFieldPermission(DhObjectPermission dhObjectPermission);

	/**
	 * 根据环节ID和表单字段ID删除字段权限信息
	 */
	int deleteFormFieldPermission(DhObjectPermission dhObjectPermission);
}
