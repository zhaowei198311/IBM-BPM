/**
 * 
 */
package com.desmart.desmartportal.service;

import com.desmart.desmartportal.common.ServerResponse;

/**  
* <p>Title: 流程Service</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
public interface DhProcessService {
	
	// 发起流程
	ServerResponse startProcess(String proUid, String proAppId, String verUid);	
	
	// 寻找流程变量 （IBM 引擎设置的变量）
	void queryProcessVariable(String activityId,String tkkid);
	
	// 设置审批人
	void setApprover();
}
