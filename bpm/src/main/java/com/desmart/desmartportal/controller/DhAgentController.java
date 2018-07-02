/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.desmart.common.exception.PlatformException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.entity.DhAgent;
import com.desmart.desmartportal.service.DhAgentService;
import com.desmart.desmartsystem.util.UUIDTool;


/**  
* <p>Title: 代理设置控制层</p>  
* <p>Description: </p>  
* @author loser_wu 
* @date 2018年5月18日  
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
        return dhAgentService.listDhProcessMetaByCategoryList(serverResponse.getData(), proName);
    }
	
    /**
     * 添加代理信息(添加之前做排他验证)
     */
    @RequestMapping(value = "/addAgentInfo")
    @ResponseBody
    public ServerResponse addAgentInfo(@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date agentSdate,
    		@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")Date agentEdate,
    		String[] agentProMetaUidArr,String agentPerson,String agentIsAll) {
    	try {
    		//先判断某个时间段，要分配的流程是否处于已启用的分配代理
    		ServerResponse<List<DhProcessMeta>> serverResponse = dhAgentService.queryConformProMeta(agentSdate,agentEdate,agentProMetaUidArr);
    		return dhAgentService.addAgentInfo(agentSdate,agentEdate,serverResponse.getData(),agentPerson,agentIsAll);
    	} catch(PlatformException e) {
    		return ServerResponse.createByErrorMessage(e.getMessage());
    	} catch(Exception e) {
    		return ServerResponse.createByErrorMessage("添加代理失败");
    	}
    }
    
    /**
     * 修改代理信息
     */
    @RequestMapping(value = "/updateAgentInfo")
    @ResponseBody
    public ServerResponse updateAgentInfo(DhAgent dhAgent,String[] agentProMetaUidArr) {
    	try {
    		ServerResponse<List<DhProcessMeta>> serverResponse;
    		//判断代理是否启用
    		if("ENABLED".equals(dhAgent.getAgentStatus())) {
    			//先判断除目标代理以外的代理中是否有目标代理流程
    	    	serverResponse = dhAgentService.queryConformProMetaNotSelf(dhAgent,agentProMetaUidArr);
    		}else {
    			serverResponse = dhAgentService.listProMeta(agentProMetaUidArr);
    		}
	    	return dhAgentService.updateAgentInfo(dhAgent,serverResponse.getData());
    	} catch(PlatformException e) {
    		return ServerResponse.createByErrorMessage(e.getMessage());
    	} catch(Exception e) {
    		return ServerResponse.createByErrorMessage("修改代理失败");
    	}
    }
    
    /**
     * 修改代理设置的状态
     */
    @RequestMapping(value = "/updateAgentStatus")
    @ResponseBody
    public ServerResponse updateAgentStatus(DhAgent dhAgent,String[] agentProMetaUidArr) {
    	try {
    		if("ENABLED".equals(dhAgent.getAgentStatus())) {
    			//判断要启用的代理是否与其他代理起冲突
    			dhAgentService.queryConformProMetaNotSelf(dhAgent,agentProMetaUidArr);
        	}
	    	return dhAgentService.updateAgentStatus(dhAgent);
    	} catch(PlatformException e) {
    		return ServerResponse.createByErrorMessage(e.getMessage());
    	} catch(Exception e) {
    		return ServerResponse.createByErrorMessage("修改代理状态失败");
    	}
    }
    
	@RequestMapping(value = "/deleteAgentById")
	@ResponseBody
	public ServerResponse deleteAgentById(@RequestParam(value="agentId")String agentId) {
		try {
			return dhAgentService.deleteAgentByAgentId(agentId);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
}
