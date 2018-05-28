package com.desmart.desmartbpm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.service.DhRouteService;

@Controller
@RequestMapping(value = "/dhRoute")
public class DhRouteController {
	
	@Autowired
	private DhRouteService dhRouteService;
	
	@RequestMapping(value = "/showRouteBar")
	@ResponseBody
	public ServerResponse showRouteBar(@RequestParam Map<String, String> parameter) {
		ServerResponse response=dhRouteService.showRouteBar(parameter.get("insUid"), parameter.get("activityId"), parameter.get("departNo"), parameter.get("companyNum"), parameter.get("formData"));
        return response;
    }
	
	
}
