/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.Map;

/**  
* <p>Title: 流程表单</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月10日  
*/
public interface DhProcessFormService {
	
	public Map<String, Object>  queryProcessForm(String proAppId, String proUid, String verUid);
}
