package com.desmart.desmartbpm.service;

import java.io.IOException;
import java.util.List;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessDefinition;

public interface BpmFormManageService {
	/**
	 * 获得某版本的流程下的所有表单
	 */
	ServerResponse listFormByProDefinition(String formTitle, String proUid, String proCategoryUid, 
			String proVerUid, Integer pageNum,Integer pageSize);

	/**
	 * 获得所有的流程定义对象(DH)
	 */
	List<DhProcessDefinition> listDefinitionAll();

	/**
	 * 添加表单数据
	 */
	ServerResponse saveForm(BpmForm bpmForm);

	/**
	 * 通过表单名查询表单
	 */
	ServerResponse queryFormByName(String dynTitle);

	/**
	 * 通过表单ID查询表单
	 */
	ServerResponse queryFormByFormUid(String formUid);

	/**
	 * 删除表单数据
	 */
	ServerResponse deleteForm(String[] formUids,String path);

	/**
	 * 复制表单
	 */
	ServerResponse copyForm(BpmForm bpmForm, String path) throws IOException;

	/**
	 * 修改表单基本信息
	 */
	ServerResponse updateFormInfo(BpmForm bpmForm) throws IOException;
	
	/**
	 * 根据传入的组合条件查询表单集合
	 */
	ServerResponse listBySelective(BpmForm bpmForm);

	/**
	 * 修改表单内容
	 */
	ServerResponse updateFormContent(BpmForm bpmForm);
}
