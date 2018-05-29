/**
 * Organization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.lyfwebservice.bean;

public class Organization  implements java.io.Serializable {
    private java.lang.String changeType;

    private java.lang.String deplead;

    private java.lang.String displayName;

    private java.lang.String lyfSupervisoryDepartment;

    private java.lang.String name;

    private java.lang.String o;

    private java.lang.String supdepname;

    public Organization() {
    }

    public Organization(
           java.lang.String changeType,
           java.lang.String deplead,
           java.lang.String displayName,
           java.lang.String lyfSupervisoryDepartment,
           java.lang.String name,
           java.lang.String o,
           java.lang.String supdepname) {
           this.changeType = changeType;
           this.deplead = deplead;
           this.displayName = displayName;
           this.lyfSupervisoryDepartment = lyfSupervisoryDepartment;
           this.name = name;
           this.o = o;
           this.supdepname = supdepname;
    }


    /**
     * Gets the changeType value for this Organization.
     * 
     * @return changeType
     */
    public java.lang.String getChangeType() {
        return changeType;
    }


    /**
     * Sets the changeType value for this Organization.
     * 
     * @param changeType
     */
    public void setChangeType(java.lang.String changeType) {
        this.changeType = changeType;
    }


    /**
     * Gets the deplead value for this Organization.
     * 
     * @return deplead
     */
    public java.lang.String getDeplead() {
        return deplead;
    }


    /**
     * Sets the deplead value for this Organization.
     * 
     * @param deplead
     */
    public void setDeplead(java.lang.String deplead) {
        this.deplead = deplead;
    }


    /**
     * Gets the displayName value for this Organization.
     * 
     * @return displayName
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this Organization.
     * 
     * @param displayName
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the lyfSupervisoryDepartment value for this Organization.
     * 
     * @return lyfSupervisoryDepartment
     */
    public java.lang.String getLyfSupervisoryDepartment() {
        return lyfSupervisoryDepartment;
    }


    /**
     * Sets the lyfSupervisoryDepartment value for this Organization.
     * 
     * @param lyfSupervisoryDepartment
     */
    public void setLyfSupervisoryDepartment(java.lang.String lyfSupervisoryDepartment) {
        this.lyfSupervisoryDepartment = lyfSupervisoryDepartment;
    }


    /**
     * Gets the name value for this Organization.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Organization.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the o value for this Organization.
     * 
     * @return o
     */
    public java.lang.String getO() {
        return o;
    }


    /**
     * Sets the o value for this Organization.
     * 
     * @param o
     */
    public void setO(java.lang.String o) {
        this.o = o;
    }


    /**
     * Gets the supdepname value for this Organization.
     * 
     * @return supdepname
     */
    public java.lang.String getSupdepname() {
        return supdepname;
    }


    /**
     * Sets the supdepname value for this Organization.
     * 
     * @param supdepname
     */
    public void setSupdepname(java.lang.String supdepname) {
        this.supdepname = supdepname;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Organization)) return false;
        Organization other = (Organization) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.changeType==null && other.getChangeType()==null) || 
             (this.changeType!=null &&
              this.changeType.equals(other.getChangeType()))) &&
            ((this.deplead==null && other.getDeplead()==null) || 
             (this.deplead!=null &&
              this.deplead.equals(other.getDeplead()))) &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.lyfSupervisoryDepartment==null && other.getLyfSupervisoryDepartment()==null) || 
             (this.lyfSupervisoryDepartment!=null &&
              this.lyfSupervisoryDepartment.equals(other.getLyfSupervisoryDepartment()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.o==null && other.getO()==null) || 
             (this.o!=null &&
              this.o.equals(other.getO()))) &&
            ((this.supdepname==null && other.getSupdepname()==null) || 
             (this.supdepname!=null &&
              this.supdepname.equals(other.getSupdepname())));
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
        if (getChangeType() != null) {
            _hashCode += getChangeType().hashCode();
        }
        if (getDeplead() != null) {
            _hashCode += getDeplead().hashCode();
        }
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getLyfSupervisoryDepartment() != null) {
            _hashCode += getLyfSupervisoryDepartment().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getO() != null) {
            _hashCode += getO().hashCode();
        }
        if (getSupdepname() != null) {
            _hashCode += getSupdepname().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Organization.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "Organization"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changeType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "changeType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deplead");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "deplead"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "displayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lyfSupervisoryDepartment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "lyfSupervisoryDepartment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("o");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "o"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supdepname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://bean.lyfwebservice.ibm.com", "supdepname"));
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
