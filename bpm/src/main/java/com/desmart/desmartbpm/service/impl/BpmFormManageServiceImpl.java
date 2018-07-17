package com.desmart.desmartbpm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.desmart.desmartbpm.dao.DhTriggerInterfaceMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

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
	
	@Autowired
	private DhTriggerInterfaceMapper dhTriggerInterfaceMapper;
	
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
			for(BpmForm bpmForm:formList) {
				//查询该表单绑定的流程定义是否被废弃
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
				//查询该表单绑定的流程定义是否被废弃
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
		//查询所有的流程定义集合
		return dhProcessDefinitionMapper.listAll();
	}

	@Override
	public ServerResponse saveForm(BpmForm bpmForm) {
		bpmForm.setDynUid(EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString());
		//获得当前的用户
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
			//删除表单关联子表单的信息
			bpmFormManageMapper.deleteFormRelePublicForm(bpmForm.getDynUid());
			//获得表单所有的字段
			List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
			//批量删除字段权限
			deleteFieldPermiss(filedList);
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

	private void deleteFieldPermiss(List<BpmFormField> fieldList) {
		//删除字段权限信息
		for(BpmFormField field:fieldList) {
			bpmFormFieldMapper.deleteFieldPermissById(field.getFldUid());
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
		List<String> publicFormUidList = bpmFormManageMapper.queryFormReleByFormUid(dynUid);
		for(String publicFormUid:publicFormUidList) {
			//将子表单id关联新的表单
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
        bpmForm.setFormNoExpression(oldBpmForm.getFormNoExpression());
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
	public ServerResponse updateFormContent(BpmForm bpmForm) {
		//修改表单内容
		int countRow = bpmFormManageMapper.updateFormContent(bpmForm);
		if(countRow!=1) {
			throw new PlatformException("表单内容修改失败");
		}
		//删除旧的字段权限信息，子表单关联信息以及字段信息
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
}
