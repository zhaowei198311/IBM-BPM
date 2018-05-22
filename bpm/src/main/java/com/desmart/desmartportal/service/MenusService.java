package com.desmart.desmartportal.service;

import java.util.List;

import com.desmart.desmartbpm.entity.BpmActivityMeta;
import com.desmart.desmartsystem.entity.SysUser;

public interface MenusService {
	
	
	 /**
     * 获取下一环节默认处理人
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
	public List<BpmActivityMeta> activityHandler(String proUid,String proAppId,String verUid);
	
	
	/**
     * 获取代办任务环节处理人
     * @param proAppId
     * @param proUid
     * @param proVerUid
     * @return
     */
	public List<BpmActivityMeta> backlogActivityHandler(BpmActivityMeta bpmActivityMeta);
	
	 /**
     * 获取环节可选处理人
     * @param activityUid
     * @return
     */
	public List<SysUser> choosableHandler(String activityUid);
}
