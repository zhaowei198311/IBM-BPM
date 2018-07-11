package com.desmart.desmartsystem.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.service.DhInterfaceExecuteService;
import com.desmart.desmartsystem.util.Json;

/**
 * 
 * @ClassName: DhInterfaceExecuteController  
 * @Description: 执行接口  
 * @author xsf  
 * @date 2018年6月4日  
 *
 */

@Controller
@RequestMapping(value = "/interfaceExecute")
public class DhInterfaceExecuteController extends  BaseController{
	private Logger logger = Logger.getLogger(DhInterfaceExecuteController.class);
	
	
	@Autowired
	private DhInterfaceExecuteService dhInterfaceExecuteService;
	
	/**
	 * 
	 * @Title: interfaceSchedule  
	 * @Description: 接口调度中心
	 * @param @param  JSONObject
	 * @param @return  Json
	 * @return ServerResponse
	 * @throws Exception
	 */
	@RequestMapping(value = "/interfaceSchedule")
	@ResponseBody
	public Json interfaceSchedule(@RequestBody JSONObject jsonObject) {
		Json j=new Json();
		try {
			j.setSuccess(true);
			j.setMsg("接口调用成功");
			j=dhInterfaceExecuteService.interfaceSchedule(jsonObject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			j.setSuccess(false);
			e.printStackTrace();
			j.setMsg("接口调用失败");
		}
		return 	j;
	}
	
}
