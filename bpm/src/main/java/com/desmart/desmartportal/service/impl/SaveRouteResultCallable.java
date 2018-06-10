package com.desmart.desmartportal.service.impl;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.BpmRoutingData;
import com.desmart.desmartportal.service.DhRouteService;

import java.util.concurrent.Callable;

public class SaveRouteResultCallable implements Callable<Boolean> {
    private DhRouteService dhRouteService;
    private Integer insId;
    private BpmRoutingData routingData;

    public SaveRouteResultCallable(DhRouteService dhRouteService, Integer insId, BpmRoutingData routingData) {
        this.dhRouteService = dhRouteService;
        this.insId = insId;
        this.routingData = routingData;
    }

    @Override
    public Boolean call() {
        ServerResponse response = null;
        try {
            response = dhRouteService.updateGatewayRouteResult(insId, routingData);
            return response.isSuccess();
        } catch (Exception e) {

        }
        return false;
    }
}