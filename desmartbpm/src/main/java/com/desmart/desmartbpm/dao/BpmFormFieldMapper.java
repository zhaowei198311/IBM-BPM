package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmFormField;

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
}
