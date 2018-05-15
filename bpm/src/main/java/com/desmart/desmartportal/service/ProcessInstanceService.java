/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.entity.ProcessInstance;

/**  
* <p>Title: 流程实例Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
public interface ProcessInstanceService {
	
	List <ProcessInstance> selectAllProcess(ProcessInstance processInstance);
	
	List <ProcessInstance> selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKey(String insUid);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(ProcessInstance processInstance);
}
