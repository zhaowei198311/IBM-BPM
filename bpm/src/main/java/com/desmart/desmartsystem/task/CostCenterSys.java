package com.desmart.desmartsystem.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartbpm.mongo.impl.InsDataDaoImpl;
import com.desmart.desmartportal.entity.DhProcessInstance;

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
	private InsDataDaoImpl dnsDataDaoImpl;
	
	@Test
	public void show() {
		System.out.println("开始时间"+System.currentTimeMillis());
		Map<String, String> insData = new HashMap<String, String>();
		insData.put("sapNum", "11111");
		List<DhProcessInstance> list =	dnsDataDaoImpl.quartInsData(insData);
		for (DhProcessInstance string : list) {
			System.out.println("----------------------------------------------"+string+"----------------------------------------------");
		}
		System.out.println("结束时间"+System.currentTimeMillis());
	}
}
