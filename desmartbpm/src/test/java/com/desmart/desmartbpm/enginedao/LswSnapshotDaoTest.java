package com.desmart.desmartbpm.enginedao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.entity.engine.LswSnapshot;


public class LswSnapshotDaoTest {
    LswSnapshotMapper dao;
    
    @Before
    public void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(ac);
        dao = ac.getBean(LswSnapshotMapper.class);
        System.out.println(dao);
    }
    
    @Test
    public void testListAll() {
        List<LswSnapshot> list = dao.listAll();
        System.out.println(list.size());
        if (list.size() > 0) {
            System.out.println(list.get(0));
        }
        
        System.out.println("DONE");
    }
    
    @Test
    public void testDb2Connection() throws Exception{
        String url = "jdbc:db2://10.0.4.201:50000/bpmdb:retrieveMessagesFromServerOnGetMessage=true;";
        String user = "deadmin";  
        String password = "passw0rd";
        Class.forName("com.ibm.db2.jcc.DB2Driver"); 
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
        conn.close();
    }

}
