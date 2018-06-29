package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.ZTreeNode;
import com.desmart.desmartbpm.service.DhProcessCategoryService;

@Controller
@RequestMapping(value = "/processCategory")
public class DhProcessCategoryController {
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    
    
    @RequestMapping(value = "/index")
    public ModelAndView toIndex() {
        String zNodes = JSON.toJSONString(dhProcessCategoryService.listAll());
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
        return JSON.toJSONString(nodesToShow);
    }
    
    /**
     * 
     * @Title: changeThePosition  
     * @Description: 改变源数据在分类树的位置  
     * @param: @param metaUid
     * @param: @param categoryUid
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    @RequestMapping(value = "/changeThePosition")
    @ResponseBody
    public ServerResponse<?> changeThePosition(String metaUid, String categoryUid){
    	return dhProcessCategoryService.changeThePosition(metaUid, categoryUid);
    }
    
    /**
     * 
     * @Title: changeStatus  
     * @Description: 更改流程状态为隐藏（DH_PROCESS_META）  
     * @param: @param metaUid
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    @RequestMapping(value = "/changeStatus")
    @ResponseBody
    public ServerResponse<?> changeStatus(String metaUid){
    	return dhProcessCategoryService.changeStatus(metaUid);
    }
    
    /**
     * 
     * @Title: closeCategory  
     * @Description: 关闭流程  
     * @param: @param metaUid
     * @param: @return  
     * @return: ServerResponse<?>
     * @throws
     */
    @RequestMapping(value = "/closeCategory")
    @ResponseBody
    public ServerResponse<?> closeCategory(String metaUid){
    	return dhProcessCategoryService.closeCategory(metaUid);
    }
    
    /**
     * 
     * @Title: enableCategory  
     * @Description: 启用流程  
     * @param @param metaUid
     * @param @return  
     * @return ServerResponse<?>  
     * @throws
     */
    @RequestMapping(value = "/enableCategory")
    @ResponseBody
    public ServerResponse<?> enableCategory(String metaUid){
    	return dhProcessCategoryService.enableCategory(metaUid);
    }
    
    @RequestMapping(value = "/queryByParent")
    @ResponseBody
    public List<DhProcessCategory> queryByParent(@RequestParam("categoryParent")String categoryParent){
    	return dhProcessCategoryService.listByCategoryParent(categoryParent);
    }
}
