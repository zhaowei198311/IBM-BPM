/**
 * 
 */
package com.desmart.common.constant;

/**  
* <p>Title: IBM流程API</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月17日  
*/
public class IBMApiUrl {
	
	/**
	 * 任务公共api 后面跟/是传了 任务实例id tkkid IBM引擎
	 */
	public static final String IBM_API_TASK = "http://10.0.4.201:9080/rest/bpm/wle/v1/task/";
	
	/**
	 * 流程公共api
	 */
	public static final String IBM_API_PROCESS = "http://10.0.4.201:9080/rest/bpm/wle/v1/process";
	
	/**
	 * 检索模型api
	 */
	public static final String IBM_API_PROCESSMODEL = "http://10.0.4.201:9080/rest/bpm/wle/v1/visual/processModel/instances";
}
