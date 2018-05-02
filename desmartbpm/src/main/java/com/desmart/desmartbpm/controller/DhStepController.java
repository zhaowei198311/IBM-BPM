package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;

@RequestMapping(value = "/step")
@Controller
public class DhStepController {
    private static final Logger LOG = LoggerFactory.getLogger(DhStepController.class);
    
    @RequestMapping(value = "/create")
    public ServerResponse createStep(DhStep dhStep) {
        // todo yao
        System.out.println(dhStep);
        
        
        return null;
    }
    
    
}
