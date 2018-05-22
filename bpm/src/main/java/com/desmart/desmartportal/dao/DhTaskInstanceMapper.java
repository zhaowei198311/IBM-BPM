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
	
	int updateByPrimaryKey(DhTaskInstance taskInstance);
	
	int deleteByPrimaryKey(String taskUid);
	
	void insertTask(DhTaskInstance taskInstance);
	
	int selectByusrUid(String usrUid);
	
	int getMaxTaskId();
	
	int insertBatch(List <DhTaskInstance> list);
	
	int countByTaskId(int taskId);
	
	/**
	 * 根据insUid查询任务，任务实例类型为：SIGN或NORMAL或TRANSFER；
	 * @param insUid
	 * @return
	 */
	List<DhTaskInstance> selectByInsUidAndTaskTypeCondition(String insUid);
	/**
	 * 
	 * @Title: updateByInsUids  
	 * @Description: 根据INS_UID集合更改DH_TASK_INSTANCE  
	 * @param @param insUids
	 * @param @return  
	 * @return int  
	 * @throws
	 */
	int updateByInsUids(List<String> insUids);
}
