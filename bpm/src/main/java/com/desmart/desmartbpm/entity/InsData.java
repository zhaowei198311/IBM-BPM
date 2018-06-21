package com.desmart.desmartbpm.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
public class InsData {
	@Id
	private String id;
	
	private String processData;
	
	private String formData;
	
	@PersistenceConstructor
	public InsData(String id, String processData, String formData) {
		super();
		this.id = id;
		this.processData = processData;
		this.formData = formData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
