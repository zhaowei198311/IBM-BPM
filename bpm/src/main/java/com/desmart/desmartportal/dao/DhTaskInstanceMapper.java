/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

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
	
	int updateByPrimaryKey(String taskUid);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int getMaxTaskId();
	
	int insertBatch(List <DhTaskInstance> list);
	
	int countByTaskId(int taskId);
}
