/**
 * CommonInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.lyfwebservice.webservice;

public interface CommonInterface extends java.rmi.Remote {
    public com.ibm.lyfwebservice.bean.CommonEmployeeField sendEmployee(java.lang.String systemID, java.lang.String syncParameter, java.lang.String token) throws java.rmi.RemoteException;
    public com.ibm.lyfwebservice.bean.CommonEmployeeField sendEmployeeNew(java.lang.String systemID, java.lang.String start_day, java.lang.String end_day, java.lang.String token) throws java.rmi.RemoteException;
}
