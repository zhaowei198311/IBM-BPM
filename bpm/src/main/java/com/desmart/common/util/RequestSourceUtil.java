package com.desmart.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据请求头获得请求来源
 * @author loser_wu
 * @since 2018/07/02
 */
public class RequestSourceUtil {
	private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" }; //定义移动端请求的所有可能类型

	/**
	 * 根据请求头判断请求来源
	 * @param requestHeader
	 * @return true为手机端，false为pc端
	 */
	public static boolean isMobileDevice(String ua) {
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
}
