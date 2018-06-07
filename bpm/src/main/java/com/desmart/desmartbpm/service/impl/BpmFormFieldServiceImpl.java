package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
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
	
	@Autowired
	private DhStepMapper dhStepMapper;
	
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
	public ServerResponse queryFieldByFormIdAndStepId(String stepUid, String formUid, String fieldType) {
		List<BpmFormField> fieldList = bpmFormFieldMapper.queryFormFieldByFormUidAndType(formUid,fieldType);
		for(BpmFormField field:fieldList) {
			List<String> opActionList = bpmFormFieldMapper.queryFieldByFieldIdAndStepId(stepUid,field.getFldUid());
			if(opActionList.size()==0 || opActionList.size()==1 && opActionList.contains("PRINT")) {
				opActionList.add("EDIT");
			}
			field.setOpActionList(opActionList);
		}
		return ServerResponse.createBySuccess(fieldList);
	}

	@Override
	public ServerResponse saveFormFieldPermission(DhObjectPermission[] dhObjectPermissions) {
		int countRow = 0;
		for(DhObjectPermission dhObjectPermission:dhObjectPermissions) {
			bpmFormFieldMapper.deleteFormFieldPermission(dhObjectPermission);
		}
		for(DhObjectPermission dhObjectPermission:dhObjectPermissions) {
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
			return ServerResponse.createBySuccess("{}");
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

	@Override
	public ServerResponse<String> queryFinshedFieldPerMissionByStepUid(String stepUid) {
		//根据stepUid找到步骤绑定的表单id
		DhStep dhStep = dhStepMapper.selectByPrimaryKey(stepUid);
		if(null==dhStep) {
			return ServerResponse.createBySuccess("{}");
		}
		String formUid = dhStep.getStepObjectUid();
		//再根据表单id找到所有的表单字段对象
		List<BpmFormField> formFieldList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
		String jsonStr = "{";
		for(int i=0;i<formFieldList.size();i++) {
			BpmFormField formField = formFieldList.get(i);
			//根据表单字段id和步骤id去对象权限表中找字段权限信息
			DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUid(stepUid,formField.getFldUid());
			if(null==objPer) {
				jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"}";
			}else {
				String opAction = objPer.getOpAction();
				if(opAction.equals("VIEW")) {
					jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"}";
				}else if(opAction.equals("HIDDEN")){
					jsonStr += "\""+formField.getFldCodeName()+"\":{\"display\":\"none\"}";
				}else {
					continue;
				}
			}
			if(i!=formFieldList.size()-1) {
				jsonStr += ",";
			}
		}
		jsonStr += "}";
		return ServerResponse.createBySuccess(jsonStr);
	}
}
