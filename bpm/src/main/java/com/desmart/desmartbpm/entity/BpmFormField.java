package com.desmart.desmartbpm.entity;

import java.util.List;

/**
 * 表单字段
 * @author loser_wu
 * @since 2018/4/23
 */
public class BpmFormField {
	public static final String TYPE_OBJECT_DATE = "object_date";
	public static final String TYPE_TITLE = "title";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_OBJECT = "object";
	public static final String TYPE_HIDDEN = "hidden";
	public static final String TYPE_OBJECT_NUMBER = "object_number";
	public static final String TYPE_NUMBER = "number";
	public static final String TYPE_OBJECT_TEXT = "object_text";
	public static final String TYPE_UPLOAD = "upload";


	private String fldUid;
	private String fldCodeName;//字段在html文件中的name
	private Integer fldIndex;
	private String fldName;
	private String fldDescription;
	private String fldType;
	private Integer fldSize;
	private String multiSeparator;
	private String multiValue;
	private String fldForeignKeyTable;//参考的业务表
	private String fldDynName;//参考的业务表字段名
	private String fldDynUid;//参考的业务表字段Id
	private String fldFilter;//是否为过滤字段--报表
	private String formUid;
	
	private List<String> opActionList;//字段权限，不是本表字段
	
	public String getFldUid() {
		return fldUid;
	}
	public void setFldUid(String fldUid) {
		this.fldUid = fldUid;
	}
	public String getFldCodeName() {
		return fldCodeName;
	}
	public void setFldCodeName(String fldCodeName) {
		this.fldCodeName = fldCodeName;
	}
	public Integer getFldIndex() {
		return fldIndex;
	}
	public void setFldIndex(Integer fldIndex) {
		this.fldIndex = fldIndex;
	}
	public String getFldName() {
		return fldName;
	}
	public void setFldName(String fldName) {
		this.fldName = fldName;
	}
	public String getFldDescription() {
		return fldDescription;
	}
	public void setFldDescription(String fldDescription) {
		this.fldDescription = fldDescription;
	}
	public String getFldType() {
		return fldType;
	}
	public void setFldType(String fldType) {
		this.fldType = fldType;
	}
	public Integer getFldSize() {
		return fldSize;
	}
	public void setFldSize(Integer fldSize) {
		this.fldSize = fldSize;
	}
	public String getMultiSeparator() {
		return multiSeparator;
	}
	public void setMultiSeparator(String multiSeparator) {
		this.multiSeparator = multiSeparator;
	}
	public String getMultiValue() {
		return multiValue;
	}
	public void setMultiValue(String multiValue) {
		this.multiValue = multiValue;
	}
	public String getFldForeignKeyTable() {
		return fldForeignKeyTable;
	}
	public void setFldForeignKeyTable(String fldForeignKeyTable) {
		this.fldForeignKeyTable = fldForeignKeyTable;
	}
	public String getFldDynName() {
		return fldDynName;
	}
	public void setFldDynName(String fldDynName) {
		this.fldDynName = fldDynName;
	}
	public String getFldDynUid() {
		return fldDynUid;
	}
	public void setFldDynUid(String fldDynUid) {
		this.fldDynUid = fldDynUid;
	}
	public String getFldFilter() {
		return fldFilter;
	}
	public void setFldFilter(String fldFilter) {
		this.fldFilter = fldFilter;
	}
	public String getFormUid() {
		return formUid;
	}
	public void setFormUid(String formUid) {
		this.formUid = formUid;
	}
	public List<String> getOpActionList() {
		return opActionList;
	}
	public void setOpActionList(List<String> opActionList) {
		this.opActionList = opActionList;
	}
	
	public BpmFormField() {}
	public BpmFormField(String fldUid, String fldCodeName, Integer fldIndex, String fldName, String fldDescription,
			String fldType, Integer fldSize, String multiSeparator, String multiValue, String fldForeignKeyTable,
			String fldDynName, String fldDynUid, String fldFilter, String formUid, List<String> opActionList) {
		super();
		this.fldUid = fldUid;
		this.fldCodeName = fldCodeName;
		this.fldIndex = fldIndex;
		this.fldName = fldName;
		this.fldDescription = fldDescription;
		this.fldType = fldType;
		this.fldSize = fldSize;
		this.multiSeparator = multiSeparator;
		this.multiValue = multiValue;
		this.fldForeignKeyTable = fldForeignKeyTable;
		this.fldDynName = fldDynName;
		this.fldDynUid = fldDynUid;
		this.fldFilter = fldFilter;
		this.formUid = formUid;
		this.opActionList = opActionList;
	}
	@Override
	public String toString() {
		return "BpmFormField [fldUid=" + fldUid + ", fldCodeName=" + fldCodeName + ", fldIndex=" + fldIndex
				+ ", fldName=" + fldName + ", fldDescription=" + fldDescription + ", fldType=" + fldType + ", fldSize="
				+ fldSize + ", multiSeparator=" + multiSeparator + ", multiValue=" + multiValue
				+ ", fldForeignKeyTable=" + fldForeignKeyTable + ", fldDynName=" + fldDynName + ", fldDynUid="
				+ fldDynUid + ", fldFilter=" + fldFilter + ", formUid=" + formUid + ", opActionList=" + opActionList
				+ "]";
	}
}
