package com.desmart.desmartsystem.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsf
 * @since 2018-05-24
 */
public class SysDictTp implements Serializable {

    private static final long serialVersionUID = 1L;

	private String dictTypeCd;
	private String dictTypeNm;


	public String getDictTypeCd() {
		return dictTypeCd;
	}

	public void setDictTypeCd(String dictTypeCd) {
		this.dictTypeCd = dictTypeCd;
	}

	public String getDictTypeNm() {
		return dictTypeNm;
	}

	public void setDictTypeNm(String dictTypeNm) {
		this.dictTypeNm = dictTypeNm;
	}

}
