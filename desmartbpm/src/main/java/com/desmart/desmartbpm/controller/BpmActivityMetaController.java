package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.BpmActivityMetaService;

@Controller
@RequestMapping(value = "/activityMeta")
public class BpmActivityMetaController {
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    
    /**
     * 给出配置环节所需要的环节折叠栏信息
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @RequestMapping(value = "/getActivitiyMetasForConfig")
    @ResponseBody
    public ServerResponse getActivitiyMetasForConfig(String proAppId, String proUid, String proVerUid) {
        return bpmActivityMetaService.getActivitiyMetasForConfig(proAppId, proUid, proVerUid);
    }
    
    
}
