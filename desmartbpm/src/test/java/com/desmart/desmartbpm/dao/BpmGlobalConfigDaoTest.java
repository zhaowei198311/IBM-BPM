package com.desmart.desmartbpm.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import java.util.List;

import static org.junit.Assert.*;

public class BpmGlobalConfigDaoTest {
    private BpmGlobalConfigDao dao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(BpmGlobalConfigDao.class);
    }

    @Test
    public void queryActiveConfig() {
        List<BpmGlobalConfig> list = dao.queryActiveConfig();
        System.out.println(list.size());
        if (list.size() > 0) {
            System.out.println(list.get(0));
        }

    }
}