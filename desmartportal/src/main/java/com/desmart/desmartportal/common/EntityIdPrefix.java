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
	public static final String DH_AGENT_META = "agent_meta:";
}
