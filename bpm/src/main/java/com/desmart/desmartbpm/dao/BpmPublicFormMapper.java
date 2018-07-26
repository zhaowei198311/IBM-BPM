package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.BpmPublicForm;

/**
 * 公共表单的数据库持久层
 * @author loser_wu
 * @since 2018年5月24日
 */
@Repository
public interface BpmPublicFormMapper {
	/**
	 * 根据表单名查询公共表单
	 */
	List<BpmPublicForm> listFormByFormName(@Param(value="formName")String formName);

	/**
	 * 根据表单名精确查询表单
	 */
	BpmPublicForm queryFormByFormName(String formName);

	/**
	 * 保存公共表单信息
	 */
	int saveForm(BpmPublicForm bpmPublicForm);

	/**
	 * 修改公共的表单内容
	 */
	int updateFormContent(BpmPublicForm bpmPublicForm);

	/**
	 * 根据表单ID精确查询表单
	 */
	BpmPublicForm queryFormByFormUid(String formUid);

	/**
	 * 修改公共的表单属性
	 */
	int updateFormInfo(BpmPublicForm bpmPublicForm);

	/**
	 * 根据传入参数选择性更新
	 * @param bpmPublicForm
	 * @return
	 */
	int updateByPrimayKeySelective(BpmPublicForm bpmPublicForm);

	/**
	 * 根据表单Id删除表单
	 */
	int deleteForm(String formUid);

	/**
	 * 根据表单id和公共表单id查询是否有相同的关联信息
	 */
	int queryReleByFormUidAndPublicFormUid(@Param("formUid")String formUid, @Param("publicFormUid")String publicFormUid);

	/**
	 * 根据表单编码查询到公共表单对象
	 */
	BpmPublicForm queryFormByFormCode(String formCode);

	/**
	 * 根据公共表单的id集合获得对应的公共表单
	 * @param publicFormUidList
	 * @return
	 */
	List<BpmPublicForm> listByPublicFormUidList(List<String> publicFormUidList);
}
