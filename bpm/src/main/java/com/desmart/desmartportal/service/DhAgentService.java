/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.common.ServerResponse;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月8日  
*/
public interface DhAgentService {
	
	int deleteAgentByAgentId(String agentId);
	
	ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<DhAgent>>> selectByAgentPerson(String person, Integer pageNum, Integer pageSize);
	int saveAgent(DhAgent agent);
}
