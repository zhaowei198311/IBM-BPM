package com.desmart.desmartsystem.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-06-26
 */
public class DhInterfaceLog implements Serializable {

    private static final long serialVersionUID = 1L;

	private String dilUid;
	private String intUid;
	private String dilRequest;
	private String dilResponse;
	private String dilErrormsg;
	private String createuser;
	private Date createdate;


	public String getDilUid() {
		return dilUid;
	}

	public void setDilUid(String dilUid) {
		this.dilUid = dilUid;
	}


	public String getIntUid() {
		return intUid;
	}

	public void setIntUid(String intUid) {
		this.intUid = intUid;
	}

	public String getDilRequest() {
		return dilRequest;
	}

	public void setDilRequest(String dilRequest) {
		this.dilRequest = dilRequest;
	}

	public String getDilResponse() {
		return dilResponse;
	}

	public void setDilResponse(String dilResponse) {
		this.dilResponse = dilResponse;
	}

	public String getDilErrormsg() {
		return dilErrormsg;
	}

	public void setDilErrormsg(String dilErrormsg) {
		this.dilErrormsg = dilErrormsg;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	@Override
	public String toString() {
		return "DhInterfaceLog{" +
			"dilUid=" + dilUid +
			", intUid=" + intUid +
			", dilRequest=" + dilRequest +
			", dilResponse=" + dilResponse +
			", dilErrormsg=" + dilErrormsg +
			", createuser=" + createuser +
			", createdate=" + createdate +
			"}";
	}
}
