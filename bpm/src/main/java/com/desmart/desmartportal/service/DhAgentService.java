/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;
import java.util.Map;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhAgent;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月8日  
*/
public interface DhAgentService {
	int deleteAgentByAgentId(String agentId);
	
	/**
	 * 根据参数组合查询某个用户的代理设置信息
	 */
	ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize, String person);
	
	int saveAgent(DhAgent agent);
	
	/**
	 * 获得指定任务的代理人和代理,没有代理人时返回null
	 * @param proAppid
	 * @param proUid
	 * @param userUid
	 * @return  Map中的键：  delegateUser  代理人    agentId 代理主键
	 */
	Map<String, String> getDelegateResult(String proAppid, String proUid, String userUid);
	
}
