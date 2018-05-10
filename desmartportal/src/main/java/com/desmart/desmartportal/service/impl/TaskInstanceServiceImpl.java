/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.dao.TaskInstanceDao;
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.ProcessInstanceService;
import com.desmart.desmartportal.service.TaskInstanceService;

/**  
* <p>Title: TaskInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Service
public class TaskInstanceServiceImpl implements TaskInstanceService {
	
	private Logger log = Logger.getLogger(TaskInstanceServiceImpl.class);
	
	@Autowired
	private TaskInstanceDao taskInstanceDao;
	
	/**
	 * 查询所有任务实例
	 */
	@Override
	public List<TaskInstance> selectAllTask(TaskInstance taskInstance) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return null;
	}
	
	/**
	 * 根据实例任务id 查询任务
	 */
	@Override
	public List<TaskInstance> selectByPrimaryKey(String taskUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return null;
	}
	
	/**
	 * 根据实例任务id 修改任务
	 */
	@Override
	public int updateByPrimaryKey(String taskUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 根据实例任务id 删除任务
	 */
	@Override
	public int deleteByPrimaryKey(String taskUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 新增任务实例
	 */
	@Override
	public void insertTask(TaskInstance taskInstance) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
	}

}
