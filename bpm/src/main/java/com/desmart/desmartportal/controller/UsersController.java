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
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desmart.common.constant.ServerResponse;
import com.desmart.common.util.BpmProcessUtil;
import com.desmart.desmartbpm.common.Const;
import com.desmart.desmartbpm.entity.DhProcessMeta;
import com.desmart.desmartportal.entity.DhTaskInstance;
import com.desmart.desmartportal.service.DhProcessFormService;
import com.desmart.desmartportal.service.DhTaskInstanceService;
import com.desmart.desmartportal.service.UserProcessService;
import com.desmart.desmartsystem.entity.BpmGlobalConfig;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.BpmGlobalConfigService;
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
public class UsersController {
	
	@Autowired
	private UserProcessService userProcessService;
	
	@Autowired
	private DhTaskInstanceService dhTaskInstanceService;
	
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	
	@Autowired
	private SysUserService sysUserService;
	
	private Logger log = Logger.getLogger(UsersController.class);
	
	@RequestMapping(value = "/logins")
	@ResponseBody
	public ServerResponse login(String username, String password, HttpServletRequest request) throws Exception {
		
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			user.login(token);
		} catch (UnknownAccountException uae) {  
			return ServerResponse.createByErrorMessage("用户名/密码错误"); 
        } catch (IncorrectCredentialsException ice) { 
        	return ServerResponse.createByErrorMessage("用户名/密码错误"); 
        }  
        return ServerResponse.createBySuccess("成功登陆");
	}
	
	@RequestMapping(value = "/logout")
	public ModelAndView logout() {
		SecurityUtils.getSubject().logout();
		ModelAndView mv = new ModelAndView("desmartportal/login");
		return mv;
	}
	
	@RequestMapping(value = "/menus")
	public ModelAndView menus() {
		ModelAndView mv = new ModelAndView("desmartportal/index");
		// 判断用户可以发起那些流程的 权限 菜单等
		String creator = (String) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		SysUser sysUser = new SysUser();
		sysUser.setUserUid(creator);
		String userName = sysUserService.findById(sysUser).getUserName();
		Map<String, List<DhProcessMeta>> resultList = userProcessService.selectByMenusProcess();
		mv.addObject("listmap",resultList);
		mv.addObject("userId",creator);
		mv.addObject("userName",userName);
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
			String user  = String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER));
			return dhTaskInstanceService.selectByusrUid(user);
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
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public void rejectTest() {
		BpmGlobalConfig bpmGlobalConfig = bpmGlobalConfigService.getFirstActConfig();
		BpmProcessUtil bpmProcessUtil = new BpmProcessUtil(bpmGlobalConfig);
		//bpmProcessUtil.rejectProcess(1276, "bpdid:0907274553f39261:351615d5:163cd8dfd67:-7ff1", "deadmin");
	}		
}
