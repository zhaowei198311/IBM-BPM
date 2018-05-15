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
import com.desmart.desmartportal.entity.TaskInstance;
import com.desmart.desmartportal.service.ProcessFormService;
import com.desmart.desmartportal.service.TaskInstanceService;
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
public class UserController1 {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskInstanceService taskInstanceService;
	
	private Logger log = Logger.getLogger(UserController1.class);
	
	@RequestMapping(value = "/logins")
	public String login(String username, String password, HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("caocao", "caocao");
		user.login(token);
		System.err.println("asdadasdasd111");
		Session session = SecurityUtils.getSubject().getSession();
		session.setAttribute(Const.CURRENT_USER, "00011178");
		session.setTimeout(17200000L);
		return "redirect:/user/menus";
	}
	
	@RequestMapping(value = "/menus")
	public ModelAndView menus() {
		ModelAndView mv = new ModelAndView("desmartportal/index");
		// 判断用户可以发起那些流程的 权限 菜单等
		List<Map<String, Object>> resultList = userService.selectByMenusProcess();
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
		return taskInstanceService.selectByusrUid(userId);
	}
}
