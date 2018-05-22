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
	ServerResponse startProcess(String proUid, String proAppId, String verUid, String dataInfo, String approval);	
	
	// 流程图查看
	String viewProcessImage(String insId);
}
