/**
 * 
 */
package com.desmart.desmartportal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.desmartportal.common.ServerResponse;
import com.desmart.desmartportal.service.DhProcessService;
import com.desmart.desmartportal.util.http.HttpClientUtils;

/**  
* <p>Title: 流程controller</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月8日  
*/
@Controller
@RequestMapping(value = "/process")
public class DhProcessController {
	
	private Logger log = Logger.getLogger(DhProcessController.class);
	
	@Autowired
	private DhProcessService dhProcessService;
	
	@RequestMapping(value = "/startProcess")
	@ResponseBody
	public ServerResponse startProcess(@RequestParam(value="proUid")String proUid, @RequestParam(value="proAppId")String proAppId, 
							@RequestParam(value="verUid")String verUid, @RequestParam(value="dataInfo")String dataInfo,
							@RequestParam(value="approval")String approval) {
		// 发起流程		
		try {
            return dhProcessService.startProcess(proUid, proAppId, verUid, dataInfo, approval);
        } catch (Exception e) {
            log.error("发起流程失败", e);
            return ServerResponse.createByErrorMessage("发起流程失败");
        }
	}
	
	@RequestMapping(value = "/queryProcessByUser")
	@ResponseBody
	public ServerResponse queryProcessByUser() {
		
		return null;	
	}
	
	@RequestMapping(value = "/viewProcess")
	@ResponseBody
	public String viewProcess(String insId) {
		// 查看流程图 需要流程实例  这一步目的是 去 掉用IBM 做一次 登陆验证
		HttpClientUtils httpUtils = new HttpClientUtils();
		String url = "http://10.0.4.201:9080/teamworks/executecf?modelID=1.36bdcc65-8d6a-4635-85cf-57cab68a7e45&branchID=2063.34a0ce6e-631b-465d-b0dc-414c39fb893f&tw.local.processInstanceId="+insId;
		httpUtils.checkApiLogin("get", url, null);
		return url;
	}
	
	///
}
