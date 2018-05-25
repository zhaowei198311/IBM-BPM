package com.desmart.desmartsystem.util;

import java.io.Serializable;

public class Json implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msg;//信息

	private boolean success;//是否成功

	private Object obj;//新对象
	
	private Object oldObj;//旧对象
	
	public Object getOldObj() {
		return oldObj;
	}

	public void setOldObj(Object oldObj) {
		this.oldObj = oldObj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	
}
