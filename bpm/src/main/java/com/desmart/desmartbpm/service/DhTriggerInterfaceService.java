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
}