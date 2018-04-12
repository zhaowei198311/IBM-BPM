package com.desmart.desmartbpm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhProcessCategoryService;
import com.desmart.desmartbpm.service.DhProcessMetaService;

/**
 * 流程定义控制器
 */
@Controller
@RequestMapping(value = "/processDefinition")
public class DhProcessDefinitionController {
    private static final Logger LOG = LoggerFactory.getLogger(DhProcessDefinitionController.class);
    
    @Autowired
    private DhProcessCategoryService dhProcessCategoryService;
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    
    
    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView toIndex() {
        ModelAndView mv = new ModelAndView("processDefinition");
        
        
        return mv;
    }
    
    /**
     * 根据流程元数据，列出数据库中的流程定义，和公开的流程中相关的版本
     * @param metaUid
     * @return
     */
    @RequestMapping(value = "/listDefinitionByProcessMeta")
    @ResponseBody
    public ServerResponse listDefinitionByProcessMeta(String metaUid) {
        System.out.println(metaUid);
        return null;
    }
    
}
