package com.desmart.desmartbpm.controller;

import com.desmart.common.annotation.log.Log;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.service.DhTriggerService;

@Controller
@RequestMapping(value = "/trigger")
public class DhTriggerController {

    @Autowired
    private DhTriggerService dhTriggerService;
    
    @RequestMapping(value = "/index")
    public String index() {
    	return "desmartbpm/trigger";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public ServerResponse searchTrigger(DhTrigger dhTrigger,
                                        @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
                                        @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
        return dhTriggerService.searchTrigger(dhTrigger, pageNum, pageSize);
    }

    @Log(description = "删除一个触发器")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ServerResponse deleteTrigger(@RequestParam(value="triUid")String triUid) {
        return dhTriggerService.deleteTrigger(triUid);
    }

    @Log(description = "新增一个触发器")
    @RequestMapping(value = "/save")
    @ResponseBody
    public ServerResponse saveTrigger(DhTrigger dhTrigger) {
    	return dhTriggerService.saveTrigger(dhTrigger);
    }


    @RequestMapping(value = "/serachByPrimarkey")
    @ResponseBody
    public ServerResponse serachByPrimarkey(@RequestParam(value = "triUid")String triUid) {
    	return  dhTriggerService.getTriggerByPrimarkey(triUid);
    }

    /**
     * 更新触发器
     * @param dhTrigger
     * @return
     */
    @Log(description = "更新一个触发器")
    @RequestMapping(value = "/update")
    @ResponseBody
    public ServerResponse updateTrigger(DhTrigger dhTrigger) {
    	return dhTriggerService.updateTrigger(dhTrigger);
    }
}