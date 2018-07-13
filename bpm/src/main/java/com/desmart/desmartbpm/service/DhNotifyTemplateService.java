package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;

import java.util.List;

public interface DhNotifyTemplateService {
	
	/**
	 * 根据条件查询通知模板集合
	 * @param pageNum
	 * @param pageSize
	 * @param dhNotifyTemplate
	 * @return
	 */
	ServerResponse pageDhNotifyTemplateListByCondition(Integer pageNum
						,Integer pageSize,DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 删除
	 * @param dhNotifyTemplate
	 * @return
	 */
	ServerResponse deleteNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 修改通知模板
	 * @param dhNotifyTemplate
	 * @return
	 */
	ServerResponse addNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 删除模板
	 * @param dhNotifyTemplate
	 * @return
	 */
	ServerResponse updateNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 根据模版主键列表，获得多个模版
	 * @param templateUidList 模版主键列表
	 * @return
	 */
	List<DhNotifyTemplate> listByTemplateUidList(List<String> templateUidList);
}
