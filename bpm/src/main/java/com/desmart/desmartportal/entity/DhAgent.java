/**
 * 
 */
package com.desmart.desmartportal.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.desmart.desmartbpm.entity.DhProcessMeta;

/**  
* <p>Title: 代理设置实体类</p>  
* <p>Description: </p>  
* @author 张志颖  
* @date 2018年5月8日  
*/
public class DhAgent {

	private String agentId; // 代理ID
	
	private Date agentOdate; // 代理设置时间
	
	private String agentOperator; // 委托人
	
	private String agentClientele; // 代理人
	
	private Date agentSdate; // 开始时间
	
	private Date agentEdate; // 结束时间
	
	private String agentStatus; // 代理状态：ENABLED；DISABLED
	
	private String agentIsAll;//是否代理了所有流程

	private List<DhProcessMeta> dhProcessMetaList;//代理的流程名集合 用于显示
	
	private String agentClienteleName;//代理人姓名 前台显示用
	
	public DhAgent() {
		super();
	}

	public DhAgent(String agentId, Date agentOdate, String agentOperator, String agentClientele, Date agentSdate,
			Date agentEdate, String agentStatus, String agentIsAll, List<DhProcessMeta> dhProcessMetaList,
			String agentClienteleName) {
		super();
		this.agentId = agentId;
		this.agentOdate = agentOdate;
		this.agentOperator = agentOperator;
		this.agentClientele = agentClientele;
		this.agentSdate = agentSdate;
		this.agentEdate = agentEdate;
		this.agentStatus = agentStatus;
		this.agentIsAll = agentIsAll;
		this.dhProcessMetaList = dhProcessMetaList;
		this.agentClienteleName = agentClienteleName;
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

	public void setAgentOdate(Timestamp agentOdate) {
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

	public void setAgentSdate(Timestamp agentSdate) {
		this.agentSdate = agentSdate;
	}

	public Date getAgentEdate() {
		return agentEdate;
	}

	public void setAgentEdate(Timestamp agentEdate) {
		this.agentEdate = agentEdate;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

	public String getAgentIsAll() {
		return agentIsAll;
	}

	public void setAgentIsAll(String agentIsAll) {
		this.agentIsAll = agentIsAll;
	}

	public List<DhProcessMeta> getDhProcessMetaList() {
		return dhProcessMetaList;
	}

	public void setDhProcessMetaList(List<DhProcessMeta> dhProcessMetaList) {
		this.dhProcessMetaList = dhProcessMetaList;
	}

	public String getAgentClienteleName() {
		return agentClienteleName;
	}

	public void setAgentClienteleName(String agentClienteleName) {
		this.agentClienteleName = agentClienteleName;
	}
}
