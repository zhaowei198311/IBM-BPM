package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhOperLog;

@Repository
public interface OperLogMapper {

	int save(DhOperLog operLog);//@Param(value="operLog")

	/**
	 * 根据条件查询日志记录集合
	 * @param dhOperLog
	 * @return
	 */
	public List<DhOperLog> getOperaLogListByCondition(DhOperLog dhOperLog);
}
