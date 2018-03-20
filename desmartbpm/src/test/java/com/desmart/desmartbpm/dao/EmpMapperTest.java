package com.desmart.desmartbpm.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EmpMapperTest {
    private EmpDao empMapper;

    @Before
    public void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        empMapper = ac.getBean(EmpDao.class);
    }

    @Test
    public void queryByPrimaryKey() {
        System.out.println(empMapper.queryByPrimaryKey(7369));
    }
}