package com.desmart.desmartbpm.common;

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

}
