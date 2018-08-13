package com.desmart.desmartsystem.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-08-09
 */
public class SysProfitCenter implements Serializable {

    private static final long serialVersionUID = 1L;

	private String profitCenterNo;
	private String profitCenterName;


	public String getProfitCenterNo() {
		return profitCenterNo;
	}

	public void setProfitCenterNo(String profitCenterNo) {
		this.profitCenterNo = profitCenterNo;
	}

	public String getProfitCenterName() {
		return profitCenterName;
	}

	public void setProfitCenterName(String profitCenterName) {
		this.profitCenterName = profitCenterName;
	}

}
