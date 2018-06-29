/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
	
    /**
     * 查询满足条件的流程实例
     */
	List <DhProcessInstance> queryBySelective(DhProcessInstance processInstance);


	/**
	 * 根据主键查询流程实例
	 */
	DhProcessInstance selectByPrimaryKey(String insUid);
	
	int updateByPrimaryKeySelective(DhProcessInstance processInstance);
	
	int deleteByPrimaryKey(String insUid);
	
	int insertProcess(DhProcessInstance processInstance);
	
	/**
	 * 查找指定流程编号的所有实例
	 */
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

	/**
	 * 查找指定流程编号的主流程
	 */
	DhProcessInstance getMainProcessByInsId(int insId);

	/**
     * 根据流程实例编号，和token停留的节点来查找指定流程
	 */
	DhProcessInstance getByInsIdAndTokenActivityId(@Param("insId") int insId, @Param("tokenActivityId")String tokenActivityId);
	
	/**
	 * 
	 * @Title: queryInsDataByUser  
	 * @Description: 查询流程实例  
	 * @param @param usrUid
	 * @param @param insUid
	 * @param @return  
	 * @return List<DhTaskInstance>  
	 * @throws
	 */
	List<DhProcessInstance> queryInsDataByUser(@Param("startTime")Date startTime,
												@Param("endTime")Date endTime);
	/**
	 * 根据条件查询流程管理界面展示
	 * @param dhProcessInstance
	 * @return
	 */
	List<DhProcessInstance> getProcesssInstanceListByCondition(DhProcessInstance dhProcessInstance);
	/**
	 * 根据insid修改流程实例
	 * @param itemList
	 * @return
	 */
	Integer updateProcessInstanceByInsId(DhProcessInstance dhProcessInstance);
}
