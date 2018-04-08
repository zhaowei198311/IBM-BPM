package com.desmart.desmartbpm.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BpmActivityMetaDaoTest {
	BpmActivityMetaDao dao;
	
	
	@Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(BpmActivityMetaDao.class);
    }
	


}
