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
import com.desmart.desmartportal.service.DhProcessService;

/**  
* <p>Title: 流程controller</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Controller
@RequestMapping(value = "/process")
public class DhProcessController {
	
	private Logger log = Logger.getLogger(DhProcessController.class);
	
	@Autowired
	private DhProcessService dhProcessService;
	
	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public ServerResponse startProcess(@RequestParam(value="proUid")String proUid, @RequestParam(value="proAppId")String proAppId, 
							@RequestParam(value="verUid")String verUid, @RequestParam(value="dataInfo")String dataInfo) {
		// 发起流程		
		return dhProcessService.startProcess(proUid, proAppId, verUid, dataInfo);
	}
	
	@RequestMapping(value = "/queryProcessByUser")
	@ResponseBody
	public ServerResponse queryProcessByUser() {
		
		return null;	
	}
	
	@RequestMapping(value = "/viewProcess")
	@ResponseBody
	public void viewProcess() {
		// 查看流程图 需要流程实例
		
	}
	
	
	///
}
