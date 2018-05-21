package com.desmart.desmartportal.common;

import java.util.UUID;

/**
 * 实体类主键生成规则的前缀
 * 一般规则：前缀 + uuid
 */
public class EntityIdPrefix {
	
	/** 接口数据前缀 */
	public static final String DH_INTERFACE_META = "int_meta:";
	
	/** 接口参数前缀 */
	public static final String DH_INTERFACE_PARAMETER = "para_meta:";
	
	/** 草稿参数前缀 */
	public static final String DH_DRAFTS_META = "draft_meta:";
	
	/** 代理参数前缀 */
	public static final String DH_AGENT_INFO = "agent_info:";
	
	/** 代理流程参数前缀 */
	public static final String DH_AGENT_PRO_INFO = "agent_pro_info:";
	
	/** 流程实例参数前缀 */
	public static final String DH_PROCESS_INSTANCE = "process_instance:";
	
	/** 任务实例参数前缀 */
	public static final String DH_TASK_INSTANCE = "task_instance:";
	/**
	 * 流程附件前缀
	 */
	public static final String DH_INSTANCE_DOCUMENT = "docid:";
	/**
	 * 审批意见前缀
	 */
	public static final String DH_APPROVAL_OPINION = "apr_idea:";
}
