package com.ibm.lyfwebservice.webservice;

public class CommonOrganizationInterfaceProxy implements com.ibm.lyfwebservice.webservice.CommonOrganizationInterface {
  private String _endpoint = null;
  private com.ibm.lyfwebservice.webservice.CommonOrganizationInterface commonOrganizationInterface = null;
  
  public CommonOrganizationInterfaceProxy() {
    _initCommonOrganizationInterfaceProxy();
  }
  
  public CommonOrganizationInterfaceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonOrganizationInterfaceProxy();
  }
  
  private void _initCommonOrganizationInterfaceProxy() {
    try {
      commonOrganizationInterface = (new com.ibm.lyfwebservice.webservice.CommonOrganizationInterfaceServiceLocator()).getCommonOrganizationInterface();
      if (commonOrganizationInterface != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonOrganizationInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonOrganizationInterface)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonOrganizationInterface != null)
      ((javax.xml.rpc.Stub)commonOrganizationInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ibm.lyfwebservice.webservice.CommonOrganizationInterface getCommonOrganizationInterface() {
    if (commonOrganizationInterface == null)
      _initCommonOrganizationInterfaceProxy();
    return commonOrganizationInterface;
  }
  
  public com.ibm.lyfwebservice.bean.CommonOrganizationField sendOrganization(java.lang.String systemID, java.lang.String syncStartDate, java.lang.String token) throws java.rmi.RemoteException{
    if (commonOrganizationInterface == null)
      _initCommonOrganizationInterfaceProxy();
    return commonOrganizationInterface.sendOrganization(systemID, syncStartDate, token);
  }
  
  
}