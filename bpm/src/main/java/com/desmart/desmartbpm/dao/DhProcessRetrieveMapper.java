package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessRetrieve;

@Repository
public interface DhProcessRetrieveMapper {
	/**
	 * 新增
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer insert(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 删除
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer delete(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 修改
	 * @param dhProcessRetrieve
	 * @return
	 */
	public Integer update(DhProcessRetrieve dhProcessRetrieve);
	/**
	 * 根据条件查询流程检索数据
	 * @param dhProcessRetrieve
	 * @return
	 */
	public List<DhProcessRetrieve> getDhprocessRetrievesByCondition(DhProcessRetrieve dhProcessRetrieve);
}
