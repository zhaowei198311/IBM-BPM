/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhProcessInstance;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 流程实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface DhProcessInstanceService {
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectAllProcess(DhProcessInstance processInstance,Integer pageNum, Integer pageSize);
	
	DhProcessInstance selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKey(String insUid);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(DhProcessInstance processInstance);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> selectProcessByUserAndType(DhProcessInstance processInstance,Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<DhProcessInstance>>> queryByStausOrTitle(Map<String, Object> paramMap,Integer pageNum, Integer pageSize);
}
