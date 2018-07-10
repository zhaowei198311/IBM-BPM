package com.desmart.common.constant;

import java.io.Serializable;
/**
 * 把exccel的实体类字段的注解存放到一个自定义类
 * @author lys
 *
 */
public class ExcelHelper implements Serializable {

	private String name;
	private String fieldName;
	private int sort;
	private boolean nullable;
	private String regexp;
	private String format;
	private String falseName;
	private String trueName;
	private Integer scale;
	private Class<?> clazz;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getFalseName() {
		return falseName;
	}
	public void setFalseName(String falseName) {
		this.falseName = falseName;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	@Override
	public String toString() {
		return "ExcelHelper [name=" + name + ", fieldName=" + fieldName + ", sort=" + sort + ", nullable=" + nullable
				+ ", regexp=" + regexp + ", format=" + format + ", falseName=" + falseName + ", trueName=" + trueName
				+ ", scale=" + scale + ", clazz=" + clazz + "]";
	}
	
}