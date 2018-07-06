package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;
import com.desmart.desmartbpm.service.DhNotifyTemplateService;

@Controller
@RequestMapping("/dhNotifyTemplate")
public class DhNotifyTemplateController {
		
	@Autowired
	private DhNotifyTemplateService dhNotifyTemplateService;
	
	@RequestMapping("/notifyTemplate")
	public ModelAndView notifyTemplate() {
		ModelAndView modelAndView = new ModelAndView("desmartbpm/notifyTemplate");
		return modelAndView;
	}
	
	@RequestMapping("/pageNotifyTemplateList")
	@ResponseBody
	public ServerResponse pageNotifyTemplateList(Integer pageNum
				,Integer pageSize,DhNotifyTemplate dhNotifyTemplate) {
		return dhNotifyTemplateService.pageDhNotifyTemplateListByCondition(pageNum, pageSize, dhNotifyTemplate);
	}
	@RequestMapping("/deleteNotifyTemplate")
	@ResponseBody
	public ServerResponse deleteNotifyTemplate(DhNotifyTemplate dhNotifyTemplate) {
		return dhNotifyTemplateService.deleteNotifyTemplate(dhNotifyTemplate);
	}
	@RequestMapping("/submitOperationTemplate")
	@ResponseBody
	public ServerResponse submitOperationTemplate(DhNotifyTemplate dhNotifyTemplate) {
		if(dhNotifyTemplate.getTemplateUid()!=null&&!"".equals(dhNotifyTemplate.getTemplateUid())) {
			return dhNotifyTemplateService.updateNotifyTemplate(dhNotifyTemplate);
		}else {
			return dhNotifyTemplateService.addNotifyTemplate(dhNotifyTemplate);
		}
	}
}
