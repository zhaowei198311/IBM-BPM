package com.desmart.desmartbpm.common;

public class Const {
    /**
     * ServletContext中保存的http连接池的attributeName
     */
    public static final String HTTP_CLIENT_CONNECTION_POOL = "_httpClientConnectionPool";
    
    public static final String CURRENT_USER = "currentUser";
    public static final String CURRENT_USER_NAME = "currentUserName";
    public interface TimeUnit {
        public static final String HOUR = "hour";
        public static final String DAY = "day";
        public static final String MONTH = "month";
    }
    public interface Boolean {
        public static final String TRUE = "TRUE";
        public static final String FALSE = "FALSE";
    }
    public interface FileStatus{
    	public static final String NORMAL = "normal";
    	public static final String DEL = "del";
    }

    /** mongodb中保存的同步任务最后同步的任务id主键 */
    public static final String LAST_SYNCHRONIZED_TASK_ID_KEY = "lastSynTaskId";

    /** mongodb中保存的同步任务最后同步的任务id的集合 */
    public static final String COMMON_COLLECTION_NAME = "commonKeyValue";
    
    /** mongodb insData集合 */
    public static final String INS_DATA = "insData";
}