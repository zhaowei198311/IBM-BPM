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
import com.desmart.desmartportal.entity.Drafts;
import com.desmart.desmartportal.service.DraftsService;

/**  
* <p>Title: 草稿箱控制层</p>  
* <p>Description: </p>  
* @author 赵伟  
* @date 2018年4月28日  
* 
*/
@Controller
@RequestMapping(value = "/drafts")
public class DraftsController {
	
	@Autowired
	private DraftsService draftsService;
	
	private Logger Log = Logger.getLogger(DraftsController.class);
	
	@RequestMapping(value = "/index")
	public String index() {
		return "drafts";
	}
	
	@RequestMapping(value = "/queryDraftsByList")
	@ResponseBody
	public ServerResponse queryDraftsByList(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize) {
		return draftsService.selectDraftsList(pageNum, pageSize);
	}
	
	@RequestMapping(value = "/deleteDraftsById")
	@ResponseBody
	public void deleteDraftsById(@RequestParam(value="dfsId")String dfsId) {
		draftsService.deleteDraftsBydfsId(dfsId);
	}
	
	@RequestMapping(value = "/queryDraftsByTitle")
	@ResponseBody
	public ServerResponse queryDraftsByTitle(@RequestParam(value="dfsTitle")String dfsTitle,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		return draftsService.selectDraftsBydfsTitle(dfsTitle, pageNum, pageSize);
	}
}
