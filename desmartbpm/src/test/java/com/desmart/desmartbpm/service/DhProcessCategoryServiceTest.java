package com.desmart.desmartbpm.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.DhProcessCategory;

public class DhProcessCategoryServiceTest {
    private DhProcessCategoryService dhProcessCategoryService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dhProcessCategoryService = ac.getBean(DhProcessCategoryService.class);
    }

    @Test
    public void getChildrenCategoryTest() {
        List<DhProcessCategory> list = dhProcessCategoryService.getChildrenCategory("rootCategory");
        for(DhProcessCategory item: list) {
            System.out.println(item);
        }
    }

    @Test
    public void processModel() {
    }

    @Test
    public void processVisualModel() {

    }
}