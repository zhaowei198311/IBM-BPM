/**
 * CommonEmployeeField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.lyfwebservice.bean;

public class CommonEmployeeField  implements java.io.Serializable {
    private com.ibm.lyfwebservice.bean.EmployeeInfo[] emp;

    private java.lang.String result;

    public CommonEmployeeField() {
    }

    public CommonEmployeeField(
           com.ibm.lyfwebservice.bean.EmployeeInfo[] emp,
           java.lang.String result) {
           this.emp = emp;
           this.result = result;
    }


    /**
     * Gets the emp value for this CommonEmployeeField.
     * 
     * @return emp
     */
    public com.ibm.lyfwebservice.bean.EmployeeInfo[] getEmp() {
        return emp;
    }


    /**
     * Sets the emp value for this CommonEmployeeField.
     * 
     * @param emp
     */
    public void setEmp(com.ibm.lyfwebservice.bean.EmployeeInfo[] emp) {
        this.emp = emp;
    }


    /**
     * Gets the result value for this CommonEmployeeField.
     * 
     * @return result
     */
    public java.lang.String getResult() {
        return result;
    }


    /**
     * Sets the result value for this CommonEmployeeField.
     * 
     * @param result
     */
    public void setResult(java.lang.String result) {
        this.result = result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CommonEmployeeField)) return false;
        CommonEmployeeField other = (CommonEmployeeField) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.emp==null && other.getEmp()==null) || 
             (this.emp!=null &&
              java.util.Arrays.equals(this.emp, other.getEmp()))) &&
            ((this.result==null && other.getResult()==null) || 
             (this.result!=null &&
              this.result.equals(other.getResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getEmp() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEmp());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEmp(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResult() != null) {
            _hashCode += getResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CommonEmployeeField.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "CommonEmployeeField"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "emp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "EmployeeInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://webservice.lyfwebservice.ibm.com", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
