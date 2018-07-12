package com.desmart.desmartbpm.service;

import java.io.IOException;
import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmPublicForm;

/**
 * 公共表单管理业务逻辑层
 * @author loser_wu
 * @since 2018年5月24日
 */
public interface BpmPublicFormService {
	/**
	 * 根据表单名分页查询公共表单
	 */
	ServerResponse listFormByFormName(String formName, Integer pageNum, Integer pageSize);

	/**
	 * 根据表单名精确查询表单
	 */
	ServerResponse queryFormByFormNameAndCode(String formName,String formCode);

	/**
	 * 根据表单id查询表单是否已被主表单引用
	 */
	ServerResponse isBindMainForm(String[] formUids);

	/**
	 * 保存公共表单信息
	 */
	ServerResponse saveForm(BpmPublicForm bpmPublicForm);

	/**
	 * 修改公共的表单内容
	 */
	ServerResponse upadteFormContent(BpmPublicForm bpmPublicForm);

	/**
	 * 根据表单ID精确查询表单
	 */
	ServerResponse queryFormByFormUid(String formUid);

	/**
	 * 修改公共的表单属性
	 */
	ServerResponse updateFormInfo(BpmPublicForm bpmPublicForm) throws Exception;

	/**
	 * 删除公共表单
	 */
	ServerResponse deleteForm(String[] formUids);

	/**
	 * 复制表单
	 */
	ServerResponse copyForm(BpmPublicForm bpmPubilcForm);

	/**
	 * 添加主表单与子表单之间的关联信息
	 */
	ServerResponse saveFormRelePublicForm(String formUid, String[] publicFormUidArr);

	/**
	 * 根据表单id和公共表单id查询是否有相同的关联信息
	 */
	ServerResponse queryReleByFormUidAndPublicFormUid(String formUid, String publicFormUid);

	/**
	 * 根据公共表单id集合获得对应的公共表单
	 * @param publicFormUidList
	 * @return
	 */
	List<BpmPublicForm> listByPublicFormUidList(List<String> publicFormUidList);
}
