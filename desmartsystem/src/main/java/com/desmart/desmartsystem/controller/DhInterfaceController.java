/**
 * 
 */
package com.desmart.desmartsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartsystem.common.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.service.DhInterfaceService;

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
	
	@RequestMapping(value = "/update")
	@ResponseBody
	public void updateDhInterface(DhInterface dhInterface) {
		dhInterfaceService.updateDhInterface(dhInterface);
	}
	
	@RequestMapping(value = "/queryDhInterfaceById")
	@ResponseBody
	public DhInterface selectDhInterfaceByid(@RequestParam(value = "intUid")String intUid) {
		return dhInterfaceService.selectDhInterfaceByid(intUid);
	}
	
	@RequestMapping(value = "/queryDhInterfaceByTitle")
	@ResponseBody
	public ServerResponse listDhInterfaceByTitle(@RequestParam(value = "intTitle") String intTitle,
			@RequestParam(value = "intType") String intType,
			@RequestParam(value = "intStatus") String intStatus,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("intTitle", intTitle);
		paramsMap.put("intType", intType);
		paramsMap.put("intStatus", intStatus);
		return dhInterfaceService.listDhInterfaceByTitle(paramsMap, pageNum, pageSize);
	}
}
