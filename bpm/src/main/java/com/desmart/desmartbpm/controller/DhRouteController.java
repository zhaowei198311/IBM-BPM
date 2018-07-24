package com.desmart.desmartbpm.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

	/**
	 * 展示下个环节名称和处理人的工具栏
	 * @param parameter
	 * @return
	 */
	@RequestMapping(value = "/showRouteBar")
	@ResponseBody
	public ServerResponse showRouteBar(@RequestParam Map<String, String> parameter) {
		ServerResponse response=dhRouteService.showRouteBar(parameter.get("taskUid"), parameter.get("insUid"), parameter.get("activityId"), 
												parameter.get("departNo"), parameter.get("companyNum"), parameter.get("formData"));
        return response;
    }


	/**
	 * 获得可选处理人信息，当可选处理人为全体时不会访问此url
 	 */
	@RequestMapping("/choosableHandler")
	@ResponseBody
	public ServerResponse choosableHandler(@RequestParam Map<String, String> parameter, HttpServletRequest request){
		return dhRouteService.getChoosableHandler(parameter.get("insUid"), parameter.get("activityId"), parameter.get("departNo"), parameter.get("companyNum"), parameter.get("formData")
				,request, parameter.get("taskUid"));
	}
	
	/**
	 * 移动端可选处理人信息，当可选处理人为全体时不会访问此url
	 */
	@RequestMapping("/choosableHandlerMove")
	@ResponseBody
	public ServerResponse choosableHandlerMove(@RequestParam Map<String, String> parameter, HttpServletRequest request){
		try {
			return dhRouteService.choosableHandlerMove(parameter.get("insUid"), parameter.get("activityId"), parameter.get("departNo"),
					parameter.get("companyNum"), parameter.get("formData"),request, parameter.get("taskUid"),
					parameter.get("userUidArrStr"),parameter.get("condition"));
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
}
