package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.service.TeamFilterService;

/**
 * 为团队过滤服务提供支持
 */
@RequestMapping(value = "/teamFilter")
@Controller
public class DhTeamFilterController {
    private static final Logger LOG = LoggerFactory.getLogger(DhTeamFilterController.class);
    
    @Autowired
    private TeamFilterService teamFilterService;
    
    
    
    @RequestMapping(value = "/getHandler")
    @ResponseBody
    public ServerResponse getHandler(Integer insId, String activityBpdId) {
        try {
            ServerResponse<String> serverResponse = teamFilterService.getHandler(insId, activityBpdId);
            if (!serverResponse.isSuccess()) {
                LOG.error("请求teamFilter失败： 实例id：" + insId + "  元素id:" + activityBpdId);
            }
            return serverResponse;
        } catch(Exception e) {
            LOG.error("请求teamFilter失败： 实例id：" + insId + "  元素id:" + activityBpdId, e);
            return ServerResponse.createByErrorMessage("调用团队过滤失败");
        }
        
        
        
    }
    
    
    
}
