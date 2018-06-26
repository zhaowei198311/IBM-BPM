package com.desmart.desmartbpm.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.desmart.desmartbpm.controller.DhProcessMetaController;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartsystem.entity.SysResource;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysResourceService;
import com.desmart.desmartsystem.service.SysUserService;

public class MyRealm extends AuthorizingRealm {
	
	private static final Logger log = LoggerFactory.getLogger(MyRealm.class);
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysResourceService sysResourceService;
	
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        
    	log.info("进入doGetAuthorizationInfo获取用户权限...");
    	String userId = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER).toString();
    	List<SysResource> resouceList = null;
    	try {
    		//通过用户ID查询当前用户的权限list
			resouceList = sysResourceService.selectResourceByUserId(userId);
			System.out.println(resouceList.get(0).getResourceCode().toString());
		} catch (Exception e) {
			log.info("查询用户权限出错...");
			e.printStackTrace();
		}
    	
    	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    	//新建一个list,把上面查询得到的权限码放进SimpleAuthorizationInfo中
    	List<String> resource = new ArrayList<String>();
		for (SysResource sysResource : resouceList) {
			//将数据库中的权限标签放入集合
			resource.add(sysResource.getResourceCode());
		}
		info.addStringPermissions(resource);
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
        List<SysUser> userList = sysUserService.login(username, password);
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