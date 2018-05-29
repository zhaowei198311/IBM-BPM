package com.ibm.lyfwebservice.webservice;

public class CommonInterfaceProxy implements com.ibm.lyfwebservice.webservice.CommonInterface {
  private String _endpoint = null;
  private com.ibm.lyfwebservice.webservice.CommonInterface commonInterface = null;
  
  public CommonInterfaceProxy() {
    _initCommonInterfaceProxy();
  }
  
  public CommonInterfaceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCommonInterfaceProxy();
  }
  
  private void _initCommonInterfaceProxy() {
    try {
      commonInterface = (new com.ibm.lyfwebservice.webservice.CommonInterfaceServiceLocator()).getCommonInterface();
      if (commonInterface != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)commonInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)commonInterface)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (commonInterface != null)
      ((javax.xml.rpc.Stub)commonInterface)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ibm.lyfwebservice.webservice.CommonInterface getCommonInterface() {
    if (commonInterface == null)
      _initCommonInterfaceProxy();
    return commonInterface;
  }
  
  public com.ibm.lyfwebservice.bean.CommonEmployeeField sendEmployee(java.lang.String systemID, java.lang.String syncParameter, java.lang.String token) throws java.rmi.RemoteException{
    if (commonInterface == null)
      _initCommonInterfaceProxy();
    return commonInterface.sendEmployee(systemID, syncParameter, token);
  }
  
  public com.ibm.lyfwebservice.bean.CommonEmployeeField sendEmployeeNew(java.lang.String systemID, java.lang.String start_day, java.lang.String end_day, java.lang.String token) throws java.rmi.RemoteException{
    if (commonInterface == null)
      _initCommonInterfaceProxy();
    return commonInterface.sendEmployeeNew(systemID, start_day, end_day, token);
  }
  
  
}