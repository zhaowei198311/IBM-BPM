package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTrigger;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhTriggerException;

@Repository
public interface DhTriggerExceptionMapper {
	/**
	 * 新增记录
	 * @param dhTriggerException
	 * @return
	 */
	int save(DhTriggerException dhTriggerException);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	DhTriggerException qureyByPrimaryKey(String id);

	/**
	 * 根据任务id加步骤号查询
	 * @param taskUid
	 * @param stepUid
	 * @return
	 */
	DhTriggerException queryByTaskUidAndStepUid(@Param("taskUid") String taskUid, @Param("stepUid") String stepUid);

	/**
	 * 根据主键选择性更新
	 * @param dhTriggerException
	 * @return
	 */
	int updateByPrimaryKeySelective(DhTriggerException dhTriggerException);

}
