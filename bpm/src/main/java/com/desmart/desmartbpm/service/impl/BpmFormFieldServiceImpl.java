package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
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
	
	@Autowired
	private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
	
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
		//根据表单id找到所关联的子表单中的所有字段
		List<BpmFormField> publicFormFieldList = bpmFormFieldMapper.queryPublicFormFieldByFormUid(formUid,fieldType);
		fieldList.addAll(publicFormFieldList);
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
		List<DhObjectPermission> objPermissList = dhObjectPermissionService.getFieldPermissionByStepUidNotPrint(stepUid);
		if(objPermissList.size()==0) {
			return ServerResponse.createBySuccess("{}");
		}else {
			String jsonStr = "{";
			String titleJsonStr = "{";
			for(int i=0;i<objPermissList.size();i++) {
				DhObjectPermission objPer = objPermissList.get(i);
				BpmFormField formField = bpmFormFieldMapper.queryFieldByFldUid(objPer.getOpObjUid());
				if(null==formField) {
					continue;
				}
				String fieldCodeName = formField.getFldCodeName();
				String fieldType = formField.getFldType();
				String opAction = objPer.getOpAction();
				//判断该字段是否为标题
				if("title".equals(fieldType)){
					if(opAction.equals("VIEW")) {
						titleJsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
					}else if(opAction.equals("HIDDEN")){
						titleJsonStr += "\""+fieldCodeName+"\":{\"display\":\"none\"},";
					}else {
						continue;
					}
				}else {
					if(opAction.equals("VIEW")) {
						jsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
					}else if(opAction.equals("HIDDEN")){
						jsonStr += "\""+fieldCodeName+"\":{\"display\":\"none\"},";
					}else {
						continue;
					}
				}
			}
			if(jsonStr.endsWith(",")) {
				jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			}
			jsonStr += "}";
			if(titleJsonStr.endsWith(",")) {
				titleJsonStr = titleJsonStr.substring(0, titleJsonStr.length()-1);
			}
			titleJsonStr += "}";
			String json = "{\"fieldJsonStr\":"+jsonStr
					+",\"titleJsonStr\":"+titleJsonStr
					+"}";
			return ServerResponse.createBySuccess(json);
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
		formFieldList.addAll(bpmFormFieldMapper.queryPublicFormFieldByFormUid(formUid, ""));
		String jsonStr = "{";
		String titleJsonStr = "{";
		String printJsonStr = "{";
		String titlePrintJsonStr = "{";
		for(int i=0;i<formFieldList.size();i++) {
			BpmFormField formField = formFieldList.get(i);
			String fieldType = formField.getFldType();
			//判断该字段是否为标题
			if("title".equals(fieldType)){
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(VIEW--只读，HIDDEN--隐藏)
				DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid,formField.getFldUid());
				if(null==objPer) {
					titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
				}else {
					String opAction = objPer.getOpAction();
					if(opAction.equals("VIEW")) {
						titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
					}else if(opAction.equals("HIDDEN")){
						titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"display\":\"none\"},";
					}else {
						continue;
					}
				}
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(PRINT--打印)
				DhObjectPermission printObjPer = dhObjectPermissionService.getFieldPrintPermissionByStepUidAndFldUid(stepUid,formField.getFldUid());
				if(null!=printObjPer) {
					titlePrintJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"yes\"},";
				}
			}else {
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(VIEW--只读，HIDDEN--隐藏)
				DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid,formField.getFldUid());
				if(null==objPer) {
					jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
				}else {
					String opAction = objPer.getOpAction();
					if(opAction.equals("VIEW")) {
						jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
					}else if(opAction.equals("HIDDEN")){
						jsonStr += "\""+formField.getFldCodeName()+"\":{\"display\":\"none\"},";
					}else {
						continue;
					}
				}
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(PRINT--打印)
				DhObjectPermission printObjPer = dhObjectPermissionService.getFieldPrintPermissionByStepUidAndFldUid(stepUid,formField.getFldUid());
				if(null!=printObjPer) {
					printJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"yes\"},";
				}
			}
		}
		if(jsonStr.endsWith(",")) {
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}
		jsonStr += "}";
		if(titleJsonStr.endsWith(",")) {
			titleJsonStr = titleJsonStr.substring(0, titleJsonStr.length()-1);
		}
		titleJsonStr += "}";
		if(printJsonStr.endsWith(",")) {
			printJsonStr = printJsonStr.substring(0, printJsonStr.length()-1);
		}
		printJsonStr += "}";
		if(titlePrintJsonStr.endsWith(",")) {
			titlePrintJsonStr = titlePrintJsonStr.substring(0, titlePrintJsonStr.length()-1);
		}
		titlePrintJsonStr += "}";
		String json = "{\"fieldJsonStr\":"+jsonStr
					+",\"titleJsonStr\":"+titleJsonStr
					+",\"fieldPrintJsonStr\":"+printJsonStr
					+",\"titlePrintJsonStr\":"+titlePrintJsonStr
					+"}";
		return ServerResponse.createBySuccess(json);
	}

	@Override
	public ServerResponse<List<BpmFormField>> queryFieldByFormUid(String formUid) {
		List<BpmFormField> FormFiled = bpmFormFieldMapper.queryFormFieldByFormUid(formUid); // 表单字段
		List<BpmFormField> publicFormFiled = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid); // 子表单字段
		List<BpmFormField> resultList = new ArrayList<>();
		resultList.addAll(FormFiled);
		resultList.addAll(publicFormFiled);
		return ServerResponse.createBySuccess(resultList);
	}
}
