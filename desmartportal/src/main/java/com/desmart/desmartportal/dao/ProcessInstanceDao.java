/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.ProcessInstance;

/**  
* <p>Title: 流程实例dao</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Repository
public interface ProcessInstanceDao {
	
	List <ProcessInstance> selectAllProcess(ProcessInstance processInstance);
	
	List <ProcessInstance> selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKey(String insUid);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(ProcessInstance processInstance);
}
