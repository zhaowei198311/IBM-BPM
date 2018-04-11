package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
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
        ModelAndView mv =  new ModelAndView("processCategory");
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
    
    
    
}
