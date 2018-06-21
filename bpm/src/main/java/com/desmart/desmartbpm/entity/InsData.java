package com.desmart.desmartbpm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class InsData {
	@Id
	private String insUid;
	
	private String processData;
	
	private String formData;
	
	public InsData(){
		
	}
	
	@PersistenceConstructor
	public InsData(String insUid, String processData, String formData) {
		super();
		this.insUid = insUid;
		this.processData = processData;
		this.formData = formData;
	}

	public String getInsUid() {
		return insUid;
	}

	public void setInsUid(String insUid) {
		this.insUid = insUid;
	}

	public String getProcessData() {
		return processData;
	}

	public void setProcessData(String processData) {
		this.processData = processData;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}
	
}
