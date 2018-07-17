package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhStep;

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
	 * 批量新增表单
	 * @param bpmFormList
	 * @return
	 */
	int insertFormBatch(List<BpmForm> bpmFormList);

	/**
	 * 根据表单名查询表单
	 */
	BpmForm queryProFormByName(@Param("dynTitle")String dynTitle,
				@Param("proUid")String proUid,@Param("proVersion")String proVersion);

	/**
	 * 根据表单Id查询表单
	 */
	BpmForm queryFormByFormUid(String formUid);

	/**
	 * 根据表单Id删除表单数据
	 */
	int deleteForm(String formUid);
	
	/**
	 * 修改表单基本信息
	 */
	int updateFormInfo(BpmForm bpmForm);

	/**
	 * 根据表单Id修改表单文件名
	 */
	int updateFormFilenameByFormUid(@Param("dynUid")String dynUid,@Param("filename")String filename);

	/**
	 * 根据传入的组合条件查询表单集合
	 */
	List<BpmForm> listBySelective(BpmForm bpmForm);

	/**
	 * 修改表单内容
	 */
	int updateFormContent(BpmForm bpmForm);

	/**
	 * 根据表单Id查询绑定的步骤集合
	 */
	List<DhStep> isBindStep(String formUid);

	/**
	 * 根据快照id获得流程元名称
	 */
	String queryMetaNameByProUid(String proUid);

	/**
	 * 根据表单id删除表单关联信息
	 */
	int deleteFormRelePublicForm(String dynUid);

	/**
	 * 根据表单id查询表单关联信息
	 */
	List<String> queryFormReleByFormUid(String dynUid);
	
	/**
	 * 添加主表单与子表单之间的关联信息
	 */
	int saveFormRelePublicForm(@Param("formUid")String formUid, @Param("publicFormUid")String publicFormUid);

	/**
	 * 根据表单主键批量删除
	 * @param formUidList
	 * @return
	 */
	int removeFormsByFormUidList(List<String> formUidList);
}
