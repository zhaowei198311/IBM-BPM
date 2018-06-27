package com.desmart.desmartsystem.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InterfaceValidate {
	
	
	
	/**
	 *日期验证
	 * @param dataFormat
	 * @return
	 */
	public static boolean dateFormat(String dataFormat) {
		boolean bl =true;
		try {
			DateFormat df = new SimpleDateFormat(dataFormat);//多态
	        String value=df.format(new Date());
	        if(dataFormat.equals(value)) {
	        	bl=false;
	        }
		} catch (Exception e) {
			e.printStackTrace();
			bl=false;
		}
		return bl;
	}
	
}
