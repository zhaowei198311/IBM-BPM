package com.desmart.desmartsystem.util;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.desmart.desmartsystem.entity.SysTeam;
import com.desmart.desmartsystem.service.SysTeamService;

  
@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试  
@ContextConfiguration(locations={"classpath:spring.xml"}) //加载配置文件   
//------------如果加入以下代码，所有继承该类的测试类都会遵循该配置，也可以不加，在测试类的方法上///控制事务，参见下一个实例    
//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！    
//@Transactional    
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！    
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)    
//------------    
public class BaseJunit4Test{  
      @Autowired SysTeamService sysTeamService;
      
      @Test
      public void batchInsertUser() {
    	  List<SysTeam> list=sysTeamService.selectAll(null);
    	  
    	  String uid="sysTeam:4dbc2fbe6db042dcad9da136dbd82672";
    	  for (SysTeam sysTeam : list) {
    	  }
      }
      
      
}  