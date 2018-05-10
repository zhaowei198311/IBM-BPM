package com.desmart.desmartportal.common;

public class Const {
    /**
     * ServletContext中保存的http连接池的 attributeName
     */
    public static final String HTTP_CLIENT_CONNECTION_POOL = "_httpClientConnectionPool";
    
    public static final String CURRENT_USER = "currentUser";
    
    public interface TimeUnit {
        public static final String HOUR = "hour";
        public static final String DAY = "day";
        public static final String MONTH = "month";
    }
    public interface Boolean {
        public static final String TRUE = "TRUE";
        public static final String FALSE = "FALSE";
    }
    
}