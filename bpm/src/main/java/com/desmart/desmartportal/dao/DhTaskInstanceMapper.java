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
	
	DhTaskInstance selectByPrimaryKey(String taskUid);
	
	int updateByPrimaryKey(DhTaskInstance taskInstance);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int selectByusrUidFinsh(String usrUid);
	
	int getMaxSynNumber();
	
	int insertBatch(List <DhTaskInstance> list);
	
	int countByTaskId(int taskId);
	
	/**
	 * 根据条件查询任务
	 * @param dhTaskInstance
	 * @return
	 */
	List<DhTaskInstance> selectByCondition(DhTaskInstance dhTaskInstance);
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
	 * 查找任务处理人或任务代理人是指定人员，流程实例是指定实例，任务节点是指定节点, 并且是属于计划内(流程图上)的任务
	 * @param insUid
	 * @param activityBpdId
	 * @param userUid
	 * @return
	 */
	List<DhTaskInstance> listTaskByCondition(@Param("insUid")String insUid, @Param("activityBpdId")String activityBpdId, @Param("userUid")String userUid);
	
	/**
	 * 
	 * @Title: getByFromTaskUid  
	 * @Description: 根据fromTaskUid查询任务集合  
	 * @param @param fromTaskUid
	 * @param @return  
	 * @return List<DhTaskInstance>  
	 * @throws
	 */
	List<DhTaskInstance> getByFromTaskUid(@Param("fromTaskUid")String fromTaskUid);
	
	/**
	 * 
	 * @Title: getByToTaskUid  
	 * @Description: 根据toTaskUid查询下一个加签任务  
	 * @param @param toTaskUid
	 * @param @return  
	 * @return DhTaskInstance  
	 * @throws
	 */
	DhTaskInstance getByToTaskUid(@Param("toTaskUid")String toTaskUid);
}
