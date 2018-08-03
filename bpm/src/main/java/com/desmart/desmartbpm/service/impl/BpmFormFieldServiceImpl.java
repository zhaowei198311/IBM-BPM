package com.desmart.desmartbpm.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.desmart.desmartbpm.entity.*;
import com.desmart.desmartbpm.enums.DhObjectPermissionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
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
	public ServerResponse<String> queryFieldPermissionByStepUid(DhStep formStep) {
        DhFieldPermissionData result = new DhFieldPermissionData();
        String formUid = formStep.getStepObjectUid();
        // 获得表单字段
        List<BpmFormField> fieldList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
        // 获得公共表单字段
        List<BpmFormField> publicFieldList = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid);
		List<BpmFormField> allFields = new ArrayList<>(fieldList);
		allFields.addAll(publicFieldList);
		if (CollectionUtils.isEmpty(allFields)) {
		    // 如果没有表单字段，返回
            return ServerResponse.createBySuccess(JSON.toJSONString(result));
        }
        List<DhObjectPermission> dhObjectPermissions = dhObjectPermissionMapper.listByStepUid(formStep.getStepUid());
		Set<String> editSet = new HashSet<>();
		Set<String> hiddenSet = new HashSet<>();
		Set<String> skipSet = new HashSet<>();
        for (DhObjectPermission permission : dhObjectPermissions) {
            DhObjectPermissionAction actionType = DhObjectPermissionAction.codeOf(permission.getOpAction());
            switch (actionType) {
                case EDIT: // 编辑
                    editSet.add(permission.getOpObjUid());
                    break;
                case HIDDEN: // 隐藏
                    hiddenSet.add(permission.getOpObjUid());
                    break;
                case SKIP: // 跳过必填验证
                    skipSet.add(permission.getOpObjUid());
                    break;
                default:
                    break;
            }
        }
        for (BpmFormField field : allFields) {
            String fieldUid = field.getFldUid();
            String codeName = field.getFldCodeName();
            if (BpmFormField.TYPE_TITLE.equals(field.getFldType()) || BpmFormField.TYPE_OBJECT.equals(field.getFldType())) {
                // 字段是title
                if (hiddenSet.contains(fieldUid)) { // 配置了隐藏
                    result.addTitleHiddenPermission(codeName);
                } else if (editSet.contains(fieldUid)) {
                    // 配置了可编辑，不放在json
                } else { // 配置了只读
                    result.addTitleReadonlyPermission(codeName);
                }
                if (skipSet.contains(fieldUid)) { // 配置了跳过必填
                    result.addTitleSkipPermissionYes(codeName);
                }
            } else {
                // 字段是普通字段
                if (hiddenSet.contains(fieldUid)) { // 配置了隐藏
                    result.addFieldHiddenPermission(codeName);
                } else if (editSet.contains(fieldUid)) {
                    // 配置了可编辑，不放在json
                } else { // 配置了只读
                    result.addFieldReadonlyPermission(codeName);
                }
                if (skipSet.contains(fieldUid)) { // 配置了跳过必填
                    result.addFieldSkipPermissionYes(codeName);
                }
            }
        }
        return ServerResponse.createBySuccess(JSON.toJSONString(result));
	}

	@Override
	public ServerResponse<String> queryFinshedFieldPerMissionByStepUid(DhStep formStep) {
        DhFieldPermissionData result = new DhFieldPermissionData();
        String formUid = formStep.getStepObjectUid();
        // 获得表单字段
        List<BpmFormField> fieldList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
        // 获得公共表单字段
        List<BpmFormField> publicFieldList = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid);
        List<BpmFormField> allFields = new ArrayList<>(fieldList);
        allFields.addAll(publicFieldList);
        if (CollectionUtils.isEmpty(allFields)) {
            // 如果没有表单字段，返回
            return ServerResponse.createBySuccess(JSON.toJSONString(result));
        }
        List<DhObjectPermission> dhObjectPermissions = dhObjectPermissionMapper.listByStepUid(formStep.getStepUid());
        Set<String> hiddenSet = new HashSet<>();
        Set<String> printSet = new HashSet<>();
        for (DhObjectPermission permission : dhObjectPermissions) {
            DhObjectPermissionAction actionType = DhObjectPermissionAction.codeOf(permission.getOpAction());
            switch (actionType) {
                case HIDDEN: // 隐藏
                    hiddenSet.add(permission.getOpObjUid());
                    break;
                case PRINT: // 可打印
                    printSet.add(permission.getOpObjUid());
                    break;
                default:
                    break;
            }
        }
        for (BpmFormField field : allFields) {
            String fieldUid = field.getFldUid();
            String codeName = field.getFldCodeName();
            if (BpmFormField.TYPE_TITLE.equals(field.getFldType()) || BpmFormField.TYPE_OBJECT.equals(field.getFldType())) {
                // 字段是title
                if (hiddenSet.contains(fieldUid)) { // 配置了隐藏
                    result.addTitleHiddenPermission(codeName);
                } else { // 没有配置隐藏，就是只读
                    result.addTitleReadonlyPermission(codeName);
                }
                if (printSet.contains(fieldUid)) { // 配置了可打印
                    result.addTitlePrintPermissionYes(codeName);
                } else { // 没有配置可打印
                    result.addTitlePrintPermissionNo(codeName);
                }
            } else {
                // 字段是普通字段
                if (hiddenSet.contains(fieldUid)) { // 配置了隐藏
                    result.addFieldHiddenPermission(codeName);
                } else { // 没有配置隐藏，就是只读
                    result.addFieldReadonlyPermission(codeName);
                }
                if (printSet.contains(fieldUid)) { // 配置了可打印
                    result.addFieldPrintPermissionYes(codeName);
                } else { // 没有配置可打印
                    result.addFieldPrintPermissionNo(codeName);
                }
            }
        }
		return ServerResponse.createBySuccess(JSON.toJSONString(result));
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
