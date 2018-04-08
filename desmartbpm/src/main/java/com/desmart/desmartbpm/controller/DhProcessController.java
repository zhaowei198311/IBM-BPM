package com.desmart.desmartbpm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.DhProcessService;

@Controller
@RequestMapping(value = "/process")
public class DhProcessController {
    @Autowired
    private DhProcessService dhProcessService;
    
    
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("processIndex");
    }
    
    
    /**
     * 获得公开的流程
     * @param page
     * @param size
     * @param request
     * @return
     */
    @RequestMapping(value = "/getPublicProcess")
    @ResponseBody
    public ServerResponse getPublicProcess(Integer page, Integer size, HttpServletRequest request) {
        
        return dhProcessService.getExposedProcess(request, page, size);
    }
    
}
