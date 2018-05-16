package com.desmart.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;


/**
 * 
 * @description (日期工具类)
 * @date 2017年11月24日
 */
public class DateUtil {
public static int compare_date(String DATE1, String DATE2) {
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

/**  
 * 计算两个日期之间相差的天数  
 * @param smdate 较小的时间 
 * @param bdate  较大的时间 
 * @return 相差天数 
 * @throws ParseException  
 * @throws java.text.ParseException 
 * @throws s 
 */    
public static int daysBetween(Date smdate,Date bdate)    
{    
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
    try {
		smdate=sdf.parse(sdf.format(smdate));
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
    try {
		bdate=sdf.parse(sdf.format(bdate));
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
    Calendar cal = Calendar.getInstance();    
    cal.setTime(smdate);    
    long time1 = cal.getTimeInMillis();                 
    cal.setTime(bdate);    
    long time2 = cal.getTimeInMillis();         
    long between_days=(time2-time1)/(1000*3600*24);  
        
   return Integer.parseInt(String.valueOf(between_days));           
}    




/**
 * 时间加N 天
 * @param date 时间
 * @param n 天
 * @return
 */
public static String getDateAddDay(String date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(stringtoDate(date));
     calendar.add(Calendar.DAY_OF_MONTH, +n);//+1今天的时间加一天
     return datetoString(calendar.getTime());
}

/**
 * 时间加小时
 * @param date 时间
 * @param n 小时
 * @return
 */
public static Date getDateAddHour(Date date,int n){
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.HOUR, +n);//+1今天的时间加一小时
     return calendar.getTime();
}

/**
 * 时间加N 天
 * @param date 时间
 * @param n 天
 * @return
 */
public static Date getDateAddDay(Date date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.DAY_OF_MONTH, +n);//+1今天的时间加一天
     return calendar.getTime();
}

/**
 * 时间加N 月
 * @param date 时间
 * @param n 月
 * @return
 */
public static Date getDateAddMonth(Date date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.MONTH, +n);//+1今天的时间加一个月
     return calendar.getTime();
}

/**
 * 时间加N 年
 * @param date 时间
 * @param n 年
 * @return
 */
public static Date getDateAddYear(Date date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(Calendar.YEAR, +n);//+1今天的时间加一年
     return calendar.getTime();
}

/**
 * 时间加N 月
 * @param date 时间
 * @param n 月
 * @return
 */
public static Date getDateAddMonth(String date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(stringtoDate(date));
     calendar.add(Calendar.MONTH, +n);//+1今天的时间加一个月
     return calendar.getTime();
}

/**
 * 时间加N 年
 * @param date 时间
 * @param n 年
 * @return
 */
public static Date getDateAddYear(String date,int n){
	 Calendar calendar = Calendar.getInstance();
     calendar.setTime(stringtoDate(date));
     calendar.add(Calendar.YEAR, +n);//+1今天的时间加一年
     return calendar.getTime();
}

/** 
*字符串的日期格式的计算 
 * @throws java.text.ParseException 
*/  
public static int daysBetween(String smdate,String bdate) {  
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
    Calendar cal = Calendar.getInstance();    
    try {
		cal.setTime(sdf.parse(smdate));
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}    
    long time1 = cal.getTimeInMillis();                 
    try {
		cal.setTime(sdf.parse(bdate));
	} catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}    
    long time2 = cal.getTimeInMillis();         
    long between_days=(time2-time1)/(1000*3600*24);  
        
   return Integer.parseInt(String.valueOf(between_days));     
}  

/**
 * String转date
 * @param sdate
 * @return
 */
