package com.desmart.desmartbpm.mongo;

import com.desmart.desmartbpm.entity.LockedTask;
import com.desmart.desmartbpm.entity.OpenedTask;

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
	 * 获得所有被锁住的任务
	 * @return
	 */
	List<LockedTask> getAllLockedTasks();


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

	/**
	 * 批量插入被锁任务
	 * @param list
	 */
	void batchSaveLockedTasks(List<LockedTask> list);

	/**
	 * 保存被打开过的任务
	 * @param openedTask
	 */
	void saveOpenedTask(OpenedTask openedTask);

	/**
	 * 指定编号的任务是否已被打开过
	 * @param taskId
	 * @return
	 */
	boolean hasTaskBeenOpened(int taskId);

	/**
	 * 指定主键的任务是否已经被打开过
	 * @param taskUid
	 * @return
	 */
	boolean hasTaskBeenOpened(String taskUid);

	/**
	 * 批量锁任务
	 * @param taskIdList  任务编号
	 * @param reason  锁任务的原因,常量  例子： LOCKED_TASK_COLLECTION_NAME
	 */
	void lockTasksForRejectTaskByTaskIdList(List<Integer> taskIdList, String reason);
}
