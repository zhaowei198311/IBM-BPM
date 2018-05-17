/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.DhProcessInstanceMapper;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: ProcessInstanceServiceImpl</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月11日  
*/
@Service
public class DhProcessInstanceServiceImpl implements DhProcessInstanceService {
	
	private Logger log = Logger.getLogger(DhProcessInstanceServiceImpl.class);
	
	@Autowired
	private DhProcessInstanceMapper dhProcessInstanceMapper;
	
	/**
	 * 查询所有流程实例
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance, Integer pageNum, Integer pageSize) {
		log.info("查询所有process开始");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.selectAllProcess(processInstance);
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
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
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectByPrimaryKey(String insUid,Integer pageNum, Integer pageSize) {
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
	public void insertProcess(DhProcessInstance processInstance) {
		log.info("添加新的流程实例 Start...");
		try {
			if(processInstance != null) {
				dhProcessInstanceMapper.insertProcess(processInstance);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("添加新的流程实例 End...");
	}

	/**
	 * 根据用户id 以及类型 查询用户所用有的流程 按条件 查询 
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(DhProcessInstance processInstance,Integer pageNum, Integer pageSize) {
		log.info("添加新的流程实例 Start...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.selectAllProcess(processInstance);
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("添加新的流程实例 End...");
		return null;
	}

	/* 
	 * 模糊按条件查询
	 */
	@Override
	public ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,Integer pageNum, Integer pageSize) {
		log.info("模糊查询流程实例 Start...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhProcessInstance> resultList = dhProcessInstanceMapper.queryByStausOrTitle(paramMap);
			PageInfo<List<DhProcessInstance>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("模糊查询流程实例 End...");
		return null;
	}
	

}
