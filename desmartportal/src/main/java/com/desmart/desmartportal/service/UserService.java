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
public interface UserService {
	
	List<Map<String, Object>> selectByMenusProcess();
}
