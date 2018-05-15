/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;

import com.desmart.desmartportal.entity.DhProcessInstance;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessInstanceService;
import com.desmart.desmartportal.service.DhTaskInstanceService;

/**  
* <p>Title: ProcessInstanceController</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月9日  
*/
@Controller
@RequestMapping(value = "/process")
public class DhProcessInstanceController {
	
	@Autowired
	private DhProcessInstanceService dhProcessInstanceService;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;

	
	/**
	 * 根据userId查询instuid,再根据instuid查processInstanceList
	 * @param userId 用户id
	 * @param proUid 流程id
	 * @param proAppId 流程应用库id
	 * @param verUid 流程版本id
	 * @return
	 */
	@RequestMapping("/getInfo")
	@ResponseBody
	public ServerResponse getInfo(@RequestParam(value="userId")String userId,@RequestParam(value="proAppId")String proAppId,
			@RequestParam(value="proUid")String proUid,@RequestParam(value="verUid")String verUid) {
		//userId = "00011178";
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setUsrUid(userId);
		List <DhProcessInstance> resultList = dhTaskInstanceService.selectTaskByUser(taskInstance);//根据userId查询taskList		
		return ServerResponse.createBySuccess(resultList);
	}
}
