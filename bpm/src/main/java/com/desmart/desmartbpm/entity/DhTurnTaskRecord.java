package com.desmart.desmartbpm.entity;

import java.util.Date;

public class DhTurnTaskRecord {
	public final static String OPERATION_TYPE_BATCH = "byBatch";
	public final static String OPERATION_TYPE_ALL = "byAll";
	
	private String turnRecordUid;
	private String sourceUserUid;
	private String targetUserUid;
	private String turnObjectUid;
	private String turnTaskOperation;
	private Date turnTaskDate;
	private String turnTaskCause;
	public String getTurnRecordUid() {
		return turnRecordUid;
	}
	public void setTurnRecordUid(String turnRecordUid) {
		this.turnRecordUid = turnRecordUid;
	}
	public String getSourceUserUid() {
		return sourceUserUid;
	}
	public void setSourceUserUid(String sourceUserUid) {
		this.sourceUserUid = sourceUserUid;
	}
	public String getTargetUserUid() {
		return targetUserUid;
	}
	public void setTargetUserUid(String targetUserUid) {
		this.targetUserUid = targetUserUid;
	}
	public String getTurnObjectUid() {
		return turnObjectUid;
	}
	public void setTurnObjectUid(String turnObjectUid) {
		this.turnObjectUid = turnObjectUid;
	}
	public String getTurnTaskOperation() {
		return turnTaskOperation;
	}
	public void setTurnTaskOperation(String turnTaskOperation) {
		this.turnTaskOperation = turnTaskOperation;
	}
	public Date getTurnTaskDate() {
		return turnTaskDate;
	}
	public void setTurnTaskDate(Date turnTaskDate) {
		this.turnTaskDate = turnTaskDate;
	}
	public String getTurnTaskCause() {
		return turnTaskCause;
	}
	public void setTurnTaskCause(String turnTaskCause) {
		this.turnTaskCause = turnTaskCause;
	}
	@Override
	public String toString() {
		return "DhTurnTaskRecord [turnRecordUid=" + turnRecordUid + ", sourceUserUid=" + sourceUserUid
				+ ", targetUserUid=" + targetUserUid + ", turnObjectUid=" + turnObjectUid + ", turnTaskOperation="
				+ turnTaskOperation + ", turnTaskDate=" + turnTaskDate + ", turnTaskCause=" + turnTaskCause + "]";
	}
	
}
