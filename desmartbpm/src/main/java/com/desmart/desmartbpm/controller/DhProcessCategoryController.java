package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.service.DhProcessCategoryService;

@Controller
@RequestMapping(value = "/processCategory")
public class DhProcessCategoryController {
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    
    
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
