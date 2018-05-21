/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartportal.common.ServerResponse;
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
	public void queryTaskSetVariable(String activityId,String tkkid);
	
	/**
	 * 完成任务
	 */
	public void perform(String tkkid);
	
	/**
	 * 查看DH_TASK_INSTANCE表中有没有指定taskId的记录
	 * @param taskId
	 * @return
	 */
	boolean isTaskExists(int taskId);
	
	/**
	 * 代办任务详细信息
	 */
	public Map<String, Object> taskInfo(String taskUid);
}
