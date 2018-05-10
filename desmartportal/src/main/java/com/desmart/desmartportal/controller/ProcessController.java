/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.service.ProcessService;

/**  
* <p>Title: 流程controller</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Controller
@RequestMapping(value = "/process")
public class ProcessController {
	
	private Logger log = Logger.getLogger(ProcessController.class);
	
	@Autowired
	private ProcessService processService;
	
	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public ServerResponse startProcess(@RequestParam(value="proUid")String proUid, @RequestParam(value="proAppId")String proAppId, 
							@RequestParam(value="verUid")String verUid) {
		// 发起流程		
		return processService.startProcess(proUid, proAppId, verUid);
	}
	
	@RequestMapping(value = "/queryProcessByUser")
	@ResponseBody
	public ServerResponse queryProcessByUser() {
		
		return null;	
	}
}
