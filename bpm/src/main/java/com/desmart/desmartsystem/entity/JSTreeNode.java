package com.desmart.desmartsystem.entity;

public class JSTreeNode {
	private String id;
	private String text;
	private Object children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Object getChildren() {
		return children;
	}
	public void setChildren(Object children) {
		this.children = children;
	}
	public JSTreeNode() {}
	public JSTreeNode(String id, String text, Object children) {
		super();
		this.id = id;
		this.text = text;
		this.children = children;
	}
}
