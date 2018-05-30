package com.desmart.desmartportal.dao;

import org.springframework.stereotype.Repository;

import com.desmart.desmartportal.entity.SysHoliday;

@Repository
public interface SysHolidayMapper {
	
	/**
	 * 
	 * @Title: queryHoliday  
	 * @Description: 查询输入日期是否有结果集  
	 * @param @param date
	 * @param @return  
	 * @return SysDate  
	 * @throws
	 */
	SysHoliday queryHoliday(SysHoliday sysHoliday);
}
