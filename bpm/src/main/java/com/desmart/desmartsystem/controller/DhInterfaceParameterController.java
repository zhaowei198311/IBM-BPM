/**
 * 
 */
package com.desmart.desmartsystem.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterfaceParameter;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;

/**  
* <p>Title: 接口参数控制层</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年4月17日  
*/
@Controller
@RequestMapping(value = "/interfaceParamers")
public class DhInterfaceParameterController {
	
	
	@Autowired
	private DhInterfaceParameterService dhInterfaceParameterService;
	
	@RequestMapping(value = "/index")
	@ResponseBody
	public ServerResponse listAll(@RequestParam(value="intUid") String intUid,@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		return dhInterfaceParameterService.listDhInterfaceParameter(intUid, pageNum, pageSize);
	}
	
	@RequestMapping(value = "/add")
	@ResponseBody
	public ServerResponse saveInterfaceParamers(DhInterfaceParameter dhInterfaceParameter) {
		return dhInterfaceParameterService.saveDhInterfaceParametere(dhInterfaceParameter);
	}
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public ServerResponse updateInterfaceParamers(DhInterfaceParameter dhInterfaceParameter) {
		return dhInterfaceParameterService.updateDhInterfaceParametere(dhInterfaceParameter);
	}
	
	@RequestMapping(value = "/queryByparaId")
	@ResponseBody
	public DhInterfaceParameter queryByparaUid(String paraUid) {
		return dhInterfaceParameterService.selectByparaUid(paraUid);
	}
	
	@RequestMapping(value = "/homePage")
	public ModelAndView homePage(String intUid) {	
		System.err.println(intUid);
		ModelAndView mv = new ModelAndView("desmartsystem/index");
		mv.addObject("intUid", intUid);
		return mv;
	}
}
