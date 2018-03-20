package com.desmart.desmartbpm.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import static org.junit.Assert.*;

public class BpmGlobalConfigServiceTest {
    private BpmGlobalConfigService bpmGlobalConfigService;

    @Before
    public void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        bpmGlobalConfigService = ac.getBean(BpmGlobalConfigService.class);
    }

    @Test
    public void getFirstActConfig() {
        BpmGlobalConfig config = bpmGlobalConfigService.getFirstActConfig();
        System.out.println(config);
    }
}