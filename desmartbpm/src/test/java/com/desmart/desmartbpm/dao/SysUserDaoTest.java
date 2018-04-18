package com.desmart.desmartbpm.dao;


import com.desmart.desmartbpm.entity.DhTrigger;
import com.desmart.desmartbpm.enums.DhTriggerType;
import com.desmart.desmartsystem.dao.SysUserMapper;
import com.desmart.desmartsystem.entity.SysUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SysUserDaoTest {

    private SysUserMapper mapper;
    

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        mapper = ac.getBean(SysUserMapper.class);

    }

    @Test
    public void test1() {
        SysUser sysUser = new SysUser();
        mapper.selectAll(sysUser);
    }

}