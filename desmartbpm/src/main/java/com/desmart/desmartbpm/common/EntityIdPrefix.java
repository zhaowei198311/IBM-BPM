package com.desmart.desmartbpm.common;

import java.util.UUID;

/**
 * 实体类主键生成规则的前缀
 * 一般规则：前缀 + uuid
 */
public class EntityIdPrefix {
	/** 环节前缀 */
	public static final String BPM_ACTIVITY_META = "act_meta:";
	
	/** 流程定义分类前缀 */
	public static final String BPM_PROCESS_CATEGORY = "pro_cate:";
	
	/** 流程元数据前缀 */
	public static final String DH_PROCESS_META = "pro_meta:";
	
	/** 接口数据前缀 */
	public static final String DH_INTERFACE_META = "int_meta:";
	
	/** 接口参数前缀 */
	public static final String DH_INTERFACE_PARAMETER = "para_meta:";

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

}
