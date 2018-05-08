/**
 * 
 */
package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartportal.entity.Agent;
import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.common.ServerResponse;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月8日  
*/
public interface AgentService {
	
	int deleteAgentByAgentId(String agentId);
	
	ServerResponse<PageInfo<List<Agent>>> selectAgentList(Integer pageNum, Integer pageSize);
	
	ServerResponse<PageInfo<List<Agent>>> selectByAgentPerson(String person, Integer pageNum, Integer pageSize);
	int saveAgent(Agent agent);
}
