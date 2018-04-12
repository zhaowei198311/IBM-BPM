package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;

import com.desmart.desmartbpm.entity.DhProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.BpmFormManageService;

/**
 * 表单管理控制器
 * @author loser_wu
 * @sina 2018/4/12
 */
@Controller
@RequestMapping(value = "/formManage")
public class BpmFormManageController {
	@Autowired
	private BpmFormManageService bpmFormManageService;
	
	@RequestMapping(value = "/index")
	public ModelAndView toIndex() {
        ModelAndView mv =  new ModelAndView("formManagement");
        return mv;
	}
	
	/**
	 * 获得流程及其子流程下的表单
	 */
	@RequestMapping(value="/listFormPage")
	public ServerResponse listFormPage(String formTitle,String processUid,String proVerUid,
			@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize) {
		
		List<DhProcessDefinition> dhProcessList = new ArrayList<>();
		DhProcessDefinition dhProcess1 = new DhProcessDefinition();
		dhProcess1.setProUid("0001");
		dhProcessList.add(dhProcess1);
		DhProcessDefinition dhProcess2 = new DhProcessDefinition();
		dhProcess2.setProUid("0002");
		dhProcessList.add(dhProcess2);
		//获得该流程所有子流程(包含自身)
		ServerResponse<List<DhProcessDefinition>> serverResponse = ServerResponse.createBySuccess(dhProcessList);
		if(!serverResponse.isSuccess()) {
			return serverResponse; 
		}
		return bpmFormManageService.listForm(serverResponse.getData(),formTitle,pageNum,pageSize);
	}
}