public static Date stringtoDate(String sdate){
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
	  
	Date date = null;
	try {
		date = sdf.parse(sdate);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	return date;
}

/**
 * date转String
 */
public static String datetoString(Date date){
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String str=sdf.format(date);
	return str;
}
/**
 * 获得时间是 第几周  
 * @param date
 * @return
 */
public static int getweekOfYear(String today){
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  Date date = null;
	try {
		date = format.parse(today);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  Calendar calendar = Calendar.getInstance();
	  calendar.setFirstDayOfWeek(Calendar.MONDAY);
	  calendar.setTime(date);
	return calendar.get(Calendar.WEEK_OF_YEAR);
}

/**
 * 获得时间是 第几周  
 * @param date
 * @return
 */
public static int getweekOfYear(Date date) {
GregorianCalendar g = new GregorianCalendar();
g.setFirstDayOfWeek(Calendar.MONDAY);
g.setTime(date);
return g.get(Calendar.WEEK_OF_YEAR);//获得周数
}

public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };

public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
 
public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
 
/**
 * 根据日期获取生肖
 * @return
 */
public static String getZodica(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return zodiacArr[cal.get(Calendar.YEAR) % 12];
}
 
/**
 * 根据日期获取星座
 * @return
 */
public static String getConstellation(Date date) {
    if (date == null) {
        return "";
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    if (day < constellationEdgeDay[month]) {
        month = month - 1;
    }
    if (month >= 0) {
        return constellationArr[month];
    }
    // default to return 魔羯
    return constellationArr[11];
}


/**
 * 获取当天的开始时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static Date getDayBegin() {
	/*
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();*/
	Date date = new Date();
	return getDayStartTime(date);
}

/**
 * 获取当天的结束时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static java.util.Date getDayEnd() {
    /*Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    return cal.getTime();*/
	Date date = new Date();
	return getDayEndTime(date);
}

/**
 * 获取昨天的开始时间
 * @return 默认格式 Wed May 31 14:47:18 CST 2017
 */
public static Date getBeginDayOfYesterday() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayBegin());
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return cal.getTime();
}

/**
 * 获取昨天的结束时间
 * @return 默认格式 Wed May 31 14:47:18 CST 2017
 */
public static Date getEndDayOfYesterDay() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayEnd());
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return cal.getTime();
}

/**
 * 获取明天的开始时间
 * @return 默认格式 Wed May 31 14:47:18 CST 2017
 */
public static Date getBeginDayOfTomorrow() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayBegin());
    cal.add(Calendar.DAY_OF_MONTH, 1);

    return cal.getTime();
}

/**
 * 获取明天的结束时间
 * @return 默认格式 Wed May 31 14:47:18 CST 2017
 */
public static Date getEndDayOfTomorrow() {
    Calendar cal = new GregorianCalendar();
    cal.setTime(getDayEnd());
    cal.add(Calendar.DAY_OF_MONTH, 1);
    return cal.getTime();
}

/**
 * 获取本周的开始时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static Date getBeginDayOfWeek(Date date) {
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
        dayofweek += 7;
    }
    cal.add(Calendar.DATE, 2 - dayofweek);
    return getDayStartTime(cal.getTime());
}

/**
 * 获取本周的开始时间
 * @return yyyy-MM-dd   格式
 */
public static String getBeginDayOfWeek(String date) {
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(stringtoDate(date));
    int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    if (dayofweek == 1) {
        dayofweek += 7;
    }
    cal.add(Calendar.DATE, 2 - dayofweek);
    return datetoString(getDayStartTime(cal.getTime()));
}
@Test
public void ss(){
	System.out.println(getweekOfYear("2017-1-2"));
}


/**
 * 获取本周的结束时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static Date getEndDayOfWeek(Date date){
    Calendar cal = Calendar.getInstance();
    cal.setTime(getBeginDayOfWeek(date));  
    cal.add(Calendar.DAY_OF_WEEK, 6); 
    Date weekEndSta = cal.getTime();
    return getDayEndTime(weekEndSta);
}

/**
 * 获取本周的结束时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static String getEndDayOfWeek(String date){
    Calendar cal = Calendar.getInstance();
    cal.setTime(getBeginDayOfWeek(stringtoDate(date)));  
    cal.add(Calendar.DAY_OF_WEEK, 6); 
    Date weekEndSta = cal.getTime();
    return datetoString(getDayEndTime(weekEndSta));
}


/**
 * 获取本月的开始时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
 public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }
 
 
 
/**
 * 获取本月的结束时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
 public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }
 
 /**
  * 获取本年的开始时间
  * @return yyyy-MM-dd HH:mm:ss  格式
  */
 public static java.util.Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());
    }
 
 /**
  * 获取本年的结束时间
  * @return yyyy-MM-dd HH:mm:ss  格式
  */
 public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }
 
/**
 * 获取某个日期的开始时间
 * @param d
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static Timestamp getDayStartTime(Date d) {
    Calendar calendar = Calendar.getInstance();
    if(null != d) calendar.setTime(d);
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return new Timestamp(calendar.getTimeInMillis());
}

/**
 * 获取某个日期的结束时间
 * @param d
 * @return yyyy-MM-dd HH:mm:ss  格式
 */
