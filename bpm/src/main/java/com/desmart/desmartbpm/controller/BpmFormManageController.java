package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.FormJSONObject;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.ZTreeNode;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;

/**
 * 表单管理控制器
 * @author loser_wu
 * @sina 2018/4/12
 */
@Controller
@RequestMapping(value = "/formManage")
public class BpmFormManageController {
	private static final Logger LOG = LoggerFactory.getLogger(BpmFormManageController.class);
	@Autowired
	private BpmFormManageService bpmFormManageService;
	
	@Autowired
	private DhProcessCategoryService dhProcessCategoryService;
	
	@Autowired
	private DhProcessMetaService dhProcessMetaService; 
	
	/**
	 * 进入流程表单管理页面
	 */
	@RequestMapping(value = "/index")
	public ModelAndView toIndex(String proUid,String proVersion) {
		ModelAndView mv = new ModelAndView("desmartbpm/formManagement");
		mv.addObject("proUid", proUid);
		mv.addObject("proVersion", proVersion);
        return mv;
	}
	
	/**
	 * 表单预览
	 */
	@RequestMapping(value = "/preIndex")
	public ModelAndView preIndex(String proUid,String proVersion,String formUid,
			String formName,String formDescription,String dynHtml,String webpage,String formNoExpression,String formNoStatic) {
		ModelAndView mv = new ModelAndView("desmartbpm/common/preDesignIndex");
		mv.addObject("formName", formName);
		mv.addObject("formDescription", formDescription);
		mv.addObject("proUid", proUid);
		mv.addObject("formUid", formUid);
		mv.addObject("proVersion", proVersion);
		mv.addObject("webpage", webpage);
		mv.addObject("dynHtml", dynHtml);
		mv.addObject("formNoExpression",formNoExpression);
		mv.addObject("formNoStatic",formNoStatic);
        return mv;
	}
	
	/**
	 * 跳转到表单设计器
	 */
	@RequestMapping(value = "/designForm")
	public ModelAndView designForm(String formName,String formDescription,String formUid,
			String proUid,String proVersion,String dynHtml,String formNoExpression,String formNoStatic) {
		ModelAndView mv = new ModelAndView("desmartbpm/common/formDesign");
		mv.addObject("formName", formName);
		mv.addObject("formDescription", formDescription);
		mv.addObject("proUid", proUid);
		mv.addObject("formUid", formUid);
		mv.addObject("proVersion", proVersion);
		mv.addObject("dynHtml", dynHtml);
		mv.addObject("formNoExpression",formNoExpression);
		mv.addObject("formNoStatic",formNoStatic);
		return mv;
	}
	
	/**
	 * 获得页面流程树数据
	 */
	@RequestMapping(value = "/getTreeData")
	@ResponseBody
	public String getTreeData() {
		//获得所有的流程分类集合
		List<DhProcessCategory> categoryList = dhProcessCategoryService.listAll();
        DhProcessCategory dhProcessCategory = new DhProcessCategory();
        dhProcessCategory.setCategoryUid("rootCategory");
        dhProcessCategory.setCategoryName("流程分类");
        dhProcessCategory.setCategoryParent("0");
        categoryList.add(dhProcessCategory);
        //获得所有的流程元集合
        List<DhProcessMeta> metaList = dhProcessMetaService.listAll();
        //获得所有的流程定义对象的集合
        List<DhProcessDefinition> definitionList = bpmFormManageService.listDefinitionAll();
        List<ZTreeNode> nodesToShow = new ArrayList<ZTreeNode>();
        //流程分类在前端显示的对象
        for (DhProcessCategory category : categoryList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(category.getCategoryUid());
            node.setName(category.getCategoryName());
            node.setPid(category.getCategoryParent());
            node.setItemType("category");
            node.setIcon("../resources/desmartbpm/images/1.png");
            nodesToShow.add(node);
        }
        Set<String> metaProUidSet = new HashSet<>();
        //流程元在前端显示的对象
        for (DhProcessMeta meta : metaList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(meta.getProUid());
            metaProUidSet.add(meta.getProUid());
            node.setName(meta.getProName());
            node.setPid(meta.getCategoryUid());
            node.setItemType("processMeta");
            node.setIcon("../resources/desmartbpm/images/2.png");
            nodesToShow.add(node);
        }
        //流程定义在前端显示的对象
        for(DhProcessDefinition definition : definitionList) {
        	ZTreeNode node = new ZTreeNode();
        	//判断proUid是否有对应流程元
        	if(metaProUidSet.contains(definition.getProUid())) {
        		node.setId(definition.getProVerUid());
        		//获得某个流程定义的版本名
            	node.setName(definition.getVerName());//页面上显示流程下某个版本
                node.setPid(definition.getProUid());
                node.setItemType("processDefinition");
                node.setIcon("../resources/desmartbpm/images/3.png");
                nodesToShow.add(node);
        	}
        }
        return JSON.toJSONString(nodesToShow);
	}
	
