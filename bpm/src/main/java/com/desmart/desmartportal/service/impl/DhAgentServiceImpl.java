/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhAgentMapper;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.entity.DhAgentProInfo;
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
	private DhAgentMapper dhAgentMapper;
	
	@Autowired
	private DhProcessMetaMapper dhProcessMetaMapper;
	
	private Logger log = Logger.getLogger(DhAgentServiceImpl.class);
	
	/**
	 * 根据草稿id 删除代理数据
	 */
	@Override
	public int deleteAgentByAgentId(String agentId) {
		log.info("删除代理数据开始...");
		int result = 0;
		try {
			result = dhAgentMapper.deleteByAgentId(agentId);
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
	public ServerResponse<PageInfo<List<DhAgent>>> selectAgentList(Integer pageNum, Integer pageSize, String person) {
		String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		log.info(currUser);
		PageHelper.startPage(pageNum, pageSize);
		List<DhAgent> resultList = dhAgentMapper.selectAgentList(currUser,person);
		for(DhAgent dhAgent:resultList) {
			if("TRUE".equals(dhAgent.getAgentIsAll())) {
				DhProcessMeta dhProcessMeta = new DhProcessMeta();
				dhProcessMeta.setProName("所有流程");
				List<DhProcessMeta> dhProcessMetaList = new ArrayList<>();
				dhProcessMetaList.add(dhProcessMeta);
				dhAgent.setDhProcessMetaList(dhProcessMetaList);
			} else {
				dhAgent.setDhProcessMetaList(queryDhProcessMetaByAgentId(dhAgent.getAgentId()));
			}
		}
		PageInfo<List<DhAgent>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}
	
	/**
	 * 通过代理信息Id获得代理流程信息
	 */
	private List<DhProcessMeta> queryDhProcessMetaByAgentId(String agentId){
		List<DhProcessMeta> dhProcessMetaList = new ArrayList<>();
		for(DhAgentProInfo dhAgentProInfo:dhAgentMapper.queryDhAgentProInfoByAgentId(agentId)) {
			DhProcessMeta dhProcessMeta = dhProcessMetaMapper
					.queryByProAppIdAndProUid(dhAgentProInfo.getProAppId(), dhAgentProInfo.getProUid());
			dhProcessMetaList.add(dhProcessMeta);
		}
		return dhProcessMetaList;
	}

	@Override
	public int saveAgent(DhAgent agent) {
		agent.setAgentId(EntityIdPrefix.DH_AGENT_META+ UUID.randomUUID().toString());
		//String user = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		//agent.setAgentOperator(user);
		return dhAgentMapper.save(agent);
	}
	
	@Override
	public Map<String, String> getDelegateResult(String proAppid, String proUid, String userUid) {
		return null;
	}
}
