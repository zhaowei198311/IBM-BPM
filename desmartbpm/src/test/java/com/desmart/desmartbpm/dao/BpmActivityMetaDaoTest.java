package com.desmart.desmartbpm.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.desmart.desmartbpm.util.http.HttpClientUtils;

public class BpmActivityMetaDaoTest {
	BpmActivityMetaDao dao;
	
	
	@Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        dao = ac.getBean(BpmActivityMetaDao.class);
    }
	
	public static void main(String[] args) {
		// A();
		// B();
		 C();
	}

	private static void A() {
		HttpClientUtils httputils = new HttpClientUtils();
		Map<String, Object> pamsMap = new HashMap<>();
		pamsMap.put("method", "put");
		pamsMap.put("action", "resume");
		pamsMap.put("type", "process");
		pamsMap.put("id", "11");
		httputils.IbmApi(pamsMap);
	}
	
	private static void B() {
		HttpClientUtils httputils = new HttpClientUtils();
		httputils.Reject("25","bpdid:06560963b419b917:6da5adb9:1621e277375:-7feb","deadmin");
	}
	
	private static void C() {
		HttpClientUtils httputils = new HttpClientUtils();
		httputils.addSign("celladmin", "加签给第二个人看看");
	}
}
