package com.desmart.desmartbpm.common;

import com.desmart.desmartbpm.entity.BpmForm;
import com.desmart.desmartbpm.entity.BpmFormField;

/**
 * 存储修改表单信息，及内容的json对象
 * @author loser_wu
 *
 */
public class FormJSONObject {
	private BpmForm form;
	
	private BpmFormField[] formFieldArr;
	
	private String[] publicFormUidArr;

	public BpmForm getForm() {
		return form;
	}

	public void setForm(BpmForm form) {
		this.form = form;
	}

	public BpmFormField[] getFormFieldArr() {
		return formFieldArr;
	}

	public void setFormFieldArr(BpmFormField[] formFieldArr) {
		this.formFieldArr = formFieldArr;
	}

	public String[] getPublicFormUidArr() {
		return publicFormUidArr;
	}

	public void setPublicFormUidArr(String[] publicFormUidArr) {
		this.publicFormUidArr = publicFormUidArr;
	}
}
