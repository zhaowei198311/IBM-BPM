package com.desmart.desmartbpm.dao;


import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.enums.DhTriggerType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class DhTriggerDaoTest {

    private DhTriggerMapper dao;
    

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(DhTriggerMapper.class);

    }

    @Test
    public void saveTest() {
        DhTrigger trigger = new DhTrigger();
        trigger.setTriUid("xxxx");
        trigger.setTriTitle("title2");
        trigger.setTriDescription("desc2");
        trigger.setTriType(DhTriggerType.JAVACLASS.getCode());
        trigger.setTriWebbot("com.desmart.cc.xxx");
        trigger.setCreator("user1");
        trigger.setTriParam("{param:1}");
        dao.save(trigger);
    }

    @Test
    public void searchBySelectiveTest() {
        DhTrigger trigger = new DhTrigger();
        trigger.setTriTitle("t");
        trigger.setTriType("");
        List<DhTrigger> list = dao.searchBySelective(trigger);
        if (list.size() > 0) {
            System.out.println(list.get(0));
        } else {
            System.out.println("没有符合的结果");
        }
    }

    @Test
    public void test1() {

    }
}