/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartportal.common.EntityIdPrefix;
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
	
	@Autowired
    private DhProcessMetaService dhProcessMetaService;
	
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
	
	private Logger Log = Logger.getLogger(DhAgentController.class);
	
	@RequestMapping(value = "/index")
	public String index() {
		return "desmartportal/agent";
	}
	
	/**
	 * 根据参数组合查询某个用户的代理设置信息
	 */
	@RequestMapping(value = "/queryAgentByList")
	@ResponseBody
	public ServerResponse queryAgentByList(@RequestParam(value="person")String person,
			@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		try {
			return dhAgentService.selectAgentList(pageNum, pageSize,person);
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage("查询数据失败");
		}
	}
	
	/**
     * 根据分类id列出下面的所有流程元数据(包括子分类下的流程元数据)
     */
    @RequestMapping(value = "/listByCategoryUid")
    @ResponseBody
    public ServerResponse listProcessMetaByCategoryUid(String categoryUid, String proName) {
        ServerResponse<List<DhProcessCategory>> serverResponse = dhProcessCategoryService.listChildrenCategoryAndThisCategory(categoryUid);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        return dhProcessMetaService.listDhProcessMetaByCategoryList(serverResponse.getData(), proName, null, null);
    }
	
	@RequestMapping(value = "/deleteAgentById")
	@ResponseBody
	public void deleteAgentById(@RequestParam(value="agentId")String agentId) {
		dhAgentService.deleteAgentByAgentId(agentId);
	}
	
	@RequestMapping(value = "/saveAgent")
	@ResponseBody
	public String saveAgent(@RequestParam(value="agentOdate") Timestamp agentOdate,@RequestParam(value="agentOperator")String agentOperator,
			@RequestParam(value="agentClientele")String agentClientele,@RequestParam(value="agentStatus")String agentStatus) {
		try {	
			DhAgent agent = new DhAgent();
			agent.setAgentOdate(agentOdate);
			agent.setAgentOperator(agentOperator);
			agent.setAgentClientele(agentClientele);
			agent.setAgentId(EntityIdPrefix.DH_AGENT_META+UUIDTool.getUUID());
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
