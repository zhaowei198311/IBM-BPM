package com.desmart.desmartbpm.controller;

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
    
    @RequestMapping(value = "/delete")
    @ResponseBody
    public void deleteTrigger(@RequestParam(value="triUid")String triUid) {
    	dhTriggerService.deleteTrigger(triUid);
    }
    
    @RequestMapping(value = "/save")
    @ResponseBody
    public void saveTrigger(DhTrigger dhTrigger) {
    	dhTriggerService.saveTrigger(dhTrigger);
    }
    
    @RequestMapping(value = "/serachByPrimarkey")
    @ResponseBody
    public ServerResponse serachByPrimarkey(@RequestParam(value = "triUid")String triUid) {
    	return  dhTriggerService.getTriggerByPrimarkey(triUid);
    }
    
    @RequestMapping(value = "/update")
    @ResponseBody
    public void updateTrigger(DhTrigger dhTrigger) {
    	dhTrigger.setUpdator(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER)));
    	dhTriggerService.updateTrigger(dhTrigger);
    }
}