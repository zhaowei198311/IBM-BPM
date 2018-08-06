package com.desmart.desmartbpm.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.entity.DhStep;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;

public class TestOneTimeTriggerDemo extends DhOneTimeJavaClassTrigger {

    @Override
    public void doOneTime(WebApplicationContext ac, String insUid, JSONObject jsonObject, DhStep dhStep) {
        System.out.println("insUid:" + insUid);
        System.out.println("StepUid:" + dhStep.getStepUid());
        System.out.println("DONE");
    }



}