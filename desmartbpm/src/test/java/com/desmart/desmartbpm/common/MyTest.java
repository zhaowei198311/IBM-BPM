package com.desmart.desmartbpm.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyTest {
    public static void main(String[] args) throws ClientProtocolException, IOException {
        
        // http://10.0.4.201:9080/teamworks/webservices/ZOW2/backTaskService.tws?WSDL
        
        
        HttpClient httpClient =  HttpClients.custom().build();
        HttpPost httpPost = new HttpPost("http://10.1.0.90/LyfWebService/services/CommonInterface");
        StringBuffer sb = new StringBuffer();
        String data = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.lyfwebservice.ibm.com\">\r\n" + 
                "   <soapenv:Header/>\r\n" + 
                "   <soapenv:Body>\r\n" + 
                "      <web:sendEmployee>\r\n" + 
                "         <web:systemID>LYF_ALL_OFFICE</web:systemID>\r\n" + 
                "         <web:syncParameter>20180408</web:syncParameter>\r\n" + 
                "         <web:token>TFlGX0FMTF9PRkZJQ0U=</web:token>\r\n" + 
                "      </web:sendEmployee>\r\n" + 
                "   </soapenv:Body>\r\n" + 
                "</soapenv:Envelope>";
        httpPost.setHeader("SOAPAction","");
        httpPost.setEntity(new StringEntity(data, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
        
        System.out.println(msg);
        
    }
}
