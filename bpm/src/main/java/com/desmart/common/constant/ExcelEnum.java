package com.desmart.common.constant;
/**
 * 布尔类字段输出到excel的内容使用枚举型
 * @author lys
 *
 */
public enum ExcelEnum {

	TRUE("是"), FALSE("否");

	public  String name;

	ExcelEnum(String name) {
	this.name = name;
	}

	@Override
	public String toString() {
	return this.name;
	}
}
