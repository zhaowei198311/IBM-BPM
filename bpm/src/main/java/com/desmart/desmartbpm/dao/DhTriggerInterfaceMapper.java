/**
 * 
 */
package com.desmart.desmartbpm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTriggerInterface;

/**  
* <p>Title: DhTriggerInterfaceMapper</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月8日  
*/
@Repository
public interface DhTriggerInterfaceMapper {
	
	/**
	 * 根据条件查询
	 * @return
	 */
	List<DhTriggerInterface> selectByCondition(DhTriggerInterface dhTriggerInterface);
	
	/**
	 * 根据主键查询
	 * @param tinUid
	 * @return
	 */
	DhTriggerInterface selectByPrimaryKey(String tinUid);
	
	/**
	 * 插入数据
	 * @param dhTriggerInterface
	 * @return
	 */
	int insert(DhTriggerInterface dhTriggerInterface);
	
	/**
	 * 根据主键删除
	 * @param tinUid
	 * @return
	 */
	int deleteByPrimaryKey(String tinUid);
	
	/**
	 * 根据主键修改
	 * @param tinUid
	 * @return
	 */
	int updateByPrimaryKey(String tinUid);
	
	/**
	 * 批量插入
	 */
	int insertBatch(@Param("tinList")List<DhTriggerInterface> tinList);
}
