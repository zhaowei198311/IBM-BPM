/**
 * 
 */
package com.desmart.desmartportal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.Agent;
/**  
* <p>Title: 代理设置接口</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月8日  
*/
@Repository
public interface AgentMapper {
		
	int deleteByAgentId(String agentId);
	
	List <Agent> select();
	
	List <Agent> selectByAgentPerson(String person);
	
	int save(Agent agent);
}
