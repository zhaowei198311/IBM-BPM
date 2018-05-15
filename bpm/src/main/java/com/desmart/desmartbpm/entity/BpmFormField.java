package com.desmart.desmartbpm.entity;

/**
 * 表单字段
 * @author loser_wu
 * @since 2018/4/23
 */
public class BpmFormField {
	private String fldUid;
	private String fldCodeId;//字段在html文件中的Id
	private Integer fldIndex;
	private String fldName;
	private String fldDescription;
	private String fldType;
	private Integer fldSize;
	private String multiSeparator;
	private String multiValue;
	private String fldForeignKeyTable;//参考的业务表
	private String fldDynName;//参考的业务表字段名
	private String fldDybUid;//参考的业务表字段Id
	private String fldFilter;//是否为过滤字段--报表
	private String formUid;
	
	private String opAction;//字段权限，不是本表字段
	
	public String getFldUid() {
		return fldUid;
	}
	public void setFldUid(String fldUid) {
		this.fldUid = fldUid;
	}
	public String getFldCodeId() {
		return fldCodeId;
	}
	public void setFldCodeId(String fldCodeId) {
		this.fldCodeId = fldCodeId;
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
	public String getFldDybUid() {
		return fldDybUid;
	}
	public void setFldDybUid(String fldDybUid) {
		this.fldDybUid = fldDybUid;
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
	public String getOpAction() {
		return opAction;
	}
	public void setOpAction(String opAction) {
		if(opAction==null || "".equals(opAction)) {
			this.opAction = "EDIT";
		}else{
			this.opAction = opAction;
		}
	}
	public BpmFormField() {}
	public BpmFormField(String fldUid, String fldCodeId, Integer fldIndex, String fldName, String fldDescription,
			String fldType, Integer fldSize, String multiSeparator, String multiValue, String fldForeignKeyTable,
			String fldDynName, String fldDybUid, String fldFilter, String formUid, String opAction) {
		super();
		this.fldUid = fldUid;
		this.fldCodeId = fldCodeId;
		this.fldIndex = fldIndex;
		this.fldName = fldName;
		this.fldDescription = fldDescription;
		this.fldType = fldType;
		this.fldSize = fldSize;
		this.multiSeparator = multiSeparator;
		this.multiValue = multiValue;
		this.fldForeignKeyTable = fldForeignKeyTable;
		this.fldDynName = fldDynName;
		this.fldDybUid = fldDybUid;
		this.fldFilter = fldFilter;
		this.formUid = formUid;
		this.opAction = opAction;
	}
	@Override
	public String toString() {
		return "BpmFormField [fldUid=" + fldUid + ", fldCodeId=" + fldCodeId + ", fldIndex=" + fldIndex + ", fldName="
				+ fldName + ", fldDescription=" + fldDescription + ", fldType=" + fldType + ", fldSize=" + fldSize
				+ ", multiSeparator=" + multiSeparator + ", multiValue=" + multiValue + ", fldForeignKeyTable="
				+ fldForeignKeyTable + ", fldDynName=" + fldDynName + ", fldDybUid=" + fldDybUid + ", fldFilter="
				+ fldFilter + ", formUid=" + formUid + ", opAction=" + opAction + "]";
	}
}
