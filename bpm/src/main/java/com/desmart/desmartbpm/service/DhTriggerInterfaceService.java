/**
 * 
 */
package com.desmart.desmartbpm.service;

import java.util.List;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhTriggerInterface;

/**  
* <p>Title: DhTriggerInterfaceService</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月13日  
*/
public interface DhTriggerInterfaceService {
	
	/**
	 * 批量插入
	 */
	ServerResponse insertBatch(List<DhTriggerInterface> tinList);
	
	/**
	 * 关联查询 表单数据 和 映射数据
	 */
	ServerResponse <List<DhTriggerInterface>> selectByTriggerActivity (DhTriggerInterface dhTriggerInterface);
	
	/**
	 * 批量修改参数映射
	 */
	ServerResponse updateBatch(List<DhTriggerInterface> tinList);

	/**
	 * 查询多个步骤的参数映射
	 * @param stepUidList
	 * @return
	 */
	List<DhTriggerInterface> listByStepUidList(List<String> stepUidList);
}
