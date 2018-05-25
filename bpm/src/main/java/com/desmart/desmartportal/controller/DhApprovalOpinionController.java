package com.desmart.desmartportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.service.DhApprovalOpinionService;

@Controller
@RequestMapping("/dhApprovalOpinion")
public class DhApprovalOpinionController {

	@Autowired
	private DhApprovalOpinionService dhApprovalOpinionServiceImpl;
	
	@RequestMapping("loadDhApprovalOpinion.do")
	@ResponseBody
	public ServerResponse loadDhApprovalOpinionListByCondition(DhApprovalOpinion dhApprovalOpinion) {
		List<DhApprovalOpinion> list = dhApprovalOpinionServiceImpl.loadDhApprovalOpinionListByCondition(dhApprovalOpinion);
		
		return ServerResponse.createBySuccess(list);
	}
	
	@RequestMapping("insertDhApprovalOpinion.do")
	@ResponseBody
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion) {
		return dhApprovalOpinionServiceImpl.insertDhApprovalOpinion(dhApprovalOpinion);
	}
	
}
