/**
 * 
 */
package com.desmart.desmartportal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.desmart.desmartbpm.common.HttpReturnStatus;
import com.desmart.desmartbpm.common.ResponseCode;
import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.service.ProcessService;
import com.desmart.desmartportal.util.http.HttpClientUtils;

/**  
* <p>Title: ProcessServiceImpl</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Service
public class ProcessServiceImpl implements ProcessService {
	
	private Logger log = Logger.getLogger(ProcessServiceImpl.class);
	
	/**
	 * 发起流程
	 */
	@Override
	public ServerResponse startProcess(String proUid, String proAppId, String verUid) {
		log.info("发起流程开始......");
		HttpReturnStatus result = new HttpReturnStatus();
		// 判断
		if("".equals(proUid) && "".equals(proAppId) && "".equals(verUid)) {
			return ServerResponse.createByError();
		}
		Map<String,Object> params = new HashMap<>();
		params.put("snapshotId", verUid);
		params.put("processAppId", proAppId);
		params.put("action", "start");
		params.put("bpdId", proUid);
		// 掉用API 发起一个流程
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		result = httpClientUtils.checkApiLogin("post", "http://10.0.4.201:9080/rest/bpm/wle/v1/process", params);
		log.info("掉用API状态码:"+result.getCode());		
		log.info("发起流程结束......");
		if(result.getCode()==200) {
			return ServerResponse.createBySuccess();
		}else {
			return ServerResponse.createByError();
		}
	}

}
