package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartsystem.entity.SysUser;

public interface MenusService {
	public List<BpmActivityMeta> activityHandler(String proUid,String proAppId,String verUid);
	
	public List<SysUser> choosableHandler(String activityUid);
}
