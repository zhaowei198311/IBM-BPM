/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

/**  
* <p>Title: 用户接口</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月3日  
*/
public interface UserProcessService {
	
	/**
	 * 查询用户流程权限
	 * @return
	 */
	List<Map<String, Object>> selectByMenusProcess();
	
	/**
	 * 发起流程用户信息
	 */
	Map<String, Object> startProcessByUserInfo();
}
