/**
 * 
 */
package com.desmart.desmartbpm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.DhTriggerInterface;
import com.desmart.desmartbpm.service.DhTriggerInterfaceService;

/**  
* <p>Title: DhTriggerInterfaceController</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年6月13日  
*/
@Controller
@RequestMapping(value = "/dhTriggerInterface")
public class DhTriggerInterfaceController {
	
	@Autowired
	private DhTriggerInterfaceService dhTriggerInterfaceService;
	
	@RequestMapping(value = "/insertBatch")
	@ResponseBody
	public ServerResponse insertBatch(@RequestBody List<DhTriggerInterface> dhTriggerInterfaceList) {
		List<DhTriggerInterface> tinList = new ArrayList<>();
		for (DhTriggerInterface dhTriggerInterface : dhTriggerInterfaceList) {
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		dhTriggerInterface.setTinUid(DhTriggerInterface.DH_TRIGGER_INTERFACE + UUID.randomUUID());
		dhTriggerInterface.setCreator(creator);
		tinList.add(dhTriggerInterface);
		}
		return dhTriggerInterfaceService.insertBatch(tinList);
	}
	
	@RequestMapping(value = "/selectTriggerAndForm")
	@ResponseBody
	public ServerResponse selectByTriggerAndForm(DhTriggerInterface dhTriggerInterface) {
		return dhTriggerInterfaceService.selectByTriggerActivity(dhTriggerInterface);
	}
	
	@RequestMapping(value = "/updateBatch")
	@ResponseBody
	public ServerResponse updateBatch(@RequestBody List<DhTriggerInterface> dhTriggerInterfaceList) {
		return dhTriggerInterfaceService.updateBatch(dhTriggerInterfaceList);
	}
	
	@RequestMapping(value = "/deleteParamerByStepId")
	@ResponseBody
	private int deleteParamer(String stepUid) {
		List<String> stepUidList = new ArrayList<>();
		stepUidList.add(stepUid);
		return dhTriggerInterfaceService.removeByStepUidList(stepUidList);
	}
}
