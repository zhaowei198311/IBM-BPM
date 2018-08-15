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

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartportal.entity.DhDrafts;
import com.desmart.desmartportal.service.DhDraftsService;

/**  
* <p>Title: 草稿箱控制层</p>  
* <p>Description: </p>  
* @author 赵伟  
* @date 2018年4月28日  
* 
*/
@Controller
@RequestMapping(value = "/drafts")
public class DhDraftsController {
	private Logger Log = Logger.getLogger(DhDraftsController.class);

	@Autowired
	private DhDraftsService dhDraftsService;

	@RequestMapping(value = "/index")
	public String index() {
		return "desmartportal/drafts";
	}
	
	@RequestMapping(value = "/queryDraftsByList")
	@ResponseBody
	public ServerResponse queryDraftsByList(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize", defaultValue="10")Integer pageSize,
			@RequestParam("insTitle")String insTitle,
			@RequestParam("proName")String proName) {
		return dhDraftsService.selectDraftsList(pageNum, pageSize, insTitle, proName);
	}
	
	@RequestMapping(value = "/deleteDraftsById")
	@ResponseBody
	public void deleteDraftsById(@RequestParam(value="dfsId")String dfsId) {
		dhDraftsService.deleteDraftsBydfsId(dfsId);
	}
	
	@RequestMapping(value = "/saveDrafts")
	@ResponseBody
	public int saveDrafts(DhDrafts drafts) {
		return dhDraftsService.saveDrafts(drafts);
	}
	
	@RequestMapping(value = "/selectBydfsId")
	@ResponseBody
	public DhDrafts selectBydfsId(@RequestParam(value="dfsId")String dfsId) {
		return dhDraftsService.selectBydfsId(dfsId);
	}
	
	@RequestMapping(value="/saveIfNotExists")
	@ResponseBody
	public ServerResponse saveIfNotExists(DhDrafts dhDrafts) {
		return dhDraftsService.saveIfNotExists(dhDrafts);
	}


	/**
	 * 保存起草草稿
	 * @return
	 */
	@RequestMapping(value = "/saveProcessDraft")
	@ResponseBody
	public ServerResponse saveProcessDraft(DhDrafts dhDrafts) {
		return dhDraftsService.saveProcessDraft(dhDrafts);
	}

	/**
	 * 保存待办草稿
	 * @return
	 */
	@RequestMapping(value = "/saveTaskDraft")
	@ResponseBody
	public ServerResponse saveTaskDraft(DhDrafts dhDraft) {
		return dhDraftsService.saveTaskDraft(dhDraft);
	}

	/**
	 * 查看发起流程的草稿是否还有效
	 * @return
	 */
	@RequestMapping(value = "/checkProcessDraftStatus")
	@ResponseBody
	public ServerResponse checkProcessDraftStatus(String dfsId) {
		return dhDraftsService.checkProcessDraftStatus(dfsId);
	}

}
