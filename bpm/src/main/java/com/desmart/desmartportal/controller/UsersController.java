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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.UserProcessService;


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
public class UsersController {
	
	@Autowired
	private UserProcessService userProcessService;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	private Logger log = Logger.getLogger(UsersController.class);
	
	@RequestMapping(value = "/logins")
	@ResponseBody
	public String login(String username, String password, HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("caocao", "caocao");
		user.login(token);
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(Const.CURRENT_USER, username);
		session.setTimeout(17200000L);
		System.err.println(username);
		return "1";
	}
	
	@RequestMapping(value = "/menus")
	public ModelAndView menus() {
		ModelAndView mv = new ModelAndView("desmartportal/index");
		// 判断用户可以发起那些流程的 权限 菜单等
		List<Map<String, Object>> resultList = userProcessService.selectByMenusProcess();
		mv.addObject("listmap",resultList);
		return mv;
	}
	
	@RequestMapping(value="/index")
	public String index(){
		return "desmartportal/login";
	}
	
	/**
	 * 代办任务
	 * @return 
	 */
	@RequestMapping(value = "/todoTask")
	@ResponseBody
	public int todoTask(String userId) {
		if(userId==null) {
			userId  = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		}
		return dhTaskInstanceService.selectByusrUid(userId);
	}
	
	/**
	 * 已办任务
	 * @return 
	 */
	@RequestMapping(value = "/todoFinshTask")
	@ResponseBody
	public int todoFinshTask(String userId) {
		if(userId==null) {
			userId  = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
		}
		return dhTaskInstanceService.selectByusrUidFinsh(userId);
	}
}
