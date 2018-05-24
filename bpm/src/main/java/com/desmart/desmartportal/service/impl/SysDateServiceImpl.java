package com.desmart.desmartportal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.dao.SysDateMapper;
import com.desmart.desmartportal.entity.SysDate;
import com.desmart.desmartportal.service.SysDateService;

/**
 * 
 * @ClassName: DateServiceImpl  
 * @Description: 实现类  
 * @author: WUZHUANG  
 * @date: 2018年5月24日  
 *
 */
@Service
public class SysDateServiceImpl implements SysDateService{
	
	@Autowired
	private SysDateMapper sysDateMapper;
	
	@Override
	public boolean queryHolidayOrRestDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SysDate sysDate = sysDateMapper.queryHoliday(new SysDate(df.format(date)));
		if (sysDate == null) {
			return false;
		}
		return true;
	}

	@Override
	public Date lastTime(Date date, Double timeAmount, String timeUnit) {
		if (timeAmount == null) {
			timeAmount = 24.0;
		}else {
			if (DhActivityConf.TIME_UNIT_DAY.equals(timeUnit)) {
				timeAmount = timeAmount * 24;
			}
			if (DhActivityConf.TIME_UNIT_MONTH.equals(timeUnit)) {
				timeAmount = timeAmount * 30 * 24;
			}
		}
		// 当前时间 - 创建时间的差值转换成小时
		long lastTime = date.getTime() + (long)(timeAmount * 60 * 60 * 1000L);
		// 查询最后日期是否为节假日或者休息日 true为是
		Boolean condition = queryHolidayOrRestDay(new Date(lastTime));
		if (condition) {
			// 如果最后日期为节假日或者休息日则往后延一天，直到最后日期不为节假日或者休息日为止
			while (true) {
				lastTime += 24 * 60 * 60 * 1000L;	
				Boolean sign = queryHolidayOrRestDay(new Date(lastTime));
				if (!sign) {
					break;
				}
			}
			return new Date(lastTime);
		}
		return new Date(lastTime);
	}

}
