package com.desmart.desmartbpm.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.util.JsonUtil;

import java.util.List;

public class DhProcessCategoryDaoTest {
    private DhProcessCategoryMapper dao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(DhProcessCategoryMapper.class);
    }
    
    @Test
    public void saveTest() {
        DhProcessCategory category = new DhProcessCategory();
        category.setCategoryUid("uid");
        category.setCategoryName("name");
        category.setCategoryParent("parent");
        category.setCategoryIcon("icon");
        dao.save(category);
        
    }
    
    @Test
    public void queryByCategoryUidTest() {
        DhProcessCategory category = dao.queryByCategoryUid("uid");
        System.out.println(category);
        
    }
    
    @Test
    public void listByParentTest() {
        List<DhProcessCategory> list = dao.listByCategoryParent("uid");
        if (list.size() > 0) {
            for (DhProcessCategory category : list) {
                System.out.println(category);
            } 
        }
    }
    
    @Test
    public void countByCategoryParentAndCategoryNameTest() {
        int i = dao.countByCategoryParentAndCategoryName("uid", "zi11");
        System.out.println(i);
        
    }
    
    
    @Test
    public void listAllTest() {
        List<DhProcessCategory> list = dao.listAll();
        System.out.println(JsonUtil.obj2String(list));
    }
}