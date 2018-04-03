package com.desmart.desmartbpm.util.http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUtils.class);
    private static final String MOBILE_FLAG_REGEX = "\\biphone\\b|\\bandroid\\b|\\bphone\\b|\\bmobile\\b|\\bipad\\b";
    private static Pattern mobilePattern = Pattern.compile("\\biphone\\b|\\bandroid\\b|\\bphone\\b|\\bmobile\\b|\\bipad\\b");

    public HttpRequestUtils() {
    }

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.length() > 15) {
                String[] ips = ip.split(",");

                for(int index = 0; index < ips.length; ++index) {
                    String strIp = ips[index];
                    if (!"unknown".equalsIgnoreCase(strIp)) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } else {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }

        return ip;
    }

    public static String getURLPostXML(HttpServletRequest request) {
        String inputLine = null;
        StringBuffer recieveData = new StringBuffer();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            while((inputLine = in.readLine()) != null) {
                recieveData.append(inputLine);
            }
        } catch (IOException var13) {
            LOG.error("获取URL提交过来的XML失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException var12) {
                LOG.error("获取URL提交过来的XML，关闭流操作失败");
            }

        }

        return recieveData.toString();
    }

    /**
     * 检查指定对象的类型
     * @param webobj
     * @return HttpRequest对象返回1<br/>
     *         Cookie对象返回2<br/>
     *         其他返回3
     */
    public static int isRequestOrCookieType(Object webobj) {
        int result = 1;
        
        if (webobj instanceof HttpServletRequest) {
            result = 1;
        } else if (webobj instanceof Cookie) {
            result = 2;
        } else {
            result = 3;
        }

        return result;
    }

    public static boolean isMobile(HttpServletRequest request) {
        boolean result = false;
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(userAgent)) {
            userAgent = userAgent.toLowerCase();
            Matcher macther = mobilePattern.matcher(userAgent);
            if (macther.find()) {
                result = true;
            }
        }

        return result;
    }
}
