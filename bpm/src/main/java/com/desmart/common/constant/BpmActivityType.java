package com.desmart.common.constant;

/**
 * 流程环节的常量值
 */
public class BpmActivityType {
    public static final String USERTASK = "UserTask";
    public static final String EVENT = "event";
    public static final String GATEWAY = "gateway";
    public static final String ACTIVITY = "activity";
    public static final String START = "start";
    public static final String CALLEDPROCESS = "CalledProcess";
    public static final String SUBPROCESS = "SubProcess";
    public static final String END = "end";
    public static final String GATEWAYAND = "gatewayAnd";
    public static final String GATEWAYOR = "gatewayOr";
    public static final String SERVICETASK = "ServiceTask";
    public static final String NONETASK = "None";
    public static final String ALONE_SIGN = "alone";
    public static final String COUNTER_SIGN = "count";
    public static final String MULTI_INSTANCE_LOOP = "MultiInstanceLoop";
    public static final String SIMPLE_LOOP = "simpleLoop";
    public static final String NONE_LOOP = "none";
    public static final String MI_ORDER_PARALLEL = "parallel";
    public static final String MI_ORDER_SEQUEL = "sequential";

    public BpmActivityType() {
    }
}