public static Timestamp getDayEndTime(Date d) {
    Calendar calendar = Calendar.getInstance();
    if(null != d) calendar.setTime(d);
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),   calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return new Timestamp(calendar.getTimeInMillis());
}

/**
 * 获取某年某月的第一天
 * @param year
 * @param month
 * @return
 */
public static Date getStartMonthDate(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    return calendar.getTime();
}

/**
 * 获取某年某月的最后一天
 * @param year
 * @param month
 * @return
 */
public static Date getEndMonthDate(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    int day = calendar.getActualMaximum(5);
    calendar.set(year, month - 1, day);
    return calendar.getTime();
}

/**
 * 获取今年是哪一年
 * @return
 */
 public static Integer getNowYear() {
         Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }
 
 /**
  * 获取本月是哪一月
  * @return
  */
 public static int getNowMonth() {
         Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }
 
 /**
  * 两个日期相减得到的天数
  * @param beginDate
  * @param endDate
  * @return
  */
 public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }
 
/**
 * 两个日期相减得到的毫秒数
 * @param beginDate
 * @param endDate
 * @return
 */
 public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }
 
 /**
  * 获取两个日期中的最大日期
  * @param beginDate
  * @param endDate
  * @return
  */
 public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }
 
 /**
  * 获取两个日期中的最小日期
  * @param beginDate
  * @param endDate
  * @return
  */
 public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }
 
 /**
  * 返回某月该季度的第一个月
  * @param date
  * @return
  */
 public static Date getFirstSeasonDate(Date date) {
         final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }
 
 /**
  * 返回某个日期下几天的日期
  * @param date
  * @param i
  * @return
  */
 public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }
 
 /**
  * 返回某个日期前几天的日期
  * @param date
  * @param i
  * @return
  */
 public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }
 
 /**
  * 获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
  * @param beginYear
  * @param beginMonth
  * @param endYear
  * @param endMonth
  * @param k
  * @return
  */
 public static List<List<Date>> getTimeList(int beginYear, int beginMonth, int endYear,
            int endMonth, int k) {
        List<List<Date>> list = new ArrayList<List<Date>>();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));

            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }

                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }
 
 /**
  * 获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
  * @param beginYear
  * @param beginMonth
  * @param k
  * @return
  */
 public static List<Date> getTimeList(int beginYear, int beginMonth, int k) {
        List<Date> list = new ArrayList<Date>();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }
 /**
  * 格式化日期
 * yyyy-MM-dd HH:mm:ss
 * @Description: 
 * @param @param date
 * @param @return     
 *
  */
 public static Date format(Date date) {
	 SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 try {
			date = sd.parse(sd.format(date));
	} catch (ParseException e) {
		e.printStackTrace();
	}
	 return date;
 }
 
 /**
  * 获取今天是星期几
  * @param date
  * @return
  */
 public  static String getWeek(Date date){
	 SimpleDateFormat sd = new SimpleDateFormat("EEEE");
	return  sd.format(date); 
 }
 
 
 public static String getWeek(String date){
	return getWeek( stringtoDate(date));
 }







/**
 * 获取本月的开始时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 * @throws java.text.ParseException 
 */
 public static String getBeginDayOfMonth(String date){
	 Calendar calendar = Calendar.getInstance();
	 calendar.setTime( stringtoDate(date));
	 calendar.set(Calendar.DAY_OF_MONTH, 1);
	 Date firstDayOfMonth = calendar.getTime(); 
		return datetoString(firstDayOfMonth);
    }
 
 
 
/**
 * 获取月的结束时间
 * @return yyyy-MM-dd HH:mm:ss  格式
 * @throws java.text.ParseException 
 */
 public static String getEndDayOfMonth(String date) {
	 Calendar calendar = Calendar.getInstance();
	 calendar.setTime( stringtoDate(date));
	 calendar.set(Calendar.DAY_OF_MONTH, 1);
	// Date firstDayOfMonth = calendar.getTime();  
	 calendar.add(Calendar.MONTH, 1);
	 calendar.add(Calendar.DAY_OF_MONTH, -1);
	 Date lastDayOfMonth = calendar.getTime();
	 return datetoString(lastDayOfMonth); 
    }

}

 
 

