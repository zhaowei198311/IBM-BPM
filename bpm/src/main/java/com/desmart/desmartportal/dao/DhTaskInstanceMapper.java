/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhTaskInstance;

/**  
* <p>Title: 任务实例dao</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Repository
public interface DhTaskInstanceMapper {
	
	List <DhTaskInstance> selectAllTask(DhTaskInstance taskInstance);
	
	List <DhTaskInstance> selectByPrimaryKey(String taskUid);
	
	int updateByPrimaryKey(DhTaskInstance taskInstance);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int selectByusrUidFinsh(String usrUid);
	
	int getMaxSynNumber();
	
	int insertBatch(List <DhTaskInstance> list);
	
	int countByTaskId(int taskId);
	
	/**
	 * 根据insUid查询任务，任务实例类型为：SIGN或NORMAL或TRANSFER；
	 * @param insUid
	 * @return
	 */
	List<DhTaskInstance> selectByInsUidAndTaskTypeCondition(String insUid);
	/**
	 * 根据流程实例id作废任务（状态改为-1）
	 * @Title: updateByInsUids  
	 * @Description: 根据INS_UID集合更改DH_TASK_INSTANCE  
	 * @param @param insUids
	 * @param @return  
	 * @return int  
	 * @throws
	 */
	int abandonTaskByInsUidList(List<String> insUids);
	
	/**
	 * 更改指定任务编号的任务的同步号
	 * @param taskId
	 * @param synNumber
	 * @return
	 */
	int updateSynNumberByTaskId(@Param("taskId")Integer taskId, @Param("synNumber")Integer synNumber);
	
	/**
	 * 更改除指定任务之外的其它任务编号的任务的状态
	 * @param taskId
	 * @param taskStatus
	 * @return
	 */
	int updateOtherTaskStatusByTaskId(@Param("taskUid")String taskUid,
			@Param("taskId")Integer taskId, @Param("taskStatus")String taskStatus);
	
	/**
	 * 根据任务实例查询任务数据和流程
	 * @param taskInstance
	 * @return
	 */
	List <DhTaskInstance> selectTaskAndProcessInfo(DhTaskInstance taskInstance);
	
	DhTaskInstance selectByTaskIdAndUser(DhTaskInstance taskInstance);
	/**
	 * 根据taskUid修改任务状态
	 * @param taskUid
	 * @return
	 */
	Integer updateTaskStatusByTaskUid(@Param("taskUid")String taskUid);
}
