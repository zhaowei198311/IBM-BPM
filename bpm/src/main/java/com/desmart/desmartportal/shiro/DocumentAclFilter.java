/**
 * 
 */
package com.desmart.desmartportal.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.desmart.desmartbpm.common.Const;

/**
 * <p>
 * Title: DocumentAclFilter
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月3日
 */
public class DocumentAclFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object arg2)
			throws Exception {
		System.out.println("=====  DocumentAclFilter.isAccessAllowed() start =====");

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String servletPath = httpServletRequest.getServletPath();
		Session session = SecurityUtils.getSubject().getSession();
		String username = (String) session.getAttribute(Const.CURRENT_USER_NAME);
		System.out.println("servletPath: " + servletPath);

		System.out.println("=====  DocumentAclFilter.isAccessAllowed() finish =====");

		return "liubei".equals(username);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		System.out.println("=====  DocumentAclFilter.onAccessDenied() start =====");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		response.sendRedirect(basePath + "error.do");

		System.out.println("=====  DocumentAclFilter.onAccessDenied() finish =====");
		return false;
	}

}
