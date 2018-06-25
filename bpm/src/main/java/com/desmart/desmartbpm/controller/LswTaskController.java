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

@Controller
@RequestMapping(value = "/lswTask")
public class LswTaskController {
    private static final Logger LOG = LoggerFactory.getLogger(LswTaskController.class);

    @Autowired
    private LswTaskService lswTaskService;

    @RequestMapping(value = "/changeOwnerOfLswTask", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse changeOwnerOfLswTask(@RequestBody String data) {
        try {
            JSONObject dataJson = JSON.parseObject(data);
            int taskId = dataJson.getIntValue("taskId");
            String userUid = dataJson.getString("userUid");
            return lswTaskService.changeOwnerOfLswTask(taskId, userUid);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("修改任务处理人失败");
        }
    }

}