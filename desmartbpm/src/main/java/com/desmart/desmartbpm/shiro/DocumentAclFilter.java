package com.desmart.desmartbpm.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DocumentAclFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        System.out.println("=====  DocumentAclFilter.isAccessAllowed() start =====");

        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String servletPath = httpServletRequest.getServletPath();
        Session session = SecurityUtils.getSubject().getSession();
        String username = (String)session.getAttribute("_currUserNum");
        System.out.println("servletPath: " + servletPath);

        System.out.println("=====  DocumentAclFilter.isAccessAllowed() finish =====");

        return "liubei".equals(username);

    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        System.out.println("=====  DocumentAclFilter.onAccessDenied() start =====");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        response.sendRedirect(basePath + "error.do");

        System.out.println("=====  DocumentAclFilter.onAccessDenied() finish =====");
        return false;
    }



}