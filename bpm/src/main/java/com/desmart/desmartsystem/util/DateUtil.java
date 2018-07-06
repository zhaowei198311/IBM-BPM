package com.desmart.desmartsystem.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	private static final String DEFAULTPATTERN = "yyyy-MM-dd";
	private static final String NORMALPATTERN = "yyyy-MM-dd hh:mm:ss";
	/**
	 * 把日期字符串转为java.util.Date类型
	 */
	public static Date strToDate(String dateStr,String parttern) throws Exception{
		if (parttern == null || parttern.equals("")){
			parttern = DEFAULTPATTERN;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(parttern);
		return sdf.parse(dateStr);
	}
	/**
	 * 把java.util.Date类型为日期字符串转
	 */
	public static String dateToStr(Date date,String parttern) throws Exception{
		if (parttern == null || parttern.equals("")){
			parttern = DEFAULTPATTERN;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(parttern);
		if(date != null){
			return sdf.format(date);
		}else{
			return "";
		}
	}
	/** 判断主方法 */	
	public static boolean validate(String dateString){
		 String reg_yyyyMMdd = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))" +
	                "|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))" +
	                "|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
		 Pattern p = Pattern.compile(reg_yyyyMMdd);
		Matcher m = p.matcher(dateString);
		return m.matches();
	}
	
	public static void main(String[] args) throws Exception {
		String parttern="ww";
		System.out.println(dateToStr(new Date(), parttern));
		
		List list=new ArrayList();
		System.out.println(list.size()>0);
	}
	
}
