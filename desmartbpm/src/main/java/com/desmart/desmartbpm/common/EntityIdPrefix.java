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
	
	/** 对象权限 */
	public static final String DH_OBJECT_PERMISSION = "obj_perm:";

}
