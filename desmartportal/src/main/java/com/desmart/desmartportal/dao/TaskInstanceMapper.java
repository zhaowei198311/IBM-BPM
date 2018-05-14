/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.TaskInstance;

/**  
* <p>Title: 任务实例dao</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Repository
public interface TaskInstanceMapper {
	
	List <TaskInstance> selectAllTask(TaskInstance taskInstance);
	
	List <TaskInstance> selectByPrimaryKey(String taskUid);
	
	int updateByPrimaryKey(String taskUid);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(TaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
}
