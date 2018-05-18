package com.desmart.desmartportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartportal.dao.DhApprovalOpinionMapper;
import com.desmart.desmartportal.entity.DhApprovalOpinion;
import com.desmart.desmartportal.service.DhApprovalOpinionService;

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

}
