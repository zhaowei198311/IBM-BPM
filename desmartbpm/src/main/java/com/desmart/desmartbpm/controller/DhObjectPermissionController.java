package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.DhObjectPermissionService;

@Controller
@RequestMapping(value = "/permission")
public class DhObjectPermissionController {
    private static final Logger LOG = LoggerFactory.getLogger(DhObjectPermissionController.class);
    
    @Autowired
    private DhObjectPermissionService dhObjectPermissionService;
    
    
    @RequestMapping(value = "/processStart")
    @ResponseBody
    public ServerResponse getPermissionStartOfProcess(String proAppId, String proUid, String proVerUid) {
        return dhObjectPermissionService.getPermissionStartOfProcess(proAppId, proUid, proVerUid);
    }
    
    
    
    
}
