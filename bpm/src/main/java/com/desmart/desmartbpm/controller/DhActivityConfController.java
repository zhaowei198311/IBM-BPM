package com.desmart.desmartbpm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhActivityConfService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;
import com.desmart.desmartbpm.service.impl.DhActivityConfServiceImpl;

@Controller
@RequestMapping(value = "/activityConf")
public class DhActivityConfController {
    private static final Logger LOG = LoggerFactory.getLogger(DhActivityConfController.class);
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    @Autowired
    private DhActivityConfService dhActivityConfService;
   
    
    /**
     * 编辑环节配置
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @RequestMapping(value = "/edit")
    public ModelAndView editActivityConf(String proAppId, String proUid, String proVerUid) {
        ModelAndView mv = new ModelAndView("desmartbpm/activityConf");
        ServerResponse response = dhProcessDefinitionService.isDhProcessDefinitionExist(proAppId, proUid, proVerUid);
        if (response.isSuccess()) {
            DhProcessDefinition definition = (DhProcessDefinition)response.getData();
            mv.addObject("processDefinition", definition);
            LswSnapshot lswSnapshot = dhProcessDefinitionService.getLswSnapshotBySnapshotId(definition.getProVerUid());
            mv.addObject("lswSnapshot", lswSnapshot);
        } 
        response = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, proVerUid);
        mv.addObject("firstHumanMeta", ((BpmActivityMeta)response.getData()).getActivityId());
        mv.addObject("firstHumanMeteConf", ((BpmActivityMeta)response.getData()).getDhActivityConf().getActcUid());
        ServerResponse<List<BpmActivityMeta>> humanActivitiesResponse = bpmActivityMetaService.getHumanActivitiesOfDhProcessDefinition(proAppId, proUid, proVerUid);
        mv.addObject("humanActivities", humanActivitiesResponse.getData());
        
        return mv;
    }
    
    /**
     * 获取当前配置环节的信息，包括配置和步骤的信息
     * @param actcUid
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public ServerResponse getData(String actcUid) {
        return dhActivityConfService.getActivityConfData(actcUid);
    }
    
    /**
     * 更新环节配置信息（不包括步骤）
     * @param dhActivityConf
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public ServerResponse updateDhActivityConf(DhActivityConf dhActivityConf) {
        System.out.println(dhActivityConf);
        try {
            return dhActivityConfService.updateDhActivityConf(dhActivityConf);
        } catch (Exception e) {
            LOG.error("更新环节失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    
    
}
