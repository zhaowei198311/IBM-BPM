package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.common.constant.EntityIdPrefix;
import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.DateUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhNotifyTemplateMapper;
import com.desmart.desmartbpm.entity.DhNotifyTemplate;
import com.desmart.desmartbpm.service.DhNotifyTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

@Service
public class DhNotifyTemplateServiceImpl implements DhNotifyTemplateService {
	
	@Autowired
	private DhNotifyTemplateMapper dhNotifyTemplateMapper;
	
	@Override
	public ServerResponse pageDhNotifyTemplateListByCondition(Integer pageNum, Integer pageSize,
			DhNotifyTemplate dhNotifyTemplate) {
		PageHelper.startPage(pageNum,pageSize);
		PageHelper.orderBy("create_time desc,update_time desc");
		List<DhNotifyTemplate> resultList = dhNotifyTemplateMapper.getDhNotifyTemplatesByCondition(dhNotifyTemplate);
		PageInfo<List<DhNotifyTemplate>> pageInfo = new PageInfo(resultList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse deleteNotifyTemplate(DhNotifyTemplate dhNotifyTemplate) {
		Integer count = dhNotifyTemplateMapper.delete(dhNotifyTemplate);
		if(count>0) {
			return ServerResponse.createBySuccessMessage("删除通知模板成功");
		}else {
			return ServerResponse.createByErrorMessage("删除通知模板失败");
		}
	}

	@Override
	public ServerResponse addNotifyTemplate(DhNotifyTemplate dhNotifyTemplate) {
		String currentUserUid = (String)SecurityUtils
				.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		Date currDate = DateUtil.format(new Date());
		dhNotifyTemplate.setTemplateUid(EntityIdPrefix.DH_NOTIFY_TEMPLATE+UUID.randomUUID());
		dhNotifyTemplate.setCreateUserUid(currentUserUid);
		dhNotifyTemplate.setCreateTime(currDate);
		Integer count = dhNotifyTemplateMapper.insert(dhNotifyTemplate);
		if(count > 0) {
			return ServerResponse.createBySuccessMessage("新增通知模板成功");
		}else {
			return ServerResponse.createByErrorMessage("新增通知模板失败");
		}
	}

	@Override
	public ServerResponse updateNotifyTemplate(DhNotifyTemplate dhNotifyTemplate) {
		String currentUserUid = (String)SecurityUtils
				.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		Date currDate = DateUtil.format(new Date());
		dhNotifyTemplate.setUpdateUserUid(currentUserUid);
		dhNotifyTemplate.setUpdateTime(currDate);
		Integer count = dhNotifyTemplateMapper.update(dhNotifyTemplate);
		if(count>0) {
			return ServerResponse.createBySuccessMessage("修改通知模板成功");
		}else {
			return ServerResponse.createByErrorMessage("修改通知模板失败");
		}
	}

	@Override
	public List<DhNotifyTemplate> listByTemplateUidList(List<String> templateUidList) {
		if (templateUidList == null || templateUidList.isEmpty()) {
			return new ArrayList<>();
		}
		return dhNotifyTemplateMapper.listByTemplateUidList(templateUidList);
	}

	@Override
	public int insertBatch(List<DhNotifyTemplate> notifyTemplateList) {
		if (CollectionUtils.isEmpty(notifyTemplateList)) {
			return 0;
		}
		return dhNotifyTemplateMapper.insertBatch(notifyTemplateList);
	}


}
