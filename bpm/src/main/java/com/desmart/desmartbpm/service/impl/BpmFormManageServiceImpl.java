package com.desmart.desmartbpm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.dao.BpmFormFieldMapper;
import com.desmart.desmartbpm.dao.BpmFormManageMapper;
import com.desmart.desmartbpm.dao.DhProcessCategoryMapper;
import com.desmart.desmartbpm.dao.DhProcessDefinitionMapper;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.util.SFTPUtil;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
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
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	@Override
	public ServerResponse queryProFormByName(String dynTitle,String proUid,String proVersion) {
		BpmForm bpmForm = bpmFormManageMapper.queryProFormByName(dynTitle,proUid,proVersion);
		if(null==bpmForm) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

	@Override
	public ServerResponse queryFormByFormUid(String formUid) {
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
			//获得的数据
			List<BpmForm> formList = bpmFormManageMapper.listFormByProDefinition(formTitle,proUid,proVerUid);
			for(BpmForm bpmForm:formList) {
				if(null!=dhProcessDefinitionService.getLswSnapshotBySnapshotId(bpmForm.getProVersion())) {
					String proVerName = dhProcessDefinitionService
	        				.getLswSnapshotBySnapshotId(bpmForm.getProVersion()).getName();
					bpmForm.setProVerName(proVerName);
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
			for(BpmForm bpmForm:formList) {
				if(null!=dhProcessDefinitionService.getLswSnapshotBySnapshotId(bpmForm.getProVersion())) {
					String proVerName = dhProcessDefinitionService
	        				.getLswSnapshotBySnapshotId(bpmForm.getProVersion()).getName();
					bpmForm.setProVerName(proVerName);
				}
			}
			PageInfo<List<BpmForm>> pageInfo = new PageInfo(formList);
			return ServerResponse.createBySuccess(pageInfo);
		}
	}

	@Override
	public List<DhProcessDefinition> listDefinitionAll() {
		return dhProcessDefinitionMapper.listAll();
	}

	@Override
	public ServerResponse saveForm(BpmForm bpmForm) {
		bpmForm.setDynUid(EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString());
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmForm.setCreator(currUser);
        int countRow = bpmFormManageMapper.saveForm(bpmForm);
        if (countRow > 0) {
            return ServerResponse.createBySuccess(bpmForm.getDynUid());
        } else {
            return ServerResponse.createByErrorMessage("添加失败");
        }
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
		for(String formUid:formUids) {
			BpmForm bpmForm = bpmFormManageMapper.queryFormByFormUid(formUid);
			if(null==bpmForm) {
				throw new PlatformException("找不到指定的表单数据");
			}
			int countRow = bpmFormManageMapper.deleteForm(formUid);
			if(1!=countRow) {
				throw new PlatformException("删除表单信息失败");
			}
			bpmFormManageMapper.deleteFormRelePublicForm(bpmForm.getDynUid());
			List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
			deleteFieldPermiss(filedList);
			int fieldCountRow = bpmFormFieldMapper.deleteFormField(formUid);
			if(fieldCountRow!=filedList.size()) {
				throw new PlatformException("删除表单字段失败");
			}
		}
		return ServerResponse.createBySuccess();
	}

	private void deleteFieldPermiss(List<BpmFormField> fieldList) {
		for(BpmFormField field:fieldList) {
			bpmFormFieldMapper.deleteFieldPermissById(field.getFldUid());
		}
	}
	
	@Override
	public ServerResponse copyForm(BpmForm bpmForm) {
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
		List<String> publicFormUidList = bpmFormManageMapper.queryFormReleByFormUid(dynUid);
		for(String publicFormUid:publicFormUidList) {
			int countRow = bpmFormManageMapper.saveFormRelePublicForm(newFormUid, publicFormUid);
			if(1!=countRow) {
				throw new PlatformException("复制子表单关联失败");
			}
		}
	}

	/**
	 * 复制表单字段信息
	 */
	private void copyFormFieldInfo(String newFormUid,String oldFormUid) {
		List<BpmFormField> oldFields = bpmFormFieldMapper.queryFormFieldByFormUid(oldFormUid);
		int fieldSize = oldFields.size();
		if(fieldSize!=0) {
			List<BpmFormField> newFields = new ArrayList<>();
			for(BpmFormField field:oldFields) {
				field.setFldUid(EntityIdPrefix.BPM_FORM_FIELD+UUID.randomUUID().toString());
				field.setFormUid(newFormUid);
				newFields.add(field);
			}
			int countRow = bpmFormFieldMapper.saveFormField(newFields);
			if(countRow!=fieldSize) {
				throw new PlatformException("表单字段复制异常");
			}
		}
	}

	/**
	 * 复制表单信息
	 */
	private String copyFormInfo(BpmForm bpmForm,BpmForm oldBpmForm) {
		String newFormUid = EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString();
		bpmForm.setDynUid(newFormUid);//重新生成唯一主键
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmForm.setCreator(currUser);
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
		List<BpmForm> bpmFormList = bpmFormManageMapper.listBySelective(bpmForm);
		return ServerResponse.createBySuccess(bpmFormList);
	}

	@Override
	public ServerResponse updateFormContent(BpmForm bpmForm) {
		int countRow = bpmFormManageMapper.updateFormContent(bpmForm);
		if(countRow!=1) {
			throw new PlatformException("表单内容修改失败");
		}
		List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(bpmForm.getDynUid());
		deleteFieldPermiss(filedList);
		bpmFormFieldMapper.deleteFormField(bpmForm.getDynUid());
		bpmFormManageMapper.deleteFormRelePublicForm(bpmForm.getDynUid());
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse isBindStep(String[] formUids) {
		for(String formUid:formUids) {
			List<DhStep> stepList = bpmFormManageMapper.isBindStep(formUid);
			if(!stepList.isEmpty()) {
				throw new PlatformException("表单已经被步骤绑定");
			}
		}
		return ServerResponse.createBySuccess();
	}
}
