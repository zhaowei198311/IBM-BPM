package com.desmart.desmartbpm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhActivityConfService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;

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
     * 进入编辑环节配置的页面
     * @param proAppId 应用库id
     * @param proUid   流程id
     * @param proVerUid  版本id
     * @return
     */
    @RequestMapping(value = "/edit")
    public ModelAndView editActivityConf(String proAppId, String proUid, String proVerUid) {
        ModelAndView mv = new ModelAndView("desmartbpm/activityConf");
        ServerResponse<DhProcessDefinition> response = dhProcessDefinitionService.isDhProcessDefinitionExist(proAppId, proUid, proVerUid);
        if (response.isSuccess()) {
            DhProcessDefinition definition = response.getData();
            mv.addObject("processDefinition", definition);
            LswSnapshot lswSnapshot = dhProcessDefinitionService.getLswSnapshotBySnapshotId(definition.getProVerUid());
            mv.addObject("lswSnapshot", lswSnapshot);
        }

        BpmActivityMeta firstHumanMeta = bpmActivityMetaService.getFirstUserTaskNodeOfMainProcess(proAppId, proUid, proVerUid);
        if (firstHumanMeta != null) {
            // 如果主流程存在第一个人工环节
            mv.addObject("firstHumanMeta", firstHumanMeta.getActivityId()); // 第一个人工环节的activityId
            mv.addObject("firstHumanMeteConf", firstHumanMeta.getDhActivityConf().getActcUid()); // 第一个人工环节的配置主键
        }
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
     * 更新环节配置信息（不包括步骤和网关）
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
