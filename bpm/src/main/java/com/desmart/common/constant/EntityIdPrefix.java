package com.desmart.common.constant;

public class EntityIdPrefix {
    /** 接口数据前缀 */
    public static final String DH_INTERFACE_META = "int_meta:";
    
    /** 接口参数前缀 */
    public static final String DH_INTERFACE_PARAMETER = "para_meta:";
    
    /** 草稿参数前缀 */
    public static final String DH_DRAFTS_META = "draft_meta:";
    
    /** 代理参数前缀 */
    public static final String DH_AGENT_META = "agent_meta:";
    
    /** 代理记录前缀 */
    public static final String DH_AGENT_RECORD = "agent_rec:";
    
    /** 流程实例参数前缀 */
    public static final String DH_PROCESS_INSTANCE = "process_instance:";
    
    /** 任务实例参数前缀 */
    public static final String DH_TASK_INSTANCE = "task_instance:";
    /**
     * 流程附件前缀
     */
    public static final String DH_INSTANCE_DOCUMENT = "docid:";
    /** 环节前缀 */
    public static final String BPM_ACTIVITY_META = "act_meta:";
    
    /** 流程定义分类前缀 */
    public static final String BPM_PROCESS_CATEGORY = "pro_cate:";
    
    /** 流程元数据前缀 */
    public static final String DH_PROCESS_META = "pro_meta:";
    

    /** 触发器前缀 */
    public static final String DH_TRIGGER = "trigger:";
    
    /** 对象权限前缀 */
    public static final String DH_OBJECT_PERMISSION = "obj_perm:";
    
    /** 环节配置前缀 */
    public static final String DH_ACTIVITY_CONF = "act_conf:";
    
    /** 事项分派前缀 */
    public static final String DH_ACTIVITY_ASSIGN = "acta:";
    
    /** 退回环节前缀 */
    public static final String DH_ACTIVITY_REJECT = "act_rej:";
    
    /** 表单前缀 */
    public static final String BPM_FORM = "form:";
    
    /** 表单字段前缀 */
    public static final String BPM_FORM_FIELD = "form_field:";
    
    /** 步骤前缀 */
    public static final String DH_STEP = "step:";
    
    /** 排他网关输出连线前缀 */
    public static final String DH_GATEWAY_LINE = "gateway_line:";
    
    /** 流转信息前缀 */
    public static final String DH_ROUTING_RECORD = "routing_record:";

    /** 网关规则前缀 */
    public static final String DAT_RULE = "datrule:";
    
    /** 网关决策服务结果前缀 */
    public static final String DH_GATEWAY_ROUTE_RESULT = "route_result:";
    
	/**
	 * 审批意见前缀
	 */
	public static final String DH_APPROVAL_OPINION = "apr_idea:";
	/** 文件标识前缀*/
	public static final String DH_INSTANCE_FILE_CARD = "file_card:";
    /** 简单循环任务的任务处理人 */
	public static final String DH_TASK_HANDLER = "task_handler:";
	/** triggerMQ消费者执行出错异常类 */
	public static final String DH_TRIGGER_EXCEPTION = "trigger_exception:";

    /** 同步任务重试记录 */
	public static final String DH_SYN_TASK_RETRY = "syn_task_retry:";
	
	/** 流程检索字段前缀 **/
	public static final String DH_PROCESS_RETRIEVE = "process_retrieve:";
	
	/** 通知模板前缀 **/
	public static final String DH_NOTIFY_TEMPLATE = "notify_template:";

    /**
     * 规则条件前缀
     */
	public static final String DAT_RULE_CONDITION = "rule_cond:";

    /**
     * 表单字段与接口参数映射表前缀
     */
    public static final String DH_TRIGGER_INTERFACE = "triggerInterface:";

    /**
     * 触发器调用记录前缀
     */
    public static final String DH_TRIGGER_INVOKE_RECORD = "tri_invoke:";

    /** 接口调用日志 */
    public static final String DH_INTERFACE_LOG = "inter_log:";

}
