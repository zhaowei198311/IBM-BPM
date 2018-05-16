/**
 * 
 */
package com.desmart.desmartportal.common;

/**  
* <p>Title: BpmStatus</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年5月14日  
*/
public class BpmStatus {
	
	/**
	 * 流程实例状态
	 */
	
	// 流程运转中
	public static final String PROCESS_STATUS_ACTIVE = "1";
	
	// 流程完成
	public static final String PROCESS_STATUS_COMPLETED = "2";
	
	// 流程失败
	public static final String PROCESS_STATUS_FAILED = "3";
	
	// 流程终止
	public static final String PROCESS_STATUS_TERMINATED = "4";
	
	// 流程未启动
	public static final String PROCESS_STATUS_DID_NOT_START = "5";
	
	// 流程暂停
	public static final String PROCESS_STATUS_SUSPENDED = "6";
	
	
	/**
	 * 任务实例状态
	 */
	
	// 新任务
	public static final String 	TASK_STATUS_NEW = "11";
	
	// 接收到任务
	public static final String 	TASK_STATUS_RECEIVED = "12";
	
	// 回复任务
	public static final String 	TASK_STATUS_REPLIED = "13";
	
	// 转发任务
	public static final String 	TASK_STATUS_FORWARDED = "14";
	
	// 传送任务
	public static final String 	TASK_STATUS_SENT = "21";
	
	// 
	public static final String 	TASK_STATUS_ACTIONED = "31";
	
	// 任务关闭
	public static final String 	TASK_STATUS_CLOSED = "32";
	
	//
	public static final String 	TASK_STATUS_SPECIAL = "41";
	
	// 删除任务
	public static final String 	TASK_STATUS_DELETED = "91";
	
	// 警报任务
	public static final String 	TASK_STATUS_ALERT = "61";
	
	// 帮助请求任务
	public static final String 	TASK_STATUS_HELP_REQUEST = "62";
	
	// 说明任务
	public static final String 	TASK_STATUS_COMMENT = "63";
	
	// 已应答帮助请求任务
	public static final String 	TASK_STATUS_ANSWERED_HELP_REQUEST = "65";
	
	// 忽略帮助请求任务
	public static final String 	TASK_STATUS_IGNORED_HELP_REQUEST = "66";
	
	// 继承任务
	public static final String 	TASK_STATUS_FOLLOWED = "70";
	
	// 标记任务
	public static final String 	TASK_STATUS_TAGGED = "71";
	
	// 
	public static final String 	TASK_STATUS_COLLABORATION = "72";
	
	/**
	 * 环节
	 */
	public static final String 	ACTIVITY_TYPE_START = "start";
}
