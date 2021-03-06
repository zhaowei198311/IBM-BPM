package com.desmart.desmartportal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.dao.DhActivityConfMapper;
import com.desmart.desmartbpm.entity.DhActivityConf;
import com.desmart.desmartportal.dao.DhTaskInstanceMapper;
import com.desmart.desmartportal.dao.SysHolidayMapper;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.entity.SysHoliday;
import com.desmart.desmartportal.service.SysHolidayService;

/**
 * 
 * @ClassName: DateServiceImpl  
 * @Description: 实现类  
 * @author: WUZHUANG  
 * @date: 2018年5月24日  
 *
 */
@Service
public class SysHolidayServiceImpl implements SysHolidayService{
	
	@Autowired
	private SysHolidayMapper sysHolidayMapper;
	@Autowired
	private DhTaskInstanceMapper dhTaskInstanceMapper;
	@Autowired
	private DhActivityConfMapper dhActivityConfMapper;
	
	@Override
	public boolean queryHolidayOrRestDay(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SysHoliday sysHoliday = sysHolidayMapper.queryHoliday(new SysHoliday(df.format(date)));
		if (sysHoliday == null) {
			return false;
		}
		return true;
	}

	@Override
	public Date calculateDueDate(Date date, Double timeAmount, String timeUnit) {
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
		// 创建时间  + 配置时间
		long lastTime = date.getTime() + (long)(timeAmount * 60 * 60 * 1000L);
		// 如果最后日期为节假日或者休息日则往后延一天，直到最后日期不为节假日或者休息日为止
		while (true) {
			
			// 查询最后日期是否为节假日 true为是
			Boolean sign = queryHolidayOrRestDay(new Date(lastTime));
			if (sign || isWeekend(new Date(lastTime))) {
				lastTime += 24 * 60 * 60 * 1000L;
			}else {
				break;
			}
		}		
		return new Date(lastTime);
	}
	
	// 判断日期是否为休息日，true为是
	public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        if (result == Calendar.SATURDAY || result == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

	@Override
	public void remainHour(String taskUid, String activityId) {
		Double timeAmount = null;
		String timeType = "";
		DhTaskInstance dhTaskInstance = dhTaskInstanceMapper.selectByPrimaryKey(taskUid);
		// 主任务会签出去时，主任务花费的时间
		Double spendHours = (double)(new Date().getTime() - dhTaskInstance.getTaskInitDate().getTime())/(60 * 60 *1000);
		// 获取配置时间
		DhActivityConf dhActivityConf = dhActivityConfMapper.getByActivityId(activityId);
		if (dhActivityConf != null) {
			timeAmount = dhActivityConf.getActcTime();
			timeType = dhActivityConf.getActcTimeunit();
		}
		if (timeAmount == null) {
			timeAmount = 24.0;
		} else {
			if (DhActivityConf.TIME_UNIT_DAY.equals(timeType)) {
				timeAmount = timeAmount * 24;
			}
			if (DhActivityConf.TIME_UNIT_MONTH.equals(timeType)) {
				timeAmount = timeAmount * 30 * 24;
			}
		}
		while (true) {
			if (spendHours < timeAmount) {
				dhTaskInstance.setRemainHours(timeAmount - spendHours);
				dhTaskInstanceMapper.updateByPrimaryKeySelective(dhTaskInstance);
				break;
			}
			spendHours -= 24;
		}
	}
}
