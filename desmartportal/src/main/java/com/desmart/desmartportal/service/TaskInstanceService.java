/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.entity.ProcessInstance;
import com.desmart.desmartportal.entity.TaskInstance;

/**  
* <p>Title: 任务实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface TaskInstanceService {
	
	List <ProcessInstance> selectAllTask(TaskInstance taskInstance);
	
	List <TaskInstance> selectByPrimaryKey(String taskUid);
	
	int updateByPrimaryKey(String taskUid);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(TaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
}
