package com.desmart.desmartbpm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.service.BpmActivityMetaService;

@Controller
@RequestMapping(value = "/activityMeta")
public class BpmActivityMetaController {
    private static final Logger LOG = LoggerFactory.getLogger(BpmActivityMetaController.class);
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
    
    /**
     * 获得流程定义中所有排他网关
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @RequestMapping(value = "/getGatewaysOfDhProcessDefinition")
    @ResponseBody
    public ServerResponse getGatewaysOfDhProcessDefinition(String proAppId, String proUid, String proVerUid) {
        try {
            return bpmActivityMetaService.getGatewaysOfDhProcessDefinition(proAppId, proUid, proVerUid);
        } catch (Exception e) {
            LOG.error("获取排他网关失败", e);
            return ServerResponse.createByErrorMessage("获取排他网关失败");
        }
    }
}
