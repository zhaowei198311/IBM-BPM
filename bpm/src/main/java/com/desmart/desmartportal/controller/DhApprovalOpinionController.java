package com.desmart.desmartportal.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.UUIDTool;

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
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	@RequestMapping("insertDhApprovalOpinion.do")
	@ResponseBody
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion) {
		List<DhApprovalOpinion> list = dhApprovalOpinionServiceImpl.getDhApprovalObinionList(dhApprovalOpinion);
		if(list!=null&&list.size()>0) {
			DhApprovalOpinion dhApprovalOpinion2 = list.get(list.size()-1);
			dhApprovalOpinion.setAprOpiIndex(dhApprovalOpinion2.getAprOpiIndex()+1);
			dhApprovalOpinion.setAprTimeNumber(dhApprovalOpinion2.getAprTimeNumber()+1);
		}else {
			dhApprovalOpinion.setAprOpiIndex(0);
			dhApprovalOpinion.setAprTimeNumber(0);
		}
		dhApprovalOpinion.setAprDate(Timestamp.valueOf(DateUtil.datetoString(new Date())));
		dhApprovalOpinion.setAprOpiId(EntityIdPrefix.DH_APPROVAL_OPINION+UUIDTool.getUUID());
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		dhApprovalOpinion.setAprUserId(creator);
		Integer count = dhApprovalOpinionServiceImpl.insert(dhApprovalOpinion);
		if(count>0) {
			return ServerResponse.createBySuccessMessage("添加成功！");
		}else {
			return ServerResponse.createByErrorMessage("添加失败！");
		}
	}
	
}
