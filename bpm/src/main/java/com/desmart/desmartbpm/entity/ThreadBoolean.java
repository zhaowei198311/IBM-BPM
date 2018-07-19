package com.desmart.desmartbpm.entity;

/**
 * 线程安全的boolean值
 */
public class ThreadBoolean {
	private static ThreadLocal<Boolean> tl = new ThreadLocal<Boolean>(){
		@Override
		public Boolean initialValue(){
			return false;
		}
	};
	
	public void setTrue(){
		tl.set(true);
	}

	public void setFalse() {
		tl.set(false);
	}

	public boolean getValue() {
		return tl.get();
	}


}
