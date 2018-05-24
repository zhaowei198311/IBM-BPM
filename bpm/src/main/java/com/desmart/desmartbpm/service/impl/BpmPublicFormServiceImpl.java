package com.desmart.desmartbpm.service.impl;

import java.io.IOException;
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
import com.desmart.desmartbpm.dao.BpmPublicFormMapper;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmPublicForm;
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
	private BpmFormFieldMapper bpmFormFieldMapper;
	@Autowired
	private BpmGlobalConfigService bpmGlobalCofigService;
	
	private SFTPUtil sftp = new SFTPUtil();

	@Override
	public ServerResponse listFormByFormName(String formName, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize,"create_time desc");
		List<BpmPublicForm> formList = bpmPublicFormMapper.listFormByFormName(formName);
		PageInfo<List<BpmPublicForm>> pageInfo = new PageInfo(formList);
		return ServerResponse.createBySuccess(pageInfo);
	}

	@Override
	public ServerResponse queryFormByFormName(String formName) {
		BpmPublicForm bpmPublicForm = bpmPublicFormMapper.queryFormByFormName(formName);
		if(null!=bpmPublicForm) {
			throw new PlatformException("已存在表单名为"+formName+"的表单");
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse isBindMainForm(String[] formUids) {
		for(String formUid:formUids) {
			List<String> mainFormUidList = bpmPublicFormMapper.isBindMainForm(formUid);
			if(!mainFormUidList.isEmpty()) {
				throw new PlatformException("该子表单已被主表单绑定");
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
		//修改表单文件名
		if(!updateFormFilename(bpmPublicForm)) {
			throw new PlatformException("表单名修改异常");
		}
		int updateRow = bpmPublicFormMapper.updateFormInfo(bpmPublicForm);
		if(1!=updateRow) {
			throw new PlatformException("修改表单属性失败");
		}
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 修改表单文件名
	 */
	private boolean updateFormFilename(BpmPublicForm bpmPublicForm) throws IOException {
		String filename = bpmPublicFormMapper.queryFormByFormUid(bpmPublicForm.getPublicFormUid()).getPublicFormFilename();
		String updateFilename = bpmPublicForm.getPublicFormName()+".html";
		BpmGlobalConfig gcfg = bpmGlobalCofigService.getFirstActConfig();
		boolean flag = sftp.renameFile(gcfg,"/form/public",filename, updateFilename);
		int updateRow = bpmPublicFormMapper.updateFormFilenameByFormUid(bpmPublicForm.getPublicFormUid(),updateFilename);
		if(1!=updateRow) {
			throw new PlatformException("修改表单文件名失败");
		}
		return flag;
	}
}
