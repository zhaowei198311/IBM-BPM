package com.desmart.desmartportal.entity;

/**
 * 代理的流程信息
 * @author loser_wu 
 * @since 2018-05-16
 */
public class DhAgentProInfo {
	private String agentProInfoId;
	private String agentId;//代理ID
	private String proAppId;
	private String proUid;
	public String getAgentProInfoId() {
		return agentProInfoId;
	}
	public void setAgentProInfoId(String agentProInfoId) {
		this.agentProInfoId = agentProInfoId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getProAppId() {
		return proAppId;
	}
	public void setProAppId(String proAppId) {
		this.proAppId = proAppId;
	}
	public String getProUid() {
		return proUid;
	}
	public void setProUid(String proUid) {
		this.proUid = proUid;
	}
	public DhAgentProInfo() {}
	public DhAgentProInfo(String agentProInfoId, String agentId, String proAppId, String proUid) {
		super();
		this.agentProInfoId = agentProInfoId;
		this.agentId = agentId;
		this.proAppId = proAppId;
		this.proUid = proUid;
	}
	@Override
	public String toString() {
		return "DhAgentProInfo [agentProInfoId=" + agentProInfoId + ", agentId=" + agentId + ", proAppId=" + proAppId
				+ ", proUid=" + proUid + "]";
	}
}
