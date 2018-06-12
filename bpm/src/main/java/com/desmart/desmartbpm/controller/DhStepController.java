package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
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
    
    @RequestMapping(value = "/createStepToAll")
    @ResponseBody
    public ServerResponse createStepToAll(DhStep dhStep) {
    	 try {
             return dhStepService.createStepToAll(dhStep);
         } catch (Exception e) {
             LOG.error("创建步骤失败", e);
             return ServerResponse.createByErrorMessage("创建步骤失败");
         }
    }
    
    @RequestMapping(value = "/updateStep")
    @ResponseBody
    public ServerResponse updateTriggerStep(DhStep dhStep) {
        try {
            return dhStepService.updateStep(dhStep);
        } catch (Exception e) {
            LOG.error("更新步骤失败", e);
            return ServerResponse.createByErrorMessage("更新步骤失败");
        }
    }
    
    /**
     * 删除步骤
     * @param stepUid
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ServerResponse deleteStep(String stepUid) {
        try {
            return dhStepService.deleteDhStep(stepUid);
        } catch (Exception e) {
            LOG.error("删除步骤失败", e);
            return ServerResponse.createByErrorMessage("更新步骤失败");
        }
    }
    
    /**
     *  重新排序步骤
     * @param stepUid 步骤主键
     * @param resortType 重排类型：减少步骤，增加步骤   increase/reduce
     * @return
     */
    @RequestMapping(value = "/resortStep")
    @ResponseBody
    public ServerResponse resortStep(String stepUid, String resortType) {
        try {
            return dhStepService.resortStep(stepUid, resortType);
        } catch (Exception e) {
            LOG.error("排序步骤失败", e);
            return ServerResponse.createByErrorMessage("排序步骤失败");
        }
    }
    
}
