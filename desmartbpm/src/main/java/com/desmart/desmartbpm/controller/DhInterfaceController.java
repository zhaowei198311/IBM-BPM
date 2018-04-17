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
import com.desmart.desmartbpm.entity.DhProcessCategory;
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
	public ModelAndView index() {
		ModelAndView modev = new ModelAndView("interfaceManagement");
		return modev;
	}
	
	@RequestMapping(value = "/queryDhInterfaceList")
	@ResponseBody
	public ServerResponse queryDhInterface(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
        // todo
        return  dhInterfaceService.listDhInterface(pageNum, pageSize);
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public void addDhInterface(DhInterface dhInterface) {
		dhInterfaceService.saveDhInterface(dhInterface);
	}

	@RequestMapping(value = "/del")
	@ResponseBody
	public void deleteDhInterface(@RequestParam(value = "intUid") String interfaceId) {
		dhInterfaceService.delDhInterface(interfaceId);
	}

	@RequestMapping(value = "/queryDhInterfaceByTitle")
	@ResponseBody
	public ServerResponse listDhInterfaceByTitle(@RequestParam(value = "intTitle") String intTitle,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		return dhInterfaceService.listDhInterfaceByTitle(intTitle, pageNum, pageSize);
	}
}
