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
	
	int updateByPrimaryKeySelective(DhProcessInstance processInstance);
	
	int deleteByPrimaryKey(String insUid);
	
	void insertProcess(DhProcessInstance processInstance);
	
	List<DhProcessInstance> queryByInsId(int insId);
	
	List <DhProcessInstance> queryByStausOrTitle(Map<String, Object> paramMap);
	/**
	 * 
	 * @Title: updateBySelective  
	 * @Description: 根据DhProcessInstance变量条件更新DhProcessInstance  
	 * @param @param dhProcessInstance
	 * @param @return  
	 * @return int  
	 * @throws
	 */
	int updateBySelective(DhProcessInstance dhProcessInstance);
}
