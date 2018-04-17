package com.desmart.desmartbpm.controller;

import com.desmart.desmartbpm.common.ServerResponse;
import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.service.DhTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/trigger")
public class DhTriggerController {

    @Autowired
    private DhTriggerService dhTriggerService;


    @RequestMapping(value = "/search")
    @ResponseBody
    public ServerResponse searchTrigger(DhTrigger dhTrigger,
                                        @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
                                        @RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
        return dhTriggerService.searchTrigger(dhTrigger, pageNum, pageSize);
    }


}