	/**
	 * 获得某版本的流程定义下的表单
	 */
	@RequestMapping(value = "/listFormByProDefinition")
	@ResponseBody
	public ServerResponse listFormByProDefinition(String formTitle,String proCategoryUid,
			String proUid,String proVerUid,
			@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize) {
		return bpmFormManageService.listFormByProDefinition(formTitle,proUid,proCategoryUid,proVerUid,pageNum,pageSize);
	}
	
	/**
	 * 根据表单名查询表单是否存在
	 */
	@RequestMapping(value = "/queryProFormByName")
	@ResponseBody
	public ServerResponse queryProFormByName(String dynTitle,String proUid,String proVersion) {
		return bpmFormManageService.queryProFormByName(dynTitle,proUid,proVersion);
	}
	
	/**
	 * 根据表单Id查询到表单数据
	 */
	@RequestMapping(value = "/queryFormByFormUid")
	@ResponseBody
	public ServerResponse queryFormByFormUid(String formUid) {
		return bpmFormManageService.queryFormByFormUid(formUid);
	}
	
	/**
	 * 保存表单数据
	 */
	@RequestMapping(value = "/saveFormContent")
	@ResponseBody
	public ServerResponse saveFormContent(@RequestBody FormJSONObject formJsonObj) {
		try {
			return bpmFormManageService.saveFormContent(formJsonObj);
		}catch(Exception e) {
			e.printStackTrace();
			LOG.error("保存表单数据异常",e);
			return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 修改表单基本信息
	 */
	@RequestMapping(value = "/updateFormInfo")
	@ResponseBody
	public ServerResponse updateFormInfo(BpmForm bpmForm) {
		try {
			return bpmFormManageService.updateFormInfo(bpmForm);
		} catch (Exception e) {
			LOG.error("修改表单信息失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 修改表单内容
	 */
	@RequestMapping(value = "/upadteFormContent")
	@ResponseBody
	public ServerResponse upadteFormContent(@RequestBody FormJSONObject formJsonObj) {
		try {
			return bpmFormManageService.updateFormContent(formJsonObj);
		} catch (Exception e) {
			LOG.error("修改表单内容失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * 删除表单文件以及表单信息
	 */
	@RequestMapping(value = "/deleteForm")
	@ResponseBody
	public ServerResponse deleteForm(String[] formUids) {
		try {
			return bpmFormManageService.deleteForm(formUids);
        } catch (Exception e) {
            LOG.error("删除表单数据失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
	}
	
	/**
	 * 表单复制
	 */
	@RequestMapping(value = "/copyForm")
	@ResponseBody
	public ServerResponse copyForm(BpmForm bpmForm) {
		try {
			return bpmFormManageService.copyForm(bpmForm);
        } catch (Exception e) {
            LOG.error("复制表单失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
	}
	
	/**
	 * 根据传入的组合条件查询表单集合
	 */
	@RequestMapping(value = "/queryFormListBySelective")
	@ResponseBody
	public ServerResponse queryFormListBySelective(BpmForm bpmForm) {
		return bpmFormManageService.listBySelective(bpmForm);
	}
	
	/**
	 * 根据表单判断表单是否被步骤绑定
	 */
	@RequestMapping(value = "/isBindStep")
	@ResponseBody
	public ServerResponse isBindStep(@RequestParam("formUids")String[] formUids){
		try {
			return bpmFormManageService.isBindStep(formUids);
		}catch(Exception e) {
			LOG.error("表单是否被步骤绑定",e);
			return ServerResponse.createByError();
		}
	}
}
