/**
 * 
 */
package com.desmart.desmartsystem.entity;

/**  
* <p>Title: DhInterfaceParameter</p>  
* <p>Description: </p>  
* @author shenlan  
* @date 2018年4月17日  
*/
public class DhInterfaceParameter {
	
	private String paraUid; // 接口参数id
	
	private Integer paraIndex; // 接口参数索引
	
	private String paraName; // 接口参数名称
	
	private String paraDescription; // 接口参数描述
	
	private String paraType; // 接口参数类型
	
	private Integer paraSize; // 接口参数长度
	
	private String multiSeparator; // 多值分隔符
	
	private String multiValue; // 是否多值
	
	private String isMust; // 是否必须
	
	private String intUid; // 接口定义id (对应接口表的主键id)

	/**
	 * @return the paraUid
	 */
	public String getParaUid() {
		return paraUid;
	}

	/**
	 * @param paraUid the paraUid to set
	 */
	public void setParaUid(String paraUid) {
		this.paraUid = paraUid;
	}

	/**
	 * @return the paraIndex
	 */
	public Integer getParaIndex() {
		return paraIndex;
	}

	/**
	 * @param paraIndex the paraIndex to set
	 */
	public void setParaIndex(Integer paraIndex) {
		this.paraIndex = paraIndex;
	}

	/**
	 * @return the paraName
	 */
	public String getParaName() {
		return paraName;
	}

	/**
	 * @param paraName the paraName to set
	 */
	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	/**
	 * @return the paraDescription
	 */
	public String getParaDescription() {
		return paraDescription;
	}

	/**
	 * @param paraDescription the paraDescription to set
	 */
	public void setParaDescription(String paraDescription) {
		this.paraDescription = paraDescription;
	}

	/**
	 * @return the paraType
	 */
	public String getParaType() {
		return paraType;
	}

	/**
	 * @param paraType the paraType to set
	 */
	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	/**
	 * @return the paraSize
	 */
	public Integer getParaSize() {
		return paraSize;
	}

	/**
	 * @param paraSize the paraSize to set
	 */
	public void setParaSize(Integer paraSize) {
		this.paraSize = paraSize;
	}

	/**
	 * @return the multiSeparator
	 */
	public String getMultiSeparator() {
		return multiSeparator;
	}

	/**
	 * @param multiSeparator the multiSeparator to set
	 */
	public void setMultiSeparator(String multiSeparator) {
		this.multiSeparator = multiSeparator;
	}

	/**
	 * @return the multiValue
	 */
	public String getMultiValue() {
		return multiValue;
	}

	/**
	 * @param multiValue the multiValue to set
	 */
	public void setMultiValue(String multiValue) {
		this.multiValue = multiValue;
	}

	/**
	 * @return the isMust
	 */
	public String getIsMust() {
		return isMust;
	}

	/**
	 * @param isMust the isMust to set
	 */
	public void setIsMust(String isMust) {
		this.isMust = isMust;
	}

	/**
	 * @return the intUid
	 */
	public String getIntUid() {
		return intUid;
	}

	/**
	 * @param intUid the intUid to set
	 */
	public void setIntUid(String intUid) {
		this.intUid = intUid;
	}
	
	public DhInterfaceParameter() {
		
	}

	/**
	 * @param paraUid
	 * @param paraIndex
	 * @param paraName
	 * @param paraDescription
	 * @param paraType
	 * @param paraSize
	 * @param multiSeparator
	 * @param multiValue
	 * @param isMust
	 * @param intUid
	 */
	public DhInterfaceParameter(String paraUid, Integer paraIndex, String paraName, String paraDescription,
			String paraType, Integer paraSize, String multiSeparator, String multiValue, String isMust, String intUid) {
		super();
		this.paraUid = paraUid;
		this.paraIndex = paraIndex;
		this.paraName = paraName;
		this.paraDescription = paraDescription;
		this.paraType = paraType;
		this.paraSize = paraSize;
		this.multiSeparator = multiSeparator;
		this.multiValue = multiValue;
		this.isMust = isMust;
		this.intUid = intUid;
	}
}
