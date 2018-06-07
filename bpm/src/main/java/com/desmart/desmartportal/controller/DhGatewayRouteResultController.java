package com.desmart.desmartportal.controller;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.service.DhRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@RequestMapping(value = "/dhGatewayRoutResult")
@Controller
public class DhGatewayRouteResultController {
    private static final Logger LOG = LoggerFactory.getLogger(DhGatewayRouteResultController.class);

    @Autowired
    private DhRouteService dhRouteService;

    @RequestMapping(value = "/getRouteResult")
    @ResponseBody
    public ServerResponse<String> getRouteResult(Integer insId, String flowObjectId) {
        try {
            return dhRouteService.getDhGatewayRouteResult(insId, flowObjectId);
        } catch (Exception e) {
            LOG.error("查询网关条件失败", e);
            return ServerResponse.createBySuccess(UUID.randomUUID().toString());
        }
    }



}