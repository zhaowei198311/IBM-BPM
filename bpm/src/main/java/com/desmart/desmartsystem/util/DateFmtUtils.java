package com.desmart.desmartsystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.text.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供字符串和日期类型之间的转换
 *
 */
public class DateFmtUtils {
    private static Logger LOG = LoggerFactory.getLogger(DateFmtUtils.class);

    public DateFmtUtils() {
    }

    /**
     * 根据给定的格式将日期转为字符串格式
     * @param date  需要转换的日期
     * @param format  转换使用的格式 如 yyyy-MM-dd
     * @return
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            result = sdf.format(date);
        }

        return result;
    }

    /**
     * 将bpm引擎中字符串类型的时间 转换为北京时间
     * @param strDate  引擎中的时间（字符串格式）
     * @return
     */
    public static String formatBpmDateToBJDate(String strDate) {
        String result = strDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            if (!StringUtils.isEmpty(result)) {
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dt = sdf.parse(strDate);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                result = sdf.format(dt);
                result = result.replace("T", " ");
                result = result.replace("Z", "");
            } else {
                result = "";
            }
        } catch (ParseException var5) {
            LOG.error("转换BPM时间为北京时间失败！", var5);
        }

        return result;
    }

    /**
     * 将bpm引擎中字符串类型的时间 转换为Date类型,不加8小时
     * @param strDate  引擎中的字符串
     * @return
     */
    public static Date formatBpmDateToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dt = null;

        try {
            if (!StringUtils.isEmpty(strDate)) {
                dt = sdf.parse(strDate);
            }
        } catch (ParseException var4) {
            LOG.error("转换BPM时间为日期类型失败！", var4);
        }

        return dt;
    }

    /**
     * 将指定字符串转换为Date类型
     * @param strDate  时间字符串
     * @param dateFmt  转换格式， 如 "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date formatDateString(String strDate, String dateFmt) {
        Date dt = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
            if (!StringUtils.isEmpty(strDate)) {
                dt = sdf.parse(strDate);
            }
        } catch (ParseException var4) {
            LOG.error("转换时间为日期类型失败！", var4);
        }

        return dt;
    }
    
    /**
     * 验证指定date字符串是否符合指定格式
     * @param strDate  时间字符串
     * @param dateFmt  转换格式， 如 "yyyy-MM-dd HH:mm:ss"
     * @return boolean
     */
    public static boolean isValidDate(String str,String dateFmt) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(dateFmt);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
