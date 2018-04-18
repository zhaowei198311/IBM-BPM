package com.desmart.desmartbpm.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.DhProcessCategory;
import com.desmart.desmartbpm.entity.DhProcessMeta;

import java.util.List;

public class DhProcessMetaDaoTest {
    private DhProcessMetaMapper dao;
    private DhProcessCategoryMapper categoryDao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(DhProcessMetaMapper.class);
        categoryDao = ac.getBean(DhProcessCategoryMapper.class);
    }
    
    @Test
    public void saveTest() {
        DhProcessMeta meta = dao.queryByProMetaUid("uid1");
        for(int i=0;i<20;i++) {
            meta.setProMetaUid("uid2" + i);
            meta.setCreator("uid1");
            meta.setUpdateUser("ddd");
            dao.save(meta);
        }
        
    }
    
    @Test
    public void qureyTest11() {
        int i = dao.countByProAppIdAndProUid("appid1", "prouid1");
        System.out.println(i);
    }
    
    @Test
    public void listByCategoryList() {
        List<DhProcessCategory> categoryList = categoryDao.listAll();
        System.out.println(categoryList.size());
        List<DhProcessMeta> list =  dao.listByCategoryList(categoryList);
        System.out.println(list.size());
    }
    @Test
    public void listByCategoryListAndProName() {
        List<DhProcessCategory> categoryList = categoryDao.listAll();
        System.out.println(categoryList.size());
        List<DhProcessMeta> list =  dao.listByCategoryListAndProName(categoryList, "");
        System.out.println(list.get(0));
        System.out.println(list.size());
    }
    
    @Test
    public void listBySelectiveTest() {
        DhProcessMeta meta = new DhProcessMeta();
        meta.setCategoryUid("rootCategory");
        meta.setProName("大幅度");
        List<DhProcessMeta> list =  dao.listByDhProcessMetaSelective(meta);
        System.out.println(list.size());
    }
}