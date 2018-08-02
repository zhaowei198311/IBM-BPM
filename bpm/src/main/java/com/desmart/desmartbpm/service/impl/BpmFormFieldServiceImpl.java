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
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormFieldService;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import org.springframework.util.CollectionUtils;

@Service
public class BpmFormFieldServiceImpl implements BpmFormFieldService{
	@Autowired
	private BpmFormFieldMapper bpmFormFieldMapper;
	
	@Autowired
	private DhObjectPermissionService dhObjectPermissionService;
	
	@Autowired
	private DhObjectPermissionMapper dhObjectPermissionMapper;
	
	@Autowired
	private DhStepMapper dhStepMapper;
	
	@Autowired
	private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
	
	@Transactional
	@Override
	public ServerResponse saveFormField(BpmFormField[] fields) {
		//批量插入表单字段
		List<BpmFormField> fieldList = new ArrayList<>();
		for(BpmFormField field:fields) {
			field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD+UUID.randomUUID().toString());
			fieldList.add(field);
		}
		int countRow = bpmFormFieldMapper.insertBatch(fieldList);
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
			//获得对象权限信息表中某个步骤下指定表单的权限信息集合
			List<String> opActionList = dhObjectPermissionMapper.queryFieldByFieldIdAndStepId(stepUid,field.getFldUid());
			//新建前台显示的权限信息集合
			List<String> showOpActionList = new ArrayList<>();
			//判断权限集合中是否有（可编辑、只读、不可见）等权限
			if(opActionList.contains("HIDDEN") || opActionList.contains("EDIT")) {
				if(opActionList.contains("HIDDEN")) {
					showOpActionList.add("HIDDEN");
				}
				if(opActionList.contains("EDIT")) {
					showOpActionList.add("EDIT");
				}
			}else{
				showOpActionList.add("VIEW");
			}
			//判断权限集合中是否有打印权限
			if(opActionList.contains("PRINT")) {
				showOpActionList.add("PRINT");
			}else{
				showOpActionList.add("false");
			}
			//判断权限集合中是否有跳过必填验证权限
			if(opActionList.contains("SKIP")) {
				showOpActionList.add("SKIP");
			}else{
				showOpActionList.add("false");
			}
			field.setOpActionList(showOpActionList);
		}
		return ServerResponse.createBySuccess(fieldList);
	}

	@Transactional
	@Override
	public ServerResponse saveFormFieldPermission(DhObjectPermission[] dhObjectPermissions) {
		int countRow = 0;
		//删除旧的权限信息
		for(DhObjectPermission dhObjectPermission:dhObjectPermissions) {
			dhObjectPermissionMapper.deleteFormFieldPermission(dhObjectPermission);
		}
		for(DhObjectPermission dhObjectPermission:dhObjectPermissions) {
			//查看权限对象的权限是否为只读，只读不用存入数据库
			if("VIEW".equals(dhObjectPermission.getOpAction())) {
				countRow++;
			}else{
				//将字段权限信息存入数据库
				dhObjectPermission.setOpUid(EntityIdPrefix.DH_OBJECT_PERMISSION+UUID.randomUUID().toString());
				countRow += dhObjectPermissionMapper.save(dhObjectPermission);
			}
		}
		if(countRow!=dhObjectPermissions.length) {
			throw new PlatformException("绑定字段权限失败");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse<String> queryFieldPermissionByStepUid(String stepUid) {
		//普通字段的权限json字符串
		String jsonStr = "{";
		//标题字段和表格字段的json字符串
		String titleJsonStr = "{";
		String formUid = dhStepMapper.selectByPrimaryKey(stepUid).getStepObjectUid();
		List<BpmFormField> publicFieldList = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid);
		List<BpmFormField> fieldList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
		fieldList.addAll(publicFieldList);
		for(int i=0;i<fieldList.size();i++) {
			BpmFormField field = fieldList.get(i);
			String fieldType = field.getFldType();
			String fieldCodeName = field.getFldCodeName();
			String fldUid = field.getFldUid();
			DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid, fldUid);
			if(null == objPer) {
				//判断该字段是否为标题或表格
				if("title".equals(fieldType) || "object".equals(fieldType)){
					//判断权限的类型
					titleJsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
				}else {
					jsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
				}
			}else {
				String opAction = objPer.getOpAction();
				//判断该字段是否为标题或表格
				if("title".equals(fieldType) || "object".equals(fieldType)){
					//判断权限的类型
					if(opAction.equals("EDIT")) {
						continue;
					}else if(opAction.equals("HIDDEN")){
						titleJsonStr += "\""+fieldCodeName+"\":{\"display\":\"none\"},";
					}else {
						titleJsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
					}
				}else {
					if(opAction.equals("EDIT")) {
						continue;
					}else if(opAction.equals("HIDDEN")){
						jsonStr += "\""+fieldCodeName+"\":{\"display\":\"none\"},";
					}else {
						jsonStr += "\""+fieldCodeName+"\":{\"edit\":\"no\"},";
					}
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
			if("title".equals(fieldType) || "object".equals(fieldType)){
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(VIEW--只读，HIDDEN--隐藏)
				DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid,formField.getFldUid());
				if(null==objPer) {
					titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
				}else {
					String opAction = objPer.getOpAction();
					if(opAction.equals("HIDDEN")){
						titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"display\":\"none\"},";
					}else {
						titleJsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
					}
				}
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(PRINT--打印)
				DhObjectPermission printObjPer = dhObjectPermissionService.getFieldPrintPermissionByStepUidAndFldUid(stepUid,formField.getFldUid());
				if(null!=printObjPer) {
					titlePrintJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"yes\"},";
				}else {
					titlePrintJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"no\"},";
				}
			}else {
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(VIEW--只读，HIDDEN--隐藏)
				DhObjectPermission objPer = dhObjectPermissionService.getFieldPermissionByStepUidAndFldUidNotPrint(stepUid,formField.getFldUid());
				if(null==objPer) {
					jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
				}else {
					String opAction = objPer.getOpAction();
					if(opAction.equals("HIDDEN")){
						jsonStr += "\""+formField.getFldCodeName()+"\":{\"display\":\"none\"},";
					}else {
						jsonStr += "\""+formField.getFldCodeName()+"\":{\"edit\":\"no\"},";
					}
				}
				//根据表单字段id和步骤id去对象权限表中找字段权限信息(PRINT--打印)
				DhObjectPermission printObjPer = dhObjectPermissionService.getFieldPrintPermissionByStepUidAndFldUid(stepUid,formField.getFldUid());
				if(null!=printObjPer) {
					printJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"yes\"},";
				}else {
					printJsonStr += "\""+formField.getFldCodeName()+"\":{\"print\":\"no\"},";
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
		//根据表单id获得表单字段以及关联的子表单下的字段
		List<BpmFormField> formFiled = bpmFormFieldMapper.queryNotTitleFormFieldByFormUid(formUid); // 表单字段
		List<BpmFormField> publicFormFiled = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid); // 子表单字段
		List<BpmFormField> resultList = new ArrayList<>();
		resultList.addAll(formFiled);
		resultList.addAll(publicFormFiled);
		return ServerResponse.createBySuccess(resultList);
	}
	
	@Override
	public ServerResponse<List<BpmFormField>> queryFormTabByFormUid(String formUid) {
		//根据表单id查询表格字段集合
		List<BpmFormField> objList = bpmFormFieldMapper.queryFormTabByFormUid(formUid);
		if(objList.isEmpty()) {
			throw new PlatformException("找不到对应的表单表格");
		}
		return ServerResponse.createBySuccess(objList);
	}

	@Override
	public ServerResponse<List<BpmFormField>> queryFormTabFieldByFormUidAndTabName(String formUid, String tableName) {
		//根据表单id和表格字段集合查询表格中所有的字段集合
		List<BpmFormField> objFieldList = bpmFormFieldMapper.queryFormTabFieldByFormUidAndTabName(formUid,tableName);
		if(objFieldList.isEmpty()) {
			throw new PlatformException("找不到对应的表单表格字段");
		}
		return ServerResponse.createBySuccess(objFieldList);
	}

	@Override
	public List<BpmFormField> listByFormUidList(List<String> formUidList) {
		if (formUidList == null || formUidList.isEmpty()) {
			return new ArrayList<>();
		}
		return bpmFormFieldMapper.listByFormUidList(formUidList);
	}

	@Transactional
	@Override
	public int removeByFormUidList(List<String> formUidList) {
		if (CollectionUtils.isEmpty(formUidList)) {
			return 0;
		}
		return bpmFormFieldMapper.removeByFormUidList(formUidList);
	}
}
