package com.desmart.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;

/**
 * 按日期自增id类
 * @author zbw
 *
 */
@Component
public class IncrementIdByDateUtil {

	private static final Logger log = LoggerFactory.getLogger(IncrementIdByDateUtil.class);
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	private static ReentrantLock lock = new ReentrantLock();
	
	//private ExecutorService threadPool = Executors.newFixedThreadPool(5);
	/**
	 * 此方法可根据当前日期生成当天自增长的id值，格式为 20180806-000111，第二天的数据从20180807-000001开始
	 * @return
	 */
	public String createId() {
		String result = null;
		lock.lock();
		try {
			BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat decimalFormat = new DecimalFormat("000000");//格式化id
			//如果为空，新增日志及id参数
			if(null == bpmGlobalConfig.getIncrementIdBydate() || "" == bpmGlobalConfig.getIncrementIdBydate()) {
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
				log.info("返回结果{}", result);
				return result;
				//idValue = (int) parseObject.get("id");
			}else {   //当日期不是今天的时候，重新从1开始生成
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
		log.info("返回结果{}", result);
		return result;
	}


}
