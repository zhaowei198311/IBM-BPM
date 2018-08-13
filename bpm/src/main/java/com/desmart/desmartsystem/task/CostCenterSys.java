package com.desmart.desmartsystem.task;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.desmart.desmartbpm.util.UUIDTool;
import com.desmart.desmartsystem.dao.DhInterfaceLogMapper;
import com.desmart.desmartsystem.entity.DhInterfaceLog;

/**
 * <p>
 *  成本中心同步
 * </p>
 *
 * @author xsf
 * @since 2018-08-09
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class CostCenterSys {
	
	@Autowired
	private DhInterfaceLogMapper dhInterfaceLogMapper;
	//同步所有成本中心
	@Test
	public void  costCenterAllSys() {
		DhInterfaceLog dhInterfaceLog= new DhInterfaceLog();
		
		dhInterfaceLog.setIntUid("dhinterfacelog:" + UUIDTool.getUUID());
		dhInterfaceLog.setCreatedate(new Date());
		dhInterfaceLog.setDilErrormsg("");
		dhInterfaceLog.setDilRequest("");
		dhInterfaceLog.setDilResponse("");
		dhInterfaceLog.setDilUid("12321312321");
		
		dhInterfaceLogMapper.insert(dhInterfaceLog);
		
		
	}
}
