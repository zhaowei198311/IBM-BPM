package com.desmart.desmartportal.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.common.EntityIdPrefix;
import com.desmart.desmartportal.dao.DhApprovalOpinionMapper;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.service.DhApprovalOpinionService;
import com.desmart.desmartportal.util.DateUtil;
import com.desmart.desmartportal.util.UUIDTool;

@Service
public class DhApprovalOpinionServiceImpl implements DhApprovalOpinionService {

	@Autowired
	private DhApprovalOpinionMapper dhApprovalOpinionMapper;
	@Override
	public List<DhApprovalOpinion> loadDhApprovalOpinionListByCondition(DhApprovalOpinion dhApprovalOpinion) {
		// TODO Auto-generated method stub
		return dhApprovalOpinionMapper.loadDhApprovalOpinionListByCondition(dhApprovalOpinion);
	}

	@Override
	public Integer insert(DhApprovalOpinion dhApprovalOpinion) {
		// TODO Auto-generated method stub
		return dhApprovalOpinionMapper.insert(dhApprovalOpinion);
	}

	@Override
	public List<DhApprovalOpinion> getDhApprovalObinionList(DhApprovalOpinion dhApprovalOpinion) {
		// TODO Auto-generated method stub
		return dhApprovalOpinionMapper.getDhApprovalObinionList(dhApprovalOpinion);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion) {
		// TODO Auto-generated method stub
		List<DhApprovalOpinion> list = getDhApprovalObinionList(dhApprovalOpinion);
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
		Integer count = insert(dhApprovalOpinion);
		if(count>0) {
			return ServerResponse.createBySuccessMessage("添加成功！");
		}else {
			return ServerResponse.createByErrorMessage("添加失败！");
		}
	}

}
