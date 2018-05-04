package com.desmart.desmartbpm.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.common.EntityIdPrefix;
import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.controller.BpmFormManageController;
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
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
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
	
	@Override
	public ServerResponse queryFormByName(String dynTitle) {
		BpmForm bpmForm = bpmFormManageMapper.queryFormByName(dynTitle);
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
	public ServerResponse updateFormInfo(BpmForm bpmForm) throws IOException {
		//修改表单文件名
		updateFormFilename(bpmForm);
		//修改表单基本信息
		int countRow = bpmFormManageMapper.updateFormInfo(bpmForm);
		if(countRow!=1) {
			throw new PlatformException("表单修改异常");
		}
		return ServerResponse.createBySuccess();
	}
	
	/**
	 * 修改表单文件名
	 */
	private void updateFormFilename(BpmForm bpmForm) throws IOException {
		String filename = bpmFormManageMapper.queryFormByFormUid(bpmForm.getDynUid()).getDynFilename();
		File file = new File(filename);
		if (!file.exists()) {
			throw new PlatformException("表单文件不存在");
		}
		String path = filename.replaceAll(file.getName(),"");//获得文件路径
		
		String updateFilename = path+bpmForm.getDynTitle()+".html";
		File updateFile = new File(updateFilename);
		if (!updateFile.exists()) {
			updateFile.createNewFile();
	    }
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(updateFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
			
		byte[] b = new byte[20];// 每次运20个，可按照实际文件大小调整
		int len;
		while ((len = bis.read(b)) != -1) {
			bos.write(b, 0, len);
		}
		bpmFormManageMapper.updateFormFilenameByFormUid(bpmForm.getDynUid(),updateFilename);
		
		if (bos != null) {
			bos.close();
			if (bis != null) {
				bis.close();
			}
		}
		//删除原表单
		file.delete();
	}

	@Override
	public ServerResponse deleteForm(String[] formUids,String path) {
		//to do 这里需要判断表单是否可删除--判断条件：所属的流程定义是否已经发布
		for(String formUid:formUids) {
			BpmForm bpmForm = bpmFormManageMapper.queryFormByFormUid(formUid);
			if(null==bpmForm) {
				throw new PlatformException("找不到指定的表单数据");
			}
			int countRow = bpmFormManageMapper.deleteForm(formUid);
			int fieldCountRow = bpmFormFieldMapper.deleteFormField(formUid);
			File file = new File(path,bpmForm.getDynTitle()+".html");
			file.delete();
		}
		return ServerResponse.createBySuccess();
	}

	@Override
	public ServerResponse copyForm(BpmForm bpmForm, String path) throws IOException {
		String newFilename = path+bpmForm.getDynTitle()+".html";
		BpmForm oldBpmForm = bpmFormManageMapper.queryFormByFormUid(bpmForm.getDynUid());
		if(null==oldBpmForm) {
			throw new PlatformException("找不到指定的表单数据");
		}
		String oldFilename = oldBpmForm.getDynFilename();
		//复制表单信息
		String newFormUid = copyFormInfo(bpmForm,oldBpmForm,newFilename);
		//复制表单字段信息
		copyFormFieldInfo(newFormUid);
		//复制表单文件
		copyFormFile(newFilename,oldFilename);
		return ServerResponse.createBySuccess();
	}

	/**
	 * 复制表单文件
	 */
	private void copyFormFile(String newFilename,String oldFilename) throws IOException {
		File oldFile = new File(oldFilename);
		if (!oldFile.exists()) {
			throw new PlatformException("表单文件不存在");
		}
		
		File newFile = new File(newFilename);
		if (!newFile.exists()) {
			newFile.createNewFile();
	    }
		FileInputStream fis = new FileInputStream(oldFile);
		FileOutputStream fos = new FileOutputStream(newFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
			
		byte[] b = new byte[20];// 每次运20个，可按照实际文件大小调整
		int len;
		while ((len = bis.read(b)) != -1) {
			bos.write(b, 0, len);
		}
		if (bos != null) {
			bos.close();
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * 复制表单字段信息
	 */
	private void copyFormFieldInfo(String newFormUid) {
		List<BpmFormField> oldFields = bpmFormFieldMapper.queryFormFieldByFormUid(newFormUid);
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
	 * @return 
	 */
	private String copyFormInfo(BpmForm bpmForm,BpmForm oldBpmForm,String newFilename) {
		String newFormUid = EntityIdPrefix.BPM_FORM + UUID.randomUUID().toString();
		bpmForm.setDynUid(newFormUid);//重新生成唯一主键
        String currUser = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
        bpmForm.setCreator(currUser);
        bpmForm.setDynContent(oldBpmForm.getDynContent());
        bpmForm.setDynFilename(newFilename);//表单类型固定为html
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
		bpmFormFieldMapper.deleteFormField(bpmForm.getDynUid());
		return ServerResponse.createBySuccess();
	}
}
