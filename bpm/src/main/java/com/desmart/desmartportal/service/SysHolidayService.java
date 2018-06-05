package com.desmart.desmartportal.service;

import java.util.Date;

/**
 * 
 * @ClassName: DateService  
 * @Description: 查询传入日期是否为节假日或者休息日  
 * @author: WUZHUANG  
 * @date: 2018年5月24日  
 *
 */
public interface SysHolidayService {
	/**
	 * 
	 * @Title: queryHolidayOrRestDay  
	 * @Description: 如果输入日期是节假日或者休息日，则返回true  
	 * @param @param date
	 * @param @return  
	 * @return boolean  
	 * @throws
	 */
	boolean queryHolidayOrRestDay(Date date);
	
	/**
	 * 
	 * @Title: remainingTime  
	 * @Description: 返回审批最后日期  
	 * @param @param date
	 * @param @param timeAmount
	 * @param @param timeUnit
	 * @param @return  
	 * @return Date  
	 * @throws
	 */
	Date calculateDueDate(Date date, Double timeAmount, String timeUnit);
	
	/**
	 * 
	 * @Title: remainHour  
	 * @Description: 计算任务会签出去时 ，任务剩余时间并保存 
	 * @param @param createDate  
	 * @return void  
	 * @throws
	 */
	void remainHour(String taskUid, String activityId);
}
