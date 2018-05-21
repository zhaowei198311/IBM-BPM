/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhProcessInstance;

/**  
* <p>Title: 流程实例dao</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Repository
public interface DhProcessInstanceMapper {
	
	List <DhProcessInstance> selectAllProcess(DhProcessInstance processInstance);
	
	DhProcessInstance selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKey(String insUid);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(DhProcessInstance processInstance);
	
	DhProcessInstance queryByInsId(int insId);
	
	List <DhProcessInstance> queryByStausOrTitle(Map<String, Object> paramMap);
}
