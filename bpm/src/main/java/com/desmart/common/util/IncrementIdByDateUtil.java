package com.desmart.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

@org.springframework.stereotype.Component
public class IncrementIdByDateUtil {

	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	//加锁
	private static ReentrantLock lock = new ReentrantLock();
	
	//private ExecutorService threadPool = Executors.newFixedThreadPool(5);
	
	public String createId() {
		String result = null;
		//拿到第一个bpmGlobalConfig对象并判断
		lock.lock();
		try {
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat decimalFormat = new DecimalFormat("000000");//格式化id
			//如果为空，新增日志及id参数
			if(null == bpmGlobalConfig.getIncrementIdBydate() && "" == bpmGlobalConfig.getIncrementIdBydate()) {
				String dateStr = format.format(new Date());
				JSONObject json = new JSONObject();
				json.put("date", dateStr);
				json.put("id", 1);
				bpmGlobalConfig.setIncrementIdBydate(json.toJSONString());
				int flag = bpmGlobalConfigService.updateByPrimaryKeySelective(bpmGlobalConfig);
			}
			JSONObject parseObject = JSONObject.parseObject(bpmGlobalConfig.getIncrementIdBydate());//获取自增id的信息
			String date =  (String) parseObject.get("date");
			String toDay = format.format(new Date());
			int compare_date = DateUtil.compare_date(date, toDay);
			Integer idValue = null;
			if(compare_date == 0) { //判断是否为今天的操作，如果是，在原来基础上id+1
				idValue = parseObject.getInteger("id");
				result = "" + date.replaceAll("-", "") + "_" + decimalFormat.format(idValue);
				parseObject.put("id", idValue+1);
				bpmGlobalConfig.setIncrementIdBydate(parseObject.toJSONString());
				bpmGlobalConfigService.updateByPrimaryKeySelective(bpmGlobalConfig);
				return result;
				//idValue = (int) parseObject.get("id");
			}else {
				JSONObject json = new JSONObject();
				json.put("date", toDay);
				json.put("id", 1);
				bpmGlobalConfig.setIncrementIdBydate(json.toJSONString());
				int flag = bpmGlobalConfigService.updateByPrimaryKeySelective(bpmGlobalConfig);
				result = "" + toDay.replaceAll("-", "") + "_" + decimalFormat.format(1);
			}
		}
		finally {
			// 释放锁
			lock.unlock();
		}
		return result;
	}

/*	@ResponseBody
	@RequestMapping(value="/haha")
	public String test(){
		String createId = createId();
		return createId;
	}*/

}
