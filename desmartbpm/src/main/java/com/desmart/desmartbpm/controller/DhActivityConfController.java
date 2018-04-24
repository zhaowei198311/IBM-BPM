package com.desmart.desmartbpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.engine.LswSnapshot;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartbpm.service.DhProcessDefinitionService;

@Controller
@RequestMapping(value = "/activityConf")
public class DhActivityConfController {
    
    @Autowired
    private BpmActivityMetaService bpmActivityMetaService;
    @Autowired
    private DhProcessDefinitionService dhProcessDefinitionService;
    
    
    /**
     * 编辑环节配置
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
    @RequestMapping(value = "/edit")
    public ModelAndView editActivityConf(String proAppId, String proUid, String proVerUid) {
        ModelAndView mv = new ModelAndView("activityConf");
        ServerResponse response = dhProcessDefinitionService.isDhProcessDefinitionExist(proAppId, proUid, proVerUid);
        if (response.isSuccess()) {
            DhProcessDefinition definition = (DhProcessDefinition)response.getData();
            mv.addObject("processDefinition", definition);
            LswSnapshot lswSnapshot = dhProcessDefinitionService.getLswSnapshotBySnapshotId(definition.getProVerUid());
            mv.addObject("lswSnapshot", lswSnapshot);
        } 
        response = dhProcessDefinitionService.getFirstHumanBpmActivityMeta(proAppId, proUid, proVerUid);
        mv.addObject("firstHumanMeta", ((BpmActivityMeta)response.getData()).getActivityId());
        
        return mv;
    }
    
    
    

}
