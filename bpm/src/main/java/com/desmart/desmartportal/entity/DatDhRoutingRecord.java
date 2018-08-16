package com.desmart.desmartportal.entity;
/**
 * 流转信息展示类
 * @author lys
 *
 */

import java.util.List;

import com.desmart.desmartbpm.entity.BpmActivityMeta;

public class DatDhRoutingRecord {
	private BpmActivityMeta bpmActivityMeta;//流程环节
	private List<DhTaskInstance> dhTaskInstanceList;//保存环节对应的任务信息
	public BpmActivityMeta getBpmActivityMeta() {
		return bpmActivityMeta;
	}
	public void setBpmActivityMeta(BpmActivityMeta bpmActivityMeta) {
		this.bpmActivityMeta = bpmActivityMeta;
	}
	public List<DhTaskInstance> getDhTaskInstanceList() {
		return dhTaskInstanceList;
	}
	public void setDhTaskInstanceList(List<DhTaskInstance> dhTaskInstanceList) {
		this.dhTaskInstanceList = dhTaskInstanceList;
	}
	
}
