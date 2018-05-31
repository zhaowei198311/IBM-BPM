package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.DhObjectPermissionService;

@Service
@Transactional
public class BpmFormFieldServiceImpl implements BpmFormFieldService{
	@Autowired
	private BpmFormFieldMapper bpmFormFieldMapper;
	
	@Autowired
	private DhObjectPermissionService dhObjectPermissionService;
	
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

	@Override
	public ServerResponse<String> queryFieldPermissionByStepUid(String stepUid) {
		//根据stepId去权限表中找字段的权限(VIEW--只读，HIDDEN--隐藏)
		List<DhObjectPermission> objPermissList = dhObjectPermissionService.getFieldPermissionByStepUid(stepUid);
		if(objPermissList.size()==0) {
			return ServerResponse.createByErrorMessage("没有权限信息");
		}else {
			String jsonStr = "{";
			for(int i=0;i<objPermissList.size();i++) {
				DhObjectPermission objPer = objPermissList.get(i);
				String fieldCodeName = bpmFormFieldMapper.queryFieldByFldUid(objPer.getOpObjUid()).getFldCodeName();
				String opAction = objPer.getOpAction();
				if(opAction.equals("VIEW")) {
					jsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"}";
				}else if(opAction.equals("HIDDEN")){
					jsonStr += "\""+fieldCodeName+"\":{\"display\":\"none\"}";
				}else {
					continue;
				}
				if(i!=objPermissList.size()-1) {
					jsonStr += ",";
				}
			}
			jsonStr += "}";
			return ServerResponse.createBySuccess(jsonStr);
		}
	}
}
