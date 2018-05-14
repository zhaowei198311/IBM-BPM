/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.ProcessInstanceMapper;
import com.desmart.desmartportal.dao.TaskInstanceMapper;
import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.entity.ProcessInstance;
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.TaskInstanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: TaskInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月11日  
*/
@Service
public class TaskInstanceServiceImpl implements TaskInstanceService {
	
	private Logger log = Logger.getLogger(TaskInstanceServiceImpl.class);
	
	@Autowired
	private TaskInstanceMapper taskInstanceMapper;
	
	@Autowired
	private ProcessInstanceMapper processInstanceMapper;
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<TaskInstance>>> selectAllTask(TaskInstance taskInstance, Integer pageNum, Integer pageSize) {
		log.info("查询taskInstance开始......");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<TaskInstance> resultList = taskInstanceMapper.selectAllTask(taskInstance);
			PageInfo<List<TaskInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询taskInstance结束......");
		return null;
	}
	
	/**
	 * 根据实例任务id 查询任务
	 */
	@Override
	public ServerResponse<PageInfo<List<TaskInstance>>> selectByPrimaryKey(String taskUid, Integer pageNum, Integer pageSize) {
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
			taskInstanceMapper.insertTask(taskInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
	}

	/* 
	 * 根据用户id 查询 有多少代办任务
	 */
	@Override
	public int selectByusrUid(String usrUid) {
		return	taskInstanceMapper.selectByusrUid(usrUid);
	}

	/* 
	 * 根据用户id  查询 他有哪些流程
	 */
	@Override
	public List<ProcessInstance> selectTaskByUser(TaskInstance taskInstance) {
		log.info("根据用户id查询有哪些流程开始......");
		List <ProcessInstance> resultList = new ArrayList<ProcessInstance>();
		try {
			List <TaskInstance> taskInstanceList = taskInstanceMapper.selectAllTask(taskInstance);//根据userId查询taskList
			if(taskInstanceList.size() > 0) {
				for(TaskInstance taskInstance1 : taskInstanceList) {
					ProcessInstance processInstance = new ProcessInstance();
					processInstance.setInsUid(taskInstance1.getInsUid());//获取taskList里的insUid
					List <ProcessInstance> processInstanceList= processInstanceMapper.selectAllProcess(processInstance);//根据instUid查询processList
					for(ProcessInstance p : processInstanceList) {
						resultList.add(p);
						System.err.println(p.getInsTitle());
					}
				}
			}
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据用户id查询有哪些流程结束......");
		return resultList;
	}

}
