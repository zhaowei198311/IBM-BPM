package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.service.BpmActivityMetaService;

@Controller
@RequestMapping(value = "/activityConf")
public class DhActivityConfController {
    
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    
    
    /**
     * 编辑环节配置
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @param actcUid
     * @return
     */
    @RequestMapping(value = "/edit")
    public ModelAndView editActivityConf(String proAppId, String proUid, String proVerUid, @RequestParam(value = "actcUid", required = false) String actcUid) {
        ModelAndView mv = new ModelAndView("activityConf");
        
        return mv;
    }
    
    
    

}
