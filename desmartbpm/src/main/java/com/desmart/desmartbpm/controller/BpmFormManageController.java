package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;

import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.ZTreeNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartbpm.util.JsonUtil;

/**
 * 表单管理控制器
 * @author loser_wu
 * @sina 2018/4/12
 */
@Controller
@RequestMapping(value = "/formManage")
public class BpmFormManageController {
	@Autowired
	private BpmFormManageService bpmFormManageService;
	
	@Autowired
	private DhProcessCategoryService dhProcessCategoryService;
	
	@Autowired
	private DhProcessMetaService dhProcessMetaService; 
	
	@Autowired
	private DhProcessDefinitionService dhProcessDefinitionService;
	
	@RequestMapping(value = "/index")
	public ModelAndView toIndex() {
        ModelAndView mv =  new ModelAndView("formManagement");
        return mv;
	}
	
	/**
	 * 获得页面流程树数据
	 */
	@RequestMapping(value = "/getTreeData")
	@ResponseBody
	public String getTreeData() {
		List<DhProcessCategory> categoryList = dhProcessCategoryService.listAll();
        List<DhProcessMeta> metaList = dhProcessMetaService.listAll();
        List<ZTreeNode> nodesToShow = new ArrayList<ZTreeNode>();
        for (DhProcessCategory category : categoryList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(category.getCategoryUid());
            node.setName(category.getCategoryName());
            node.setPid(category.getCategoryParent());
            node.setItemType("category");
            node.setIcon("http://localhost:8088/desmartbpm/resources/images/face/60.gif");
            nodesToShow.add(node);
        }
        
        for (DhProcessMeta meta : metaList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(meta.getProMetaUid());
            node.setName(meta.getProName());
            node.setPid(meta.getCategoryUid());
            node.setItemType("processMeta");
            node.setIcon("http://localhost:8088/desmartbpm/resources/images/face/47.gif");
            nodesToShow.add(node);
        }
        //同步查询所有流程定义代码，生成所有树 itemType为processDefinition
        //for() {}
        return JsonUtil.obj2String(nodesToShow);
	}
	
	/**
	 * 获得某流程分类下所有流程的表单
	 */
	@RequestMapping(value = "/listFormByProcessCategory")
	@ResponseBody
	public ServerResponse listFormByProcessCategory(String formTitle,String proCategoryUid,
			@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize) {
		
		List<DhProcessDefinition> dhProcessList = new ArrayList<>();
		DhProcessDefinition dhProcess1 = new DhProcessDefinition();
		dhProcess1.setProUid("0001");
		dhProcessList.add(dhProcess1);
		DhProcessDefinition dhProcess2 = new DhProcessDefinition();
		dhProcess2.setProUid("0002");
		dhProcessList.add(dhProcess2);
		DhProcessDefinition dhProcess3 = new DhProcessDefinition();
		dhProcess3.setProUid("0003");
		dhProcessList.add(dhProcess3);
		//获得某分类下所有的流程定义
		ServerResponse<List<DhProcessDefinition>> serverResponse = ServerResponse.createBySuccess(dhProcessList);
		if(!serverResponse.isSuccess()) {
			return serverResponse; 
		}
		return bpmFormManageService.listFormByProcessCategory(serverResponse.getData(),formTitle,pageNum,pageSize);
	}
	
	/**
	 * 获得某版本的流程定义下的表单
	 */
	@RequestMapping(value = "/listFormByProDefinition")
	@ResponseBody
	public ServerResponse listFormByProDefinition(String formTitle,String proUid,String proVerUid,
			@RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",defaultValue="10") Integer pageSize) {
		return bpmFormManageService.listFormByProDefinition(formTitle,proUid,proVerUid,pageNum,pageSize);
	}
}
