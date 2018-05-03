package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormFieldService;

@Service
public class BpmFormFieldServiceImpl implements BpmFormFieldService{
	@Autowired
	private BpmFormFieldMapper bpmFormFieldMapper;

	@Override
	public ServerResponse saveFormField(BpmFormField[] fields) {
		List<BpmFormField> fieldList = new ArrayList<>();
		for(BpmFormField field:fields) {
			field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD+UUID.randomUUID().toString());
			fieldList.add(field);
		}
		int countRow = bpmFormFieldMapper.saveFormField(fieldList);
		if(countRow>0) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	@Override
	public ServerResponse queryFieldByFormIdAndStepId(String stepUid, String formUid) {
		List<BpmFormField> fieldList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
		for(BpmFormField field:fieldList) {
			String opAction = bpmFormFieldMapper.queryFieldByFieldIdAndStepId(stepUid,field.getFldUid());
			field.setOpAction(opAction);
		}
		return ServerResponse.createBySuccess(fieldList);
	}

	@Override
	public ServerResponse saveFormFieldPermission(DhObjectPermission[] dhObjectPermissions) {
		int countRow = 0;
		for(DhObjectPermission dhObjectPermission:dhObjectPermissions) {
			bpmFormFieldMapper.deleteFormFieldPermission(dhObjectPermission);
			if("EDIT".equals(dhObjectPermission.getOpAction())) {
				countRow++;
			}else{
				dhObjectPermission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION+UUID.randomUUID().toString());
				countRow += bpmFormFieldMapper.saveFormFieldPermission(dhObjectPermission);
			}
		}
		if(countRow!=dhObjectPermissions.length) {
			throw new PlatformException("绑定字段权限失败");
		}
		return ServerResponse.createBySuccess();
	}
}
