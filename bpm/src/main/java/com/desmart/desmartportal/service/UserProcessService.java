/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.desmartbpm.entity.DhProcessMeta;

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
	Map<String, List<DhProcessMeta>> selectByMenusProcess();
	
	/**
	 * 发起流程用户信息
	 */
	Map<String, Object> startProcessByUserInfo(String proUid,String proAppId,String verUid,String proName,String categoryName);
	
	
	List<Map<String, Object>> selectByMenusProcess2();

}
