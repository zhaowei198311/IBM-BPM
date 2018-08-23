package com.desmart.desmartbpm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.annotation.log.Log;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/permission")
public class DhObjectPermissionController {
    private static final Logger LOG = LoggerFactory.getLogger(DhObjectPermissionController.class);
    
    @Autowired
    private DhObjectPermissionService dhObjectPermissionService;
    
    
    @RequestMapping(value = "/processStart")
    @ResponseBody
    public ServerResponse getPermissionStartOfProcess(String proAppId, String proUid, String proVerUid) {
        return dhObjectPermissionService.getPermissionStartOfProcess(proAppId, proUid, proVerUid);
    }
    
    @RequestMapping(value = "/processReadByPage")
    @ResponseBody
    public ServerResponse<PageInfo<List<DhObjectPermission>>> getPermissionReadOfMetaByPage(Integer pageNum,Integer pageSize,String proAppId,String proUid) {
    	return dhObjectPermissionService.getPermissionReadOfMetaByPage(pageNum, pageSize, proAppId, proUid);
    }
    
    @Log(description="根据主键批量删除权限数据")
    @RequestMapping("/deleteBatchByPrimaryKeys")
    @ResponseBody
    public ServerResponse deleteBatchByPrimaryKeys(@RequestParam("primaryKeys")List<String> primaryKeys) {
    	return dhObjectPermissionService.deleteBatchByPrimaryKeys(primaryKeys);
    }
    
    
    
}
