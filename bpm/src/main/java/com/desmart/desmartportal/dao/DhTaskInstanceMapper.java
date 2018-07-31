/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartportal.entity.DhAgentRecord;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;

/**  
* <p>Title: 任务实例dao</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Repository
public interface DhTaskInstanceMapper {

	/**
	 * 检索满足 条件的任务实例
	 * @param taskInstance
	 * @return
	 */
	List <DhTaskInstance> selectAllTask(DhTaskInstance taskInstance);
	
	DhTaskInstance selectByPrimaryKey(String taskUid);
	
	int updateByPrimaryKeySelective(DhTaskInstance taskInstance);
	
	int deleteByPrimaryKey(String taskUid);
	
	int insertTask(DhTaskInstance taskInstance);
	
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
	 * @param taskId 任务id
	 * @param  taskUid 任务主键
	 * @return
	 */
	int abandonOtherUnfinishedTaskByTaskId(@Param("taskUid")String taskUid, @Param("taskId")Integer taskId);
	
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
	
	/**
	 * 
	 * @Title: getByUserAndFromTaskUid  
	 * @Description: 根据usrUid和fromTaskUid查询单个任务  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return DhTaskInstance  
	 * @throws
	 */
	DhTaskInstance getByUserAndFromTaskUid(DhTaskInstance dhTaskInstance);
	/**
	 * 根据条件查询待办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @return
	 */
	List<DhTaskInstance> selectBackLogTaskInfoByCondition(
			@Param("startTime")Date startTime,
			@Param("endTime")Date endTime,
			@Param("dhTaskInstance")DhTaskInstance dhTaskInstance,
			@Param("isAgent")String isAgent);
	
	/**
	 * 根据条件查询已办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<DhTaskInstance> selectPageTaskByClosedByCondition(
			@Param("startTime")Date startTime,
			@Param("endTime")Date endTime,
			@Param("dhTaskInstance")DhTaskInstance dhTaskInstance,
			@Param("isAgent")String isAgent);

	/**
	 * 根据用户查询已办数量
	 * @param userId
	 * @return
	 */
	Integer alreadyClosedTaskByusrUid(String usrId);
	
	/**
	 * 
	 * @Title: queryTransferByTypeAndStatus  
	 * @Description: 查询传阅任务（未读/已读）  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return List<DhTaskInstance>  
	 * @throws
	 */
	List<DhTaskInstance> queryTransferByTypeAndStatus(
						@Param("startTime")Date startTime,
						@Param("endTime")Date endTime,
						@Param("dhTaskInstance")DhTaskInstance dhTaskInstance);
	
	/**
	 * 
	 * @Title: queryTransferNumberByusrUid  
	 * @Description: 查询抄送任务数量 （未读/已读） 
	 * @param @param usrUid
	 * @param @return  
	 * @return Integer  
	 * @throws
	 */
	Integer queryTransferNumberByusrUid(DhTaskInstance dhTaskInstance);
	
	/**
	 * 
	 * @Title: getBytaskTypeAndUsrUid  
	 * @Description: 查询一人是否已经抄送  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return DhTaskInstance  
	 * @throws
	 */
	DhTaskInstance getBytaskTypeAndUsrUid(DhTaskInstance dhTaskInstance);

	/**
	 * 作废指定流程在指定节点的其他没完成的任务
	 * @param taskActivityId  任务节点id
	 * @param taskUid  任务主键
	 * @param insUid 流程实例主键
	 * @return
	 */
	int abandonOtherUnfinishedTasksOnTaskActivityId(@Param("taskUid")String taskUid, @Param("taskActivityId")String taskActivityId,
													@Param("insUid")String insUid);
	/*
	 * 通过insUid集合批量的查询任务
	 */
	public List<DhTaskInstance> getDhTaskInstancesByBatch(@Param("itemList")List<DhProcessInstance> itemList);

	/**
	 * 批量修改任务状态
	 * @param itemList
	 * @param dhTaskInstance 用来传递任务状态
	 * @return
	 */
	public Integer updateTaskStatusByBatch(@Param("itemList")List<DhTaskInstance> itemList
								,@Param("dhTaskInstance")DhTaskInstance dhTaskInstance);
	/**
	 * 批量修改任务
	 * @param itemList
	 * @return
	 */
	public Integer updateTaskByBatch(@Param("itemList")List<DhTaskInstance> itemList);

	/**
	 * 查询已经超时的任务
	 * @param selective
	 * @return
	 */
	List<DhTaskInstance> getTimeoutTaskList(DhTaskInstance selective);

	/**
	 * 根据传入的任务主键id集合删除对应任务的代理人
	 * @param revokeTaskUidList
	 * @return
	 */
	int updateDelegateUserBatch(List<String> revokeTaskUidList);

	/**
	 * 根据流程库id，流程id以及代理id获得需要取回的任务id集合
	 * @param agentId
	 * @param metaList
	 * @return
	 */
	List<String> queryNoFinishedTaskUidListByCondition(@Param("agentId")String agentId, @Param("metaList")List<DhProcessMeta> metaList);

	/**
     * 根据代理信息id取回被代理的但是还未完成的任务
     * @param agentId
     * @return
     */
	List<String> queryNotFinishedAgentRecordListByAgentId(String agentId);
}
