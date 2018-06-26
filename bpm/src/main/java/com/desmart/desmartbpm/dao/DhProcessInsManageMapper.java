package com.desmart.desmartbpm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhProcessInstance;

@Repository
public interface DhProcessInsManageMapper {
	/**
	 * 根据条件查询流程管理界面展示
	 * @param dhProcessInstance
	 * @return
	 */
	List<DhProcessInstance> getProcesssInstanceListByCondition(DhProcessInstance dhProcessInstance);
}
