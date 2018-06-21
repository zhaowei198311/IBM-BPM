package com.desmart.desmartbpm.mongo;

import com.desmart.desmartbpm.entity.LockedTask;

import java.util.List;

public interface TaskMongoDao {

	/**
	 * 更新最后同步的任务id
	 * @param taskId
	 * @return
	 */
	int saveOrUpdateLastSynchronizedTaskId(int taskId);

	/**
	 * 查询最后被同步的任务id
	 * @return
	 */
	int queryLastSynchronizedTaskId();

	/**
	 * 根据键查询对应的value， 没有则返回null
	 * @param taskId 键
	 * @return
	 */
	LockedTask queryLockTaskByTaskId(int taskId);


	/**
	 * 删除被锁的任务
	 * @param taskId  任务编号
	 * @return
	 */
	int removeLockedTask(int taskId);

	/**
	 * 记录被锁的任务
	 * @param lockedTask
	 */
	void saveLockedTask(LockedTask lockedTask);


}
