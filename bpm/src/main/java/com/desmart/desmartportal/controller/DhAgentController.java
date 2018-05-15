/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.service.DhAgentService;
import com.desmart.desmartsystem.util.UUIDTool;


/**  
* <p>Title: 代理设置控制层</p>  
* <p>Description: </p>  
* @author 张志颖 
* @date 2018年5月88日  
* 
*/
@Controller
@RequestMapping(value = "/agent")
public class DhAgentController {
	
	@Autowired
	private DhAgentService dhAgentService;
	
	private Logger Log = Logger.getLogger(DhAgentController.class);
	
	@RequestMapping(value = "/index")
	public String index() {
		return "desmartportal/agent";
	}
	
	@RequestMapping(value = "/queryAgentByList")
	@ResponseBody
	public ServerResponse queryAgentByList(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		return dhAgentService.selectAgentList(pageNum, pageSize);
	}
	
	@RequestMapping(value = "/deleteAgentById")
	@ResponseBody
	public void deleteAgentById(@RequestParam(value="agentId")String agentId) {
		dhAgentService.deleteAgentByAgentId(agentId);
	}
	
	@RequestMapping(value = "/queryAgentByPerson")
	@ResponseBody
	public ServerResponse queryAgentByPerson(@RequestParam(value="person")String person,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		return dhAgentService.selectByAgentPerson(person, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/saveAgent")
	@ResponseBody
	public String saveAgent(@RequestParam(value="agentOdate") Date agentOdate,@RequestParam(value="agentOperator")String agentOperator,
			@RequestParam(value="agentClientele")String agentClientele,@RequestParam(value="agentStatus")String agentStatus) {
		try {	
			DhAgent agent = new DhAgent();
			agent.setAgentOdate(agentOdate);
			agent.setAgentOperator(agentOperator);
			agent.setAgentClientele(agentClientele);
			agent.setAgentId(EntityIdPrefix.DH_AGENT_META+UUIDTool.getUUID());
			agent.setAgentSdate(new Date());
			agent.setAgentStatus(agentStatus);
			System.out.println("==========================="+agent);
			dhAgentService.saveAgent(agent);
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
}
