package com.desmart.desmartbpm.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String username = usernamePasswordToken.getUsername();
        String password = new String((char[])token.getCredentials());

        System.out.println("======= MyRealm.doGetAuthenticationInfo() start =========");
        System.out.println("username: " + username + ", password: " + password);

        if (!"liubei".equals(username) && !"caocao".equals(username)) {
            throw new AuthenticationException("用户不存在");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password.toCharArray(), this.getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("_currUserNum", username);
        session.setAttribute("_password", password);

        System.out.println("======= MyRealm.doGetAuthenticationInfo() finish =======");
        return authenticationInfo;
    }


}