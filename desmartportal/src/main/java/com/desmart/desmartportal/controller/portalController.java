/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desmart.desmartbpm.service.DhObjectPermissionService;

/**  
* <p>Title: testController</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月19日  
*/
@Controller
@RequestMapping(value = "/user")
public class portalController {
	
	@Autowired
	private DhObjectPermissionService dhObjectPermissionService;
	
	@RequestMapping(value = "/a")
	public String A() {
		dhObjectPermissionService.getPermissionStartOfProcess("2066.0fd7d7bb-f83e-411e-bd43-075b0fe64a51", "25.fb99340a-d6d1-4bc3-85fc-78a1149937e4", "2064.bc487f4b-af74-422c-ad9e-ca06e26e9a44");
		return "";
	}
	
	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}
}
