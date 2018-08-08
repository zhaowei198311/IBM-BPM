package com.desmart.desmartbpm.controller;

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
import com.desmart.common.exception.PlatformException;
import com.desmart.desmartbpm.service.BpmPublicFormService;
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
	
	/**
	 * 转到公共表单的管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		return new ModelAndView("desmartbpm/publicForm");
	}
	
	/**
	 * 转到表单设计器
	 */
	@RequestMapping(value = "/designForm")
	public ModelAndView designForm(String formUid,String formName,String formDescription,
			String dynHtml,String formCode) {
		ModelAndView modelAndView = new ModelAndView("desmartbpm/common/publicFormDesign");
		modelAndView.addObject("formUid", formUid);
		modelAndView.addObject("formName", formName);
		modelAndView.addObject("formDescription", formDescription);
		modelAndView.addObject("dynHtml",dynHtml);
		modelAndView.addObject("formCode",formCode);
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
	@RequestMapping(value = "/queryFormByFormNameAndCode")
	@ResponseBody
	public ServerResponse queryFormByFormNameAndCode(String formName,String formCode) {
		try {
			return bpmPublicFormService.queryFormByFormNameAndCode(formName,formCode);
		}catch(Exception e) {
			LOG.error("查询表单异常",e);
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
	 * 根据表单id查询表单是否已被关联了步骤的主表单引用
	 */
	@RequestMapping(value = "/isBindMainForm")
	@ResponseBody
	public ServerResponse isBindMainForm(@Param("formUids") String[] formUids) {
		try {
			return bpmPublicFormService.isBindMainForm(formUids);
		}catch(Exception e) {
			LOG.error("查询公共表单是否被主表单绑定", e);
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
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
			LOG.error("保存子表单异常", e);
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
			LOG.error("修改子表单异常", e);
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
			LOG.error("修改子表单异常", e);
			return ServerResponse.createByErrorMessage(e.getMessage());
		}catch(Exception e) {
			LOG.error("修改子表单异常", e);
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
			LOG.error("删除子表单异常", e);
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
	
	/**
	 * 进入选择关联子表单的页面
	 */
	@RequestMapping(value = "/selectPublicForm")
	@ResponseBody
	public ModelAndView toChoosePublicForm(String elementId,String formUid) {
		ModelAndView mv = new ModelAndView("desmartbpm/choosePublicForm");
		mv.addObject("elementId",elementId);
		mv.addObject("formUid",formUid);
		return mv;
	}
	
	/**
	 * 添加主表单与公共表单之间的关联信息
	 */
	@RequestMapping(value = "/saveFormRelePublicForm")
	@ResponseBody
	public ServerResponse saveFormRelePublicForm(String formUid,String[] publicFormUidArr) {
		try {
			return bpmPublicFormService.saveFormRelePublicForm(formUid,publicFormUidArr);
		}catch(Exception e) {
			LOG.error("增加子表单与主表单关联异常", e);
			return ServerResponse.createByError();
		}
	}
	
	/**
	 * 根据表单id和公共表单id查询是否有相同的关联信息
	 */
	@RequestMapping(value = "/queryReleByFormUidAndPublicFormUid")
	@ResponseBody
	public ServerResponse queryReleByFormUidAndPublicFormUid(String formUid,String publicFormUid) {
		try {
			return bpmPublicFormService.queryReleByFormUidAndPublicFormUid(formUid,publicFormUid);
		}catch(Exception e) {
			LOG.error("查询子表单与主表单关联异常", e);
			return ServerResponse.createByError();
		}
	}
}
