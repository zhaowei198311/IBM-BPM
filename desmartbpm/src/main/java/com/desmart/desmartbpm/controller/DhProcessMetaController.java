package com.desmart.desmartbpm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.DhProcessMetaService;

@Controller
@RequestMapping(value = "/processMeta")
public class DhProcessMetaController {
    @Autowired
    private DhProcessMetaService dhProcessMetaService;
    
    
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("processIndex");
    }
    
    
    /**
     * 根据分类id列出下面的所有流程
     */
    @RequestMapping(value = "")
    public void listProcessMetaByCategoryUid(String categoryUid, Integer pageNum, Integer pageSize) {
        // todo
    }
    
    
    
    /**
     * 获得公开的流程
     * @param pageNum 
     * @param pageSize 
     * @param request 
     * @param processAppName  应用库名
     * @param processAppAcronym  应用库名缩略
     * @param display 流程名
     * @return
     */
    @RequestMapping(value = "/getExposedProcess")
    @ResponseBody
    public ServerResponse getExposedProcess(Integer pageNum, Integer pageSize, HttpServletRequest request,
            String processAppName, String processAppAcronym, String display) {
        
        return dhProcessMetaService.getExposedProcess(pageNum, pageSize, processAppName, processAppAcronym, display);
    }
    
    
    
    
}
