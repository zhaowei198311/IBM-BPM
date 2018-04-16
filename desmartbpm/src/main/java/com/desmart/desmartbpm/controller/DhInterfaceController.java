/**
 * 
 */
package com.desmart.desmartbpm.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.entity.DhInterface;
import com.desmart.desmartbpm.service.DhInterfaceService;

/**  
* <p>Title: DhInterfaceController</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月12日  
*/
@Controller
@RequestMapping(value = "/interfaces")
public class DhInterfaceController {
	
	@Autowired
	private DhInterfaceService dhInterfaceService;
	
	@RequestMapping(value = "/index")
	public ModelAndView queryDhInterface() {	
		ModelAndView modev = new ModelAndView("interfaceManagement");
		modev.addObject("listAll",dhInterfaceService.listDhInterface());
		modev.addObject("pageNum", "1");
		modev.addObject("pageSize", "10");
		return modev;
	}
	
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public void addDhInterface(DhInterface dhInterface) {
		dhInterfaceService.saveDhInterface(dhInterface);
	}
	
	@RequestMapping(value = "/del")
	@ResponseBody
	public void deleteDhInterface(@RequestParam(value="interfaceId") String interfaceId) {
		System.err.println(interfaceId);
	}
}
