/**
 * 
 */
package com.desmart.desmartbpm.entity;

/**  
* <p>Title: 表单子表单关联实体类</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年6月20日  
*/
public class BpmFormRelePublicForm {
	
	private String formUid; // 表单id
	
	private String publicFormUid; // 子表单id

	/**
	 * @return the formUid
	 */
	public String getFormUid() {
		return formUid;
	}

	/**
	 * @param formUid the formUid to set
	 */
	public void setFormUid(String formUid) {
		this.formUid = formUid;
	}

	/**
	 * @return the publicFormUid
	 */
	public String getPublicFormUid() {
		return publicFormUid;
	}

	/**
	 * @param publicFormUid the publicFormUid to set
	 */
	public void setPublicFormUid(String publicFormUid) {
		this.publicFormUid = publicFormUid;
	}
	
	public BpmFormRelePublicForm() {
		
	}

	/**
	 * @param formUid
	 * @param publicFormUid
	 */
	public BpmFormRelePublicForm(String formUid, String publicFormUid) {
		super();
		this.formUid = formUid;
		this.publicFormUid = publicFormUid;
	}

	@Override
	public String toString() {
		return "BpmFormRelePublicForm [formUid=" + formUid + ", publicFormUid=" + publicFormUid + "]";
	}
	
}
