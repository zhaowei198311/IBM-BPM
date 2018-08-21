package com.desmart.desmartportal.entity;

/**
 * 封装的用户代理信息对象
 * @author loser_wu
 * @since 2018年8月21日
 */
public class DhUserAgent {
	private String userUid;
	private String delegateUserUid;
	private String agentId;
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public String getDelegateUserUid() {
		return delegateUserUid;
	}
	public void setDelegateUserUid(String delegateUserUid) {
		this.delegateUserUid = delegateUserUid;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public DhUserAgent() {}
	public DhUserAgent(String userUid, String delegateUserUid, String agentId) {
		super();
		this.userUid = userUid;
		this.delegateUserUid = delegateUserUid;
		this.agentId = agentId;
	}
}
