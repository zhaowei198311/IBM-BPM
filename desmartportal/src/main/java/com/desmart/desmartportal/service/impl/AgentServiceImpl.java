/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.dao.AgentDao;
import com.desmart.desmartportal.entity.Agent;
import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.service.AgentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置实现类</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月8日  
*/
@Service
public class AgentServiceImpl implements AgentService {
	
	@Autowired
	private AgentDao agentDao;
	
	private Logger log = Logger.getLogger(AgentServiceImpl.class);
	
	/**
	 * 根据草稿id 删除代理数据
	 */
	@Override
	public int deleteAgentByAgentId(String agentId) {
		log.info("删除代理数据开始...");
		int result = 0;
		try {
			result = agentDao.deleteByAgentId(agentId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("删除代理数据结束...");
		return result;
	}

	/**
	 * 查询所有代理数据
	 */
	@Override
	public ServerResponse<PageInfo<List<Agent>>> selectAgentList(Integer pageNum, Integer pageSize) {
		log.info("查询所有代理数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Agent> resultList = agentDao.select();
			PageInfo<List<Agent>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询所有代理数据结束...");
		return null;
	}

	@Override
	public int saveAgent(Agent agent) {
		agent.setAgentId(EntityIdPrefix.DH_AGENT_META+ UUID.randomUUID().toString());
		//String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//agent.setAgentOperator(user);
		return agentDao.save(agent);
	}
	
	/**
	 * 根据代理设置人或代理委托人查询 代理数据
	 */
	@Override
	public ServerResponse<PageInfo<List<Agent>>> selectByAgentPerson(String person, Integer pageNum, Integer pageSize) {
		log.info("根据代理设置人或代理委托人查询 开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<Agent> resultList = agentDao.selectByAgentPerson(person);
			PageInfo<List<Agent>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据代理设置人或代理委托人查询 结束...");
		return null;
	}

}
