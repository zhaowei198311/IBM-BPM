package com.desmart.desmartbpm.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.DhObjectPermission;

public class DhObjectPermissionMapperTest {
    private DhObjectPermissionMapper mapper;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        mapper = ac.getBean(DhObjectPermissionMapper.class);
    }
    
    @Test
    public void test1() {
        DhObjectPermission permission = new DhObjectPermission();
        permission.setOpUid("xxxxxx");
        permission.setProAppId("appid");
        mapper.save(permission);
    }
    
    @Test
    public void batchInsert() {
        DhObjectPermission p1 = new DhObjectPermission();
        p1.setOpUid("xxxdx1xx");
        p1.setProAppId("a1dppid");
        DhObjectPermission p2 = new DhObjectPermission();
        p2.setOpUid("xxxd2xxx");
        p2.setProAppId("ap2dpid");
        List<DhObjectPermission> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        mapper.saveBatch(list);
    }
    
}