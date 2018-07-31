package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.FormJSONObject;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.BpmFormRelePublicFormMapper;
import com.desmart.desmartbpm.dao.DhObjectPermissionMapper;
import com.desmart.desmartbpm.dao.DhProcessCategoryMapper;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.dao.DhStepMapper;
import com.desmart.desmartbpm.dao.DhTriggerInterfaceMapper;
import com.desmart.desmartbpm.enginedao.LswSnapshotMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.BpmFormRelePublicForm;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class BpmFormManageServiceImpl implements BpmFormManageService{
	@Autowired
	private BpmFormManageMapper bpmFormManageMapper;
	
	@Autowired
	private DhProcessDefinitionMapper dhProcessDefinitionMapper;
	
	@Autowired
	private DhProcessCategoryMapper dhProcessCategoryMapper;
	
	@Autowired
	private DhProcessMetaMapper dhProcessMetaMapper;
	
	@Autowired
	private BpmFormFieldMapper bpmFormFieldMapper;
	
	@Autowired
	private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
	
	@Autowired
	private LswSnapshotMapper lswSnapshotMapper;
	
	@Autowired
	private BpmFormRelePublicFormMapper bpmFormRelePublicFormMapper;
	
	@Autowired
	private DhObjectPermissionMapper dhObjectPermissionMapper;
	
	@Autowired
	private DhStepMapper dhStepMapper;
	
	@Override
	public ServerResponse queryProFormByName(String dynTitle,String proUid,String proVersion) {
		//根据表单名查询某个流程定义下是否有重复表单
		BpmForm bpmForm = bpmFormManageMapper.queryProFormByName(dynTitle,proUid,proVersion);
		if(null==bpmForm) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	@Override
	public ServerResponse queryFormByFormUid(String formUid) {
		//根据表单id查询表单并返回
		BpmForm bpmForm = bpmFormManageMapper.queryFormByFormUid(formUid);
		if(null==bpmForm) {
			return ServerResponse.createByError();
		}else {
			return ServerResponse.createBySuccess(bpmForm);
		}
	}

	@Override
	public ServerResponse listFormByProDefinition(String formTitle, String proUid, String proCategoryUid,
			String proVerUid, Integer pageNum, Integer pageSize) {
		if(null==proCategoryUid || "".equals(proCategoryUid)) {
			PageHelper.startPage(pageNum, pageSize);
			//获得表单数据
			List<BpmForm> formList = bpmFormManageMapper.listFormByProDefinition(formTitle,proUid,proVerUid);
			List<String> snapshotIdList = new ArrayList<>();
			//获得表单所属快照id集合
			for(BpmForm bpmForm:formList) {
				String notPrefixProVerUid = bpmForm.getProVersion().replace("2064.", "");
				snapshotIdList.add(notPrefixProVerUid);
			}
			//根据快照id集合获得快照对象集合
			List<LswSnapshot> lswSnapshotList = lswSnapshotMapper.listBySnapshotIdList(snapshotIdList);
			//将对应的快照名称赋值给form对象
			for(BpmForm bpmForm:formList) {
				String notPrefixProVerUid = bpmForm.getProVersion().replace("2064.", "");
				for(LswSnapshot lswSnapshot:lswSnapshotList) {
					if(notPrefixProVerUid.equals(lswSnapshot.getSnapshotId())) {
						bpmForm.setProVerName(lswSnapshot.getName());
						break;
					}
				}
			}
			PageInfo<List<BpmForm>> pageInfo = new PageInfo(formList);
			return ServerResponse.createBySuccess(pageInfo);
		}else{
			//根据父分类获得子分类集合
			List<DhProcessCategory> categoryList = dhProcessCategoryMapper.listByCategoryParent(proCategoryUid);
			//添加本身
			categoryList.add(dhProcessCategoryMapper.queryByCategoryUid(proCategoryUid));
			//根据分类集合找到所有的流程元集合
			List<DhProcessMeta> metaList = dhProcessMetaMapper.listByCategoryList(categoryList);
			PageHelper.startPage(pageNum, pageSize);
			//获得的数据
			List<BpmForm> formList = bpmFormManageMapper.listFormByProUidList(metaList,formTitle);
			List<String> snapshotIdList = new ArrayList<>();
			//获得表单所属快照id集合
			for(BpmForm bpmForm:formList) {
				String notPrefixProVerUid = bpmForm.getProVersion().replace("2064.", "");
				snapshotIdList.add(notPrefixProVerUid);
			}
			//根据快照id集合获得快照对象集合
			List<LswSnapshot> lswSnapshotList = lswSnapshotMapper.listBySnapshotIdList(snapshotIdList);
			//将对应的快照名称赋值给form对象
			for(BpmForm bpmForm:formList) {
				String notPrefixProVerUid = bpmForm.getProVersion().replace("2064.", "");
				for(LswSnapshot lswSnapshot:lswSnapshotList) {
					if(notPrefixProVerUid.equals(lswSnapshot.getSnapshotId())) {
						bpmForm.setProVerName(lswSnapshot.getName());
						break;
					}
				}
			}
			PageInfo<List<BpmForm>> pageInfo = new PageInfo(formList);
			return ServerResponse.createBySuccess(pageInfo);
		}
	}

	@Override
	public List<DhProcessDefinition> listDefinitionAll() {
		List<DhProcessDefinition> dhProcessDefinitionList = dhProcessDefinitionMapper.listAll();
		List<DhProcessDefinition> resultList = new ArrayList<>();
		List<String> snapshotIdList = new ArrayList<>();
		for(DhProcessDefinition dhProcessDefinition:dhProcessDefinitionList) {
			String proVerUid = dhProcessDefinition.getProVerUid().replace("2064.", "");
			snapshotIdList.add(proVerUid);
		}
		List<LswSnapshot> lswSnapshotList = lswSnapshotMapper.listBySnapshotIdList(snapshotIdList);
		for(DhProcessDefinition dhProcessDefinition:dhProcessDefinitionList) {
			String proVerUid = dhProcessDefinition.getProVerUid().replace("2064.", "");	
			for(LswSnapshot lswSnapshot:lswSnapshotList) {
				if(proVerUid.equals(lswSnapshot.getSnapshotId())) {
					DhProcessDefinition newDhProDef = new DhProcessDefinition();
					newDhProDef.setProUid(dhProcessDefinition.getProUid());
					newDhProDef.setProVerUid(dhProcessDefinition.getProVerUid());
					newDhProDef.setVerName(lswSnapshot.getName());
					resultList.add(newDhProDef);
					break;
				}
			}
		}
		return resultList;
	}
	
	@Override
	public ServerResponse updateFormInfo(BpmForm bpmForm) throws Exception {
		//修改表单基本信息
		int countRow = bpmFormManageMapper.updateFormInfo(bpmForm);
		if(countRow!=1) {
			throw new PlatformException("表单修改异常");
		}
		return ServerResponse.createBySuccess();
	}
	
	@Override
	public ServerResponse deleteForm(String[] formUids) {
		//批量删除表单
		for(String formUid:formUids) {
			BpmForm bpmForm = bpmFormManageMapper.queryFormByFormUid(formUid);
			if(null==bpmForm) {
				throw new PlatformException("找不到指定的表单数据");
			}
			//删除表单数据
			int countRow = bpmFormManageMapper.deleteForm(formUid);
			if(1!=countRow) {
				throw new PlatformException("删除表单信息失败");
			}
			//删除表单字段映射接口参数信息
			dhTriggerInterfaceMapper.deleteByDynUid(formUid);
			//获得表单所有的字段
			List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
			//批量删除字段权限
			deleteFieldPermiss(filedList,formUid);
			//删除表单关联子表单的信息
			bpmFormRelePublicFormMapper.deleteFormRelePublicForm(formUid);
			//删除表单字段
			int fieldCountRow = bpmFormFieldMapper.deleteFormField(formUid);
			if(fieldCountRow!=filedList.size()) {
				throw new PlatformException("删除表单字段失败");
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public int removeFormsByFormUidList(List<String> formUidList) {
		if (CollectionUtils.isEmpty(formUidList)) {
			return 0;
		}
		return bpmFormManageMapper.removeFormsByFormUidList(formUidList);
	}

	private void deleteFieldPermiss(List<BpmFormField> fieldList,String formUid) {
		List<BpmFormField> publicFormFieldList = bpmFormRelePublicFormMapper.listPublicFormFieldByFormUid(formUid);
		fieldList.addAll(publicFormFieldList);
		List<DhStep> dhStepList = dhStepMapper.queryStepListByFormUid(formUid);
		//删除字段权限信息
		for(BpmFormField field:fieldList) {
			for(DhStep dhStep:dhStepList) {
				DhObjectPermission dhObjectPermission = new DhObjectPermission();
				dhObjectPermission.setOpObjUid(field.getFldUid());
				dhObjectPermission.setStepUid(dhStep.getStepUid());
				dhObjectPermissionMapper.delectByDhObjectPermissionSelective(dhObjectPermission);
			}
		}
	}
	
	@Override
	public ServerResponse copyForm(BpmForm bpmForm) {
		//查询表单是否存在
		BpmForm oldBpmForm = bpmFormManageMapper.queryFormByFormUid(bpmForm.getDynUid());
		if(null==oldBpmForm) {
			throw new PlatformException("找不到指定的表单数据");
		}
		//复制表单信息
		String newFormUid = copyFormInfo(bpmForm,oldBpmForm);
		//复制表单字段信息
		copyFormFieldInfo(newFormUid,oldBpmForm.getDynUid());
		//复制子表单关联信息
		copyPublicFormInfo(newFormUid,oldBpmForm.getDynUid());
		return ServerResponse.createBySuccess(newFormUid);
	}

	/**
	 * 复制子表单关联信息
	 */
	private void copyPublicFormInfo(String newFormUid, String dynUid) {
		//查询表单关联的子表单id
		List<String> publicFormUidList = bpmFormRelePublicFormMapper.queryFormReleByFormUid(dynUid);
		for(String publicFormUid:publicFormUidList) {
			//将子表单id关联新的表单
			int countRow = bpmFormRelePublicFormMapper.saveFormRelePublicForm(newFormUid, publicFormUid);
			if(1!=countRow) {
				throw new PlatformException("复制子表单关联失败");
			}
		}
	}

	/**
	 * 复制表单字段信息
	 */
	private void copyFormFieldInfo(String newFormUid,String oldFormUid) {
		//获得旧表单的所有字段信息
		List<BpmFormField> oldFields = bpmFormFieldMapper.queryFormFieldByFormUid(oldFormUid);
		int fieldSize = oldFields.size();
		if(fieldSize!=0) {
			List<BpmFormField> newFields = new ArrayList<>();
			for(BpmFormField field:oldFields) {
				field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD+UUID.randomUUID().toString());
				field.setFormUid(newFormUid);
				newFields.add(field);
			}
			//将所有的字段信息批量插入表中
			int countRow = bpmFormFieldMapper.insertBatch(newFields);
			if(countRow!=fieldSize) {
				throw new PlatformException("表单字段复制异常");
			}
		}
	}

	/**
	 * 复制表单信息
	 */
	private String copyFormInfo(BpmForm bpmForm,BpmForm oldBpmForm) {
		//将旧表单信息复制给新表单
		String newFormUid = EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString();
		bpmForm.setDynUid(newFormUid);//重新生成唯一主键
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmForm.setCreator(currUser);
        /*bpmForm.setFormNoExpression(oldBpmForm.getFormNoExpression());
        bpmForm.setFormNoStatic(oldBpmForm.getFormNoStatic());*/
        bpmForm.setDynWebpage(oldBpmForm.getDynWebpage());
        bpmForm.setDynContent(oldBpmForm.getDynContent());
        int countRow = bpmFormManageMapper.saveForm(bpmForm);
        if(countRow!=1) {
        	throw new PlatformException("表单数据复制异常");
        }
        return newFormUid;
	}

	@Override
	public ServerResponse listBySelective(BpmForm bpmForm) {
		//模糊查询表单---表单名，表单描述
		List<BpmForm> bpmFormList = bpmFormManageMapper.listBySelective(bpmForm);
		return ServerResponse.createBySuccess(bpmFormList);
	}

	@Override
	public ServerResponse updateFormContent(FormJSONObject formJsonObj) {
		BpmForm bpmForm = formJsonObj.getForm();
		//修改表单内容
		bpmFormManageMapper.updateFormContent(bpmForm);	
		//获得表单原来所有的字段
		List<BpmFormField> oldFieldList = bpmFormFieldMapper.queryFormFieldByFormUid(bpmForm.getDynUid());
		BpmFormField[] bpmFormFieldArr = formJsonObj.getFormFieldArr();
		//记录需要添加的字段
		List<BpmFormField> insertFieldList = new ArrayList<>();
		//记录需要删除的旧表单字段
		List<BpmFormField> deleteFieldList = new ArrayList<>();
		for(BpmFormField field:bpmFormFieldArr) {
			boolean insertFieldFlag = true;
			for(int i=0;i<oldFieldList.size();i++) {
				BpmFormField oldField = oldFieldList.get(i);
				//判断新、旧字段的name(同一个表单中字段的唯一标识)
				if(field.getFldCodeName().equals(oldField.getFldCodeName())) {
					//修改旧的表单字段
					int updateFieldRow = bpmFormFieldMapper.updateFormField(field);
					if(updateFieldRow!=1) {
						throw new PlatformException("修改字段("+field.getFldName()+")信息失败");
					}
					//移除旧表单字段集合中的该字段，直至最后删除剩余的字段集合
					deleteFieldList.add(oldField);
					insertFieldFlag = false;
					break;
				}
			}
			//当新字段未匹配到对应的旧字段，则添加
			if(insertFieldFlag) {
				field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD + UUID.randomUUID().toString());
				insertFieldList.add(field);
			}
		}
		if(insertFieldList.size()!=0) {
			int insertFieldRow = bpmFormFieldMapper.insertBatch(insertFieldList);
			if(insertFieldRow != insertFieldList.size()) {
				throw new PlatformException("新增字段信息失败");
			}
		}
		oldFieldList.removeAll(deleteFieldList);
		if(oldFieldList.size()!=0) {
			int deleteFieldRow = bpmFormFieldMapper.deleteFieldBatch(oldFieldList);
			dhTriggerInterfaceMapper.deleteTriIntBatchByFieldInfo(oldFieldList);
			if(oldFieldList.size()!=deleteFieldRow) {
				throw new PlatformException("删除字段信息失败");
			}
		}
		//修改表单关联子表单信息
		String[] publicFormUidArr = formJsonObj.getPublicFormUidArr();
		//获得表单关联原来的子表单信息
		List<String> oldPublicFormUidList = bpmFormRelePublicFormMapper.queryFormReleByFormUid(bpmForm.getDynUid());
		//记录要添加的关联信息
		List<BpmFormRelePublicForm> insertReleList = new ArrayList<>(); 
		//记录要删除的关联信息
		List<String> deletePublicUidList = new ArrayList<>();
		for(String publicFormUid:publicFormUidArr) {
			//新增关联信息的变量
			boolean insertReleFlag = true;
			if(publicFormUid==null || "".equals(publicFormUid)) {
				continue;
			}
			for(int i=0;i<oldPublicFormUidList.size();i++) {
				String oldPublicFormUid = oldPublicFormUidList.get(i);
				if(publicFormUid.equals(oldPublicFormUid)) {
					//去除依旧存在的子表单id
					deletePublicUidList.add(oldPublicFormUid);
					insertReleFlag = false;
					break;
				}
			}
			if(insertReleFlag) {
				BpmFormRelePublicForm insertRele = new BpmFormRelePublicForm();
				insertRele.setFormUid(bpmForm.getDynUid());
				insertRele.setPublicFormUid(publicFormUid);
				insertReleList.add(insertRele);
			}
		}
		if(insertReleList.size()!=0) {
			int insertReleRow = bpmFormRelePublicFormMapper.insertBatch(insertReleList);
			if(insertReleRow != insertReleList.size()) {
				throw new PlatformException("新增表单关联信息失败");
			}
		}
		oldPublicFormUidList.removeAll(deletePublicUidList);
		//获得绑定该表单的所有步骤集合
		List<DhStep> dhStepList = dhStepMapper.queryStepListByFormUid(bpmForm.getDynUid());
		for(String publicFormUid:oldPublicFormUidList) {
			BpmFormRelePublicForm bpmFormRelePublicForm = new BpmFormRelePublicForm(bpmForm.getDynUid(), publicFormUid);
			int deleteReleRow = bpmFormRelePublicFormMapper.deleteRele(bpmFormRelePublicForm);
			if(deleteReleRow!=1) {
				throw new PlatformException("删除表单关联信息失败");
			}
			//获得子表单的所有字段信息
			List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(publicFormUid);
			for(BpmFormField field:filedList) {
				for(DhStep dhStep:dhStepList) {
					//删除该表单下子表单字段权限信息
					DhObjectPermission dhObjectPermission = new DhObjectPermission();
					dhObjectPermission.setOpObjUid(field.getFldUid());
					dhObjectPermission.setStepUid(dhStep.getStepUid());
					//删除该步骤下该字段权限信息
					dhObjectPermissionMapper.delectByDhObjectPermissionSelective(dhObjectPermission);
				}
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse isBindStep(String[] formUids) {
		for(String formUid:formUids) {
			List<DhStep> stepList = dhStepMapper.queryStepListByFormUid(formUid);
			if(!stepList.isEmpty()) {
				throw new PlatformException("表单已经被步骤绑定");
			}
		}
		return ServerResponse.createBySuccess();
	}
	
	@Override
	public List<BpmForm> listAllFormsOfProcessDefinition(String proUid, String proVerUid) {
		if (StringUtils.isBlank(proUid) || StringUtils.isBlank(proVerUid)) {
			return new ArrayList<>();
		}
		BpmForm formSelective = new BpmForm();
		formSelective.setProUid(proUid);
		formSelective.setProVersion(proVerUid);
	    return bpmFormManageMapper.listBySelective(formSelective);
	}

	@Override
	public BpmForm getByFormUid(String formUid) {
		if (StringUtils.isBlank(formUid)) {
		    return null;
		}
		return bpmFormManageMapper.queryFormByFormUid(formUid);
	}

	@Override
	public ServerResponse saveFormContent(FormJSONObject formJsonObj) {
		BpmForm bpmForm = formJsonObj.getForm();
		String formUid = EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString();
		//新增表单信息
		bpmForm.setDynUid(formUid);
		//获得当前的用户
		String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		bpmForm.setCreator(currUser);
		int insertFormRow = bpmFormManageMapper.saveForm(bpmForm);
		if(insertFormRow!=1) {
			throw new PlatformException("新增表单失败");
		}
		//新增表单字段信息
		BpmFormField[] formFieldArr = formJsonObj.getFormFieldArr();
		List<BpmFormField> fieldList = new ArrayList<>();
		for(BpmFormField field:formFieldArr) {
			field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD+UUID.randomUUID().toString());
			field.setFormUid(formUid);
			fieldList.add(field);
		}
		int insertFieldRow = bpmFormFieldMapper.insertBatch(fieldList);
		if(insertFieldRow!=formFieldArr.length) {
			throw new PlatformException("新增表单字段失败");
		}
		//新增表单关联子表单信息
		String[] publicFormUidArr = formJsonObj.getPublicFormUidArr();
		List<BpmFormRelePublicForm> bpmFormRelePublicFormList = new ArrayList<>();
		int blankCount = 0;
		for(String publicFormUid:publicFormUidArr) {
			if(publicFormUid==null || "".equals(publicFormUid)) {
				blankCount++;
				continue;
			}
			BpmFormRelePublicForm bpmFormRelePublicForm = new BpmFormRelePublicForm();
			bpmFormRelePublicForm.setFormUid(formUid);
			bpmFormRelePublicForm.setPublicFormUid(publicFormUid);
			bpmFormRelePublicFormList.add(bpmFormRelePublicForm);
		}
		int insertReleRow = bpmFormRelePublicFormMapper.insertBatch(bpmFormRelePublicFormList);
		if(insertReleRow!=publicFormUidArr.length-blankCount) {
			throw new PlatformException("新增表单关联子表单信息失败");
		}
		return ServerResponse.createBySuccess();
	}
}
