package com.desmart.desmartbpm.service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;

public interface DhNotifyTemplateService {
	
	/**
	 * 根据条件查询通知模板集合
	 * @param pageNum
	 * @param pageSize
	 * @param dhNotifyTemplate
	 * @return
	 */
	public ServerResponse pageDhNotifyTemplateListByCondition(Integer pageNum
						,Integer pageSize,DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 删除
	 * @param dhNotifyTemplate
	 * @return
	 */
	public ServerResponse deleteNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 修改通知模板
	 * @param dhNotifyTemplate
	 * @return
	 */
	public ServerResponse addNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 删除模板
	 * @param dhNotifyTemplate
	 * @return
	 */
	public ServerResponse updateNotifyTemplate(DhNotifyTemplate dhNotifyTemplate);
}
