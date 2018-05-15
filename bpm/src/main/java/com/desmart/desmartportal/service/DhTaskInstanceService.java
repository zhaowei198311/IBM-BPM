/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

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
	
	List <DhProcessInstance> selectTaskByUser(DhTaskInstance taskInstance);
	
	int getMaxTaskIdInDb();
}
