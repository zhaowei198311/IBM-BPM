package com.desmart.desmartbpm.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class BpmProcessSnapshotServiceTest {
    private BpmProcessSnapshotService bpmProcessSnapshotService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        bpmProcessSnapshotService = ac.getBean(BpmProcessSnapshotService.class);
    }

    @Test
    public void startSysncActivityMeta() {
    }

    @Test
    public void processModel() {
    }

    @Test
    public void processVisualModel() {

    }
}