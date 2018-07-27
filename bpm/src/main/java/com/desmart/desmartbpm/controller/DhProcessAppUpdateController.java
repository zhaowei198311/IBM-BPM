package com.desmart.desmartbpm.controller;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhProcessDefinition;
import com.desmart.desmartbpm.entity.DhProcessDefinitionBo;
import com.desmart.desmartbpm.service.DhProcessAppUpdateService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Queue;

@Controller
@RequestMapping(value = "/processAppUpdate")
public class DhProcessAppUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(DhProcessAppUpdateController.class);

    @Autowired
    private DhProcessAppUpdateService dhProcessAppUpdateService;


    @RequestMapping(value = "/updateToNewVersion")
    @ResponseBody
    public ServerResponse updateToNewVersion(String proAppId, String oldProVerUid, String newProVerUid) {
        // 准备数据
        ServerResponse<Queue<DhProcessDefinitionBo>> prepareResponse = dhProcessAppUpdateService.prepareData(proAppId, newProVerUid);
        if (!prepareResponse.isSuccess()) {
            return prepareResponse;
        }
        // 开始同步
        return dhProcessAppUpdateService.updateProcessApp(proAppId, oldProVerUid, newProVerUid, prepareResponse.getData());
    }

    @RequestMapping(value = "/pullDefintionByAppIdAndSnapshotId")
    public ServerResponse pullProcessDefinitionByProAppIdAndProVerUid(String proAppId, String proVerUid) {
        // 准备数据
        ServerResponse<Queue<DhProcessDefinitionBo>> prepareResponse = dhProcessAppUpdateService.prepareData(proAppId, proVerUid);
        if (!prepareResponse.isSuccess()) {
            return prepareResponse;
        }
        return dhProcessAppUpdateService.pullAllProcessActivityMeta(prepareResponse.getData());
    }

}