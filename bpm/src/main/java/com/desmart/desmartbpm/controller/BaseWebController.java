package com.desmart.desmartbpm.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

public class BaseWebController {
	
    public Session getSession() {
        Session session = SecurityUtils.getSubject().getSession();
        return session;
    }

    public String getEmployeeDeptNum() {
        String userNum = (String)this.getSession().getAttribute("_currUserDeptNum");
        return userNum;
    }

    public String getEmployeeNum() {
        String userNum = (String)this.getSession().getAttribute("_currUserNum");
        return userNum;
    }

    public String getEmployeeName() {
        String userName = (String)this.getSession().getAttribute("_currUserName");
        return userName;
    }
    
    
}
