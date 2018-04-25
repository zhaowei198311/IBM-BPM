package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessMeta;

@Repository
public interface BpmFormManageMapper {
	/**
	 * 根据流程定义Id以及版本Id获得所有的表单集合
	 */
	List<BpmForm> listFormByProDefinition(@Param("formTitle")String formTitle, 
			@Param("proUid")String proUid, @Param("proVerUid")String proVerUid);

	/**
	 * 根据流程元集合中的流程定义Id获得所有的表单集合
	 */
	List<BpmForm> listFormByProUidList(@Param("metaList")List<DhProcessMeta> metaList, 
			@Param("formTitle")String formTitle);

	/**
	 * 保存表单数据
	 */
	int saveForm(BpmForm bpmForm);

	/**
	 * 根据表单名查询表单
	 */
	BpmForm queryFormByName(String dynTitle);

	/**
	 * 根据表单Id查询表单
	 */
	BpmForm queryFormByFormUid(String formUid);

	/**
	 * 根据表单Id删除表单数据
	 */
	int deleteForm(String formUid);
}
