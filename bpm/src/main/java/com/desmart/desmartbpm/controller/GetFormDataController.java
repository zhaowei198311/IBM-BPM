package com.desmart.desmartbpm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.service.GetFormDataService;

/**
 * 
 * @ClassName: GetFormDataController  
 * @Description: 获取表单字段值  
 * @author: WUZHUANG  
 * @date: 2018年6月13日  
 *
 */
@RequestMapping(value = "/test")
@Controller
public class GetFormDataController {
	
	@Autowired
	private GetFormDataService getFormDataService;
	
	/**
	 * 
	 * @Title: getFormData  
	 * @Description: 获取formData值保存到DH_PROCESS_INSTANCE  
	 * @param @param map
	 * @param @return  
	 * @return ServerResponse<?>  
	 * @throws
	 */
	@RequestMapping(value = "/getFormData")
	@ResponseBody
	public ServerResponse<?> getFormData(@RequestParam Map<String, Object> map){
		return getFormDataService.insertFormData(map);
	}
	
}
