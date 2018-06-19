package com.desmart.desmartportal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.desmart.desmartbpm.controller.BpmActivityMetaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartbpm.service.BpmActivityMetaService;
import com.desmart.desmartportal.entity.DhRoutingRecord;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhRoutingRecordService;
import com.desmart.desmartportal.service.DhTaskInstanceService;

@Controller
@RequestMapping("/dhRoutingRecord")
public class DhRoutingRecordController {
	private static final Logger log = LoggerFactory.getLogger(DhRoutingRecordController.class);

	@Autowired
	private DhRoutingRecordService dhRoutingRecordServiceImpl;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaServiceImpl;
	@Autowired
	private DhTaskInstanceService dhTaskInstanceServiceImpl;
	
	@RequestMapping("/loadDhRoutingRecords.do")
	@ResponseBody
	public ServerResponse loadDhRoutingRecords(@RequestParam String insUid) {
		try {
			return dhRoutingRecordServiceImpl.loadDhRoutingRecords(insUid);
		} catch (Exception e) {
			log.error("读取流转信息失败", e);
			return ServerResponse.createByErrorMessage("读取流转信息失败");
		}
	}
}
