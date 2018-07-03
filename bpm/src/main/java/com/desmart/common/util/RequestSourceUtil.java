package com.desmart.common.util;

/**
 * 根据请求头获得请求来源
 * @author loser_wu
 * @since 2018/07/02
 */
public class RequestSourceUtil {
	/**
	 * 根据请求头判断请求来源
	 * @param requestHeader
	 * @return true为手机端，false为pc端
	 */
	public static boolean isMobileDevice(String requestHeader) {
		/*
		 * android : 所有android设备
		 * mac os : iphone ipad
		 * windows phone:Nokia等windows系统的手机
		 */
		String[] deviceArray = new String[] { "android", "mac os", "windows phone" };
		if (requestHeader == null) {
			return false;
		}
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}
}
