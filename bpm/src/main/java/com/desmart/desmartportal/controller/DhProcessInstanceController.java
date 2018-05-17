/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.ServerResponse;

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
			@RequestParam(value="proUid")String proUid,@RequestParam(value="verUid")String verUid,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		//userId = "00011178";
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setUsrUid(userId);
		return dhTaskInstanceService.selectTaskByUser(taskInstance, pageNum, pageSize);//根据userId查询taskList		
	}
	
	@RequestMapping("/queryProcessByUserAndType")
	@ResponseBody
	public ServerResponse queryProcessByUserAndType(@RequestParam(value="insTitle",required = false)String insTitle,
								@RequestParam(value="insStatusId",required = false)String insStatusId) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("insTitle", insTitle);
		paramsMap.put("insStatusId", insStatusId);
		List <DhProcessInstance> resultList = dhProcessInstanceService.queryByStausOrTitle(paramsMap);
		return ServerResponse.createBySuccess(resultList);
	}
	
	@RequestMapping("/queryProcessByActive")
	@ResponseBody
	public ServerResponse queryProcessByActive(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
	// 根据当前用户查询 他有哪些流程
		DhTaskInstance taskInstance = new DhTaskInstance();
		taskInstance.setUsrUid(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
		return  dhTaskInstanceService.selectTaskByUser(taskInstance,pageNum,pageSize);
	}
}
