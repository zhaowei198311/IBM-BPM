package com.desmart.desmartbpm.service;

import java.io.IOException;

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
	ServerResponse queryFormByFormName(String formName);

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
}
