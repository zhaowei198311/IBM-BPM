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
import com.desmart.desmartportal.dao.DhAgentMapper;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.service.DhAgentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**  
* <p>Title: 代理设置实现类</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月8日  
*/
@Service
public class DhAgentServiceImpl implements DhAgentService {
	
	@Autowired
	private DhAgentMapper agentMapper;
	
	private Logger log = Logger.getLogger(DhAgentServiceImpl.class);
	
	/**
	 * 根据草稿id 删除代理数据
	 */
	@Override
	public int deleteAgentByAgentId(String agentId) {
		log.info("删除代理数据开始...");
		int result = 0;
		try {
			result = agentMapper.deleteByAgentId(agentId);
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
	public ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize) {
		log.info("查询所有代理数据开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhAgent> resultList = agentMapper.select();
			PageInfo<List<DhAgent>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询所有代理数据结束...");
		return null;
	}

	@Override
	public int saveAgent(DhAgent agent) {
		agent.setAgentId(EntityIdPrefix.DH_AGENT_META+ UUID.randomUUID().toString());
		//String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//agent.setAgentOperator(user);
		return agentMapper.save(agent);
	}
	
	/**
	 * 根据代理设置人或代理委托人查询 代理数据
	 */
	@Override
	public ServerResponse<PageInfo<List<DhAgent>>> selectByAgentPerson(String person, Integer pageNum, Integer pageSize) {
		log.info("根据代理设置人或代理委托人查询 开始...");
		try {
			PageHelper.startPage(pageNum, pageSize);
			List<DhAgent> resultList = agentMapper.selectByAgentPerson(person);
			PageInfo<List<DhAgent>> pageInfo = new PageInfo(resultList);
			return ServerResponse.createBySuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("根据代理设置人或代理委托人查询 结束...");
		return null;
	}

}
