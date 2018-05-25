package com.desmart.desmartbpm.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmPublicForm;
import com.desmart.desmartbpm.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmPublicFormService;
import com.desmart.desmartbpm.util.SFTPUtil;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

/**
 * 公共表单管理控制器
 * @author loser_wu
 * @since 2018年5月24日
 */
@Controller
@RequestMapping(value = "/publicForm")
public class BpmPublicFormController {
	private static final Logger LOG = LoggerFactory.getLogger(BpmFormManageController.class);
	@Autowired
	private BpmPublicFormService bpmPublicFormService;
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		return new ModelAndView("desmartbpm/publicForm");
	}
	
	@RequestMapping(value = "/designForm")
	public ModelAndView designForm(String formUid,String formName,String formDescription) {
		ModelAndView modelAndView = new ModelAndView("desmartbpm/common/publicFormDesign");
		modelAndView.addObject("formUid", formUid);
		modelAndView.addObject("formName", formName);
		modelAndView.addObject("formDescription", formDescription);
		return modelAndView;
	}
	
	/**
	 * 根据表单名分页查询公共表单
	 */
	@RequestMapping(value = "/listFormByFormName")
	@ResponseBody
	public ServerResponse listFormByFormName(String formName,
			@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize) {
		return bpmPublicFormService.listFormByFormName(formName,pageNum,pageSize);
	}
	
	/**
	 * 根据表单名精确查询表单
	 */
	@RequestMapping(value = "/queryFormByFormName")
	@ResponseBody
	public ServerResponse queryFormByFormName(String formName) {
		try {
			return bpmPublicFormService.queryFormByFormName(formName);
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 根据表单ID精确查询表单
	 */
	@RequestMapping(value = "/queryFormByFormUid")
	@ResponseBody
	public ServerResponse queryFormByFormUid(String formUid) {
		return bpmPublicFormService.queryFormByFormUid(formUid);
	}
	
	/**
	 * 根据表单id查询表单是否已被主表单引用
	 */
	@RequestMapping(value = "/isBindMainForm")
	@ResponseBody
	public ServerResponse isBindMainForm(@Param("formUids") String[] formUids) {
		try {
			return bpmPublicFormService.isBindMainForm(formUids);
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 保存表单文件
	 */
	@RequestMapping(value = "/saveFormFile")
	@ResponseBody
	public String saveFormFile(String webpage,String filename) {
		InputStream input = new ByteArrayInputStream(webpage.getBytes());
        try {
        	input.close();
        	SFTPUtil sftp = new SFTPUtil();
        	sftp.upload(bpmGlobalConfigService.getFirstActConfig(), "/form/public", filename, input);
		} catch (Exception e) {
			LOG.error("保存表单文件失败", e);
		} 
        return filename;
	}
	
	/**
	 * 保存公共表单信息
	 */
	@RequestMapping(value = "/saveForm")
	@ResponseBody
	public ServerResponse saveForm(@RequestBody BpmPublicForm bpmPublicForm) {
		try {
			return bpmPublicFormService.saveForm(bpmPublicForm);
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 修改公共的表单内容
	 */
	@RequestMapping(value = "/upadteFormContent")
	@ResponseBody
	public ServerResponse upadteFormContent(@RequestBody BpmPublicForm bpmPublicForm) {
		try {
			return bpmPublicFormService.upadteFormContent(bpmPublicForm);
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 修改公共的表单属性
	 */
	@RequestMapping(value = "/updateFormInfo")
	@ResponseBody
	public ServerResponse updateFormInfo(BpmPublicForm bpmPublicForm) {
		try {
			return bpmPublicFormService.updateFormInfo(bpmPublicForm);
		}catch(PlatformException e) {
			return ServerResponse.createByErrorMessage(e.getMessage());
		}catch(Exception e) {
			return ServerResponse.createByErrorMessage("表单属性修改失败");
		}
	}
	
	/**
	 * 删除公共表单
	 */
	@RequestMapping(value = "/deleteForm")
	@ResponseBody
	public ServerResponse deleteForm(@Param("formUids") String[] formUids) {
		try {
			return bpmPublicFormService.deleteForm(formUids);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 复制表单
	 */
	@RequestMapping(value = "/copyForm")
	@ResponseBody
	public ServerResponse copyForm(BpmPublicForm bpmPubilcForm) {
		try {
			return bpmPublicFormService.copyForm(bpmPubilcForm);
		}catch(Exception e) {
			return ServerResponse.createByError();
		}
	}
}
