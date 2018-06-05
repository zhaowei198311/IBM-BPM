/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 任务实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface DhTaskInstanceService {
	
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectAllTask(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectByPrimaryKey(String taskUid,Integer pageNum, Integer pageSize);
	
	int updateByPrimaryKey(String taskUid);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int selectByusrUidFinsh(String usrUid);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectTaskByUser(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	int getMaxTaskIdInDb();
	
	/**
	 * 批量插入任务
	 * @param list
	 * @return
	 */
	int insertBatch(List<DhTaskInstance> list);
	
	/**
	 * 给任务设置变量
	 */
	public ServerResponse queryTaskSetVariable(String activityId,String tkkid);
	
	/**
	 * 完成任务
	 */
	public ServerResponse perform(String data);
	
	/**
	 * 查看DH_TASK_INSTANCE表中有没有指定taskId的记录
	 * @param taskId
	 * @return
	 */
	boolean isTaskExists(int taskId);
	
	/**
	 * 获得待办任务信息, 跳转到处理待办页面的数据
	 * @param taskUid
	 * @return
	 */
	ServerResponse<Map<String, Object>> toDealTask(String taskUid);
	
	/**
	 * 根据条件查询任务
	 * @param dhTaskInstance
	 * @return
	 */
	List<DhTaskInstance> selectByCondition(DhTaskInstance dhTaskInstance);
	
	/**
	 * 根据任务实例查询任务数据和流程数据
	 * @param taskInstance
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectTaskAndProcessInfo(DhTaskInstance taskInstance,Integer pageNum, Integer pageSize);
	
	/**
	 * 
	 * @Title: queryProgressBar  
	 * @Description: 查询审批进度  
	 * @param @param bpmActivityMeta
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	ServerResponse<?> queryProgressBar(String activityId, String taskUid);
	
	/**
	 * 
	 * @Title: addSure  
	 * @Description: 加签确定  
	 * @param @param dhTaskInstance
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	ServerResponse<?> addSure(DhTaskInstance dhTaskInstance, String creator);

	/**
	 * 根据已办任务id查询已办的任务明细
	 * @param taskUid
	 */
	ServerResponse<Map<String, Object>> toFinshedTaskDetail(String taskUid);
	
	/**
	 * 
	 * @Title: toAddSign  
	 * @Description: 根据taskUid查询加签代办任务
	 * @param @param taskUid
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	ServerResponse<Map<String, Object>> toAddSign(String taskUid);
	
	/**
	 * 
	 * @Title: finishAdd  
	 * @Description: 会签人审批完成  
	 * @param @param taskUid
	 * @param @param approvalContent
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	ServerResponse<?> finishAdd(String taskUid, String activityId, String approvalContent);
	/**
	 * 根据条件查询待办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> selectBackLogTaskInfoByCondition(
			Date startTime,
			Date endTime,
			DhTaskInstance dhTaskInstance, Integer pageNum, Integer pageSize);
	/**
	 * 根据用户查询待办数量
	 * @param usrUid
	 * @return
	 */
	Integer selectBackLogByusrUid(String usrUid);

	/**
	 * 根据条件查询已办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo<List<DhTaskInstance>>> loadPageTaskByClosedByCondition(Date startTime, Date endTime, DhTaskInstance dhTaskInstance,
			Integer pageNum, Integer pageSize);

	/**
	 * 根据用户查询已办数量
	 * @param userId
	 * @return
	 */
	Integer alreadyClosedTaskByusrUid(String userId);
}
