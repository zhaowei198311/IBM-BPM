package com.desmart.desmartsystem.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @功能说明：表单对象
 * @作者： herun
 * @创建日期：2015-09-19
 * @版本号：V1.0
 */
public class DatagridForLayUI implements java.io.Serializable {

	private static final long serialVersionUID = 4137170628914512450L;

	private Long count = 0L;
	private String code = "";
	private String msg = "";
	private List data = new ArrayList();

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

}
