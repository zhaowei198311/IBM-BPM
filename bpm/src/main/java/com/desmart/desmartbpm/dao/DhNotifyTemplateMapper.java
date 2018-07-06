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
	public Integer insert(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 删除
	 * @param DhNotifyTemplate
	 * @return
	 */
	public Integer delete(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 修改
	 * @param DhNotifyTemplate
	 * @return
	 */
	public Integer update(DhNotifyTemplate dhNotifyTemplate);
	/**
	 * 根据条件查询通知模板
	 * @param DhNotifyTemplate
	 * @return
	 */
	public List<DhNotifyTemplate> getDhNotifyTemplatesByCondition(DhNotifyTemplate dhNotifyTemplate);
}
