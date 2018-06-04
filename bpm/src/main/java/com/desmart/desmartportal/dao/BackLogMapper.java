/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.DhTaskInstance;

/**  
* <p>Title: 代办任务dao</p>  
* <p>Description: </p>  
* @author liyangsa  
* @date 2018年6月4日  
*/
@Repository
public interface BackLogMapper {
	/**
	 * 根据条件查询待办
	 * @param startTime
	 * @param endTime
	 * @param dhTaskInstance
	 * @return
	 */
	List<DhTaskInstance> selectBackLogTaskInfoByCondition(
			@Param("startTime")Date startTime,
			@Param("endTime")Date endTime,
			@Param("dhTaskInstance")DhTaskInstance dhTaskInstance);
	
	/**
	 * 根据用户查询待办
	 * @param usrUid
	 * @return
	 */
	Integer selectBackLogByusrUid(String usrUid);
}
