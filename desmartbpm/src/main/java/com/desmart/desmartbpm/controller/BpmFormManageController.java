package com.desmart.desmartbpm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.entity.ZTreeNode;
import com.desmart.desmartbpm.service.BpmFormManageService;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
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
	
	@RequestMapping(value = "/index")
	public ModelAndView toIndex(String proUid,String proVersion) {
		ModelAndView mv = new ModelAndView("formManagement");
		mv.addObject("proUid", proUid);
		mv.addObject("proVersion", proVersion);
        return mv;
	}
	
	/**
	 * 跳转到表单设计器
	 */
	@RequestMapping(value = "/designForm")
	public ModelAndView designForm(String formName,String formDescription,
			String proUid,String proVersion) {
		ModelAndView mv = new ModelAndView("common/formDesign");
		mv.addObject("formName", formName);
		mv.addObject("formDescription", formDescription);
		mv.addObject("proUid", proUid);
		mv.addObject("proVersion", proVersion);
		return mv;
	}
	
	/**
	 * 获得页面流程树数据
	 */
	@RequestMapping(value = "/getTreeData")
	@ResponseBody
	public String getTreeData() {
		List<DhProcessCategory> categoryList = dhProcessCategoryService.listAll();
        DhProcessCategory dhProcessCategory = new DhProcessCategory();
        dhProcessCategory.setCategoryUid("rootCategory");
        dhProcessCategory.setCategoryName("流程分类");
        dhProcessCategory.setCategoryParent("0");
        categoryList.add(dhProcessCategory);
        List<DhProcessMeta> metaList = dhProcessMetaService.listAll();
        List<DhProcessDefinition> definitionList = bpmFormManageService.listDefinitionAll();
        List<ZTreeNode> nodesToShow = new ArrayList<ZTreeNode>();
        for (DhProcessCategory category : categoryList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(category.getCategoryUid());
            node.setName(category.getCategoryName());
            node.setPid(category.getCategoryParent());
            node.setItemType("category");
            node.setIcon("../resources/images/1.png");
            nodesToShow.add(node);
        }
        Set<String> metaProUidSet = new HashSet<>();
        for (DhProcessMeta meta : metaList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(meta.getProUid());
            metaProUidSet.add(meta.getProUid());
            node.setName(meta.getProName());
            node.setPid(meta.getCategoryUid());
            node.setItemType("processMeta");
            node.setIcon("../resources/images/2.png");
            nodesToShow.add(node);
        }
        
        for(DhProcessDefinition definition : definitionList) {
        	ZTreeNode node = new ZTreeNode();
        	if(metaProUidSet.contains(definition.getProUid())) {
        		node.setId(definition.getProVerUid());
            	node.setName(definition.getProVerUid());//页面上显示流程下某个版本
                node.setPid(definition.getProUid());
                node.setItemType("processDefinition");
                node.setIcon("../resources/images/3.png");
                nodesToShow.add(node);
        	}
        }
        return JsonUtil.obj2String(nodesToShow);
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
	
	@RequestMapping(value = "/queryFormByName")
	@ResponseBody
	public ServerResponse queryFormByName(String dynTitle) {
		return bpmFormManageService.queryFormByName(dynTitle);
	}
	
	/**
	 * 保存表单数据
	 */
	@RequestMapping(value = "/saveForm")
	@ResponseBody
	public ServerResponse saveForm(@RequestBody BpmForm bpmForm) {
		return bpmFormManageService.saveForm(bpmForm);
	}
	
	/**
	 * 保存上传文件
	 */
	@RequestMapping(value = "/saveFile")
	@ResponseBody
	public String saveFile(MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException {
		//上传文件路径
        String path = request.getServletContext().getRealPath("/resources/file/");
        //上传文件名
        String filename = file.getOriginalFilename();
        File filepath = new File(path,filename);
        //判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) { 
            filepath.getParentFile().mkdirs();
        }
        //将上传文件保存到一个目标文件当中
        file.transferTo(new File(path + File.separator + filename));
		return "{\"filename\":\""+filename+"\"}";
	}
	
	/**
	 * 保存表单html文件
	 */
	@RequestMapping(value = "/saveFormFile")
	@ResponseBody
	public String saveFormFile(String webpage,String filename,HttpServletRequest request) throws IllegalStateException, IOException {
        String path = request.getServletContext().getRealPath("/resources/form/");
        File file = new File(path+filename);
        FileOutputStream fop = new FileOutputStream(file);
        if (!file.exists()) {
        	file.createNewFile();
        }
        byte[] contentInBytes = webpage.getBytes();
        fop.write(contentInBytes);
        fop.flush();
        fop.close();
        return path+filename;
	}
	
	/**
	 * 删除上传文件
	 */
	@RequestMapping(value = "/deleteFile")
	@ResponseBody
	public String deleteFile(String filename,HttpServletRequest request) {
		String path = request.getServletContext().getRealPath("/resources/file/");
		File file = new File(path,filename);
		if(file.delete()) {
			return "true";
		}else {
			return "false";
		}
	}
}
