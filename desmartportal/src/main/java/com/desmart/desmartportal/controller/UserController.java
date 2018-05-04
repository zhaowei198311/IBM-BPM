/**
 * 
 */
package com.desmart.desmartportal.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.dao.DhProcessMetaMapper;
import com.desmart.desmartbpm.entity.DhObjectPermission;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartbpm.service.DhObjectPermissionService;
import com.desmart.desmartbpm.service.DhProcessMetaService;
import com.desmart.desmartportal.service.UserService;
import com.desmart.desmartsystem.entity.SysRoleUser;
import com.desmart.desmartsystem.service.SysResourceService;
import com.desmart.desmartsystem.service.SysRoleUserService;
import com.desmart.desmartsystem.service.SysUserService;

/**
 * <p>
 * Title: 用户控制层
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhaowei
 * @date 2018年5月3日
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private Logger log = Logger.getLogger(UserController.class);
	
	@RequestMapping(value = "/login")
	public String login(String username, String password, HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("caocao", "caocao");
		user.login(token);
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(Const.CURRENT_USER, "00001111");
		session.setTimeout(17200000L);
		return "redirect:/user/menus";
	}
	
	@RequestMapping(value = "/menus")
	public String menus() {
		List<Object> resultList = userService.selectMap();
		for (int i = 0; i < resultList.size(); i++) {
			System.err.println(resultList.get(i));
		}
		return "redirect:/test/index";
	}
}
