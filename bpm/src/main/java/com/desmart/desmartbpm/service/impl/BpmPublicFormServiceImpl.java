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
import com.desmart.desmartbpm.dao.BpmPublicFormMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.BpmPublicForm;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmPublicFormService;
import com.desmart.desmartbpm.util.SFTPUtil;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 公共表单业务逻辑层的实现类
 * @author loser_wu
 * @since 2018年5月24日
 */
@Service
@Transactional
public class BpmPublicFormServiceImpl implements BpmPublicFormService{
	@Autowired 
	private BpmPublicFormMapper bpmPublicFormMapper;
	@Autowired
	private BpmFormManageMapper bpmFormManageMapper;
	@Autowired
	private BpmFormFieldMapper bpmFormFieldMapper;
	
	@Override
	public ServerResponse listFormByFormName(String formName, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize,"create_time desc");
		List<BpmPublicForm> formList = bpmPublicFormMapper.listFormByFormName(formName);
		PageInfo<List<BpmPublicForm>> pageInfo = new PageInfo(formList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse queryFormByFormNameAndCode(String formName,String formCode) {
		BpmPublicForm bpmPublicForm = bpmPublicFormMapper.queryFormByFormName(formName);
		if(null!=bpmPublicForm) {
			throw new PlatformException("已存在表单名为"+formName+"的表单");
		}
		BpmPublicForm publicForm = bpmPublicFormMapper.queryFormByFormCode(formCode);
		if(null!=publicForm) {
			throw new PlatformException("已存在表单编码为"+formCode+"的表单");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse isBindMainForm(String[] formUids) {
		for(String formUid:formUids) {
			List<String> mainFormUidList = bpmPublicFormMapper.isBindMainForm(formUid);
			if(!mainFormUidList.isEmpty()) {
				for(String mainFormUid:mainFormUidList) {
					List<DhStep> stepList = bpmFormManageMapper.isBindStep(mainFormUid);
					if(!stepList.isEmpty()) {
						throw new PlatformException("该子表单已被主表单绑定");
					}
				}
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse saveForm(BpmPublicForm bpmPublicForm) {
		String publicFormUid = EntityIdPrefix.BPM_PUBLIC_FORM + UUID.randomUUID().toString();
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		bpmPublicForm.setPublicFormUid(publicFormUid);
		bpmPublicForm.setCreator(creator);
		int insertRow = bpmPublicFormMapper.saveForm(bpmPublicForm);
		if(1!=insertRow) {
			throw new PlatformException("保存表单失败");
		}
		return ServerResponse.createBySuccess(publicFormUid);
	}

	@Override
	public ServerResponse upadteFormContent(BpmPublicForm bpmPublicForm) {
		int countRow = bpmPublicFormMapper.updateFormContent(bpmPublicForm);
		if(countRow!=1) {
			throw new PlatformException("表单内容修改失败");
		}
		List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(bpmPublicForm.getPublicFormUid());
		deleteFieldPermiss(filedList);
		bpmFormFieldMapper.deleteFormField(bpmPublicForm.getPublicFormUid());
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse queryFormByFormUid(String formUid) {
		BpmPublicForm bpmPublicForm = bpmPublicFormMapper.queryFormByFormUid(formUid);
		if(null==bpmPublicForm) {
			return ServerResponse.createByError();
		}else {
			return ServerResponse.createBySuccess(bpmPublicForm);
		}
	}

	@Override
	public ServerResponse updateFormInfo(BpmPublicForm bpmPublicForm) throws Exception {
		int updateRow = bpmPublicFormMapper.updateFormInfo(bpmPublicForm);
		if(1!=updateRow) {
			throw new PlatformException("修改表单属性失败");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse deleteForm(String[] formUids) {
		for(String formUid:formUids) {
			BpmPublicForm bpmPublicForm = bpmPublicFormMapper.queryFormByFormUid(formUid);
			if(null==bpmPublicForm) {
				throw new PlatformException("找不到指定的表单数据");
			}
			int countRow = bpmPublicFormMapper.deleteForm(formUid);
			if(1!=countRow) {
				throw new PlatformException("删除表单信息失败");
			}
			List<BpmFormField> filedList = bpmFormFieldMapper.queryFormFieldByFormUid(formUid);
			deleteFieldPermiss(filedList);
			int fieldCountRow = bpmFormFieldMapper.deleteFormField(formUid);
			if(fieldCountRow!=filedList.size()) {
				throw new PlatformException("删除表单字段失败");
			}
		}
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 删除表单字段的权限信息
	 */
	private void deleteFieldPermiss(List<BpmFormField> fieldList) {
		for(BpmFormField field:fieldList) {
			bpmFormFieldMapper.deleteFieldPermissById(field.getFldUid());
		}
	}

	@Override
	public ServerResponse copyForm(BpmPublicForm bpmPubilcForm) {
		String newFilename = bpmPubilcForm.getPublicFormName()+".html";
		BpmPublicForm oldBpmPublicForm = bpmPublicFormMapper.queryFormByFormUid(bpmPubilcForm.getPublicFormUid());
		if(null==oldBpmPublicForm) {
			throw new PlatformException("找不到指定的表单数据");
		}
		//复制表单信息
		String newFormUid = copyFormInfo(bpmPubilcForm,oldBpmPublicForm);
		//复制表单字段信息
		copyFormFieldInfo(newFormUid,oldBpmPublicForm.getPublicFormUid());
		return ServerResponse.createBySuccess(newFormUid);
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
	private String copyFormInfo(BpmPublicForm bpmPublicForm,BpmPublicForm oldBpmPublicForm) {
		String newFormUid = EntityIdPrefix.BPM_PUBLIC_FORM + UUID.randomUUID().toString();
		bpmPublicForm.setPublicFormUid(newFormUid);//重新生成唯一主键
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmPublicForm.setCreator(currUser);
        bpmPublicForm.setPublicFormContent(oldBpmPublicForm.getPublicFormContent());
        bpmPublicForm.setPublicFormWebpage(oldBpmPublicForm.getPublicFormWebpage());
        int countRow = bpmPublicFormMapper.saveForm(bpmPublicForm);
        if(countRow!=1) {
        	throw new PlatformException("表单数据复制异常");
        }
        return newFormUid;
	}

	@Override
	public ServerResponse saveFormRelePublicForm(String formUid, String[] publicFormUidArr) {
		for(String publicFormUid:publicFormUidArr) {
			if(!"".equals(publicFormUid) && null!=publicFormUid) {
				int countRow = bpmFormManageMapper.saveFormRelePublicForm(formUid,publicFormUid);
				if(1!=countRow) {
					throw new PlatformException("添加子表单关联失败");
				}
			}
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse queryReleByFormUidAndPublicFormUid(String formUid, String publicFormUid) {
		int countRow = bpmPublicFormMapper.queryReleByFormUidAndPublicFormUid(formUid,publicFormUid);
		if(countRow>=1) {
			throw new PlatformException("添加子表单关联失败");
		}
		return ServerResponse.createBySuccess();
	}
}
