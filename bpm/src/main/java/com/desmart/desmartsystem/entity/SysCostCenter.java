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
public class SysCostCenter implements Serializable {

    private static final long serialVersionUID = 1L;

	private String costCenterNo;
	private String costCenterName;
	private String profitCenterNo;


	public String getCostCenterNo() {
		return costCenterNo;
	}

	public void setCostCenterNo(String costCenterNo) {
		this.costCenterNo = costCenterNo;
	}

	public String getCostCenterName() {
		return costCenterName;
	}

	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}

	public String getProfitCenterNo() {
		return profitCenterNo;
	}

	public void setProfitCenterNo(String profitCenterNo) {
		this.profitCenterNo = profitCenterNo;
	}

}
