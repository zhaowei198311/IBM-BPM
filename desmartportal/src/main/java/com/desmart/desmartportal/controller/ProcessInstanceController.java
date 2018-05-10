/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desmart.desmartportal.service.ProcessInstanceService;

/**  
* <p>Title: ProcessInstanceController</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月9日  
*/
@Controller
@RequestMapping(value = "/processInstance")
public class ProcessInstanceController {
	
	@Autowired
	private ProcessInstanceService processInstanceService;
}
