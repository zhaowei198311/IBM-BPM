package com.desmart.desmartbpm.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.BpmGlobalConfig;

import java.util.List;

public class BpmGlobalConfigDaoTest {
    private BpmGlobalConfigMapper dao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(BpmGlobalConfigMapper.class);
    }
public static void main(String[] args) {
    
}
    @Test
    public void queryActiveConfig() {
        List<BpmGlobalConfig> list = dao.queryActiveConfig();
        System.out.println(list.size());
        if (list.size() > 0) {
            System.out.println(list.get(0));
        }

    }
    
    @Test
    public void testInsert() {
    	List<BpmGlobalConfig> list = dao.queryActiveConfig();
    	if (list.size() > 0) {
    		BpmGlobalConfig newConfig = list.get(0);
    		newConfig.setConfigId("test111");
    		newConfig.setConfigStatus("off");
    		dao.insert(newConfig);
        }
    }
}