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
	
	public SysDate() {
		super();
	}
	
	public SysDate(String holiday){
		super();
		this.holiday = holiday;
	}
	
	public String getDateId() {
		return dateId;
	}
	public void setDateId(String dateId) {
		this.dateId = dateId;
	}

	public String getDate() {
		return holiday;
	}
	public void setDate(String holiday) {
		this.holiday = holiday;
	}

	@Override
	public String toString() {
		return "SysDate [dateId=" + dateId + ", holiday=" + holiday + "]";
	}
	
}
