package com.desmart.desmartsystem.controller;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
import com.desmart.desmartsystem.util.UUIDTool;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  全局变量配置
 * </p>
 *
 * @author 张志颖
 * @since 2018-05-8
 */
@Controller
@RequestMapping("/globalConfig")
public class SysGlobalConfigController {
	@Autowired
	private BpmGlobalConfigService systemGlobalConfigService;
	
	@RequestMapping(value="/globalConfig")
	public String globalConfig(){
		return "desmartsystem/usermanagement/globalConfig";
	}
	
	
	@RequestMapping(value="/allGlobalConfig")
	@ResponseBody
	public ServerResponse allGlobalConfig(@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,@RequestParam(value="pageSize", defaultValue="10")Integer pageSize){
		ServerResponse<PageInfo<List<BpmGlobalConfig>>> queryByPage=systemGlobalConfigService.getFirstActConfig(pageNum, pageSize);
		return queryByPage; 
	}
	
	@RequestMapping(value = "/saveGlobalConfig")
	@ResponseBody
	public String saveAgent(BpmGlobalConfig sysGlobalConfig) {
		try {	
			//BpmGlobalConfig sysGlobalConfig = new BpmGlobalConfig();
			/*sysGlobalConfig.setBpmAdminName(bpmAdminName);
			sysGlobalConfig.setBpmAdminPsw(bpmAdminPsw);
			sysGlobalConfig.setBpmClientTimeout(Integer.valueOf(bpmClientTimeout));
			sysGlobalConfig.setBpmServerHost(bpmServerHost);
			sysGlobalConfig.setBpmformsHost(bpmformsHost);
			sysGlobalConfig.setBpmformsWebContext(bpmformsWebContext);
			sysGlobalConfig.setConfigName(configName);*/
			sysGlobalConfig.setConfigStatus("on");
			sysGlobalConfig.setUpdateTime(new Timestamp(new Date().getTime()));
			/*sysGlobalConfig.setSftpPath(sftpPath);
			sysGlobalConfig.setSftpUserName(sftpUserName);
			sysGlobalConfig.setSftpPassword(sftpPassword);
			sysGlobalConfig.setSftpIp(sftpIp);
			sysGlobalConfig.setFileFormat(fileFormat);*/
			if(sysGlobalConfig.getConfigId() == null || sysGlobalConfig.getConfigId() == "") {
				sysGlobalConfig.setConfigId("bpm_gcfg:"+UUIDTool.getUUID());
				systemGlobalConfigService.insert(sysGlobalConfig);
			}else {
				systemGlobalConfigService.updateByPrimaryKeySelective(sysGlobalConfig);
			}
			return "{\"msg\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"error\"}";
		}
	}
	
}
