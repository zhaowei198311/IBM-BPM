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
import org.springframework.web.servlet.ModelAndView;

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
        try {
            // 准备数据
            ServerResponse<Queue<DhProcessDefinitionBo>> prepareResponse = dhProcessAppUpdateService.prepareData(proAppId, newProVerUid);
            if (!prepareResponse.isSuccess()) {
                return prepareResponse;
            }
            // 开始同步
            return dhProcessAppUpdateService.updateProcessApp(proAppId, oldProVerUid, newProVerUid, prepareResponse.getData());
        } catch (Exception e) {
            logger.error("升级应用库失败", e);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/pullDefintionByAppIdAndSnapshotId")
    @ResponseBody
    public ServerResponse pullProcessDefinitionByProAppIdAndProVerUid(String proAppId, String proVerUid) {
        // 准备数据
        ServerResponse<Queue<DhProcessDefinitionBo>> prepareResponse = dhProcessAppUpdateService.prepareData(proAppId, proVerUid);
        if (!prepareResponse.isSuccess()) {
            return prepareResponse;
        }
        return dhProcessAppUpdateService.pullAllProcessActivityMeta(prepareResponse.getData());
    }

    /**
     * 获得所有应用库信息
     * @return
     */
    @RequestMapping(value = "/getAllProcessApp")
    @ResponseBody
    public ServerResponse getAllProcessApp() {
        return dhProcessAppUpdateService.getAllProcessApp();
    }


    /**
     * 根据应用库id获得已经同步过的版本
     * @param proAppId 应用库id
     * @return
     */
    @RequestMapping(value = "/findSynchronizedSnapshotByProAppId")
    @ResponseBody
    public ServerResponse findSynchronizedSnapshotByProAppId(String proAppId) {
        return dhProcessAppUpdateService.findSynchronizedSnapshotByProAppId(proAppId);
    }

    /**
     * 根据应用库id获得未同步过的版本
     * @param proAppId 应用库id
     * @return
     */
    @RequestMapping(value = "/findUnsynchronizedSnapshotByProAppId")
    @ResponseBody
    public ServerResponse findUnsynchronizedSnapshotByProAppId(String proAppId) {
        return dhProcessAppUpdateService.findUnsynchronizedSnapshotByProAppId(proAppId);
    }


    @RequestMapping(value = "/toAppUpdate")
    public ModelAndView toAppUpdate() {
        return new ModelAndView("desmartbpm/common/proAppUpdate");
    }

}