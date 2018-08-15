package com.desmart.common.excelForm;

import com.desmart.common.annotation.excel.ExcelField;

/**
 * 数据字典Excel导入类
 * @author lys
 *
 */
public class SysDictionaryDataForm {
	@ExcelField(name="字典代码",sort=1,nullable=false)
	private String dicDataCode;
	@ExcelField(name="字典名称",sort=2,nullable=false)
	private String dicDataName;
	@ExcelField(name="字典类型代码",sort=3,nullable=false)
	private String dicCode;
	/*@ExcelField(name="字典说明",sort=4,nullable=true)
	private String dicDataDescription;*/
	@ExcelField(name="排序号",sort=0,nullable=false)
	private Double dicDataSort;
	public String getDicDataCode() {
		return dicDataCode;
	}
	public void setDicDataCode(String dicDataCode) {
		this.dicDataCode = dicDataCode;
	}
	public String getDicDataName() {
		return dicDataName;
	}
	public void setDicDataName(String dicDataName) {
		this.dicDataName = dicDataName;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	/*public String getDicDataDescription() {
		return dicDataDescription;
	}
	public void setDicDataDescription(String dicDataDescription) {
		this.dicDataDescription = dicDataDescription;
	}*/
	public Double getDicDataSort() {
		return dicDataSort;
	}
	public void setDicDataSort(Double dicDataSort) {
		this.dicDataSort = dicDataSort;
	}
	
}
