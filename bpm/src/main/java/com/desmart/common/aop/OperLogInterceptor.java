package com.desmart.common.aop;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.desmart.common.annotation.log.Log;
import com.desmart.common.constant.ServerResponse;
import com.desmart.desmartbpm.entity.DhOperLog;
import com.desmart.desmartbpm.service.OperLogService;
import com.desmart.desmartportal.common.Const;
import com.desmart.desmartsystem.entity.SysUser;
import com.desmart.desmartsystem.service.SysUserService;

/**
 * aop记录日志切面类
 * @author zbw
 */
@Aspect
@Component
public class OperLogInterceptor {
	
	@Resource
	private OperLogService logService;
	
	/**
	 * 环绕通知
	 * @param pjd
	 * @param log
	 * @return
	 * @throws Exception
	 */
	//@Around("within(com.desmart.*.*) && @annotation(log)")
	@Around(value = "execution(* com.desmart..*(..)) && @annotation(log) ")
	public Object aroundMethod(ProceedingJoinPoint pjd, Log log) throws Exception {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		DhOperLog operLog = new DhOperLog();
		//请求参数
		Signature signature = pjd.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] strings = methodSignature.getParameterNames();  //请求参数的key数组
		Object[] args = pjd.getArgs();   //请求参数的value数组
		String requestParam = getRequestParam(strings, args); //封装请求参数字符串
		//获取方法注解描述信息
		String serviceMthodDescription = getServiceMthodDescription(pjd);
		String host = InetAddress.getLocalHost().getHostAddress().toString();//获取ip地址
	    //String host = request.getRemoteHost().get; //ip
		Object proceed = null;
		try {
			proceed = pjd.proceed(); //必须放在获取用户id之前
		} catch (Throwable e) {
			e.printStackTrace();
			operLog.setMethodDescription(e.toString());  //e.toString()和e..getMessage()区别
		}
		//获取相应内容
		try {
			ServerResponse serverResponse = (ServerResponse)proceed; //判断是否为ServerResponse对象
			operLog.setResponseParam("返回信息:" + serverResponse.getMsg() +";"+ "返回状态:" + serverResponse.getStatus()+";" + "返回数据:" + serverResponse.getData());
			operLog.setLogType(DhOperLog.errorLog);
		} catch (Exception e) {
			operLog.setResponseParam(proceed.toString());
			operLog.setLogType(DhOperLog.systemLog);
		}
		
		//获取当前用户id及当前用户姓名
	    String
	    
	    
	    userId = SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER).toString();
	    String userName = SecurityUtils.getSubject().getSession().getAttribute("userName").toString();
	   /* SysUser sysUser = new SysUser();
	    sysUser.setUserId(userId);
	    SysUser user = sysUserService.select(sysUser);*/
		StringBuffer requestURL = request.getRequestURL();
		String path = new String(requestURL);
		
		//封装实体类
		operLog.setMethodDescription(serviceMthodDescription);
		operLog.setUserId(userId); 
		operLog.setUserName(userName); 
		operLog.setHost(host);
		operLog.setPath(path);
		operLog.setUserName(userName);
		operLog.setRequestParam(requestParam);
		operLog.setId(UUID.randomUUID().toString());
		//operLog.setCreatTime(new Date());
		operLog.setAttach("");
		int result = logService.save(operLog);
		System.out.println(result);
		return proceed;
	}
	
	//封装请求参数kv工具方法
	private String getRequestParam(String[] strings, Object[] args) {
		
		String result = "";
		if(strings.length == args.length) {
			for (int i = 0; i < strings.length; i++) {
				result = result + strings[i] + ":" + args[i].toString() + ";" ; 
			}
		}else {
			if(args != null) {
				for (int i = 0; i < args.length; i++) {
					result = args[i].toString() + result + ";";
				}
			}
		}
		return result;
	}

	//获取方法注解的描述内容
	private String getServiceMthodDescription(JoinPoint joinPoint)throws Exception {
      
	   String targetName = joinPoint.getTarget().getClass().getName();
       String methodName = joinPoint.getSignature().getName();
       Object[] arguments = joinPoint.getArgs();
       Class targetClass = Class.forName(targetName);
       Method[] methods = targetClass.getMethods();
       String description = "";
        for(Method method : methods) {
            if(method.getName().equals(methodName)) {
               Class[] clazzs = method.getParameterTypes();
                if(clazzs.length == arguments.length) {
                   description = method.getAnnotation(Log.class).description();
                    break;
               }
           }
       }
       return description;
   }

}
