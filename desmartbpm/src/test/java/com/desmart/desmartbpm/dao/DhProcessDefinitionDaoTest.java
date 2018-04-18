package com.desmart.desmartbpm.dao;

import com.desmart.desmartbpm.entity.DhProcessDefinition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class DhProcessDefinitionDaoTest {
    private DhProcessDefinitionMapper dao;

    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(DhProcessDefinitionMapper.class);
    }

    @Test
    public void saveTest() {
        DhProcessDefinition definition = new DhProcessDefinition();
        definition.setProUid("dfsddfdf");
        definition.setProVerUid("dsfd");
        definition.setProUid("sddsfd");
        dao.save(definition);
    }

    @Test
    public void listBySelectiveTest() {
        DhProcessDefinition definition = new DhProcessDefinition();
        definition.setProUid("1");
        definition.setProVerUid("1");
        definition.setProUid("1");

        List<DhProcessDefinition> dhProcessDefinitions = dao.listBySelective(definition);
        if (dhProcessDefinitions.size() > 0) {
            DhProcessDefinition definition1 = dhProcessDefinitions.get(0);
            definition1.setProUid("2");
            //dao.save(definition1);
        }
    }

    @Test
    public void test3() {
        DhProcessDefinition definition = new DhProcessDefinition();
        definition.setProUid("1");
        definition.setProVerUid("1");
        definition.setProUid("1");

        List<DhProcessDefinition> dhProcessDefinitions = dao.listBySelective(definition);
        if (dhProcessDefinitions.size() > 0) {
            DhProcessDefinition definition1 = dhProcessDefinitions.get(0);
            definition1.setProAssignment("dfdddfdssdf");

            dao.updateByProAppIdAndProUidAndProVerUidSelective(definition1);
        }
    }

}