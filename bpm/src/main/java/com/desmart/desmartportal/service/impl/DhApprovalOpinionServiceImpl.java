package com.desmart.desmartportal.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.entity.DhTaskInstance;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
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
		return dhApprovalOpinionMapper.loadDhApprovalOpinionListByCondition(dhApprovalOpinion);
	}

	@Override
	public Integer insert(DhApprovalOpinion dhApprovalOpinion) {
		return dhApprovalOpinionMapper.insert(dhApprovalOpinion);
	}

	@Override
	public List<DhApprovalOpinion> getDhApprovalObinionList(DhApprovalOpinion dhApprovalOpinion) {
		return dhApprovalOpinionMapper.getDhApprovalObinionList(dhApprovalOpinion);
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public ServerResponse insertDhApprovalOpinion(DhApprovalOpinion dhApprovalOpinion) {
		List<DhApprovalOpinion> list = getDhApprovalObinionList(dhApprovalOpinion);
		if(list != null && list.size() > 0) {
			DhApprovalOpinion dhApprovalOpinion2 = list.get(list.size()-1);
			dhApprovalOpinion.setAprOpiIndex(dhApprovalOpinion2.getAprOpiIndex()+1);
			dhApprovalOpinion.setAprTimeNumber(dhApprovalOpinion2.getAprTimeNumber()+1);
		}else {
			dhApprovalOpinion.setAprOpiIndex(0);
			dhApprovalOpinion.setAprTimeNumber(0);
		}
		dhApprovalOpinion.setAprOpiId(EntityIdPrefix.DH_APPROVAL_OPINION+UUIDTool.getUUID());
		dhApprovalOpinion.setAprDate(Timestamp.valueOf(DateUtil.datetoString(new Date())));
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		dhApprovalOpinion.setAprUserId(creator);
		Integer count = insert(dhApprovalOpinion);
		if(count > 0) {
			return ServerResponse.createBySuccessMessage("添加成功！");
		}else {
			return ServerResponse.createByErrorMessage("添加失败！");
		}
	}

	@Override
	public ServerResponse saveDhApprovalOpinionWhenSubmitTask(DhTaskInstance currTask, DhActivityConf dhActivityConf, JSONObject dataJson) {
		if (!"TRUE".equals(dhActivityConf.getActcCanApprove())) {
			return ServerResponse.createBySuccess();
		}
		JSONObject approvalData = dataJson.getJSONObject("approvalData");// 获取审批信息
		if (approvalData == null) {
			return ServerResponse.createByErrorMessage("缺少审批意见");
		}
		String aprOpiComment = approvalData.getString("aprOpiComment");
		if (StringUtils.isBlank(aprOpiComment)) {
			return ServerResponse.createByErrorMessage("缺少审批意见");
		}
		DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
		dhApprovalOpinion.setInsUid(currTask.getInsUid());
		dhApprovalOpinion.setTaskUid(currTask.getTaskUid());
		dhApprovalOpinion.setActivityId(currTask.getTaskActivityId());
		dhApprovalOpinion.setAprOpiComment(aprOpiComment);
		dhApprovalOpinion.setAprStatus("通过");
		return this.insertDhApprovalOpinion(dhApprovalOpinion);
	}

	@Override
	public ServerResponse saveDhApprovalOpinionWhenRejectTask(DhTaskInstance currTask, JSONObject dataJson) {
		JSONObject approvalData = dataJson.getJSONObject("approvalData");// 获取审批信息
		if (approvalData == null) {
			return ServerResponse.createByErrorMessage("缺少审批意见");
		}
		String aprOpiComment = approvalData.getString("aprOpiComment");
		if (StringUtils.isBlank(aprOpiComment)) {
			return ServerResponse.createByErrorMessage("缺少审批意见");
		}
		DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
		dhApprovalOpinion.setInsUid(currTask.getInsUid());
		dhApprovalOpinion.setTaskUid(currTask.getTaskUid());
		dhApprovalOpinion.setActivityId(currTask.getTaskActivityId());
		dhApprovalOpinion.setAprOpiComment(aprOpiComment);
		dhApprovalOpinion.setAprStatus("驳回");
		return this.insertDhApprovalOpinion(dhApprovalOpinion);
	}

	@Override
	public ServerResponse saveDhApprovalOpinionWhenFinishAdd(DhTaskInstance currTask, String content) {
		if (StringUtils.isBlank(content)) {
			return ServerResponse.createByErrorMessage("缺少审批意见");
		}
		DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
		dhApprovalOpinion.setInsUid(currTask.getInsUid());
		dhApprovalOpinion.setTaskUid(currTask.getTaskUid());
		dhApprovalOpinion.setActivityId(currTask.getTaskActivityId());
		dhApprovalOpinion.setAprOpiComment(content);
		dhApprovalOpinion.setAprStatus("会签意见");
		return this.insertDhApprovalOpinion(dhApprovalOpinion);
	}

	@Override
	public ServerResponse saveDhApprovalOpinionWhenAutoCommit(DhTaskInstance currTask, String adminUid) {
		DhApprovalOpinion dhApprovalOpinion = new DhApprovalOpinion();
		dhApprovalOpinion.setAprOpiId(EntityIdPrefix.DH_APPROVAL_OPINION + UUIDTool.getUUID());
		dhApprovalOpinion.setAprDate(Timestamp.valueOf(DateUtil.datetoString(new Date())));
		dhApprovalOpinion.setAprOpiIndex(0);
		dhApprovalOpinion.setAprTimeNumber(0);
		dhApprovalOpinion.setAprUserId(adminUid);
		dhApprovalOpinion.setInsUid(currTask.getInsUid());
		dhApprovalOpinion.setTaskUid(currTask.getTaskUid());
		dhApprovalOpinion.setActivityId(currTask.getTaskActivityId());
		dhApprovalOpinion.setAprOpiComment("管理员自动提交");
		dhApprovalOpinion.setAprStatus("自动提交");
		return dhApprovalOpinionMapper.insert(dhApprovalOpinion) > 0 ? ServerResponse.createBySuccess()
				: ServerResponse.createByErrorMessage("保存审批意见失败");
	}


}
