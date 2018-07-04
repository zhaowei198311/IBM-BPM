package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessRetrieve;
import com.desmart.desmartbpm.service.DhProcessRetrieveService;

@Controller
@RequestMapping("/dhProcessRetrieve")
public class DhProcessRetrieveController {
	@Autowired
	private DhProcessRetrieveService dhProcessRetrieveService;
	
	@RequestMapping("/processRetrieve")
	public ModelAndView processRetrieve() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("desmartbpm/processRetrieve");
		return modelAndView;
	}
	@RequestMapping("/queryProcessRetrieve")
	@ResponseBody
	public ServerResponse queryProcessRetrieve(String metaUid
				,Integer pageNum,Integer pageSize) {
		return dhProcessRetrieveService.queryProcessRetrieve(metaUid,pageNum,pageSize);
	}
	@RequestMapping("/operationProcessRetrieve")
	@ResponseBody
	public ServerResponse operationProcessRetrieve(DhProcessRetrieve dhProcessRetrieve
				,String metaUid) {
		if(dhProcessRetrieve.getRetrieveUid()!=null&&!"".equals(dhProcessRetrieve.getRetrieveUid())) {
			return dhProcessRetrieveService.updateProcessRetrieve(dhProcessRetrieve);
		}else {
			return dhProcessRetrieveService.addProcessRetrieve(dhProcessRetrieve,metaUid);
		}
	}
	@RequestMapping("/deleteProcessRetrieve")
	@ResponseBody
	public ServerResponse deleteProcessRetrieve(DhProcessRetrieve dhProcessRetrieve) {
		return dhProcessRetrieveService.deleteProcessRetrieve(dhProcessRetrieve);
	}
}
