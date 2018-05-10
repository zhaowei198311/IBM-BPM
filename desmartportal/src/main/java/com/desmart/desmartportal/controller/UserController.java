/**
 * 
 */
package com.desmart.desmartportal.controller;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.service.UserService;


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
		session.setAttribute(Const.CURRENT_USER, "00011178");
		session.setTimeout(17200000L);
		return "redirect:/user/menus";
	}
	
	@RequestMapping(value = "/menus")
	public ModelAndView menus() {
		ModelAndView mv = new ModelAndView("index");
		// 判断用户可以发起那些流程的 权限 菜单等
		List<Map<String, Object>> resultList = userService.selectByMenusProcess();
		mv.addObject("listmap",resultList);
		return mv;
	}
	
	@RequestMapping(value="/index")
	public void index() throws MalformedURLException {
		URL url = new URL("http://test-bpm-app:9080/bpmasset/25.3bb4f453-ea79-4c26-aeac-5884b2bee3b3?snapshotId=2064.e3eef12e-1333-4b2e-b451-cd6c3502d0d2");
	}
}
