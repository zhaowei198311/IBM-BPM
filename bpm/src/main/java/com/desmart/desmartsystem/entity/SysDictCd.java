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
public class SysDictCd implements Serializable {

    private static final long serialVersionUID = 1L;

	private String dcUid;
	private String dictCd;
	private String dictNm;
	private String dictTypeCd;


	public String getDcUid() {
		return dcUid;
	}

	public void setDcUid(String dcUid) {
		this.dcUid = dcUid;
	}

	public String getDictCd() {
		return dictCd;
	}

	public void setDictCd(String dictCd) {
		this.dictCd = dictCd;
	}

	public String getDictNm() {
		return dictNm;
	}

	public void setDictNm(String dictNm) {
		this.dictNm = dictNm;
	}

	public String getDictTypeCd() {
		return dictTypeCd;
	}

	public void setDictTypeCd(String dictTypeCd) {
		this.dictTypeCd = dictTypeCd;
	}


}
