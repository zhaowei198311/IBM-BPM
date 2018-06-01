/**
 * 
 */
package com.desmart.desmartportal.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.desmart.common.util.DataTool;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartportal.service.UserProcessService;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysUserService;

/**  
* <p>Title: MyRealm</p>  
* <p>Description: </p>  
* @author zhaowei  
* @date 2018年5月3日  
*/
public class MyRealm extends AuthorizingRealm {

	@Autowired
	private SysUserService sysUserService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		 SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
	     return info;
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		System.out.println("***********shiro login  start**************");
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String username = usernamePasswordToken.getUsername();
        String password = new String((char[])token.getCredentials());

        System.out.println("username: " + username + ", password: " + password);
        //login
        List<SysUser> userList = sysUserService.login(username, DataTool.encodeMD5(password));
        if (userList.size() == 0) { 
        	 throw new UnknownAccountException();//没找到帐号  
        }  
        /*if (!"liubei".equals(username) && !"caocao".equals(username)) {
            throw new AuthenticationException("用户不存在");
        }*/
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password.toCharArray(), this.getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("_currUserNum", username);
        session.setAttribute("_password", password);
        session.setAttribute(Const.CURRENT_USER, userList.get(0).getUserId());
        System.out.println("***********shiro login  end**************");
        return authenticationInfo;
	}

}
