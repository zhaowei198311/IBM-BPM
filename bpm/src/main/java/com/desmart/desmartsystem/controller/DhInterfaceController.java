/**
 * 
 */
package com.desmart.desmartsystem.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.DhInterface;
import com.desmart.desmartsystem.service.DhInterfaceParameterService;
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
	@Autowired
	private DhInterfaceParameterService dhInterfaceParameterService;

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView modev = new ModelAndView("desmartsystem/interfaceManagement");
		return modev;
	}
	
	@RequestMapping(value = "/interfaceTest")
	public ModelAndView interfaceTest(String intUid,String intTitle) {
		try {
			intTitle=new String(intTitle.getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ModelAndView modev = new ModelAndView("desmartsystem/usermanagement/interface/interfaceTest");
		modev.addObject("intUid", intUid);
		modev.addObject("intTitle", intTitle);
		modev.addObject("dhInterfaceParameterList", dhInterfaceParameterService.querybyintUid(intUid));
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
