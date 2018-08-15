package com.desmart.desmartbpm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.service.LswTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 操作引擎中的lswTask
 */
@Controller
@RequestMapping(value = "/lswTask")
public class LswTaskController {
    private static final Logger logger = LoggerFactory.getLogger(LswTaskController.class);

    @Autowired
    private LswTaskService lswTaskService;

    /**
     * 修改任务的处理人
     * @param data  json类型数据包含 taskId 和 userUid
     * @return
     */
    @RequestMapping(value = "/changeOwnerOfLswTask", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse changeOwnerOfLswTask(@RequestBody String data) {
        try {
            JSONObject dataJson = JSON.parseObject(data);
            int taskId = dataJson.getIntValue("taskId");
            String userUid = dataJson.getString("userUid");
            return lswTaskService.changeOwnerOfLswTask(taskId, userUid);
        } catch (Exception e) {
            logger.error("修改引擎任务表中的处理人失败，", e);
            return ServerResponse.createByErrorMessage("修改任务处理人失败");
        }
    }

}