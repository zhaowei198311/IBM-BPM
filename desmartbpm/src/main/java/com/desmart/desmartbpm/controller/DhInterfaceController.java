/**
 * 
 */
package com.desmart.desmartbpm.controller;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhInterface;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.DhInterfaceService;

/**
 * <p>
 * Title: DhInterfaceController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
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
		modev.addObject("listAll", dhInterfaceService.listDhInterface());
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
	public void deleteDhInterface(@RequestParam(value = "interfaceId") String interfaceId) {
		dhInterfaceService.delDhInterface(interfaceId);
	}

	@RequestMapping(value = "/queryDhInterfaceById")
	@ResponseBody
	public ServerResponse queryDhInterfaceById(@RequestParam(value = "interfaceId") String interfaceId,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		return dhInterfaceService.listDhInterfaceById(interfaceId, pageNum, pageSize);
	}
}
