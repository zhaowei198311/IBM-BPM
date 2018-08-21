package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhTaskException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DhTaskExceptionMapper {
	/**
	 * 新增记录
	 * @param dhTaskException
	 * @return
	 */
	int save(DhTaskException dhTaskException);

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
	 * @param dhTaskException
	 * @return
	 */
	int updateByPrimaryKeySelective(DhTaskException dhTaskException);

	/**
	 * 根据主键全量更新
	 * @param dhTaskException
	 * @return
	 */
	int updateByPrimaryKey(DhTaskException dhTaskException);


	int removeByPrimaryKey(String id);

	/**
	 * 根据任务主键查询该任务的异常记录，按创建时间倒序排列
	 * @param taskUid
	 * @return
	 */
	List<DhTaskException> listByTaskUid(String taskUid);

}
