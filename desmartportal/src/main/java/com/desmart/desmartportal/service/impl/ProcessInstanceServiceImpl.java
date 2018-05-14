/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.dao.ProcessInstanceDao;
import com.desmart.desmartportal.entity.ProcessInstance;
import com.desmart.desmartportal.service.ProcessInstanceService;

/**  
* <p>Title: ProcessInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月11日  
*/
@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
	
	private Logger log = Logger.getLogger(ProcessInstanceServiceImpl.class);
	
	@Autowired
	private ProcessInstanceDao processInstanceDao;
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public List<ProcessInstance> selectAllProcess(ProcessInstance processInstance) {
		log.info("查询所有process开始");
		try {
			return processInstanceDao.selectAllProcess(processInstance);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询所有process结束");
		return null;
	}
	
	/**
	 * 根据流程实例主键 查询流程
	 */
	@Override
	public List<ProcessInstance> selectByPrimaryKey(String insUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return null;
	}
	
	/**
	 * 根据流程实例主键 修改流程
	 */
	@Override
	public int updateByPrimaryKey(String insUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 根据流程实例主键 删除流程
	 */
	@Override
	public int deleteByPrimaryKey(String insUid) {
		log.info("");
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("");
		return 0;
	}
	
	/**
	 * 添加新的流程实例
	 */
	@Override
	public void insertProcess(ProcessInstance processInstance) {
		log.info("添加新的流程实例 Start...");
		try {
			if(processInstance != null) {
				processInstanceDao.insertProcess(processInstance);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("添加新的流程实例 End...");
	}

}
