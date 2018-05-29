/**
 * CommonInterfaceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ibm.lyfwebservice.webservice;

public class CommonInterfaceServiceLocator extends org.apache.axis.client.Service implements com.ibm.lyfwebservice.webservice.CommonInterfaceService {

    public CommonInterfaceServiceLocator() {
    }


    public CommonInterfaceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CommonInterfaceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CommonInterface
    private java.lang.String CommonInterface_address = "http://10.1.0.90/LyfWebService/services/CommonInterface";

    public java.lang.String getCommonInterfaceAddress() {
        return CommonInterface_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CommonInterfaceWSDDServiceName = "CommonInterface";

    public java.lang.String getCommonInterfaceWSDDServiceName() {
        return CommonInterfaceWSDDServiceName;
    }

    public void setCommonInterfaceWSDDServiceName(java.lang.String name) {
        CommonInterfaceWSDDServiceName = name;
    }

    public com.ibm.lyfwebservice.webservice.CommonInterface getCommonInterface() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CommonInterface_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCommonInterface(endpoint);
    }

    public com.ibm.lyfwebservice.webservice.CommonInterface getCommonInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ibm.lyfwebservice.webservice.CommonInterfaceSoapBindingStub _stub = new com.ibm.lyfwebservice.webservice.CommonInterfaceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCommonInterfaceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCommonInterfaceEndpointAddress(java.lang.String address) {
        CommonInterface_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ibm.lyfwebservice.webservice.CommonInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ibm.lyfwebservice.webservice.CommonInterfaceSoapBindingStub _stub = new com.ibm.lyfwebservice.webservice.CommonInterfaceSoapBindingStub(new java.net.URL(CommonInterface_address), this);
                _stub.setPortName(getCommonInterfaceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CommonInterface".equals(inputPortName)) {
            return getCommonInterface();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.lyfwebservice.ibm.com", "CommonInterfaceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.lyfwebservice.ibm.com", "CommonInterface"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CommonInterface".equals(portName)) {
            setCommonInterfaceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
