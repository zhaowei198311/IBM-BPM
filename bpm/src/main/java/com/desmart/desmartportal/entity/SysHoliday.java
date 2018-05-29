package com.desmart.desmartportal.entity;

import java.lang.String;

/**
 * 
 * @ClassName: SysDate  
 * @Description: SYS_DATE  
 * @author: WUZHUANG  
 * @holiday: 2018年5月24日  
 *
 */
public class SysHoliday {
	
	private String dateId;	
	private String holiday;
	
	public SysHoliday() {
		super();
	}
	
	public SysHoliday(String holiday){
		super();
		this.holiday = holiday;
	}
	
	public String getDateId() {
		return dateId;
	}
	public void setDateId(String dateId) {
		this.dateId = dateId;
	}

	public String getHoliday() {
		return holiday;
	}
	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	@Override
	public String toString() {
		return "SysHoliday [dateId=" + dateId + ", holiday=" + holiday + "]";
	}

	
	
}
