package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhNotifyTemplate;

@Repository
public interface DhNotifyTemplateMapper {

	/**
	 * 新增
	 * @param dhNotifyTemplate
	 * @return
	 */
	Integer insert(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 批量插入
	 * @param notifyTemplateList
	 * @return
	 */
	int insertBatch(List<DhNotifyTemplate> notifyTemplateList);

	/**
	 * 删除
	 * @param dhNotifyTemplate
	 * @return
	 */
	Integer delete(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 修改
	 * @param dhNotifyTemplate
	 * @return
	 */
	public Integer update(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 根据条件查询通知模板
	 * @param dhNotifyTemplate
	 * @return
	 */
	List<DhNotifyTemplate> getDhNotifyTemplatesByCondition(DhNotifyTemplate dhNotifyTemplate);

	/**
	 * 根据模版id集合查询多个模版
	 * @param templateUidList 模版id集合
	 * @return
	 */
    List<DhNotifyTemplate> listByTemplateUidList(List<String> templateUidList);



}
