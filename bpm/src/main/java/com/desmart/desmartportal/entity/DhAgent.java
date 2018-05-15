/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.util.Date;

/**  
* <p>Title: 代理设置实体类</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月8日  
*/
public class DhAgent {

	private String agentId; // 代理ID
	
	private Date agentOdate; // 代理设置时间
	
	private String agentOperator; // 代理设置人
	
	private String agentClientele; // 代理委托人
	
	private Date agentSdate; // 开始时间
	
	private Date agentEdate; // 结束时间
	
	private String agentStatus; // 代理状态：ENABLED；DISABLED

	public DhAgent(String agentId, Date agentOdate, String agentOperator, String agentClientele, Date agentSdate,
			Date agentEdate, String agentStatus) {
		super();
		this.agentId = agentId;
		this.agentOdate = agentOdate;
		this.agentOperator = agentOperator;
		this.agentClientele = agentClientele;
		this.agentSdate = agentSdate;
		this.agentEdate = agentEdate;
		this.agentStatus = agentStatus;
	}

	public DhAgent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Date getAgentOdate() {
		return agentOdate;
	}

	public void setAgentOdate(Date agentOdate) {
		this.agentOdate = agentOdate;
	}

	public String getAgentOperator() {
		return agentOperator;
	}

	public void setAgentOperator(String agentOperator) {
		this.agentOperator = agentOperator;
	}

	public String getAgentClientele() {
		return agentClientele;
	}

	public void setAgentClientele(String agentClientele) {
		this.agentClientele = agentClientele;
	}

	public Date getAgentSdate() {
		return agentSdate;
	}

	public void setAgentSdate(Date agentSdate) {
		this.agentSdate = agentSdate;
	}

	public Date getAgentEdate() {
		return agentEdate;
	}

	public void setAgentEdate(Date agentEdate) {
		this.agentEdate = agentEdate;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

}
