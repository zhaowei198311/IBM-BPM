package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.ZTreeNode;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.util.JsonUtil;

@Controller
@RequestMapping(value = "/processCategory")
public class DhProcessCategoryController {
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    
    
    @RequestMapping(value = "/index")
    public ModelAndView toIndex() {
        String zNodes = JsonUtil.obj2String(dhProcessCategoryService.listAll());
        ModelAndView mv =  new ModelAndView("desmartbpm/processCategory");
        mv.addObject("zNodes", zNodes);
        return mv;
    }
    
    // 删除指定的分类
    @RequestMapping(value = "/removeCategory")
    @ResponseBody
    public ServerResponse removeDhProceeeCategory(String categoryUid) {
        
        return dhProcessCategoryService.removeDhProcessCategory(categoryUid);
    }
    
    @RequestMapping(value = "/renameCategory")
    @ResponseBody
    public ServerResponse renameDhProceeeCategory(String categoryUid, String newName) {
        return dhProcessCategoryService.renameDhProcessCategory(categoryUid, newName);
    }
    
    /**
     * 增加流程分类
     * @param dhProcessCategory
     * @return
     */
    @RequestMapping(value = "/addCategory")
    @ResponseBody
    public ServerResponse addCategory(DhProcessCategory dhProcessCategory) {
        return dhProcessCategoryService.save(dhProcessCategory);
    }


    @RequestMapping(value = "/getTreeData")
    @ResponseBody
    public String getTreeData() {
        List<DhProcessCategory> categoryList = dhProcessCategoryService.listAll();
        DhProcessCategory dhProcessCategory = new DhProcessCategory();
        dhProcessCategory.setCategoryUid("rootCategory");
        dhProcessCategory.setCategoryName("流程分类");
        dhProcessCategory.setCategoryParent("0");
        categoryList.add(dhProcessCategory);
        List<ZTreeNode> nodesToShow = new ArrayList<ZTreeNode>();
        for (DhProcessCategory category : categoryList) {
            ZTreeNode node = new ZTreeNode();
            node.setId(category.getCategoryUid());
            node.setName(category.getCategoryName());
            node.setPid(category.getCategoryParent());
            node.setItemType("category");
            node.setIcon("../resources/desmartbpm/images/1.png");
            nodesToShow.add(node);
        }
        return JsonUtil.obj2String(nodesToShow);
    }
    
    @RequestMapping(value = "/changeThePosition")
    @ResponseBody
    public ServerResponse<?> changeThePosition(String metaUid, String categoryUid){
    	return dhProcessCategoryService.changeThePosition(metaUid, categoryUid);
    }
}
