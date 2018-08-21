package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTaskException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DhTaskExceptionMapper {
	/**
	 * 新增记录
	 * @param dhTriggerException
	 * @return
	 */
	int save(DhTaskException dhTriggerException);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	DhTaskException qureyByPrimaryKey(String id);

	/**
	 * 根据任务id加步骤号查询
	 * @param taskUid
	 * @param stepUid
	 * @return
	 */
	DhTaskException queryByTaskUidAndStepUid(@Param("taskUid") String taskUid, @Param("stepUid") String stepUid);

	/**
	 * 根据主键选择性更新
	 * @param dhTriggerException
	 * @return
	 */
	int updateByPrimaryKeySelective(DhTaskException dhTriggerException);

	/**
	 * 根据主键全量更新
	 * @param dhTriggerException
	 * @return
	 */
	int updateByPrimaryKey(DhTaskException dhTriggerException);


	int removeByPrimaryKey(String id);

}
