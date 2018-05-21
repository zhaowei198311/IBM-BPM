/**
 * 
 */
package com.desmart.common.constant;

/**  
* <p>Title: 流转信息 操作类型</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月21日  
*/
public class RouteStatus {
	
	/**
	 * 流转信息 发起流程操作
	 */
	public static final String ROUTE_STARTPROCESS = "startProcess";
	
	/**
	 * 流转信息 提交任务
	 */
	public static final String ROUTE_SUBMITTASK = "submitTask";
	
	/**
	 * 流转信息 取回任务
	 */
	public static final String R0UTE_RETAKETASK = "retakeTask";
	
	/**
	 * 流转信息 传阅
	 */
	public static final String ROUTE_TRANSFERTASK = "transferTask";
	
	/**
	 * 流传信息 驳回
	 */
	public static final String ROUTE_REJECTTASK = "rejectTask";
	
	/**
	 * 流转信息 加签
	 */
	public static final String ROUTE_ADDTASK = "addTask";
	
	/**
	 * 流转信息 加签完成
	 */
	public static final String ROUTE_FINISHADDTASK = "finishAddTask";
}
