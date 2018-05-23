package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.desmart.desmartbpm.service.DhProcessInsManageService;

/**
 * 流程实例管理控制器
 * @author loser_wu
 * @since 2018年5月22日
 */
@Controller
public class DhProcessInsManageController {
	@Autowired
	private DhProcessInsManageService dhProcessInstanceService;
	
}
