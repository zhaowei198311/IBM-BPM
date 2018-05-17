/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartportal.entity.DhProcessInstance;

/**  
* <p>Title: 流程实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface DhProcessInstanceService {
	
	List <DhProcessInstance> selectAllProcess(DhProcessInstance processInstance);
	
	List <DhProcessInstance> selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKey(String insUid);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(DhProcessInstance processInstance);
	
	List <DhProcessInstance> selectProcessByUserAndType(DhProcessInstance processInstance);
	
	List <DhProcessInstance> queryByStausOrTitle(Map<String, Object> paramMap);
}
