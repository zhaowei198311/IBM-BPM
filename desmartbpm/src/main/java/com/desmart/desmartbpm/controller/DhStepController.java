package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhStep;
import com.desmart.desmartbpm.service.DhStepService;

@RequestMapping(value = "/step")
@Controller
public class DhStepController {
    private static final Logger LOG = LoggerFactory.getLogger(DhStepController.class);
    
    @Autowired
    private DhStepService dhStepService;
    
    
    @RequestMapping(value = "/create")
    @ResponseBody
    public ServerResponse createStep(DhStep dhStep) {
        try {
            return dhStepService.create(dhStep);
        } catch (Exception e) {
            LOG.error("创建步骤失败", e);
            return ServerResponse.createByErrorMessage("创建步骤失败");
        }
        
    }
    
    @RequestMapping(value = "/updateStep")
    @ResponseBody
    public ServerResponse updateTriggerStep(DhStep dhStep) {
        try {
            System.out.println(dhStep.getStepBusinessKey());
            return dhStepService.updateStep(dhStep);
        } catch (Exception e) {
            LOG.error("更新步骤失败", e);
            return ServerResponse.createByErrorMessage("更新步骤失败");
        }
    }
    
    /**
     * 删除触发器步骤
     * @param stepUid
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ServerResponse deleteStep(String stepUid) {
        try {
            
        } catch (Exception e) {
            LOG.error("更新步骤失败", e);
            return ServerResponse.createByErrorMessage("更新步骤失败");
        }
        return null;
        
    }
    
    
}
